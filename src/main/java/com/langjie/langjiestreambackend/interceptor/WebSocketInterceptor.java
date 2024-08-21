package com.langjie.langjiestreambackend.interceptor;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.langjie.langjiestreambackend.constant.ResultType;
import com.langjie.langjiestreambackend.exception.code.InterceptorResultCode;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @Author ZooMEISTER
 * @Description: Websocket 连接打开时的拦截器
 * @DateTime 2024/8/21 9:41
 **/

@Component
public class WebSocketInterceptor implements HandshakeInterceptor {

    private RedisTemplate redisTemplate;
    private Environment environment;

    public WebSocketInterceptor(RedisTemplate redisTemplate, Environment environment) {
        this.redisTemplate = redisTemplate;
        this.environment = environment;
    }

    // 用来保存本次进来的连接的 live_id 和 user_id
    private static final ThreadLocal<String> currentRequestLiveID = new ThreadLocal<>();
    private static final ThreadLocal<String> currentRequestUserID = new ThreadLocal<>();

    /**
    * @Author: ZooMEISTER
    * @Description: 拦截，在这里判断连接能否建立
    * @DateTime: 2024/8/21 11:03
    * @Params: [request, response, wsHandler, attributes]
    * @Return boolean
    */
    @Override
    public boolean beforeHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response, @NonNull WebSocketHandler wsHandler, @NonNull Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) request;
            // 塞进去用户信息
            currentRequestLiveID.set(servletServerHttpRequest.getServletRequest().getParameter("live_id"));
            currentRequestUserID.set(servletServerHttpRequest.getServletRequest().getParameter("user_id"));

            // 这里对连接携带的信息进行验证
            if(redisTemplate.opsForValue().get(environment.getProperty("stream.server.redis.user-enter-live-room-status") + currentRequestLiveID.get() + ":" + currentRequestUserID.get()) != null &&
                    redisTemplate.opsForValue().get(environment.getProperty("stream.server.redis.user-enter-live-room-status") + currentRequestLiveID.get() + ":" + currentRequestUserID.get()).equals("ON")){
                // Redis中存在该用户的连接信息，可以连接
                return true;
            }
            else{
                // Redis中不存在该用户的连接信息，直接阻断
                return false;
            }
        }
        return false;
    }

    @Override
    public void afterHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response, @NonNull WebSocketHandler wsHandler, Exception exception) {}


    /**
    * @Author: ZooMEISTER
    * @Description: 从 ThreadLocal 中获取本次请求的直播间id
    * @DateTime: 2024/8/21 11:44
    * @Params: []
    * @Return java.lang.String
    */
    public static String getLiveIdFromInterceptor(){
        return currentRequestLiveID.get();
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 从 ThreadLocal中获取本次请求的用户id
    * @DateTime: 2024/8/21 11:44
    * @Params: []
    * @Return java.lang.String
    */
    public static String getUserIdFromInterceptor(){
        return currentRequestUserID.get();
    }


    /**
     * @Author: ZooMEISTER
     * @Description: 拦截器返回结果的方法
     * @DateTime: 2024/8/12 13:21
     * @Params: [response, json]
     * @Return void
     */
    private void ReturnJson(ServerHttpResponse serverHttpResponse, String json) throws Exception{
        ServletServerHttpResponse servletServerHttpResponse = (ServletServerHttpResponse) serverHttpResponse;
        HttpServletResponse response = servletServerHttpResponse.getServletResponse();

        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(json);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null)
                writer.close();
        }
    }
}
