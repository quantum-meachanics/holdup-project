package com.ohgiraffers.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {



    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");  // 클라이언트 구독 경로
        config.setApplicationDestinationPrefixes("/app");  // 메시지 전송 경로
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // SockJS 지원을 통해 WebSocket 연결을 설정
        // setAllowedOrigins() : 허용할 URL
        registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:3000").withSockJS();
    }
}