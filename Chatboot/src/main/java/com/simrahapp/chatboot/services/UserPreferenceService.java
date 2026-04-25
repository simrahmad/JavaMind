package com.simrahapp.chatboot.services;

import com.simrahapp.chatboot.entity.UserPreference;
import com.simrahapp.chatboot.repository.UserPreferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class UserPreferenceService {

    @Autowired
    private UserPreferenceRepository userPreferenceRepository;

    // Save or update preference
    public UserPreference savePreference(UUID uid, String theme) {
        UserPreference pref = userPreferenceRepository
                .findByUid(uid)
                .orElse(new UserPreference());
        pref.setUid(uid);
        pref.setTheme(theme);
        pref.setFontSize("medium");
        pref.setLanguage("english");
        pref.setNotificationEnabled(true);
        return userPreferenceRepository.save(pref);
    }

    // Get preference for user
    public UserPreference getPreference(UUID uid) {
        return userPreferenceRepository
                .findByUid(uid)
                .orElse(null);
    }
}