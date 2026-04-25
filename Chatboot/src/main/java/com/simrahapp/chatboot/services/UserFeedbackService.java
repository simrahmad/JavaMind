package com.simrahapp.chatboot.services;

import com.simrahapp.chatboot.entity.UserFeedback;
import com.simrahapp.chatboot.repository.UserFeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserFeedbackService {

    @Autowired
    private UserFeedbackRepository userFeedbackRepository;

    public UserFeedback saveFeedback(UUID uid, int rating, String comment) {
        UserFeedback feedback = new UserFeedback();
        feedback.setUid(uid);
        feedback.setRating(rating);
        feedback.setComment(comment);
        feedback.setSubmittedAt(LocalDateTime.now());
        feedback.setIsRead(false);
        return userFeedbackRepository.save(feedback);
    }
}