package com.simrahapp.chatboot.controller;

import com.simrahapp.chatboot.entity.Conversation;
import com.simrahapp.chatboot.entity.Persona;
import com.simrahapp.chatboot.services.PersonaService;
import com.simrahapp.chatboot.entity.Message;
import com.simrahapp.chatboot.entity.User;
import com.simrahapp.chatboot.entity.FileUploadLog;
import com.simrahapp.chatboot.services.FileUploadLogService;
import com.simrahapp.chatboot.model.ChatRequest;
import com.simrahapp.chatboot.repository.SessionRepository;
import com.simrahapp.chatboot.entity.Session;
import com.simrahapp.chatboot.services.SessionService;
import com.simrahapp.chatboot.services.ConversationService;   // ← ADD
import com.simrahapp.chatboot.services.MessageService;        // ← ADD
import com.simrahapp.chatboot.services.UserService;           // ← ADD
import com.simrahapp.chatboot.services.FileExtractionService;
import com.simrahapp.chatboot.services.QuickReplyService;
import com.simrahapp.chatboot.entity.QuickReply;
import java.util.List;
import com.simrahapp.chatboot.services.ImageUploadService;
import  com.simrahapp.chatboot.entity.ImageUploadLog;
import com.simrahapp.chatboot.repository.ImageUploadRepository;
import com.simrahapp.chatboot.services.GroqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import jakarta.servlet.http.HttpSession;
import java.util.Base64;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

import com.simrahapp.chatboot.entity.Notification;
import com.simrahapp.chatboot.repository.NotificationRepository;
import com.simrahapp.chatboot.services.NotificationService;

