package com.simrahapp.chatboot.controller;

import com.simrahapp.chatboot.model.ChatRequest;
import com.simrahapp.chatboot.services.FileExtractionService;
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

@Controller
public class ChatController {

    @Autowired
    private GroqService groqService;

    @Autowired
    private FileExtractionService fileExtractionService;

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
    public String chat(@RequestBody ChatRequest request, HttpSession session) {
        try {
            String message = request.getUserMessage();
            String reply;

            if (groqService.isImageRequest(message)) {
                reply = "IMAGE:" + groqService.generateImageUrl(message);
            } else {
                reply = groqService.chat(message);
            }

            return reply;

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // Handle file uploads
    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file, HttpSession session) {
        try {
            String filename = file.getOriginalFilename();
            String lower = filename != null ? filename.toLowerCase() : "";

            String reply;

            if (isImage(lower)) {
                byte[] bytes = file.getBytes();
                String base64 = Base64.getEncoder().encodeToString(bytes);
                String mimeType = file.getContentType();
                reply = groqService.chatWithImage(base64, mimeType, "Please describe and analyze this image in detail.");
            } else {
                String extractedText = fileExtractionService.extractText(file);
                if (extractedText.startsWith("Error") || extractedText.startsWith("Unsupported")) {
                    return extractedText;
                }
                String prompt = "I have uploaded a file named '" + filename + "'. "
                        + "Here is its content:\n\n" + extractedText
                        + "\n\nPlease analyze this document and give me a summary.";
                reply = groqService.chat(prompt);
            }

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
        return "Cleared!";
    }

    // Get logged in user info
    @GetMapping("/api/user")
    @ResponseBody
    public Map<String, Object> getUser(
            @AuthenticationPrincipal org.springframework.security.oauth2.core.oidc.user.OidcUser oidcUser) {

        Map<String, Object> user = new HashMap<>();

        if (oidcUser != null) {
            user.put("name", oidcUser.getFullName());
            user.put("email", oidcUser.getEmail());
            user.put("picture", oidcUser.getPicture());
            user.put("authenticated", true);
        } else {
            user.put("authenticated", false);
        }

        return user;
    }

    private boolean isImage(String filename) {
        return filename.endsWith(".jpg") || filename.endsWith(".jpeg")
                || filename.endsWith(".png") || filename.endsWith(".gif")
                || filename.endsWith(".webp") || filename.endsWith(".bmp");
    }
}