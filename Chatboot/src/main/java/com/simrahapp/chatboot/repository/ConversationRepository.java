package com.simrahapp.chatboot.repository;

import com.simrahapp.chatboot.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

    // Get all conversations for a user, newest first
    List<Conversation> findByUidOrderByUpdatedAtDesc(UUID uid);
}