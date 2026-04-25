package com.simrahapp.chatboot.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "persona")
public class Persona {

    @Id
    @Column(name = "persona_id")
    private UUID personaId;

    @Column(name = "system_prompt", columnDefinition = "TEXT")
    private String systemPrompt;

    @Column(name = "persona_name")
    private String personaName;

    @Column(name = "greeting", columnDefinition = "TEXT")
    private String greeting;

    // Getters and Setters
    public UUID getPersonaId() { return personaId; }
    public void setPersonaId(UUID personaId) { this.personaId = personaId; }

    public String getSystemPrompt() { return systemPrompt; }
    public void setSystemPrompt(String systemPrompt) { this.systemPrompt = systemPrompt; }

    public String getPersonaName() { return personaName; }
    public void setPersonaName(String personaName) { this.personaName = personaName; }

    public String getGreeting() { return greeting; }
    public void setGreeting(String greeting) { this.greeting = greeting; }
}