@Controller
public class ChatController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private FileUploadLogService fileUploadLogService;

    @Autowired
    private GroqService groqService;

     @Autowired
    private QuickReplyService quickReplyService;

    @Autowired
    private FileExtractionService fileExtractionService;

    @Autowired
    private UserService userService;           // ← ADD

    @Autowired
    private MessageService messageService;     // ← ADD

    @Autowired
    private ConversationService conversationService;  // ← ADD

    @Autowired
    private PersonaService personaService;

    // Serve main page
    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }

    // Login page
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Handle text messages
    @PostMapping("/chat")
    @ResponseBody
    public String chat(@RequestBody ChatRequest request, HttpSession session,
                       @AuthenticationPrincipal
                       org.springframework.security.oauth2.core.oidc.user.OidcUser oidcUser) {
        try {
            String message = request.getUserMessage();
            String reply;

            if (groqService.isImageRequest(message)) {
                reply = "IMAGE:" + groqService.generateImageUrl(message);
            } else {
                // Get active persona system prompt
                UUID personaId = (UUID) session.getAttribute("personaId");
                Persona activePersona = null;
                if (personaId != null) {
                    activePersona = personaService.getPersonaById(personaId).orElse(null);
                }
                if (activePersona == null) {
                    activePersona = personaService.getDefaultPersona();
                }

                reply = groqService.chatWithPersona(message, activePersona != null ? activePersona.getSystemPrompt() : null);
            }

            // ── SAVE TO DATABASE ──────────────────────────────
            if (oidcUser != null) {
                try {
                    User user = userService.saveOrUpdateUser(
                            oidcUser.getEmail(),
                            oidcUser.getFullName(),
                            null,
                            oidcUser.getPicture()
                    );

                    UUID conversationId = (UUID) session.getAttribute("conversationId");
                    if (conversationId == null) {
                        Conversation conv = conversationService.startNewConversation(user.getUid());
                        conversationId = conv.getConversationId();
                        session.setAttribute("conversationId", conversationId);
                    }

                    messageService.saveMessage(conversationId, "user", message);

                    List<Message> existing = messageService.getMessages(conversationId);
                    if (existing.size() <= 1) {
                        conversationService.autoTitle(conversationId, message);
                    }

                    Message savedAiMsg = messageService.saveMessage(conversationId, "assistant", reply);
                    quickReplyService.generateAndSave(conversationId, savedAiMsg.getMessageId());

                } catch (Exception e) {
                    System.out.println("DB save error: " + e.getMessage());
                }
            }
            // ─────────────────────────────────────────────────

            return reply;

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // Handle file uploads (unchanged)
    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file,
                         HttpSession session,
                         @AuthenticationPrincipal
                         org.springframework.security.oauth2.core.oidc.user.OidcUser oidcUser) {
        try {
            String filename = file.getOriginalFilename();
            String lower = filename != null ? filename.toLowerCase() : "";
            String reply;
            String extractedText = "";

            if (isImage(lower)) {
                byte[] bytes = file.getBytes();
                String base64 = Base64.getEncoder().encodeToString(bytes);
                String mimeType = file.getContentType();
                reply = groqService.chatWithImage(base64, mimeType,
                        "Please describe and analyze this image in detail.");
                // ← ADD THIS BLOCK
                if (oidcUser != null) {
                    try {
                        User user = userService.saveOrUpdateUser(
                                oidcUser.getEmail(), oidcUser.getFullName(),
                                null, oidcUser.getPicture());
                        UUID conversationId = (UUID) session.getAttribute("conversationId");
                        if (conversationId == null) {
                            Conversation conv = conversationService.startNewConversation(user.getUid());
                            conversationId = conv.getConversationId();
                            session.setAttribute("conversationId", conversationId);
                        }
                        Message savedMsg = messageService.saveMessage(
                                conversationId, "user", "Uploaded image: " + filename);
                        messageService.saveMessage(conversationId, "assistant", reply);
                        imageUploadService.logImageUpload(
                                savedMsg.getMessageId(), file.getSize());
                    } catch (Exception e) {
                        System.out.println("Image log error: " + e.getMessage());
                    }
                }
            }
             else {
                extractedText = fileExtractionService.extractText(file);
                if (extractedText.startsWith("Error") ||
                        extractedText.startsWith("Unsupported")) {
                    return extractedText;
                }
                String prompt = "I have uploaded a file named '" + filename + "'. "
                        + "Here is its content:\n\n" + extractedText
                        + "\n\nPlease analyze this document and give me a summary.";
                reply = groqService.chat(prompt);
            }

            // ── LOG FILE UPLOAD TO DATABASE ───────────────────
            if (oidcUser != null) {
                try {
                    User user = userService.saveOrUpdateUser(
                            oidcUser.getEmail(),
                            oidcUser.getFullName(),
                            null,
                            oidcUser.getPicture()
                    );

                    UUID conversationId = (UUID) session.getAttribute("conversationId");
                    if (conversationId == null) {
                        Conversation conv = conversationService
                                .startNewConversation(user.getUid());
                        conversationId = conv.getConversationId();
                        session.setAttribute("conversationId", conversationId);
                    }

                    // Save user message
                    String userMsg = "Uploaded file: " + filename;
                    Message savedMsg = messageService.saveMessage(
                            conversationId, "user", userMsg);

                    // Save AI reply
                    messageService.saveMessage(
                            conversationId, "assistant", reply);

                    // Log file details
                    String fileType = lower.substring(lower.lastIndexOf('.') + 1);
                    fileUploadLogService.logFileUpload(
                            savedMsg.getMessageId(),
                            fileType,
                            extractedText.length()
                    );

                } catch (Exception e) {
                    System.out.println("File log error: " + e.getMessage());
                }
            }
            // ──────────────────────────────────────────────────

            return reply;

        } catch (Exception e) {
            return "Error processing file: " + e.getMessage();
        }
    }
    // Clear conversation
    @PostMapping("/clear")
    @ResponseBody
    public String clear(HttpSession session) {
        groqService.clearHistory();
        session.removeAttribute("conversationId"); // ← ADD this line
        return "Cleared!";
    }

    // Get logged in user info
    @GetMapping("/api/user")
    @ResponseBody
    public Map<String, Object> getUser(
            @AuthenticationPrincipal
            org.springframework.security.oauth2.core.oidc.user.OidcUser oidcUser,
            jakarta.servlet.http.HttpServletRequest request) {  // ← ADD THIS

        Map<String, Object> userMap = new HashMap<>();

        if (oidcUser != null) {
            User savedUser = userService.saveOrUpdateUser(
                    oidcUser.getEmail(),
                    oidcUser.getFullName(),
                    null,
                    oidcUser.getPicture()
            );

            // ✅ Session tracking goes HERE
            String ip = request.getRemoteAddr();
            String device = request.getHeader("User-Agent");
            sessionService.startSession(savedUser.getUid(), device, ip);

            notificationService.createNotification(
                    savedUser.getUid(),
                    "Welcome back, " + oidcUser.getFullName() + "! 👋"
            );

            userMap.put("name", oidcUser.getFullName());
            userMap.put("email", oidcUser.getEmail());
            userMap.put("picture", oidcUser.getPicture());
            userMap.put("authenticated", true);
        } else {
            userMap.put("authenticated", false);
        }

        return userMap;
    }

    private boolean isImage(String filename) {
        return filename.endsWith(".jpg") || filename.endsWith(".jpeg")
                || filename.endsWith(".png") || filename.endsWith(".gif")
                || filename.endsWith(".webp") || filename.endsWith(".bmp");
    }
}