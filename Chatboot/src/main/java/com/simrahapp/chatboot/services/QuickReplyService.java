package com.simrahapp.chatboot.services;

import com.simrahapp.chatboot.entity.Message;
import com.simrahapp.chatboot.entity.QuickReply;
import com.simrahapp.chatboot.repository.MessageRepository;
import com.simrahapp.chatboot.repository.QuickReplyRepository;
import com.simrahapp.chatboot.services.GroqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class QuickReplyService {

    @Autowired
    private QuickReplyRepository quickReplyRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private GroqService groqService;

    // Generate and save quick replies after every AI response
    public List<QuickReply> generateAndSave(UUID conversationId, UUID messageId) {
        try {
            // Get last 6 messages from this conversation
            List<Message> messages = messageRepository
                    .findByConversationIdOrderByCreatedAtAsc(conversationId);

            // Extract just the text content
            List<String> pastMessages = new ArrayList<>();
            int start = Math.max(0, messages.size() - 6);
            for (int i = start; i < messages.size(); i++) {
                Message m = messages.get(i);
                pastMessages.add(m.getRole().toUpperCase() + ": " + m.getContent());
            }

            // Ask Groq to generate 3 suggestions
            List<String> suggestions = groqService.generateQuickReplies(pastMessages);

            // Delete old quick replies for this message
            quickReplyRepository.deleteByMessageId(messageId);

            // Save new ones
            List<QuickReply> saved = new ArrayList<>();
            String[] categories = {"followup", "clarify", "example"};

            for (int i = 0; i < suggestions.size(); i++) {
                QuickReply qr = new QuickReply();
                qr.setMessageId(messageId);
                qr.setMessageText(suggestions.get(i));
                qr.setCategory(categories[i % categories.length]);
                qr.setCreatedAt(LocalDateTime.now());
                saved.add(quickReplyRepository.save(qr));
            }

            return saved;

        } catch (Exception e) {
            System.out.println("Quick reply generation failed: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Get quick replies for a message
    public List<QuickReply> getQuickReplies(UUID messageId) {
        return quickReplyRepository.findByMessageId(messageId);
    }
}