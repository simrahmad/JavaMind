package com.simrahapp.chatboot.repository;

import com.simrahapp.chatboot.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    // Get all messages in a conversation, oldest first
    List<Message> findByConversationIdOrderByCreatedAtAsc(UUID conversationId);
}