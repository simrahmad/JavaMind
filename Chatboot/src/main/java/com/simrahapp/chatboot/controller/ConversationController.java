package com.simrahapp.chatboot.controller;

import com.simrahapp.chatboot.entity.Conversation;
import com.simrahapp.chatboot.entity.Message;
import com.simrahapp.chatboot.entity.QuickReply;
import org.springframework.http.ResponseEntity;
import com.simrahapp.chatboot.entity.User;
import org.springframework.http.ResponseEntity;
import com.simrahapp.chatboot.services.ConversationService;
import com.simrahapp.chatboot.services.MessageService;
import com.simrahapp.chatboot.services.QuickReplyService;
import com.simrahapp.chatboot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import com.simrahapp.chatboot.entity.UserFeedback;
import com.simrahapp.chatboot.repository.UserFeedbackRepository;
import com.simrahapp.chatboot.services.UserFeedbackService;
import com.simrahapp.chatboot.entity.UserPreference;
import com.simrahapp.chatboot.repository.UserPreferenceRepository;
import com.simrahapp.chatboot.services.UserPreferenceService;
import com.simrahapp.chatboot.entity.Notification;
import com.simrahapp.chatboot.repository.NotificationRepository;
import com.simrahapp.chatboot.services.NotificationService;

@RestController
@RequestMapping("/api")
public class ConversationController {


    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserFeedbackService userFeedbackService;

    @Autowired
    private UserPreferenceService userPreferenceService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private QuickReplyService quickReplyService;

    // Get latest quick replies for a message
    @GetMapping("/quick-replies/{messageId}")
    public List<QuickReply> getQuickReplies(@PathVariable UUID messageId) {
        return quickReplyService.getQuickReplies(messageId);
    }

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    // Start a new conversation
    @PostMapping("/conversations/new")
    @ResponseBody
    public ResponseEntity<?> newConversation(
            @AuthenticationPrincipal
            org.springframework.security.oauth2.core.oidc.user.OidcUser oidcUser) {

        if (oidcUser == null) {
            return ResponseEntity.status(401)
                    .body("{\"error\":\"Not authenticated\"}");
        }

        try {
            User user = userService.saveOrUpdateUser(
                    oidcUser.getEmail(),
                    oidcUser.getFullName(),
                    null,
                    oidcUser.getPicture()
            );
            Conversation conv = conversationService
                    .startNewConversation(user.getUid());
            return ResponseEntity.ok(conv);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    // Get all conversations for logged in user
    @GetMapping("/conversations")
    public ResponseEntity<?> getConversations(
            @AuthenticationPrincipal
            org.springframework.security.oauth2.core.oidc.user.OidcUser oidcUser) {
        try {
            if (oidcUser == null) return ResponseEntity.ok(List.of());
            User user = userService.findByEmail(oidcUser.getEmail()).orElse(null);
            if (user == null) return ResponseEntity.ok(List.of());
            return ResponseEntity.ok(conversationService.getUserConversations(user.getUid()));
        } catch (Exception e) {
            return ResponseEntity.ok(List.of());
        }
    }

    // Get all messages in a conversation
    @GetMapping("/conversations/{conversationId}/messages")
    public List<Message> getMessages(@PathVariable UUID conversationId) {
        return messageService.getMessages(conversationId);
    }

    // Save theme preference
    @PostMapping("/api/preferences/theme")
    public ResponseEntity<?> saveTheme(
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal
            org.springframework.security.oauth2.core.oidc.user.OidcUser oidcUser) {
        try {
            User user = userService.findByEmail(oidcUser.getEmail()).orElseThrow();
            String theme = body.get("theme");
            userPreferenceService.savePreference(user.getUid(), theme);
            return ResponseEntity.ok(Map.of("saved", true));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("saved", false));
        }
    }

    // Get saved preference
    @GetMapping("/api/preferences")
    public ResponseEntity<?> getPreferences(
            @AuthenticationPrincipal
            org.springframework.security.oauth2.core.oidc.user.OidcUser oidcUser) {
        try {
            User user = userService.findByEmail(oidcUser.getEmail()).orElseThrow();
            UserPreference pref = userPreferenceService.getPreference(user.getUid());
            if (pref != null) return ResponseEntity.ok(pref);
            return ResponseEntity.ok(Map.of("theme", "arctic"));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("theme", "arctic"));
        }
    }

    @PostMapping("/api/feedback")
    public ResponseEntity<?> submitFeedback(
            @RequestBody Map<String, Object> body,
            @AuthenticationPrincipal
            org.springframework.security.oauth2.core.oidc.user.OidcUser oidcUser) {
        try {
            User user = userService.findByEmail(oidcUser.getEmail()).orElseThrow();
            int rating = (int) body.get("rating");
            String comment = (String) body.getOrDefault("comment", "");
            userFeedbackService.saveFeedback(user.getUid(), rating, comment);
            return ResponseEntity.ok(Map.of("saved", true));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("saved", false));
        }
    }

    @GetMapping("/api/notifications")
    public List<Notification> getNotifications(
            @AuthenticationPrincipal
            org.springframework.security.oauth2.core.oidc.user.OidcUser oidcUser) {
        User user = userService.findByEmail(oidcUser.getEmail()).orElseThrow();
        return notificationService.getUnread(user.getUid());
    }

    @PostMapping("/api/notifications/read")
    public ResponseEntity<?> markRead(
            @AuthenticationPrincipal
            org.springframework.security.oauth2.core.oidc.user.OidcUser oidcUser) {
        User user = userService.findByEmail(oidcUser.getEmail()).orElseThrow();
        notificationService.markAllRead(user.getUid());
        return ResponseEntity.ok(Map.of("done", true));
    }

}