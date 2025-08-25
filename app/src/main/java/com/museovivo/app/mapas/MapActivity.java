package com.museovivo.app.mapas;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;

import android.os.Bundle;

import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.view.LayoutInflater;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Looper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.Priority;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.museovivo.app.R;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.museovivo.app.pointdetail.PointDetailActivity;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, MapLayersBottomSheetFragment.OnMapLayerSelectedListener {

    // Google Maps
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Marker userLocationMarker;
    private boolean isFirstLocation = true;

    // Permisos
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE = 2;
    private static final int SETTINGS_REQUEST_CODE = 3;

    private static final String[] LOCATION_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private static final String[] BACKGROUND_LOCATION_PERMISSIONS = {
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
    };
    // UI Elements
    private MaterialCardView cardPointInfo;
    private TextView tvPointName, tvPointCategory, tvPointDistance, tvPointDescription;
    private ImageView ivPointImage;
    private MaterialButton btnViewDetails, btnNavigate, btnBack, btnLocation;
    private FloatingActionButton fabZoomIn, fabZoomOut, fabLayers;
    private ChipGroup chipGroupCategories;
    private BottomSheetBehavior<View> bottomSheetBehavior;

    // Datos
    private CulturalPoint selectedPoint;
    private List<CulturalPoint> allPoints;
    private List<CulturalPoint> filteredPoints;
    private Map<String, List<CulturalPoint>> pointsByCategory;
    private String currentCategory = "Todos";

    // Coordenadas de Abancay
    private static final LatLng ABANCAY_CENTER = new LatLng(-13.6333, -72.8833);

    private int currentMapType = 1;
    // 1: Normal, 2: Satélite, 3: Terreno, 4: Híbrido
    private static final String TAG = "MapActivity";
    // Constructor
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_map);
            // Inicializar cliente de ubicación
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            // Configurar request de ubicación
            locationRequest = LocationRequest.create();
            locationRequest.setInterval(5000);
            locationRequest.setFastestInterval(2000);
            locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);

            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null || mMap == null) return;
                    Location loc = locationResult.getLastLocation();
                    if (loc != null) {
                        LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
                        runOnUiThread(() -> {
                            if (userLocationMarker == null) {
                                MarkerOptions mo = new MarkerOptions().position(latLng).title("Mi ubicación").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                                userLocationMarker = mMap.addMarker(mo);
                            } else {
                                userLocationMarker.setPosition(latLng);
                            }
                            if (isFirstLocation) {
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                                isFirstLocation = false;
                            }
                        });
                    }
                }
            };
            // Inicializar datos
            initializeData();
            // Inicializar UI
            initializeUI();
            // Configurar listeners
            setupListeners();
            // Verificar permisos
            checkAndRequestLocationPermissions();
            // Configurar mapa
            setupMap();
        } catch (Exception e) {
            Log.e(TAG, "Error crítico en onCreate", e);
            Toast.makeText(this, "Error al inicializar la aplicación: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    // Inicializar datos de puntos culturales
    private void initializeData() {
        allPoints = CulturalPoint.getAllPoints();
        filteredPoints = new ArrayList<>(allPoints);
        pointsByCategory = new HashMap<>();

        // Agrupar puntos por categoría
        for (CulturalPoint point : allPoints) {
            String category = point.getCategory();
            if (!pointsByCategory.containsKey(category)) {
                pointsByCategory.put(category, new ArrayList<>());
            }
            pointsByCategory.get(category).add(point);
        }
    }
    // Inicializar elementos de la interfaz de usuario
    private void initializeUI() {
        // Inicializar elementos UI
        cardPointInfo = findViewById(R.id.card_point_info);
        if (cardPointInfo == null) Log.e(TAG, "cardPointInfo es null");
        tvPointName = findViewById(R.id.tv_point_name);
        if (tvPointName == null) Log.e(TAG, "tvPointName es null");
        tvPointCategory = findViewById(R.id.tv_point_category);
        if (tvPointCategory == null) Log.e(TAG, "tvPointCategory es null");
        tvPointDistance = findViewById(R.id.tv_point_distance);
        if (tvPointDistance == null) Log.e(TAG, "tvPointDistance es null");
        tvPointDescription = findViewById(R.id.tv_point_description);
        if (tvPointDescription == null) Log.e(TAG, "tvPointDescription es null");
        ivPointImage = findViewById(R.id.iv_point_image);
        if (ivPointImage == null) Log.e(TAG, "ivPointImage es null");
        btnViewDetails = findViewById(R.id.btn_view_details);
        btnNavigate = findViewById(R.id.btn_navigate);
        btnBack = findViewById(R.id.btn_back);
        btnLocation = findViewById(R.id.btn_location);
        fabZoomIn = findViewById(R.id.fab_zoom_in);
        fabZoomOut = findViewById(R.id.fab_zoom_out);
        fabLayers = findViewById(R.id.fab_layers);
        chipGroupCategories = findViewById(R.id.chip_group_categories);

        // Configurar BottomSheetBehavior
        View bottomSheet = findViewById(R.id.bottom_sheet);
        if (bottomSheet == null) {
            Toast.makeText(this, "Error: No se encontró el bottom sheet en el layout.", Toast.LENGTH_LONG).show();
            Log.e(TAG, "No se encontró el bottom sheet en el layout");
            bottomSheetBehavior = null;
        } else {
            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
            bottomSheet.setVisibility(View.GONE);
        }

        // Configurar chips de categorías
        setupCategoryChips();
    }
    // Configurar listeners para botones y eventos
    private void setupListeners() {
        // Botón de regreso
        btnBack.setOnClickListener(v -> {
            animateButton(v, () -> finish());
        });

        // Botón de ubicación
        btnLocation.setOnClickListener(v -> {
            animateButton(v, this::centerOnMyLocation);
        });

        // Botones de zoom
        fabZoomIn.setOnClickListener(v -> {
            if (mMap != null) {
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });
        // Animar zoom in
        fabZoomOut.setOnClickListener(v -> {
            if (mMap != null) {
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });

        // Botón de capas
        fabLayers.setOnClickListener(v -> {
            MapLayersBottomSheetFragment bottomSheet = new MapLayersBottomSheetFragment(currentMapType, false, false);
            bottomSheet.show(getSupportFragmentManager(), "MapLayersBottomSheet");
        });

        // Botones de información del punto
        btnViewDetails.setOnClickListener(v -> {
            if (selectedPoint != null) {
                showDetailedInfo(selectedPoint);
            }
        });
        // Botón de navegación
        btnNavigate.setOnClickListener(v -> {
            if (selectedPoint != null) {
                navigateToPoint(selectedPoint);
            }
        });
    }
    // Configurar chips de categorías
    private void setupCategoryChips() {
        chipGroupCategories.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;
            Chip selectedChip = findViewById(checkedIds.get(0));
            if (selectedChip != null) {
                String category = selectedChip.getText().toString();
                filterPointsByCategory(category);
            }
        });

    }
    // Filtrar puntos culturales por categoría
    private void filterPointsByCategory(String category) {
        currentCategory = category;
        filteredPoints.clear();

        if ("Todos".equals(category)) {
            filteredPoints.addAll(allPoints);
        } else {
            List<CulturalPoint> categoryPoints = pointsByCategory.get(category);
            if (categoryPoints != null) {
                filteredPoints.addAll(categoryPoints);
            }
        }

        updateMapMarkers();
    }
    // Actualizar marcadores en el mapa
    private void updateMapMarkers() {
        if (mMap == null) return;

        mMap.clear();
        for (CulturalPoint point : filteredPoints) {
            Bitmap markerBitmap = createCustomMarker(point.getImageResId(), point.getMarkerColor());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(point.getLatitude(), point.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(markerBitmap));
            mMap.addMarker(markerOptions);
        }
    }

    // Metodo para crear el marcador personalizado con color dinámico
    private Bitmap createCustomMarker(int imageResId, float markerColor) {
        View markerView = LayoutInflater.from(this).inflate(R.layout.custom_marker, null);
        ImageView markerImage = markerView.findViewById(R.id.marker_image);
        markerImage.setImageResource(imageResId);
        ImageView markerBackground = markerView.findViewById(R.id.marker_background);
        markerBackground.setColorFilter(getColorFromHue(markerColor), android.graphics.PorterDuff.Mode.SRC_IN);
        markerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        markerView.layout(0, 0, markerView.getMeasuredWidth(), markerView.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(markerView.getMeasuredWidth(), markerView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        markerView.draw(canvas);
        return bitmap;
    }
    // Configurar mapa
    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }
    // Metodo para manejar el mapa una vez que esté listo
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Configuración básica del mapa
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(false); // Usamos nuestros botones
        mMap.getUiSettings().setCompassEnabled(false); // Controlado por nuestro botón
        mMap.getUiSettings().setMyLocationButtonEnabled(false); // Usamos nuestro botón

        // Configurar listener de marcadores
        mMap.setOnMarkerClickListener(this);

        // --- NUEVO: Ocultar tarjeta al tocar el mapa ---
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (cardPointInfo.getVisibility() == View.VISIBLE) {
                    cardPointInfo.animate()
                            .alpha(0f)
                            .setDuration(250)
                            .withEndAction(() -> cardPointInfo.setVisibility(View.GONE))
                            .start();
                }
            }
        });
        // --- FIN NUEVO ---

        // Centrar en Abancay
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ABANCAY_CENTER, 14));

        // Habilitar ubicación si tenemos permisos
        if (checkLocationPermission()) {
            enableMyLocation();
        }

        // Agregar marcadores
        updateMapMarkers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMap != null && checkLocationPermission()) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_REQUEST_CODE) {
            // After user interacts with location settings dialog, try to start updates
            if (checkLocationPermission()) {
                startLocationUpdates();
            }
        }
    }
    // Manejar clics en marcadores
    @Override
    public boolean onMarkerClick(Marker marker) {
        // Verificar nulos antes de usar vistas
        if (cardPointInfo == null || tvPointName == null || tvPointCategory == null || tvPointDescription == null || tvPointDistance == null || ivPointImage == null) {
            Toast.makeText(this, "Error crítico: Vista no inicializada", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Alguna vista es null en onMarkerClick");
            return false;
        }
        // Ocultar panel anterior
        cardPointInfo.setVisibility(View.GONE);

        // Buscar el punto cultural correspondiente
        selectedPoint = findCulturalPointByMarker(marker);

        if (selectedPoint != null) {
            // Actualizar información del punto
            tvPointName.setText(selectedPoint.getName());
            tvPointCategory.setText(selectedPoint.getCategory());
            tvPointDescription.setText(selectedPoint.getDescription());

            // Calcular distancia si tenemos ubicación
            if (checkLocationPermission()) {
                calculateAndShowDistance(selectedPoint);
            } else {
                tvPointDistance.setText("Distancia no disponible");
            }

            // Cargar imagen
            loadPointImage(selectedPoint);

            // Mostrar panel con animación mejorada
            cardPointInfo.setAlpha(0f);
            cardPointInfo.setVisibility(View.VISIBLE);
            cardPointInfo.animate().alpha(1f).setDuration(300).start();

            // Animar cámara hacia el marcador
            mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));

            return true;
        }

        return false;
    }
    // Buscar el punto cultural por el marcador
    private CulturalPoint findCulturalPointByMarker(Marker marker) {
        LatLng position = marker.getPosition();

        for (CulturalPoint point : allPoints) {
            if (point.getLatitude() == position.latitude &&
                    point.getLongitude() == position.longitude) {
                return point;
            }
        }
        return null;
    }
    // Calcular y mostrar la distancia al punto cultural
    private void calculateAndShowDistance(CulturalPoint point) {
        try {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    float[] results = new float[1];
                    Location.distanceBetween(
                            location.getLatitude(), location.getLongitude(),
                            point.getLatitude(), point.getLongitude(), results
                    );
                    float distance = results[0];
                    String distanceText;
                    if (distance < 1000) {
                        distanceText = String.format("A %.0f m de tu ubicación", distance);
                    } else {
                        distanceText = String.format("A %.1f km de tu ubicación", distance / 1000);
                    }
                    tvPointDistance.setText(distanceText);
                } else {
                    tvPointDistance.setText("Distancia no disponible");
                }
            });
        } catch (SecurityException e) {
            tvPointDistance.setText("Distancia no disponible");
        }
    }
    // Cargar imagen del punto cultural
    private void loadPointImage(CulturalPoint point) {
        int imageResource = getImageResourceForPoint(point);
        ivPointImage.setImageResource(imageResource);
    }
    // Obtener recurso de imagen según el nombre del punto
    private int getImageResourceForPoint(CulturalPoint point) {
        String pointName = point.getName().toLowerCase();
        if (pointName.contains("plaza de armas")) {
            return R.drawable.plazadearmas1;
        } else if (pointName.contains("iglesia") || pointName.contains("catedral") || pointName.contains("santuario")) {
            return R.drawable.catedral;
        } else if (pointName.contains("mirador")) {
            return R.drawable.mirador;
        } else if (pointName.contains("puente")) {
            return R.drawable.puentepacha;
        } else if (pointName.contains("mercado")) {
            return R.drawable.mercadocentral;
        } else if (pointName.contains("museo")) {
            return R.drawable.museo;
        } else {
            return R.drawable.plaza_armas_placeholder;
        }
    }
    // Centrar en la ubicación del usuario
    private void centerOnMyLocation() {
        if (!checkLocationPermission()) {
            Toast.makeText(this, "Permiso de ubicación requerido", Toast.LENGTH_SHORT).show();
            checkAndRequestLocationPermissions();
            return;
        }
        Toast.makeText(this, "Obteniendo tu ubicación...", Toast.LENGTH_SHORT).show();
        try {
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16), 1000, null);
                            Toast.makeText(this, "¡Ubicación obtenida!", Toast.LENGTH_SHORT).show();
                        } else {
                            getLastKnownLocation();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error al obtener ubicación", Toast.LENGTH_SHORT).show();
                        getLastKnownLocation();
                    });
        } catch (SecurityException e) {
            Toast.makeText(this, "Error de permisos", Toast.LENGTH_SHORT).show();
            checkAndRequestLocationPermissions();
        }
    }
    // Obtener última ubicación conocida
    private void getLastKnownLocation() {
        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16), 1000, null);
                            Toast.makeText(this, "Ubicación obtenida (última conocida)", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Centrando en Abancay", Toast.LENGTH_SHORT).show();
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ABANCAY_CENTER, 14));
                        }
                    });
        } catch (SecurityException e) {
            Toast.makeText(this, "Error de permisos", Toast.LENGTH_SHORT).show();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ABANCAY_CENTER, 14));
        }
    }
    // Mostrar diálogo de configuración de capas
    private void showLayersDialog() {
        String[] options = {"Tráfico", "Edificios 3D"};
        boolean[] checkedItems = {false, false}; // Removed isTrafficEnabled and isBuildingsEnabled

        new AlertDialog.Builder(this)
                .setTitle("Configurar capas del mapa")
                .setMultiChoiceItems(options, checkedItems, (dialog, which, isChecked) -> {
                    // Removed switch cases for isTrafficEnabled and isBuildingsEnabled
                })
                .setPositiveButton("Aceptar", null)
                .show();
    }
    private void showDetailedInfo(CulturalPoint point) {
        Intent intent = new Intent(this, PointDetailActivity.class);
        intent.putExtra("point_name", point.getName());
        intent.putExtra("point_description", point.getDescription());
        intent.putExtra("point_history", point.getHistory());
        intent.putExtra("point_schedule", point.getSchedule());
        intent.putExtra("point_entry_fee", point.getEntryFee());
        intent.putExtra("point_contact", point.getContact());
        intent.putExtra("latitude", point.getLatitude());
        intent.putExtra("longitude", point.getLongitude());
        intent.putExtra("image_resource", getImageResourceForPoint(point));
        // Añadir el tipo de variante explícito para forzar el layout correcto en la Activity de detalle
        String variant = mapVariantForPoint(point);
        intent.putExtra("point_variant", variant);
        startActivity(intent);
    }

    // Determina una etiqueta de variante simple basada en la categoría o el nombre.
    private String mapVariantForPoint(CulturalPoint point) {
        String name = point.getName() == null ? "" : point.getName().toLowerCase();
        String category = point.getCategory() == null ? "" : point.getCategory().toLowerCase();

        if (category.contains("plaza") || name.contains("plaza")) return "plaza";
        if (category.contains("iglesia") || category.contains("catedral") || name.contains("iglesia") || name.contains("catedral") || name.contains("santuario")) return "iglesia";
        if (category.contains("mirador") || name.contains("mirador")) return "mirador";
        if (category.contains("puente") || name.contains("puente")) return "puente";
        if (category.contains("mercado") || name.contains("mercado")) return "mercado";
        if (category.contains("museo") || name.contains("museo")) return "museo";
        return "default";
    }
    // Navegar al punto
    private void navigateToPoint(CulturalPoint point) {
        String uri = "google.navigation:q=" + point.getLatitude() + "," + point.getLongitude();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            String webUri = "https://www.google.com/maps/dir/?api=1&destination=" +
                    point.getLatitude() + "," + point.getLongitude();
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUri));
            startActivity(webIntent);
        }
    }
    // Botones de animación y acción
    private void animateButton(View button, Runnable action) {
        button.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(100)
                .withEndAction(() -> {
                    button.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .withEndAction(action)
                            .start();
                })
                .start();
    }
    // Métodos de permisos
    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
    // Solicitar permisos
    private void checkAndRequestLocationPermissions() {
        if (!checkLocationPermission()) {
            showLocationPermissionDialog();
        }
    }
    // Solicitar permisos
    private void showLocationPermissionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Permiso de Ubicación")
                .setMessage("Esta aplicación necesita acceso a tu ubicación para mostrarte puntos culturales cercanos.")
                .setPositiveButton("Permitir", (dialog, which) -> {
                    ActivityCompat.requestPermissions(this, LOCATION_PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
    // Habilitar ubicación
    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            // Ensure device settings allow location
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);
            SettingsClient client = LocationServices.getSettingsClient(this);
            Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
            task.addOnSuccessListener(response -> startLocationUpdates());
            task.addOnFailureListener(e -> {
                if (e instanceof ResolvableApiException) {
                    try {
                        ((ResolvableApiException) e).startResolutionForResult(this, SETTINGS_REQUEST_CODE);
                    } catch (Exception ex) {
                        startLocationUpdates();
                    }
                } else {
                    startLocationUpdates();
                }
            });
        }
    }

    private void startLocationUpdates() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) return;
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        } catch (Exception e) {
            Log.e(TAG, "Error starting location updates", e);
        }
    }

    private void stopLocationUpdates() {
        try {
            if (fusedLocationClient != null && locationCallback != null) fusedLocationClient.removeLocationUpdates(locationCallback);
        } catch (Exception e) {
            Log.e(TAG, "Error stopping location updates", e);
        }
    }
    // Manejar resultado de solicitud de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            // Verificar el estado real de permisos (puede variar el orden de permisos devueltos)
            if (checkLocationPermission()) {
                enableMyLocation();
                Toast.makeText(this, "¡Permisos concedidos!", Toast.LENGTH_SHORT).show();
            } else {
                // Mostrar diálogo que ofrezca abrir ajustes de la app para otorgar permisos
                new AlertDialog.Builder(this)
                        .setTitle("Permiso de Ubicación Denegado")
                        .setMessage("La funcionalidad de ubicación estará limitada. ¿Deseas abrir los ajustes de la app para habilitar los permisos?")
                        .setPositiveButton("Abrir ajustes", (dialog, which) -> openAppSettings())
                        .setNegativeButton("Cerrar", null)
                        .show();
            }
        }
    }
