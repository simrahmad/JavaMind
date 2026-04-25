package com.simrahapp.chatboot.services;

import com.simrahapp.chatboot.entity.Conversation;
import com.simrahapp.chatboot.repository.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    // Start a brand new conversation
    public Conversation startNewConversation(UUID userId) {
        Conversation conv = new Conversation();
        conv.setUid(userId);
        conv.setTitle("New Chat");
        conv.setIsArchived(false);
        conv.setStartedAt(LocalDateTime.now());
        conv.setUpdatedAt(LocalDateTime.now());
        return conversationRepository.save(conv);
    }

    public void autoTitle(UUID conversationId, String firstMessage) {
        conversationRepository.findById(conversationId).ifPresent(conv -> {
            // Use first 40 chars of first message as title
            String title = firstMessage.length() > 40
                    ? firstMessage.substring(0, 40) + "..."
                    : firstMessage;
            conv.setTitle(title);
            conv.setUpdatedAt(LocalDateTime.now());
            conversationRepository.save(conv);
        });
    }

    // Get all conversations for a user
    public List<Conversation> getUserConversations(UUID userId) {
        return conversationRepository.findByUidOrderByUpdatedAtDesc(userId);
    }

    // Update conversation title
    public void updateTitle(UUID conversationId, String title) {
        conversationRepository.findById(conversationId).ifPresent(conv -> {
            conv.setTitle(title);
            conv.setUpdatedAt(LocalDateTime.now());
            conversationRepository.save(conv);
        });
    }

    public void updatePersona(UUID conversationId, UUID personaId) {
        conversationRepository.findById(conversationId).ifPresent(conv -> {
            conv.setPersonaId(personaId);
            conv.setUpdatedAt(LocalDateTime.now());
            conversationRepository.save(conv);
        });
    }
}