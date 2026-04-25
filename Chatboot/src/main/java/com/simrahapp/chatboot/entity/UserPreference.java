package com.simrahapp.chatboot.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "user_preference")
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "preference_id")
    private UUID preferenceId;

    @Column(name = "uid")
    private UUID uid;

    @Column(name = "theme")
    private String theme;

    @Column(name = "font_size")
    private String fontSize;

    @Column(name = "language")
    private String language;

    @Column(name = "notification_enabled")
    private Boolean notificationEnabled;

    // Getters and Setters
    public UUID getPreferenceId() { return preferenceId; }
    public void setPreferenceId(UUID preferenceId) { this.preferenceId = preferenceId; }

    public UUID getUid() { return uid; }
    public void setUid(UUID uid) { this.uid = uid; }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public String getFontSize() { return fontSize; }
    public void setFontSize(String fontSize) { this.fontSize = fontSize; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public Boolean getNotificationEnabled() { return notificationEnabled; }
    public void setNotificationEnabled(Boolean n) { this.notificationEnabled = n; }
}