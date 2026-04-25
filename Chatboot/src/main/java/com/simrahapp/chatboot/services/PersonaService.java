package com.simrahapp.chatboot.services;

import com.simrahapp.chatboot.entity.Persona;
import com.simrahapp.chatboot.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    public List<Persona> getAllPersonas() {
        return personaRepository.findAll();
    }

    public Optional<Persona> getPersonaById(UUID personaId) {
        return personaRepository.findById(personaId);
    }

    // Default persona is JavaMind
    public Persona getDefaultPersona() {
        return personaRepository.findById(
                UUID.fromString("55555555-5555-5555-5555-555555555555")
        ).orElse(null);
    }
}