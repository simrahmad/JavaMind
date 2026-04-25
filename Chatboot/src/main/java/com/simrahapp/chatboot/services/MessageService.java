package com.simrahapp.chatboot.services;

import com.simrahapp.chatboot.entity.Message;
import com.simrahapp.chatboot.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    // Save a single message (user or assistant)
    public Message saveMessage(UUID conversationId,
                               String role, String content) {
        Message message = new Message();
        message.setConversationId(conversationId);
        message.setRole(role);
        message.setContent(content);
        message.setCreatedAt(LocalDateTime.now());
        return messageRepository.save(message);
    }

    // Get full chat history for a conversation
    public List<Message> getMessages(UUID conversationId) {
        return messageRepository
                .findByConversationIdOrderByCreatedAtAsc(conversationId);
    }
}