package com.example.ChatApp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws")
			.setAllowedOriginPatterns("*") // ✅ Allow all origins temporarily
			.withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes("/app");
		registry.enableSimpleBroker("/topic");
	}
	 @Override
	    public void configureClientInboundChannel(ChannelRegistration registration) {
	        registration.interceptors(new ChannelInterceptor() {
	            @Override
	            public Message<?> preSend(Message<?> message, MessageChannel channel) {
	                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
	                if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
	                    String host = accessor.getHost();
	                    // Handle Railway's proxy headers
	                    if (host != null && host.contains("railway")) {
	                        accessor.setHost(null);
	                    }
	                }
	                return message;
	            }
	        });
	    }
}
