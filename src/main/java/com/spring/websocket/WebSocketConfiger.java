package com.spring.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiger implements WebSocketMessageBrokerConfigurer {


    public void configureMessageBroker(MessageBrokerRegistry config) {
        //允许订阅的前缀
        config.enableSimpleBroker("/topic","/user");

        //路由到controller
        config.setApplicationDestinationPrefixes("/app");
    }


    //注册指定名称的Stomp协议的webSocket
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/socket").withSockJS();
    }

}
