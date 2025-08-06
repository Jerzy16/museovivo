package com.museovivo.app.ui.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.museovivo.app.R;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient;
    private FloatingActionButton fabMyLocation;
    private FloatingActionButton fabRoutes;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        
        initializeViews(view);
        setupMapFragment();
        setupClickListeners();
        
        return view;
    }
    
    private void initializeViews(View view) {
        fabMyLocation = view.findViewById(R.id.fab_my_location);
        fabRoutes = view.findViewById(R.id.fab_routes);
    }
    
    private void setupMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }
    
    private void setupClickListeners() {
        fabMyLocation.setOnClickListener(v -> {
            if (googleMap != null) {
                getCurrentLocation();
            }
        });
        
        fabRoutes.setOnClickListener(v -> {
            // TODO: Mostrar dialog con rutas disponibles
            Toast.makeText(getContext(), "Cargando rutas culturales...", Toast.LENGTH_SHORT).show();
        });
    }
    
    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        
        // Configurar el mapa
        setupMap();
        
        // Verificar permisos de ubicación
        checkLocationPermission();
        
        // Cargar puntos culturales
        loadCulturalPoints();
    }
    
    private void setupMap() {
        if (googleMap == null) return;
        
        // Configuraciones del mapa
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false); // Usamos nuestro FAB
        
        // Establecer vista inicial (por ejemplo, ciudad de México)
        LatLng defaultLocation = new LatLng(19.4326, -99.1332);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12));
        
        // Configurar listeners
        googleMap.setOnMarkerClickListener(marker -> {
            // TODO: Mostrar detalles del punto cultural
            Toast.makeText(getContext(), "Punto cultural: " + marker.getTitle(), Toast.LENGTH_SHORT).show();
            return false;
        });
    }
    
    private void checkLocationPermission() {
        Dexter.withContext(requireContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        enableMyLocation();
                        getCurrentLocation();
                    }
                    
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(getContext(), "Permiso de ubicación necesario para mostrar tu posición", Toast.LENGTH_LONG).show();
                    }
                    
                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }
    
    private void enableMyLocation() {
        if (googleMap != null && 
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) 
            == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        }
    }
    
    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) 
            != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null && googleMap != null) {
                        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                        
                        // Buscar puntos culturales cercanos
                        searchNearbyPoints(location);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al obtener ubicación", Toast.LENGTH_SHORT).show();
                });
    }
    
    private void loadCulturalPoints() {
        // TODO: Cargar puntos culturales desde Firebase
        // Por ahora agregar algunos puntos de ejemplo
        addSampleCulturalPoints();
    }
    
    private void addSampleCulturalPoints() {
        if (googleMap == null) return;
        
        // Ejemplos de puntos culturales en Ciudad de México
        LatLng zocalo = new LatLng(19.4326, -99.1332);
        googleMap.addMarker(new MarkerOptions()
                .position(zocalo)
                .title("Zócalo")
                .snippet("Plaza principal de la Ciudad de México")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        
        LatLng temploMayor = new LatLng(19.4352, -99.1322);
        googleMap.addMarker(new MarkerOptions()
                .position(temploMayor)
                .title("Templo Mayor")
                .snippet("Sitio arqueológico azteca")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        
        LatLng palacioFinas = new LatLng(19.4344, -99.1419);
        googleMap.addMarker(new MarkerOptions()
                .position(palacioFinas)
                .title("Palacio de Bellas Artes")
                .snippet("Centro cultural y artístico")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
    }
    
    private void searchNearbyPoints(Location userLocation) {
        // TODO: Implementar búsqueda de puntos culturales cercanos
        // Usar la ubicación del usuario para filtrar puntos cercanos
        Toast.makeText(getContext(), "Buscando puntos culturales cercanos...", Toast.LENGTH_SHORT).show();
    }
}