// Abrir ajustes de la aplicación
    private void openAppSettings() {
        try {
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "No se pudo abrir ajustes", e);
            Toast.makeText(this, "No se pudo abrir los ajustes. Habilita permisos manualmente.", Toast.LENGTH_LONG).show();
        }
    }
    // Manejar cambios en el mapa
    @Override
    public void onMapTypeSelected(int mapType) {
        if (mMap == null) return;
        currentMapType = mapType;
        switch (mapType) {
            case 1:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case 2:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case 3:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case 4:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
        }
    }
    // Clase CulturalPoint
    public static class CulturalPoint {
        private String name, description, shortDescription, history, schedule, entryFee, contact, category;
        private double latitude, longitude;
        private float markerColor;
        private int imageResId; // Nuevo campo para el recurso de imagen

        public CulturalPoint(String name, String description, String shortDescription,
                             String history, String schedule, String entryFee, String contact,
                             String category, double latitude, double longitude, float markerColor, int imageResId) {
            this.name = name;
            this.description = description;
            this.shortDescription = shortDescription;
            this.history = history;
            this.schedule = schedule;
            this.entryFee = entryFee;
            this.contact = contact;
            this.category = category;
            this.latitude = latitude;
            this.longitude = longitude;
            this.markerColor = markerColor;
            this.imageResId = imageResId;
        }
        // Getters
        public String getName() { return name; }
        public String getDescription() { return description; }
        public String getShortDescription() { return shortDescription; }
        public String getHistory() { return history; }
        public String getSchedule() { return schedule; }
        public String getEntryFee() { return entryFee; }
        public String getContact() { return contact; }
        public String getCategory() { return category; }
        public double getLatitude() { return latitude; }
        public double getLongitude() { return longitude; }
        public float getMarkerColor() { return markerColor; }
        public int getImageResId() { return imageResId; }
        // Clase para obtener todos los puntos
        public static List<CulturalPoint> getAllPoints() {
            List<CulturalPoint> points = new ArrayList<>();

            points.add(new CulturalPoint(
                    "Plaza de Armas de Abancay",
                    "La plaza principal de Abancay, corazón de la ciudad y centro histórico",
                    "Corazón de la ciudad",
                    "La plaza de Armas está ubicada en el centro de la Ciudad de Abancay " +
                            "entre el Jr. Lima y Jr Puno, a una altitud de 2 350 m.s.n.m., presenta una forma cuadrangular, " +
                            "sus principales atractivos son la glorieta que está ubicada en la parte central de la Plaza, alrededor " +
                            "de ésta se ubican 7 palmeras cada una representando a una provincia de la región de Apurímac y al frente " +
                            "de la plaza se encuentra la Catedral de la ciudad. Cuenta la historia que antiguamente la zona de la plaza " +
                            "de armas era un cementerio.",
                    "Abierto 24 horas",
                    "Gratuito",
                    "Municipalidad de Abancay",
                    "Plazas",
                    -13.637333, -72.879142,
                    BitmapDescriptorFactory.HUE_BLUE,
                    R.drawable.plazadearmas1
            ));
            points.add(new CulturalPoint(
                    "Iglesia de la Virgen del Rosario",
                    "Templo colonial de gran belleza arquitectónica",
                    "Templo colonial histórico",
                    "Construida en el siglo XVII, es una de las iglesias más antiguas de la región.",
                    "Lunes a Domingo: 7:00 AM - 7:00 PM",
                    "Gratuito",
                    "Parroquia de la Virgen del Rosario",
                    "Iglesias",
                    -13.6373373, -72.8784776,
                    BitmapDescriptorFactory.HUE_YELLOW,
                    R.drawable.catedral
            ));
            points.add(new CulturalPoint(
                    "Puente Pachachaca",
                    "Puente colonial histórico sobre el río Pachachaca",
                    "Puente histórico colonial",
                    "Construido en el siglo XVIII, este puente de piedra conectaba Abancay con Cusco.",
                    "Abierto 24 horas",
                    "Gratuito",
                    "Municipalidad de Abancay",
                    "Monumentos",
                    -13.6630436, -72.9372847,
                    BitmapDescriptorFactory.HUE_RED,
                    R.drawable.puentepacha
            ));
            points.add(new CulturalPoint(
                    "El Mirador de Taraccasa",
                    "Vista panorámica de la ciudad y los valles",
                    "Vista panorámica espectacular",
                    "Es ideal para observar el panorama de la ciudad de Abancay y Tamburco. En la parte más elevada se alza una cruz gigante, símbolo de protección. En fechas trascendentales se ilumina con mensajes de saludo y bienvenida al valle.",
                    "Lunes a Domingo: 6:00 AM - 6:00 PM",
                    "Gratuito",
                    "Municipalidad de Abancay",
                    "Miradores",
                    -13.6226755, -72.8662554,
                    BitmapDescriptorFactory.HUE_GREEN,
                    R.drawable.mirador
            ));
            points.add(new CulturalPoint(
                    "Casa Hacienda de Illanya",
                    "Patrimonio cultural prehispánico de las culturas Wari y Chanka",
                    "Se ubica a 4 km de la ciudad de Abancay (15 min vía terrestre).",
                    "Ubicado en la antigua Casa Hacienda Illanya, exhibe más de 200 bienes culturales incluyendo cerámica, textiles, metales, instrumentos textiles, queros, sandalias de cuero y una momia. Destaca su colección de objetos de las culturas Wari y Chanka hallados en Apurímac.",
                    "Lunes a Domingo: 5:00 AM - 8:00 PM",
                    "Adultos: S/3.00 | Escolares: S/1.00 | Universitarios: Gratis",
                    "Dirección Regional de Cultura Apurímac",
                    "Museos",
                    -13.6507399, -72.9017527,
                    BitmapDescriptorFactory.HUE_ORANGE,
                    R.drawable.museo
            ));
            points.add(new CulturalPoint(
                    "Mercado Central de Abancay",
                    "Centro comercial tradicional con productos locales",
                    "Mercado tradicional local",
                    "El mercado más importante de la ciudad, donde se pueden encontrar productos frescos.",
                    "Lunes a Domingo: 5:00 AM - 8:00 PM",
                    "Gratuito",
                    "Municipalidad de Abancay",
                    "Mercados",
                    -13.6363291, -72.8779927,
                    BitmapDescriptorFactory.HUE_CYAN,R.drawable.mercadocentral
            ));

            return points;
        }
    }
    // Métodos de ciclo de vida
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fusedLocationClient != null) {
            fusedLocationClient = null;
        }
        if (mMap != null) {
            mMap.clear();
            mMap = null;
        }
    }
    // Convierte el valor de tono a un color específico
    private int getColorFromHue(float hue) {
        if (hue == BitmapDescriptorFactory.HUE_BLUE) return ContextCompat.getColor(this, R.color.blue);
        if (hue == BitmapDescriptorFactory.HUE_YELLOW) return ContextCompat.getColor(this, R.color.yellow);
        if (hue == BitmapDescriptorFactory.HUE_RED) return ContextCompat.getColor(this, R.color.red);
        if (hue == BitmapDescriptorFactory.HUE_GREEN) return ContextCompat.getColor(this, R.color.green);
        if (hue == BitmapDescriptorFactory.HUE_ORANGE) return ContextCompat.getColor(this, R.color.orange);
        if (hue == BitmapDescriptorFactory.HUE_CYAN) return ContextCompat.getColor(this, R.color.cyan);
        return ContextCompat.getColor(this, R.color.gray);
    }
}