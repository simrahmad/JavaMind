package com.simrahapp.chatboot.services;

import com.simrahapp.chatboot.entity.User;
import com.simrahapp.chatboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Save new user or update existing one
    public User saveOrUpdateUser(String email, String firstName,
                                 String lastName, String profilePicture) {
        Optional<User> existing = userRepository.findByEmail(email);

        if (existing.isPresent()) {
            // User exists — just update last login
            User user = existing.get();
            user.setLastLogin(LocalDateTime.now());
            return userRepository.save(user);
        } else {
            // New user — save everything
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setProfilePicture(profilePicture);
            newUser.setCreatedAt(LocalDateTime.now());
            newUser.setLastLogin(LocalDateTime.now());
            return userRepository.save(newUser);
        }
    }

    // Find user by email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}