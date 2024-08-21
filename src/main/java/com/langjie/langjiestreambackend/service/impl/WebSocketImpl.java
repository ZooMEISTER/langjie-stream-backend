package com.langjie.langjiestreambackend.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.langjie.langjiestreambackend.constant.WebSocketMessageType;
import com.langjie.langjiestreambackend.interceptor.WebSocketInterceptor;
import com.langjie.langjiestreambackend.mapper.LiveMapper;
import com.langjie.langjiestreambackend.mapper.UserMapper;
import com.langjie.langjiestreambackend.pojo.bo.WebSocketMessageBO;
import com.langjie.langjiestreambackend.pojo.po.UserPO;
import com.langjie.langjiestreambackend.service.WebSocket;
import com.langjie.langjiestreambackend.utils.TimeUtils;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/21 9:34
 **/

@Slf4j
@Component
public class WebSocketImpl implements WebSocket {

    private UserMapper userMapper;
    private LiveMapper liveMapper;
    private RedisTemplate redisTemplate;
    private Environment environment;

    public WebSocketImpl(UserMapper userMapper, LiveMapper liveMapper, RedisTemplate redisTemplate, Environment environment) {
        this.userMapper = userMapper;
        this.liveMapper = liveMapper;
        this.redisTemplate = redisTemplate;
        this.environment = environment;
    }

    /**
     * 线程安全的无序集合（存储会话）
     */
    private final CopyOnWriteArraySet<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    /**
     * 存储在线连接
     */
    private static final ConcurrentHashMap<String, WebSocketSession> sessionPool = new ConcurrentHashMap<>();

