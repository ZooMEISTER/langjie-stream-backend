package com.langjie.langjiestreambackend.config;

import com.langjie.langjiestreambackend.interceptor.DefaultWebSocketHandler;
import com.langjie.langjiestreambackend.interceptor.WebSocketInterceptor;
import com.langjie.langjiestreambackend.service.WebSocket;
import com.langjie.langjiestreambackend.service.impl.WebSocketImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @Author ZooMEISTER
 * @Description: WebSocket Config
 * @DateTime 2024/8/21 9:43
 **/

@Configuration
@EnableWebSocket
@EnableTransactionManagement
public class WebSocketConfiguration implements WebSocketConfigurer {

    private DefaultWebSocketHandler defaultWebSocketHandler;
    private WebSocketInterceptor webSocketInterceptor;

    public WebSocketConfiguration(DefaultWebSocketHandler defaultWebSocketHandler, WebSocketInterceptor webSocketInterceptor) {
        this.defaultWebSocketHandler = defaultWebSocketHandler;
        this.webSocketInterceptor = webSocketInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
        // 添加处理器，拦截器，允许跨域
        registry.addHandler(defaultWebSocketHandler, "langjie-stream/chat")
                .addInterceptors(webSocketInterceptor)
                .setAllowedOrigins("*");
    }
}
