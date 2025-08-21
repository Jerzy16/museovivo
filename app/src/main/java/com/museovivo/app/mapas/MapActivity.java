package com.museovivo.app.mapas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.cardview.widget.CardView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

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
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.museovivo.app.R;
import com.museovivo.app.domain.model.CulturalPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, MapLayersBottomSheetFragment.OnMapLayerSelectedListener {
    // Google Maps
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

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
    private CardView cardPointInfo;
    private TextView tvPointName, tvPointCategory, tvPointDistance, tvPointDescription;
    private ImageView ivPointImage;
    private MaterialButton btnViewDetails, btnNavigate, btnBack, btnLocation;
    private FloatingActionButton fabZoomIn, fabZoomOut, fabLayers, fabFilter;
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

    private void checkGoogleMapsAPIKey() {
        try {
            // Verificar si la API key está configurada
            String apiKey = getString(R.string.google_maps_key);
            if (apiKey == null || apiKey.isEmpty() || apiKey.equals("YOUR_API_KEY_HERE")) {
                Log.e(TAG, "API Key de Google Maps no configurada");
                Toast.makeText(this, "Error: API Key de Google Maps no configurada", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al verificar API Key: " + e.getMessage());
        }
    }

    private void testMapFunctionality() {
        try {
            // Probar funcionalidades básicas del mapa
            if (mMap != null) {
                // Probar zoom
                mMap.animateCamera(CameraUpdateFactory.zoomIn());

                // Probar movimiento de cámara
                mMap.animateCamera(CameraUpdateFactory.newLatLng(ABANCAY_CENTER));

                // Probar marcadores
                mMap.addMarker(new MarkerOptions()
                    .position(ABANCAY_CENTER)
                    .title("Centro de Abancay")
                    .snippet("Punto de prueba"));

                Log.d(TAG, "Funcionalidades del mapa probadas correctamente");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al probar funcionalidades del mapa: " + e.getMessage());
        }
    }

    private void initializeData() {
        try {
            // Inicializar listas
            allPoints = new ArrayList<>();
            filteredPoints = new ArrayList<>();
            pointsByCategory = new HashMap<>();

            // Crear puntos culturales de ejemplo
            CulturalPoint plaza = new CulturalPoint();
            plaza.setId("1");
            plaza.setName("Plaza de Armas de Abancay");
            plaza.setCategory("Plaza");
            plaza.setDescription("La plaza principal de Abancay, corazón de la ciudad y centro histórico.");
            plaza.setLatitude(-13.6333);
            plaza.setLongitude(-72.8833);
            plaza.setImageUrl("plazadearmas1");

            CulturalPoint iglesia = new CulturalPoint();
            iglesia.setId("2");
            iglesia.setName("Iglesia de Abancay");
            iglesia.setCategory("Iglesia");
            iglesia.setDescription("Iglesia histórica con arquitectura colonial.");
            iglesia.setLatitude(-13.6350);
            iglesia.setLongitude(-72.8850);
            iglesia.setImageUrl("iglesia1");

            CulturalPoint museo = new CulturalPoint();
            museo.setId("3");
            museo.setName("Museo Regional");
            museo.setCategory("Museo");
            museo.setDescription("Museo que preserva la historia y cultura de la región.");
            museo.setLatitude(-13.6310);
            museo.setLongitude(-72.8810);
            museo.setImageUrl("museo1");

            CulturalPoint parque = new CulturalPoint();
            parque.setId("4");
            parque.setName("Parque Central");
            parque.setCategory("Parque");
            parque.setDescription("Parque recreativo con áreas verdes y juegos.");
            parque.setLatitude(-13.6370);
            parque.setLongitude(-72.8870);
            parque.setImageUrl("parque1");

            // Agregar puntos a la lista
            allPoints.add(plaza);
            allPoints.add(iglesia);
            allPoints.add(museo);
            allPoints.add(parque);

            // Agrupar por categoría
            pointsByCategory.put("Todos", allPoints);
            pointsByCategory.put("Plaza", List.of(plaza));
            pointsByCategory.put("Iglesia", List.of(iglesia));
            pointsByCategory.put("Museo", List.of(museo));
            pointsByCategory.put("Parque", List.of(parque));

            filteredPoints = new ArrayList<>(allPoints);

            Log.d(TAG, "Datos inicializados: " + allPoints.size() + " puntos culturales");

        } catch (Exception e) {
            Log.e(TAG, "Error al inicializar datos: " + e.getMessage());
        }
    }

    private void initializeUI() {
        try {
            // Inicializar elementos de UI
            cardPointInfo = findViewById(R.id.card_point_info);
            tvPointName = findViewById(R.id.tv_point_name);
            tvPointCategory = findViewById(R.id.tv_point_category);
            tvPointDistance = findViewById(R.id.tv_point_distance);
            tvPointDescription = findViewById(R.id.tv_point_description);
            ivPointImage = findViewById(R.id.iv_point_image);
            btnViewDetails = findViewById(R.id.btn_view_details);
            btnNavigate = findViewById(R.id.btn_navigate);
            btnBack = findViewById(R.id.btn_back);
            btnLocation = findViewById(R.id.btn_location);
            fabZoomIn = findViewById(R.id.fab_zoom_in);
            fabZoomOut = findViewById(R.id.fab_zoom_out);
            fabLayers = findViewById(R.id.fab_layers);
            fabFilter = findViewById(R.id.fab_filter);
            chipGroupCategories = findViewById(R.id.chip_group_categories);

            // Configurar bottom sheet
            View bottomSheet = findViewById(R.id.bottom_sheet);
            if (bottomSheet != null) {
                bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }

            // Verificar que todos los elementos se inicializaron correctamente
            if (cardPointInfo == null) Log.e(TAG, "cardPointInfo es null");
            if (tvPointName == null) Log.e(TAG, "tvPointName es null");
            if (tvPointCategory == null) Log.e(TAG, "tvPointCategory es null");
            if (tvPointDistance == null) Log.e(TAG, "tvPointDistance es null");
            if (tvPointDescription == null) Log.e(TAG, "tvPointDescription es null");
            if (ivPointImage == null) Log.e(TAG, "ivPointImage es null");
            if (btnViewDetails == null) Log.e(TAG, "btnViewDetails es null");
            if (btnNavigate == null) Log.e(TAG, "btnNavigate es null");
            if (btnBack == null) Log.e(TAG, "btnFilter es null");
            if (btnLocation == null) Log.e(TAG, "btnCloseFilter es null");
            if (fabZoomIn == null) Log.e(TAG, "fabZoomIn es null");
            if (fabZoomOut == null) Log.e(TAG, "fabZoomOut es null");
            if (fabLayers == null) Log.e(TAG, "fabLayers es null");
            if (fabFilter == null) Log.e(TAG, "fabFilter es null");
            if (chipGroupCategories == null) Log.e(TAG, "chipGroupCategories es null");

            Log.d(TAG, "UI inicializada correctamente");

        } catch (Exception e) {
            Log.e(TAG, "Error al inicializar UI: " + e.getMessage());
        }
    }

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

            // Botones de zoom
            if (fabZoomIn != null) {
                fabZoomIn.setOnClickListener(v -> {
                    Log.d(TAG, "Botón zoom in presionado");
                    if (mMap != null) {
                        mMap.animateCamera(CameraUpdateFactory.zoomIn());
                    }
                });
            }

            if (fabZoomOut != null) {
                fabZoomOut.setOnClickListener(v -> {
                    Log.d(TAG, "Botón zoom out presionado");
                    if (mMap != null) {
                        mMap.animateCamera(CameraUpdateFactory.zoomOut());
                    }
                });
            }

            // Botón de capas
            if (fabLayers != null) {
                fabLayers.setOnClickListener(v -> {
                    Log.d(TAG, "Botón de capas presionado");
                    showLayersDialog();
                });
            }

            // Botón de filtro (toolbar)
            if (btnBack != null) {
                btnBack.setOnClickListener(v -> {
                    Log.d(TAG, "Botón de filtro (toolbar) presionado");
                    showFilterBottomSheet();
                });
            }

            // Botón de filtro (FAB)
            if (fabFilter != null) {
                fabFilter.setOnClickListener(v -> {
                    Log.d(TAG, "Botón de filtro (FAB) presionado");
                    showFilterBottomSheet();
                });
            }

            // Botón de cerrar filtro
            if (btnLocation != null) {
                btnLocation.setOnClickListener(v -> {
                    Log.d(TAG, "Botón de cerrar filtro presionado");
                    hideFilterBottomSheet();
                });
            }

            // Botón de ver detalles
            if (btnViewDetails != null) {
                btnViewDetails.setOnClickListener(v -> {
                    Log.d(TAG, "Botón de ver detalles presionado");
                    if (selectedPoint != null) {
                        showDetailedInfo(selectedPoint);
                    } else {
                        Log.w(TAG, "selectedPoint es null para btnViewDetails");
                    }
                });
            }

            // Botón de navegar
            if (btnNavigate != null) {
                btnNavigate.setOnClickListener(v -> {
                    Log.d(TAG, "Botón de navegar presionado");
                    if (selectedPoint != null) {
                        navigateToPoint(selectedPoint);
                    } else {
                        Log.w(TAG, "selectedPoint es null para btnNavigate");
                    }
                });
            }
    }
// Configurar chips de categoría
// Dentro de tu archivo C:\museovivo\app\src\main\java\com\museovivo\app\mapas\MapActivity.java
// ...
private void setupCategoryChips() {
    try {
        if (chipGroupCategories != null) {
            chipGroupCategories.setOnCheckedChangeListener((group, checkedId) -> { // <--- LÍNEA CORREGIDA
                // checkedId es ahora un int, no una List<Integer>
                // Representa el ID del chip seleccionado o View.NO_ID si ninguno está seleccionado.

                String selectedCategory = "Todos"; // Valor por defecto

                if (checkedId == View.NO_ID) {

                    Log.d(TAG, "Ninguna categoría seleccionada (o selección borrada)");
                    // Si no quieres filtrar o hacer nada cuando se deselecciona, puedes añadir un return aquí:
                    // return;
                    // O, si quieres que "Todos" sea el default cuando se deselecciona:
                    filterPointsByCategory("Todos");
                    hideFilterBottomSheet();
                    return; // Salir después de manejar la deselección
                }

                // Ahora usas directamente checkedId (que es un int)
                if (checkedId == R.id.chip_all) { // Asegúrate de tener R.id.chip_all si es un chip válido
                    selectedCategory = "Todos";
                } else if (checkedId == R.id.chip_plaza) {
                    selectedCategory = "Plaza";
                } else if (checkedId == R.id.chip_church) {
                    selectedCategory = "Iglesia";
                } else if (checkedId == R.id.chip_museum) {
                    selectedCategory = "Museo";
                } else if (checkedId == R.id.chip_park) {
                    selectedCategory = "Parque";
                }

                Log.d(TAG, "Categoría seleccionada: " + selectedCategory + " con ID: " + checkedId);
                filterPointsByCategory(selectedCategory);
                hideFilterBottomSheet();
            });

            Log.d(TAG, "Chips de categoría configurados correctamente");
        } else {
            Log.e(TAG, "chipGroupCategories es null en setupCategoryChips");
        }
    } catch (Exception e) {
        Log.e(TAG, "Error al configurar chips de categoría: " + e.getMessage(), e); // Añade 'e' para el stack trace
    }
}
// ...


    private void filterPointsByCategory(String category) {
        try {
            Log.d(TAG, "Filtrando puntos por categoría: " + category);
            currentCategory = category;

            if (category.equals("Todos")) {
                filteredPoints = new ArrayList<>(allPoints);
            } else {
                filteredPoints = pointsByCategory.getOrDefault(category, new ArrayList<>());
            }

            Log.d(TAG, "Puntos filtrados: " + filteredPoints.size());
            updateMapMarkers();

            if (filteredPoints.isEmpty()) {
                Log.w(TAG, "No se encontraron puntos para la categoría: " + category);
                Toast.makeText(this, "No hay puntos en esta categoría", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e(TAG, "Error al filtrar puntos: " + e.getMessage());
        }
    }

    private void updateMapMarkers() {
        try {
            if (mMap == null) return;

            // Limpiar marcadores existentes
            mMap.clear();

            // Agregar nuevos marcadores
            for (CulturalPoint point : filteredPoints) {
                MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(point.getLatitude(), point.getLongitude()))
                    .title(point.getName())
                    .snippet(point.getCategory());

                // Crear marcador personalizado según la categoría
                if (point.getCategory().equals("Plaza")) {
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_plaza));
                } else if (point.getCategory().equals("Iglesia")) {
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_church));
                } else if (point.getCategory().equals("Museo")) {
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_museum));
                } else if (point.getCategory().equals("Parque")) {
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_park));
                }

                mMap.addMarker(markerOptions);
            }

        } catch (Exception e) {
            Log.e(TAG, "Error al actualizar marcadores: " + e.getMessage());
        }
    }

    private void setupMap() {
        try {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);

            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            } else {
                Log.e(TAG, "MapFragment no encontrado, creando programáticamente");
                createMapFragmentProgrammatically();
            }

        } catch (Exception e) {
            Log.e(TAG, "Error al configurar mapa: " + e.getMessage());
        }
    }

    private void createMapFragmentProgrammatically() {
        try {
            SupportMapFragment mapFragment = SupportMapFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.map_fragment, mapFragment)
                .commit();
            mapFragment.getMapAsync(this);

        } catch (Exception e) {
            Log.e(TAG, "Error al crear MapFragment programáticamente: " + e.getMessage());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;

            // Configurar mapa
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.getUiSettings().setZoomControlsEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            // Configurar listeners
            mMap.setOnMarkerClickListener(this);
            mMap.setOnMapClickListener(latLng -> {
                // Ocultar información del punto al hacer clic en el mapa
                if (cardPointInfo != null) {
                    cardPointInfo.setVisibility(View.GONE);
                }
            });

            // Mover cámara al centro de Abancay
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ABANCAY_CENTER, 15f));

            // Actualizar marcadores
            updateMapMarkers();

            // Habilitar ubicación si se tienen permisos
            if (checkLocationPermission()) {
                enableMyLocation();
            }

            Log.d(TAG, "Mapa configurado correctamente");

        } catch (Exception e) {
            Log.e(TAG, "Error en onMapReady: " + e.getMessage());
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        try {
            // Encontrar el punto cultural correspondiente
            CulturalPoint point = findCulturalPointByMarker(marker);
            if (point != null) {
                selectedPoint = point;

                // Mostrar información del punto
                if (tvPointName != null) tvPointName.setText(point.getName());
                if (tvPointCategory != null) tvPointCategory.setText(point.getCategory());
                if (tvPointDescription != null) tvPointDescription.setText(point.getDescription());

                // Calcular y mostrar distancia
                calculateAndShowDistance(point);

                // Cargar imagen del punto
                loadPointImage(point);

                // Mostrar tarjeta de información
                if (cardPointInfo != null) {
                    cardPointInfo.setVisibility(View.VISIBLE);
                }

                // Animar marcador
                marker.showInfoWindow();

                return true;
            }

        } catch (Exception e) {
            Log.e(TAG, "Error en onMarkerClick: " + e.getMessage());
        }

        return false;
    }

    private CulturalPoint findCulturalPointByMarker(Marker marker) {
        try {
            LatLng markerPosition = marker.getPosition();

            for (CulturalPoint point : filteredPoints) {
                if (point.getLatitude() == markerPosition.latitude &&
                    point.getLongitude() == markerPosition.longitude) {
                    return point;
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "Error al encontrar punto cultural: " + e.getMessage());
        }

        return null;
    }

    private void calculateAndShowDistance(CulturalPoint point) {
        try {
            if (tvPointDistance != null) {
                // Por ahora mostrar distancia fija, en una implementación real
                // se calcularía la distancia real desde la ubicación del usuario
                tvPointDistance.setText("A 500m de tu ubicación");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al calcular distancia: " + e.getMessage());
        }
    }

    private void loadPointImage(CulturalPoint point) {
        try {
            if (ivPointImage != null && point.getImageUrl() != null) {
                // Cargar imagen desde recursos
                int imageResource = getResources().getIdentifier(
                    point.getImageUrl(), "drawable", getPackageName());

                if (imageResource != 0) {
                    ivPointImage.setImageResource(imageResource);
                } else {
                    // Imagen por defecto si no se encuentra
                    ivPointImage.setImageResource(R.drawable.ic_placeholder);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al cargar imagen: " + e.getMessage());
        }
    }

    private void centerOnMyLocation() {
        try {
            if (mMap != null && checkLocationPermission()) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    getLastKnownLocation();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al centrar en mi ubicación: " + e.getMessage());
        }
    }

    private void getLastKnownLocation() {
        try {
            if (fusedLocationClient == null) {
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null && mMap != null) {
                            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16f));
                        }
                    });
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al obtener última ubicación: " + e.getMessage());
        }
    }

    private void showLayersDialog() {
        try {
            MapLayersBottomSheetFragment fragment = MapLayersBottomSheetFragment.newInstance();
            fragment.setOnMapLayerSelectedListener(this);
            fragment.show(getSupportFragmentManager(), "MapLayers");

        } catch (Exception e) {
            Log.e(TAG, "Error al mostrar diálogo de capas: " + e.getMessage());
        }
    }

    @Override
    public void onMapLayerSelected(int mapType) {
        try {
            changeMapType(mapType);
        } catch (Exception e) {
            Log.e(TAG, "Error al cambiar tipo de mapa: " + e.getMessage());
        }
    }

    private void changeMapType(int mapType) {
        try {
            if (mMap == null) return;

            currentMapType = mapType;

            switch (mapType) {
                case 1: // Normal
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    break;
                case 2: // Satélite
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    break;
                case 3: // Terreno
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    break;
                case 4: // Híbrido
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    break;
            }

            Log.d(TAG, "Tipo de mapa cambiado a: " + mapType);

        } catch (Exception e) {
            Log.e(TAG, "Error al cambiar tipo de mapa: " + e.getMessage());
        }
    }

    private void showDetailedInfo(CulturalPoint point) {
        try {
            Toast.makeText(this, "Mostrando detalles de: " + point.getName(), Toast.LENGTH_SHORT).show();
            // Aquí se implementaría la navegación a la pantalla de detalles
        } catch (Exception e) {
            Log.e(TAG, "Error al mostrar detalles: " + e.getMessage());
        }
    }

    private void navigateToPoint(CulturalPoint point) {
        String uri = "google.navigation:q=" + point.getLatitude() + "," + point.getLongitude();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            String webUri = "https://www.google.com/maps/dir/?api=1&destination=" + point.getLatitude() + "," + point.getLongitude();
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUri));
            startActivity(webIntent);
        }
    }

    private void animateButton(View button, Runnable action) {
        try {
            button.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
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
        } catch (Exception e) {
            Log.e(TAG, "Error en animación del botón: " + e.getMessage());
            // Ejecutar acción sin animación si falla
            action.run();
        }
    }

    private boolean checkLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
               ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void checkAndRequestLocationPermissions() {
        try {
            if (!checkLocationPermission()) {
                ActivityCompat.requestPermissions(this, LOCATION_PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al verificar permisos: " + e.getMessage());
        }
    }

    private void enableMyLocation() {
        try {
            if (mMap != null && checkLocationPermission()) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al habilitar mi ubicación: " + e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        try {
            if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation();
                } else {
                    Toast.makeText(this, "Permisos de ubicación denegados", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error en onRequestPermissionsResult: " + e.getMessage());
        }
    }

    private void showFilterBottomSheet() {
        try {
            Log.d(TAG, "Mostrando bottom sheet de filtros");
            if (bottomSheetBehavior != null) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al mostrar bottom sheet: " + e.getMessage());
        }
    }

    private void hideFilterBottomSheet() {
        try {
            Log.d(TAG, "Ocultando bottom sheet de filtros");
            if (bottomSheetBehavior != null) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al ocultar bottom sheet: " + e.getMessage());
        }
    }

    private void createCustomMarker(int imageResId, float markerColor) {
        try {
            // Crear marcador personalizado con color
            BitmapDescriptorFactory.defaultMarker(markerColor);
        } catch (Exception e) {
            Log.e(TAG, "Error al crear marcador personalizado: " + e.getMessage());
        }
    }

    private float getColorFromHue(float hue) {
        try {
            // Convertir hue a color de marcador
            return hue;
        } catch (Exception e) {
            Log.e(TAG, "Error al obtener color del hue: " + e.getMessage());
            return BitmapDescriptorFactory.HUE_RED; // Color por defecto
        }
    }

    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
            if (fusedLocationClient != null) {
                fusedLocationClient = null;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error en onDestroy: " + e.getMessage());
        }
    }
}
