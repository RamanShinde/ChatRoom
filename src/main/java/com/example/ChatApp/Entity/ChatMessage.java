package com.example.ChatApp.Entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessage {
	private String content;
	private String sender;
	private Messagetype messagetype;
}
