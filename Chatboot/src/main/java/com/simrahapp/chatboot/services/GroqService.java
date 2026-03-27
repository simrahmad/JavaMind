package com.simrahapp.chatboot.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import okhttp3.*;

import java.util.*;

@Service
public class GroqService {

    private final String API_KEY = "Private_key";
    private final String API_URL = "URL";
    private final String MODEL = "MODEL";
    private final String VISION_MODEL = "VISION_MODEL";

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<Map<String, Object>> conversationHistory = new ArrayList<>();

    // ── Regular text chat ─────────────────────────────────────
    public String chat(String userMessage) throws Exception {
        Map<String, Object> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", userMessage);
        conversationHistory.add(userMsg);

        return callAPI(MODEL, conversationHistory);
    }
    //Image detection method
    public boolean isImageRequest(String message) {
        String lower = message.toLowerCase();
        return lower.contains("generate image") ||
                lower.contains("create image") ||
                lower.contains("create an image") ||
                lower.contains("generate an image") ||
                lower.contains("make an image") ||
                lower.contains("draw an image") ||
                lower.contains("draw a") ||
                lower.contains("draw me") ||
                lower.contains("make a picture") ||
                lower.contains("create a picture") ||
                lower.contains("generate a picture") ||
                lower.contains("create a photo") ||
                lower.contains("generate a photo") ||
                lower.contains("make me an image") ||
                lower.contains("make me a picture");
    }
   //image generator method
   public String generateImageUrl(String prompt) {
       String cleanedPrompt = prompt
               .replaceAll("(?i)generate image of", "")
               .replaceAll("(?i)create image of", "")
               .replaceAll("(?i)create an image of", "")
               .replaceAll("(?i)generate an image of", "")
               .replaceAll("(?i)draw a", "")
               .replaceAll("(?i)draw me", "")
               .replaceAll("(?i)make an image of", "")
               .trim();

       String encodedPrompt = java.net.URLEncoder.encode(
               cleanedPrompt,
               java.nio.charset.StandardCharsets.UTF_8
       );

       return "https://picsum.photos/seed/" + encodedPrompt + "/800/600";
   }

    // ── Image chat using vision model ─────────────────────────
    public String chatWithImage(String base64Image, String mimeType, String prompt) throws Exception {

        // Build vision message with image + text
        List<Map<String, Object>> contentList = new ArrayList<>();

        Map<String, Object> imageContent = new HashMap<>();
        imageContent.put("type", "image_url");
        Map<String, String> imageUrl = new HashMap<>();
        imageUrl.put("url", "data:" + mimeType + ";base64," + base64Image);
        imageContent.put("image_url", imageUrl);

        Map<String, Object> textContent = new HashMap<>();
        textContent.put("type", "text");
        textContent.put("text", prompt);

        contentList.add(imageContent);
        contentList.add(textContent);

        Map<String, Object> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", contentList);

        List<Map<String, Object>> messages = new ArrayList<>();
        messages.add(userMsg);

        return callAPI(VISION_MODEL, messages);
    }

    // ── Shared API caller ─────────────────────────────────────
    private String callAPI(String model, List<Map<String, Object>> messages) throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("messages", messages);

        String jsonBody = objectMapper.writeValueAsString(requestBody);

        Request request = new Request.Builder()
                .url(API_URL)
                .post(RequestBody.create(jsonBody, MediaType.get("application/json")))
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = response.body().string();
            System.out.println("API RESPONSE: " + responseBody);

            Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);
            Object choicesObj = responseMap.get("choices");

            if (choicesObj == null) {
                return "API Error: " + responseBody;
            }

            List<Map<String, Object>> choices = (List<Map<String, Object>>) choicesObj;
            Map<String, Object> firstChoice = choices.get(0);
            Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");

            String reply = (String) message.get("content");

            // Save to history (text only)
            Map<String, Object> assistantMsg = new HashMap<>();
            assistantMsg.put("role", "assistant");
            assistantMsg.put("content", reply);
            conversationHistory.add(assistantMsg);

            return reply;
        }
    }

    public void clearHistory() {
        conversationHistory.clear();
    }
}