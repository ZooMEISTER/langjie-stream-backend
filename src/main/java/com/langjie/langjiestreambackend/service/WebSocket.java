package com.langjie.langjiestreambackend.service;

import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Set;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/21 9:29
 **/

@Component
public interface WebSocket {
    /**
     * 会话开始回调
     *
     * @param session 会话
     */
    void handleOpen(WebSocketSession session);

    /**
     * 会话结束回调
     *
     * @param session 会话
     */
    void handleClose(WebSocketSession session);

    /**
     * 处理消息
     *
     * @param session 会话
     * @param message 接收的消息
     */
    void handleMessage(WebSocketSession session, String message);

    /**
     * 处理会话异常
     *
     * @param session 会话
     * @param error   异常
     */
    void handleError(WebSocketSession session, Throwable error);

    /**
     * @Author: ZooMEISTER
     * @Description: 给某个直播间的某一个观众发送消息
     * @DateTime: 2024/8/21 13:42
     */
    void sendMsgToSingleViewer(WebSocketSession session, String msg);

    /**
    * @Author: ZooMEISTER
    * @Description: 给某个直播间的某一个观众发送消息
    * @DateTime: 2024/8/21 13:42
    */
    void sendMsgToSingleViewer(String live_id, String user_id, String senderName, String targetViewerId, String msgType, String msg);

    /**
    * @Author: ZooMEISTER
    * @Description: 给某一个直播间发送消息
    * @DateTime: 2024/8/21 11:06
    */
    void sendMsgToSingleLiveRoom(String live_id, String user_id, String senderName, String msgType, String msgId, String msg);

    /**
    * @Author: ZooMEISTER
    * @Description: 给所有连接发送消息
    * @DateTime: 2024/8/21 11:06
    */
    void broadCastMsgToALL(String user_id, String senderName, String msgType, String msg);

    /**
     * 获得所有的 websocket 会话
     *
     * @return 所有 websocket 会话
     */
    Set<WebSocketSession> getSessions();

    /**
     * 得到当前连接数
     *
     * @return 连接数
     */
    int getConnectionCount();

}
