package com.langjie.langjiestreambackend.interceptor;

import com.langjie.langjiestreambackend.service.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/21 9:38
 **/

@Component
public class DefaultWebSocketHandler implements WebSocketHandler {

    private WebSocket webSocket;

    public DefaultWebSocketHandler(WebSocket webSocket) {
        this.webSocket = webSocket;
    }

    /**
     * 建立连接
     *
     * @param session Session
     */
    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        webSocket.handleOpen(session);
    }

    /**
     * 接收消息
     *
     * @param session Session
     * @param message 消息
     */
    @Override
    public void handleMessage(@NonNull WebSocketSession session, @NonNull WebSocketMessage<?> message) {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            webSocket.handleMessage(session, textMessage.getPayload());
        }
    }

    /**
     * 发生错误
     *
     * @param session   Session
     * @param exception 异常
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        webSocket.handleError(session, exception);
    }

    /**
     * 关闭连接
     *
     * @param session     Session
     * @param closeStatus 关闭状态
     */
    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus closeStatus) {
        webSocket.handleClose(session);
    }

    /**
     * 是否支持发送部分消息
     *
     * @return false
     */
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
