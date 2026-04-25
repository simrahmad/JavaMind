package com.simrahapp.chatboot.services;

import com.simrahapp.chatboot.entity.Session;
import com.simrahapp.chatboot.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    public Session startSession(UUID uid, String deviceType, String ip) {
        Session session = new Session();
        session.setUid(uid);
        session.setStartedAt(LocalDateTime.now());
        session.setDeviceType(deviceType);
        session.setAddressIp(ip);
        return sessionRepository.save(session);
    }

    public void endSession(UUID uid) {
        Session session = sessionRepository
                .findTopByUidOrderByStartedAtDesc(uid);
        if (session != null) {
            session.setEndedAt(LocalDateTime.now());
            sessionRepository.save(session);
        }
    }
}