    /**
    * @Author: ZooMEISTER
    * @Description: 有新的连接进来
    * @DateTime: 2024/8/21 10:29
    * @Params: [session]
    * @Return void
    */
    @Override
    public void handleOpen(WebSocketSession session) {
        try{
            String live_id = WebSocketInterceptor.getLiveIdFromInterceptor();
            String user_id = WebSocketInterceptor.getUserIdFromInterceptor();

            // 同一个用户，在一个直播间同时只能有一个登入
            // 先判断该用户是否已经在当前直播间
            if(sessionPool.containsKey(live_id + ":" + user_id)){
                // 该用户在当前直播间连接已存在，先发送一个你将要被移除的消息
                sessionPool.get(live_id + ":" + user_id).sendMessage(new TextMessage(
                        JSON.toJSONString(
                                new WebSocketMessageBO(
                                        UUID.randomUUID().toString().replace("-", ""),
                                        "SERVER_ERROR",
                                        "服务器",
                                        WebSocketMessageType.SERVER_ERROR,
                                        "哥们你被顶号了，CHAT已断开",
                                        TimeUtils.getCurrentTimeMsString()
                                )
                        )
                ));
                // 移除已存在的链接
                sessions.remove(sessionPool.get(live_id + ":" + user_id));
                sessionPool.remove(live_id + ":" + user_id);
            }

            // 用户进来时，发送一条直播间消息
            UserPO userPO = userMapper.getUserPOByUserId(user_id);
            sendMsgToSingleLiveRoom(live_id, "SERVER_INFO", "服务器", WebSocketMessageType.SERVER_INFO, userPO.getUser_name() + " 已加入直播间");

            // 把新进来的连接添加进去
            sessions.add(session);
            sessionPool.put(live_id + ":" + user_id, session);

            // 在这里把历史记录中有的消息全部发出去
            Set<String> historyMsgSet = redisTemplate.opsForZSet().range(
                    environment.getProperty("stream.server.redis.live-room-chat-history") + live_id,
                    0,
                    redisTemplate.opsForZSet().size(environment.getProperty("stream.server.redis.live-room-chat-history") + live_id));

            // 遍历发送历史消息
            for(String hisMsg : historyMsgSet){
                JSONObject hisMsgJSON = (JSONObject) JSON.parse(hisMsg);
                session.sendMessage(new TextMessage(
                        JSON.toJSONString(
                                new WebSocketMessageBO(
                                        (String) hisMsgJSON.get("msgId"),
                                        (String) hisMsgJSON.get("senderId"),
                                        (String) hisMsgJSON.get("senderName"),
                                        WebSocketMessageType.USER_MESSAGE_HISTORY,
                                        (String) hisMsgJSON.get("msg"),
                                        (String) hisMsgJSON.get("sendTime")
                                )
                        )
                ));
            }

            // 再给连接进来的用户发个已连接消息
            session.sendMessage(new TextMessage(
                    JSON.toJSONString(
                            new WebSocketMessageBO(
                                    UUID.randomUUID().toString().replace("-", ""),
                                    "SERVER_INFO",
                                    "服务器",
                                    WebSocketMessageType.SERVER_INFO,
                                    "以上为历史消息, CHAT已连接",
                                    TimeUtils.getCurrentTimeMsString()
                            )
                    )
            ));

            log.info("当前连接数: {}", sessions.size());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 有连接断开
    * @DateTime: 2024/8/21 10:29
    * @Params: [session]
    * @Return void
    */
    @Override
    public void handleClose(WebSocketSession session) {
        try{
            // 先从连接集合中移除该连接
            sessions.remove(session);

            // 先找到发送消息的连接对应的KEY，也就是这个连接的 用户id 和 直播间id
            String currentSessionKey = "SESSION_NOT_FOUND";
            // 从map中寻找该链接
            for(String key : sessionPool.keySet()){
                if(sessionPool.get(key).equals(session)){
                    currentSessionKey = key;
                }
            }

            if(currentSessionKey.equals("SESSION_NOT_FOUND")){
                // 未找到该连接
                log.info("该链接已移除");
                return;
            }
            else {
                // 找到该连接
                // 从redis中将认证信息设为HOLD，寿命5s
                redisTemplate.opsForValue().set(environment.getProperty("stream.server.redis.user-enter-live-room-status") + currentSessionKey, "HOLD", 5, TimeUnit.SECONDS);

                // 从SESSION_POOL中移除该连接
                sessionPool.remove(currentSessionKey);

                // 在直播间内广播一条退出消息
                UserPO userPO = userMapper.getUserPOByUserId(currentSessionKey.split(":")[1]);
                sendMsgToSingleLiveRoom(currentSessionKey.split(":")[0], "SERVER_INFO", "服务器", WebSocketMessageType.SERVER_INFO, userPO.getUser_name() + " 已退出直播间");
            }

            log.info("当前连接数: {}", sessions.size());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 接收到消息
    * @DateTime: 2024/8/21 10:50
    * @Params: [session, message]
    * @Return void
    */
    @Override
    public void handleMessage(WebSocketSession session, String message) {
        try{
            // 先找到发送消息的连接对应的KEY，也就是这个连接的 用户id 和 直播间id
            String currentSessionKey = "SESSION_NOT_FOUND";
            for(String key : sessionPool.keySet()){
                if(sessionPool.get(key).equals(session)){
                    currentSessionKey = key;
                }
            }

            // 判断是否找到该连接对应的信息
            if(currentSessionKey.equals("SESSION_NOT_FOUND")){
                log.info("未知连接");
                return;
            }

            // 该信息的 [0]直播间id 和 [1]发送者id
            String[] msgInfo = currentSessionKey.split(":");

            // 找到的连接对应的信息
            if(message.startsWith("[ROOM_MSG]")){
                // 发送的是直播间消息
                String actualMsg = message.substring(10);
                sendMsgToSingleLiveRoom(msgInfo[0], msgInfo[1], msgInfo[1], WebSocketMessageType.USER_MESSAGE, actualMsg);
            }
            else if(message.startsWith("[BROADCAST_MSG]")){
                // 发送的是广播消息
                String actualMsg = message.substring(15);
                broadCastMsgToALL(msgInfo[1], msgInfo[1], WebSocketMessageType.USER_MESSAGE, "[BROADCAST] " + actualMsg);
            }
            else{
                // 默认发送的是直播间消息
                String actualMsg = message;
                sendMsgToSingleLiveRoom(msgInfo[0], msgInfo[1], msgInfo[1], WebSocketMessageType.USER_MESSAGE, actualMsg);
            }

            log.info(currentSessionKey + ": " + message);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 给某个直播间的某个用户发送消息
    * @DateTime: 2024/8/21 13:45
    * @Params: [live_id, user_id, senderName, targetViewerId, msgType, msg]
    * @Return void
    */
    @Override
    public void sendMsgToSingleViewer(String live_id, String user_id, String senderName, String targetViewerId, String msgType, String msg){
        try{
            // 查询发送者的信息
            UserPO userPO = userMapper.getUserPOByUserId(user_id);
            if(userPO != null){
                senderName = userPO.getUser_name();
            }

            // 该消息的发送时间
            String msgTime = TimeUtils.getCurrentTimeMsString();
            // 获取实际发送的字符串
            String msgString = JSON.toJSONString(
                    new WebSocketMessageBO(
                            UUID.randomUUID().toString().replace("-", ""),
                            user_id,
                            senderName,
                            msgType,
                            msg,
                            msgTime
                    )
            );

            // 遍历连接 Map 把消息发送出去
            for(String key : sessionPool.keySet()){
                if(key.split(":")[0].equals(live_id) && key.split(":")[1].equals(targetViewerId)){
                    // 该key对应的连接对象为该直播间的连接
                    if(sessionPool.get(key).isOpen()){
                        // 给该链接发送消息
                        sessionPool.get(key).sendMessage(new TextMessage(
                                msgString
                        ));
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 给单个直播间的连接发送消息
    * @DateTime: 2024/8/21 10:38
    * @Params: [live_id, user_id, senderName, msgType, msg]
    * @Return void
    */
    @Override
    public void sendMsgToSingleLiveRoom(String live_id, String user_id, String senderName, String msgType, String msg){
        try{
            // 查询发送者的信息
            UserPO userPO = userMapper.getUserPOByUserId(user_id);
            if(userPO != null){
                senderName = userPO.getUser_name();
            }

            // 该消息的发送时间
            String msgTime = TimeUtils.getCurrentTimeMsString();
            // 获取实际发送的字符串
            String msgString = JSON.toJSONString(
                    new WebSocketMessageBO(
                            UUID.randomUUID().toString().replace("-", ""),
                            user_id,
                            senderName,
                            msgType,
                            msg,
                            msgTime
                    )
            );

            if(msgType.equals(WebSocketMessageType.USER_MESSAGE)){
                // 如果是用户发送的普通聊天消息，则把该消息放暂存到 Redis 中
                // 存放的是一个 zset 信息的发送时间作为 score
                redisTemplate.opsForZSet().add(
                        environment.getProperty("stream.server.redis.live-room-chat-history") + live_id,
                        msgString,
                        Double.parseDouble(msgTime)
                );
            }


            // 遍历连接 Map 把消息发送出去
            for(String key : sessionPool.keySet()){
                if(key.split(":")[0].equals(live_id)){
                    // 该key对应的连接对象为该直播间的连接
                    if(sessionPool.get(key).isOpen()){
                        // 给该链接发送消息
                        sessionPool.get(key).sendMessage(new TextMessage(
                                msgString
                        ));
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 给所有连接广播消息
    * @DateTime: 2024/8/21 10:54
    * @Params: [user_id, senderName, msgType, msg]
    * @Return void
    */
    @Override
    public void broadCastMsgToALL(String user_id, String senderName, String msgType, String msg){
        try{
            // 查询发送者的信息
            UserPO userPO = userMapper.getUserPOByUserId(user_id);
            if(userPO != null){
                senderName = userPO.getUser_name();
            }

            // 该消息的发送时间
            String msgTime = TimeUtils.getCurrentTimeMsString();
            // 获取实际发送的字符串
            String msgString = JSON.toJSONString(
                    new WebSocketMessageBO(
                            UUID.randomUUID().toString().replace("-", ""),
                            user_id,
                            senderName,
                            msgType,
                            msg,
                            msgTime
                    )
            );

            // 遍历连接 Map 把消息发送出去
            for(WebSocketSession session : sessionPool.values()){
                if(session.isOpen()){
                    // 给该链接发送消息
                    session.sendMessage(new TextMessage(
                            msgString
                    ));
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void handleError(WebSocketSession session, Throwable error) {
        log.error("websocket error：{}，session id：{}", error.getMessage(), session.getId());
        log.error("", error);
    }

    @Override
    public Set<WebSocketSession> getSessions() {
        return sessions;
    }

    @Override
    public int getConnectionCount() {
        return sessions.size();
    }
}
