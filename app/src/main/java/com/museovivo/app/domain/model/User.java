package com.museovivo.app.domain.model;

import java.util.List;

public class User {
    private String uid;
    private String email;
    private String displayName;
    private String profileImageUrl;
    private int totalPoints;
    private List<String> unlockedAchievements;
    private List<String> visitedLocations;
    private long createdAt;
    private long lastLoginAt;
    
    // Constructor vacío para Firebase
    public User() {}
    
    public User(String uid, String email, String displayName) {
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
        this.totalPoints = 0;
        this.createdAt = System.currentTimeMillis();
        this.lastLoginAt = System.currentTimeMillis();
    }
    
    // Getters y Setters
    public String getUid() {
        return uid;
    }
    
    public void setUid(String uid) {
        this.uid = uid;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    public String getProfileImageUrl() {
        return profileImageUrl;
    }
    
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
    
    public int getTotalPoints() {
        return totalPoints;
    }
    
    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }
    
    public List<String> getUnlockedAchievements() {
        return unlockedAchievements;
    }
    
    public void setUnlockedAchievements(List<String> unlockedAchievements) {
        this.unlockedAchievements = unlockedAchievements;
    }
    
    public List<String> getVisitedLocations() {
        return visitedLocations;
    }
    
    public void setVisitedLocations(List<String> visitedLocations) {
        this.visitedLocations = visitedLocations;
    }
    
    public long getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
    
    public long getLastLoginAt() {
        return lastLoginAt;
    }
    
    public void setLastLoginAt(long lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }
}