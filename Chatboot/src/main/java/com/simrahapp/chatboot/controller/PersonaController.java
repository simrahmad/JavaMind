package com.simrahapp.chatboot.controller;

import com.simrahapp.chatboot.entity.Conversation;
import com.simrahapp.chatboot.entity.Persona;
import com.simrahapp.chatboot.services.ConversationService;
import com.simrahapp.chatboot.services.PersonaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PersonaController {

    @Autowired
    private PersonaService personaService;

    @Autowired
    private ConversationService conversationService;

    // Get all personas for sidebar
    @GetMapping("/personas")
    public List<Persona> getAllPersonas() {
        return personaService.getAllPersonas();
    }

    // Switch persona mid-conversation
    @PostMapping("/personas/switch")
    public Map<String, Object> switchPersona(
            @RequestBody Map<String, String> body,
            HttpSession session) {

        String personaIdStr = body.get("personaId");
        UUID personaId = UUID.fromString(personaIdStr);

        // Update session
        session.setAttribute("personaId", personaId);

        // Update conversation in DB if one exists
        UUID conversationId = (UUID) session.getAttribute("conversationId");
        if (conversationId != null) {
            conversationService.updatePersona(conversationId, personaId);
        }

        // Get persona greeting
        Persona persona = personaService.getPersonaById(personaId).orElse(null);
        String greeting = persona != null ? persona.getGreeting() : "Hello! How can I help?";
        String name = persona != null ? persona.getPersonaName() : "JavaMind";

        return Map.of(
                "success", true,
                "greeting", greeting,
                "personaName", name
        );
    }
}