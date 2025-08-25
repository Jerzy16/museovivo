package com.museovivo.app.ui.main;

// Camera permission and manifest imports removed because AR was removed
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
// Removed unused permission helper imports
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.museovivo.app.R;
import android.content.Intent;

import com.museovivo.app.mapas.MapActivity;
import com.museovivo.app.ui.content.CultureFragment;
import java.util.List;

public class HomeFragment extends Fragment {
    
    private TextView textWelcome;
    private Button buttonExploreMap;
    private Button buttonViewCulture;
    private TextView textVisitedPlaces;
    private TextView textTotalPoints;
    // Featured routes RecyclerView is kept local to avoid unnecessary field scope
    
    private FirebaseAuth firebaseAuth;
    // Actualizar estadísticas al reanudar
    @Override
    public void onResume() {
        super.onResume();
        updateStats();
    }
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
        updateStats();
        loadFeaturedRoutes();
    }
    // Inicializar vistas
    private void initializeViews(View view) {
        textWelcome = view.findViewById(R.id.text_welcome);
        buttonExploreMap = view.findViewById(R.id.button_explore_map);
        buttonViewCulture = view.findViewById(R.id.button_view_culture);
        textVisitedPlaces = view.findViewById(R.id.text_visited_places_home);
        textTotalPoints = view.findViewById(R.id.text_total_points_home);
    }
    // Configurar listeners de clic
    private void setupClickListeners() {
        buttonExploreMap.setOnClickListener(v -> {
            // Navegar al fragmento del mapa (implementar navegación)
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                startActivity(intent);
            }
        });
        // Navegar al fragmento de cultura
        buttonViewCulture.setOnClickListener(v -> {
            if (getActivity() != null) {
                // Abrir el fragmento de cultura en pantalla principal
                getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new CultureFragment()).addToBackStack(null).commit();
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
    
    // camera permission helper removed
    
    private void enableLocationFeatures() {
        // Habilitar funciones que requieren ubicación
    // Funcionalidades basadas en ubicación: pendiente de implementación
    }
    private void updateStats() {
        android.content.SharedPreferences prefs = getActivity() != null ? getActivity().getSharedPreferences("user_stats", android.content.Context.MODE_PRIVATE) : null;
        int points = prefs != null ? prefs.getInt("points_gained", 0) : 0;
        int visited = prefs != null ? prefs.getInt("places_visited", 0) : 0;
        if (textTotalPoints != null) textTotalPoints.setText(String.valueOf(points));
        if (textVisitedPlaces != null) textVisitedPlaces.setText(String.valueOf(visited));
    }
}