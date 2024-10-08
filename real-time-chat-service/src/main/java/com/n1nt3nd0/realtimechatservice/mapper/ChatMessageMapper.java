package com.n1nt3nd0.realtimechatservice.mapper;



import com.n1nt3nd0.realtimechatservice.config.UserDetailsImpl;
import com.n1nt3nd0.realtimechatservice.entity.ConversationEntity;
import com.n1nt3nd0.realtimechatservice.entity.UserEntity;
import com.n1nt3nd0.realtimechatservice.model.ChatMessage;
import com.n1nt3nd0.realtimechatservice.model.MessageType;
import com.n1nt3nd0.realtimechatservice.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ChatMessageMapper {

  private final UserRepository userRepository;

  public ChatMessageMapper(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<ChatMessage> toChatMessages(
      List<ConversationEntity> conversationEntities,
      UserDetailsImpl userDetails,
      MessageType messageType) {
    List<UUID> fromUsersIds =
        conversationEntities.stream().map(ConversationEntity::getFromUser).toList();
    Map<UUID, String> fromUserIdsToUsername =
        userRepository.findAllById(fromUsersIds).stream()
            .collect(Collectors.toMap(UserEntity::getId, UserEntity::getUsername));

    return conversationEntities.stream()
        .map(e -> toChatMessage(e, userDetails, fromUserIdsToUsername, messageType))
        .toList();
  }

  private static ChatMessage toChatMessage(
      ConversationEntity e,
      UserDetailsImpl userDetails,
      Map<UUID, String> fromUserIdsToUsername,
      MessageType messageType) {
    return ChatMessage.builder()
        .id(e.getId())
        .messageType(messageType)
        .content(e.getContent())
        .receiverId(e.getToUser())
        .receiverUsername(userDetails.getUsername())
        .senderId(e.getFromUser())
        .senderUsername(fromUserIdsToUsername.get(e.getFromUser()))
        .build();
  }
}
