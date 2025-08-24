package com.museovivo.app.ui.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.museovivo.app.R;
import android.content.Intent;
import com.museovivo.app.ui.content.CultureDetailActivity;

import java.util.List;

public class HomeFragment extends Fragment {
    
    private TextView textWelcome;
    private Button buttonExploreMap;
    private Button buttonStartAR;
    private Button buttonViewCulture;
    // Featured routes RecyclerView is kept local to avoid unnecessary field scope
    
    private FirebaseAuth firebaseAuth;
    //
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        firebaseAuth = FirebaseAuth.getInstance();
        
        initializeViews(view);
        setupClickListeners();
        checkPermissions();
        
        return view;
    }
    // Llamado después de que la vista ha sido creada
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateWelcomeText();
        loadFeaturedRoutes();
    }
    // Inicializar vistas
    private void initializeViews(View view) {
        textWelcome = view.findViewById(R.id.text_welcome);
        buttonExploreMap = view.findViewById(R.id.button_explore_map);
        buttonStartAR = view.findViewById(R.id.button_start_ar);
        buttonViewCulture = view.findViewById(R.id.button_view_culture);
        // Prefer a dedicated featured routes RecyclerView in home layout. Fall back to cultural content if missing.
        RecyclerView recyclerFeaturedRoutes = view.findViewById(R.id.recycler_featured_routes);
        if (recyclerFeaturedRoutes == null) {
            recyclerFeaturedRoutes = view.findViewById(R.id.recycler_cultural_content);
        }
        if (recyclerFeaturedRoutes != null) {
            recyclerFeaturedRoutes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        }
    }
    
    private void setupClickListeners() {
        buttonExploreMap.setOnClickListener(v -> {
            // Navegar al fragmento del mapa (implementar navegación)
            if (getActivity() != null) {
                Toast.makeText(getContext(), "Navegando al mapa...", Toast.LENGTH_SHORT).show();
            }
        });
        
        buttonStartAR.setOnClickListener(v -> {
            // Verificar permisos de cámara antes de iniciar AR
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                // Navegación a AR (pendiente implementación detallada)
                Toast.makeText(getContext(), "Iniciando realidad aumentada...", Toast.LENGTH_SHORT).show();
            } else {
                requestCameraPermission();
            }
        });
        
        buttonViewCulture.setOnClickListener(v -> {
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), CultureDetailActivity.class);
                startActivity(intent);
            }
        });
    }
    
    private void updateWelcomeText() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null && user.getDisplayName() != null) {
            String welcomeMessage = "¡Hola, " + user.getDisplayName() + "!";
            textWelcome.setText(welcomeMessage);
        } else {
            textWelcome.setText("¡Bienvenido a Museo Vivo!");
        }
    }
    
    private void loadFeaturedRoutes() {
        // Carga de rutas destacadas pendiente: actualmente se muestra un mensaje de placeholder
        if (getContext() != null) {
            Toast.makeText(getContext(), "Cargando rutas destacadas...", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void checkPermissions() {
        // Simplificado - sin verificación de permisos por ahora
        enableLocationFeatures();
    }
    
    private void requestCameraPermission() {
        // Simplificado - sin verificación de permisos por ahora
        Toast.makeText(getContext(), "Permiso de cámara concedido", Toast.LENGTH_SHORT).show();
    }
    
    private void enableLocationFeatures() {
        // Habilitar funciones que requieren ubicación
    // Funcionalidades basadas en ubicación: pendiente de implementación
    }
}