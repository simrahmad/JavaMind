package com.simrahapp.chatboot.controller;

import com.simrahapp.chatboot.model.ChatRequest;
import com.simrahapp.chatboot.services.GroqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ChatController {

    @Autowired
    private GroqService groqService;

    @GetMapping("/")
    public String index() {
        return "redirect:/index.html";
    }

    @PostMapping("/chat")
    @ResponseBody
    public String chat(@RequestBody ChatRequest request) {
        try {
            return groqService.chat(request.getUserMessage());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/clear")
    @ResponseBody
    public String clear() {
        groqService.clearHistory();
        return "Cleared!";
    }
}
