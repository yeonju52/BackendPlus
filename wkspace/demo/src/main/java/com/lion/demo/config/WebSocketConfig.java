package com.lion.demo.config;

import com.lion.demo.websocket.ChattingHandshakeInterceptor;
import com.lion.demo.websocket.ChattingWebSocketHandler;
import com.lion.demo.websocket.EchoWebSocketHandler;
import com.lion.demo.websocket.PersonalWebSocketHandler;
import com.lion.demo.websocket.UserHandshakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Autowired private EchoWebSocketHandler echoWebSocketHandler;
    @Autowired private PersonalWebSocketHandler personalWebSocketHandler;
    @Autowired private ChattingWebSocketHandler chattingWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(echoWebSocketHandler, "/echo")
                .setAllowedOrigins("*");                // 모든 도메인에서 접근 가능

        registry.addHandler(personalWebSocketHandler, "/personal")
                .addInterceptors(new UserHandshakeInterceptor())        // 1 : 1 messaging
                .setAllowedOrigins("*");

        registry.addHandler(chattingWebSocketHandler, "/chat")
                .addInterceptors(new ChattingHandshakeInterceptor())    // chatting
                .setAllowedOrigins("*");
    }
}