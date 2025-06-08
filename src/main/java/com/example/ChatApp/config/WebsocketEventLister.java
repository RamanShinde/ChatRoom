package com.example.ChatApp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.example.ChatApp.Entity.ChatMessage;
import com.example.ChatApp.Entity.Messagetype;

import org.springframework.context.event.EventListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebsocketEventLister {

	private final SimpMessageSendingOperations messagingTemplate;

	@EventListener
	public void handleWebsocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

		String username = (String) headerAccessor.getSessionAttributes().get("username");

		if (username != null) {
			log.info("User disconnected: {}", username);

			ChatMessage chatMessage = ChatMessage.builder()
			        .messagetype(Messagetype.leave)
			        .sender(username)
			        .build();


			messagingTemplate.convertAndSend("/topic/public", chatMessage);
		}
	}
}
