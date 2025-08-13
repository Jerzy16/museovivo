package com.museovivo.app.domain.model;

import java.util.List;
import java.util.ArrayList;

public class CulturalPoint {
    private String id;
    private String name;
    private String description;
    private String category; // historic, traditional, gastronomic, artistic
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
    
    // Constructor completo
    public CulturalPoint(String id, String name, String description, String category, 
                        double latitude, double longitude, String imageUrl, int pointsValue, 
                        boolean hasAR, String arModelUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageUrl = imageUrl;
        this.pointsValue = pointsValue;
        this.hasAR = hasAR;
        this.arModelUrl = arModelUrl;
        this.createdAt = System.currentTimeMillis();
        this.visitCount = 0;
    }
    
    // Método estático para obtener todos los puntos culturales de Abancay
    public static List<CulturalPoint> getAllPoints() {
        List<CulturalPoint> points = new ArrayList<>();
        
        // Plaza de Armas de Abancay
        points.add(new CulturalPoint(
            "plaza_armas_001",
            "Plaza de Armas de Abancay",
            "La plaza principal de Abancay, corazón de la ciudad y centro histórico. Ubicada entre el Jr. Lima y Jr Puno, a una altitud de 2,350 m.s.n.m., presenta una forma cuadrangular. Sus principales atractivos son la glorieta central y las 7 palmeras que representan a cada provincia de la región de Apurímac.",
            "Plazas",
            -13.637333, -72.879142,
            "plaza_armas_abancay.jpg",
            15,
            true,
            "plaza_armas_3d.glb"
        ));
        
        // Iglesia de la Virgen del Rosario
        points.add(new CulturalPoint(
            "iglesia_001",
            "Iglesia de la Virgen del Rosario",
            "Templo colonial de gran belleza arquitectónica construido en el siglo XVII. Es una de las iglesias más antiguas de la región y representa un importante patrimonio religioso y cultural de Abancay.",
            "Iglesias",
            -13.6373373, -72.8784776,
            "iglesia_virgen_rosario.jpg",
            12,
            false,
            null
        ));
        
        // Puente Pachachaca
        points.add(new CulturalPoint(
            "puente_001",
            "Puente Pachachaca",
            "Puente colonial histórico sobre el río Pachachaca construido en el siglo XVIII. Este puente de piedra conectaba Abancay con Cusco y es un testimonio de la ingeniería colonial peruana.",
            "Monumentos",
            -13.6630436, -72.9372847,
            "puente_pachachaca.jpg",
            18,
            true,
            "puente_pachachaca_3d.glb"
        ));
        
        // Mirador de Taraccasa
        points.add(new CulturalPoint(
            "mirador_001",
            "El Mirador de Taraccasa",
            "Vista panorámica espectacular de la ciudad y los valles. Es ideal para observar el panorama de Abancay y Tamburco. En la parte más elevada se alza una cruz gigante, símbolo de protección.",
            "Miradores",
            -13.6226755, -72.8662554,
            "mirador_taraccasa.jpg",
            10,
            false,
            null
        ));
        
        // Casa Hacienda de Illanya
        points.add(new CulturalPoint(
            "museo_001",
            "Casa Hacienda de Illanya",
            "Patrimonio cultural prehispánico que exhibe más de 200 bienes culturales incluyendo cerámica, textiles, metales, instrumentos textiles, queros, sandalias de cuero y una momia. Destaca su colección de objetos de las culturas Wari y Chanka.",
            "Museos",
            -13.6507399, -72.9017527,
            "casa_hacienda_illanya.jpg",
            20,
            true,
            "casa_hacienda_3d.glb"
        ));
        
        // Mercado Central de Abancay
        points.add(new CulturalPoint(
            "mercado_001",
            "Mercado Central de Abancay",
            "Centro comercial tradicional con productos locales. El mercado más importante de la ciudad, donde se pueden encontrar productos frescos y artesanías típicas de la región.",
            "Mercados",
            -13.6363291, -72.8779927,
            "mercado_central.jpg",
            8,
            false,
            null
        ));
        
        // Parque de la Libertad
        points.add(new CulturalPoint(
            "parque_001",
            "Parque de la Libertad",
            "Espacio verde recreativo en el centro de Abancay. Ideal para paseos familiares y actividades al aire libre. Cuenta con áreas de descanso y juegos infantiles.",
            "Parques",
            -13.6350000, -72.8800000,
            "parque_libertad.jpg",
            5,
            false,
            null
        ));
        
        return points;
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