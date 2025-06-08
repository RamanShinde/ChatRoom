package com.example.ChatApp.Contoller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.example.ChatApp.Entity.ChatMessage;

@Controller
public class ChatContoller {
	@MessageMapping("/chat.sendmessage")
	@SendTo("/topic/public")
	public ChatMessage sendmessage(@Payload ChatMessage chatmessage) {
		return chatmessage;
	}

	@MessageMapping("/chat.adduser")
	@SendTo("/topic/public")
	public ChatMessage adduser(@Payload ChatMessage chatmessage, SimpMessageHeaderAccessor accessor) {
		accessor.getSessionAttributes().put("username", chatmessage.getSender());
		return chatmessage;
	}
}
