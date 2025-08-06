package com.museovivo.app.domain.model;

import java.util.List;

public class CulturalRoute {
    private String id;
    private String name;
    private String description;
    private String category; // historic, traditional, gastronomic, artistic
    private List<String> pointIds; // IDs de los puntos culturales en orden
    private double estimatedDurationMinutes;
    private double estimatedDistanceKm;
    private String difficulty; // easy, medium, hard
    private String imageUrl;
    private int rewardPoints;
    private boolean isDownloaded; // para modo offline
    private long createdAt;
    private String creatorId;
    private int completionCount;
    private List<String> tags;
    
    // Constructor vacío para Firebase
    public CulturalRoute() {}
    
    public CulturalRoute(String id, String name, String description, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.difficulty = "easy";
        this.rewardPoints = 50; // puntos por completar la ruta
        this.isDownloaded = false;
        this.createdAt = System.currentTimeMillis();
        this.completionCount = 0;
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
    
    public List<String> getPointIds() {
        return pointIds;
    }
    
    public void setPointIds(List<String> pointIds) {
        this.pointIds = pointIds;
    }
    
    public double getEstimatedDurationMinutes() {
        return estimatedDurationMinutes;
    }
    
    public void setEstimatedDurationMinutes(double estimatedDurationMinutes) {
        this.estimatedDurationMinutes = estimatedDurationMinutes;
    }
    
    public double getEstimatedDistanceKm() {
        return estimatedDistanceKm;
    }
    
    public void setEstimatedDistanceKm(double estimatedDistanceKm) {
        this.estimatedDistanceKm = estimatedDistanceKm;
    }
    
    public String getDifficulty() {
        return difficulty;
    }
    
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public int getRewardPoints() {
        return rewardPoints;
    }
    
    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }
    
    public boolean isDownloaded() {
        return isDownloaded;
    }
    
    public void setDownloaded(boolean downloaded) {
        isDownloaded = downloaded;
    }
    
    public long getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getCreatorId() {
        return creatorId;
    }
    
    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }
    
    public int getCompletionCount() {
        return completionCount;
    }
    
    public void setCompletionCount(int completionCount) {
        this.completionCount = completionCount;
    }
    
    public List<String> getTags() {
        return tags;
    }
    
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}