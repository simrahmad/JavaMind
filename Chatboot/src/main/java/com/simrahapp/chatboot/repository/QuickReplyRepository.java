package com.simrahapp.chatboot.repository;

import com.simrahapp.chatboot.entity.QuickReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface QuickReplyRepository extends JpaRepository<QuickReply, UUID> {

    // Get quick replies for a specific message
    List<QuickReply> findByMessageId(UUID messageId);

    // Delete old quick replies for a message
    void deleteByMessageId(UUID messageId);
}