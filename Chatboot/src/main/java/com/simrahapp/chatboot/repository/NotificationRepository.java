package com.simrahapp.chatboot.repository;

import com.simrahapp.chatboot.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByUidAndIsReadFalseOrderByCreatedAtDesc(UUID uid);
    List<Notification> findByUidOrderByCreatedAtDesc(UUID uid);
}