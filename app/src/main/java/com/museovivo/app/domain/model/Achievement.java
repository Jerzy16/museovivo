package com.museovivo.app.domain.model;

public class Achievement {
    private String id;
    private String name;
    private String description;
    private String category; // explorer, historian, photographer, contributor
    private String iconUrl;
    private int pointsRequired;
    private String badgeType; // bronze, silver, gold
    private String condition; // visit_5_places, upload_content, complete_route
    private boolean isUnlocked;
    private long unlockedAt;
    
    // Constructor vacío para Firebase
    public Achievement() {}
    // Constructor con parámetros
    public Achievement(String id, String name, String description, String category, 
                      int pointsRequired, String badgeType) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.pointsRequired = pointsRequired;
        this.badgeType = badgeType;
        this.isUnlocked = false;
        this.unlockedAt = 0;
    }
    
    // Getters y Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getIconUrl() {
        return iconUrl;
    }
    
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
    
    public int getPointsRequired() {
        return pointsRequired;
    }
    
    public void setPointsRequired(int pointsRequired) {
        this.pointsRequired = pointsRequired;
    }
    
    public String getBadgeType() {
        return badgeType;
    }
    
    public void setBadgeType(String badgeType) {
        this.badgeType = badgeType;
    }
    
    public String getCondition() {
        return condition;
    }
    
    public void setCondition(String condition) {
        this.condition = condition;
    }
    
    public boolean isUnlocked() {
        return isUnlocked;
    }
    
    public void setUnlocked(boolean unlocked) {
        isUnlocked = unlocked;
    }
    
    public long getUnlockedAt() {
        return unlockedAt;
    }
    
    public void setUnlockedAt(long unlockedAt) {
        this.unlockedAt = unlockedAt;
    }
}