package com.n1nt3nd0.realtimechatservice.controller;



import com.n1nt3nd0.realtimechatservice.model.ChatMessage;
import com.n1nt3nd0.realtimechatservice.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class ChatController {

  private final ChatService chatService;

  @Autowired
  public ChatController(ChatService chatService) {
    this.chatService = chatService;
  }

  @MessageMapping("/chat/sendMessage/{convId}")
  public ChatMessage sendMessageToConvId(
      @Payload ChatMessage chatMessage,
      SimpMessageHeaderAccessor headerAccessor,
      @DestinationVariable("convId") String conversationId) {
    chatService.sendMessageToConvId(chatMessage, conversationId, headerAccessor);
    return chatMessage;
  }
}
