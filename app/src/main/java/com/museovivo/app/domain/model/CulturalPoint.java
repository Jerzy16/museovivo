package com.museovivo.app.domain.model;

import java.util.List;

public class CulturalPoint {
    private String id;
    private String name;
    private String description;
    private String category; // monument, tradition, legend, recipe, dance
    private double latitude;
    private double longitude;
    private String imageUrl;
    private List<String> mediaUrls; // fotos, videos, audios adicionales
    private String qrCode;
    private int pointsValue;
    private boolean hasAR; // tiene contenido de realidad aumentada
    private String arModelUrl;
    private long createdAt;
    private String creatorId;
    private int visitCount;
    private List<String> tags;
    
    // Constructor vacío para Firebase
    public CulturalPoint() {}
    
    public CulturalPoint(String id, String name, String description, String category, 
                        double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pointsValue = 10; // puntos por defecto
        this.hasAR = false;
        this.createdAt = System.currentTimeMillis();
        this.visitCount = 0;
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
    
    public double getLatitude() {
        return latitude;
    }
    
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    
    public double getLongitude() {
        return longitude;
    }
    
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public List<String> getMediaUrls() {
        return mediaUrls;
    }
    
    public void setMediaUrls(List<String> mediaUrls) {
        this.mediaUrls = mediaUrls;
    }
    
    public String getQrCode() {
        return qrCode;
    }
    
    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
    
    public int getPointsValue() {
        return pointsValue;
    }
    
    public void setPointsValue(int pointsValue) {
        this.pointsValue = pointsValue;
    }
    
    public boolean isHasAR() {
        return hasAR;
    }
    
    public void setHasAR(boolean hasAR) {
        this.hasAR = hasAR;
    }
    
    public String getArModelUrl() {
        return arModelUrl;
    }
    
    public void setArModelUrl(String arModelUrl) {
        this.arModelUrl = arModelUrl;
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
    
    public int getVisitCount() {
        return visitCount;
    }
    
    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }
    
    public List<String> getTags() {
        return tags;
    }
    
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}