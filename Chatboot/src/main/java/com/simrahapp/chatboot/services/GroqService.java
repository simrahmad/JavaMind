package com.simrahapp.chatboot.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import okhttp3.*;

import java.util.*;

@Service
public class GroqService {

    // ✅ HARD-CODE EVERYTHING (no Spring config)
    private final String API_KEY = System.getenv("GROQ_API_KEY");
    private final String API_URL = "https://api.groq.com/openai/v1/chat/completions";
    private final String MODEL = "llama-3.3-70b-versatile";

    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<Map<String, String>> conversationHistory = new ArrayList<>();

    public String chat(String userMessage) throws Exception {

        // add user message
        Map<String, String> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", userMessage);
        conversationHistory.add(userMsg);

        // request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", MODEL);
        requestBody.put("messages", conversationHistory);

        String jsonBody = objectMapper.writeValueAsString(requestBody);

        Request request = new Request.Builder()
                .url(API_URL)
                .post(RequestBody.create(jsonBody, MediaType.get("application/json")))
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            String responseBody = response.body().string();

            // 🔴 PRINT RESPONSE (VERY IMPORTANT)
            System.out.println("API RESPONSE: " + responseBody);

            Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);

            Object choicesObj = responseMap.get("choices");

            // ✅ SAFE CHECK
            if (choicesObj == null) {
                return "API Error: " + responseBody;
            }

            List<Map<String, Object>> choices = (List<Map<String, Object>>) choicesObj;

            Map<String, Object> firstChoice = choices.get(0);
            Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");

            String assistantReply = (String) message.get("content");

            // save assistant reply
            Map<String, String> assistantMsg = new HashMap<>();
            assistantMsg.put("role", "assistant");
            assistantMsg.put("content", assistantReply);
            conversationHistory.add(assistantMsg);

            return assistantReply;
        }
    }

    public void clearHistory() {
        conversationHistory.clear();
    }
}