package com.simrahapp.chatboot.services;

import com.simrahapp.chatboot.entity.Notification;
import com.simrahapp.chatboot.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void createNotification(UUID uid, String message) {
        Notification n = new Notification();
        n.setUid(uid);
        n.setMessage(message);
        n.setIsRead(false);
        n.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(n);
    }

    public List<Notification> getUnread(UUID uid) {
        return notificationRepository
                .findByUidAndIsReadFalseOrderByCreatedAtDesc(uid);
    }

    public void markAllRead(UUID uid) {
        List<Notification> all = notificationRepository
                .findByUidOrderByCreatedAtDesc(uid);
        all.forEach(n -> n.setIsRead(true));
        notificationRepository.saveAll(all);
    }
}