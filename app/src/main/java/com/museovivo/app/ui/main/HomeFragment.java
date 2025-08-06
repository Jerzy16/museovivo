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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.museovivo.app.R;

import java.util.List;

public class HomeFragment extends Fragment {
    
    private TextView textWelcome;
    private Button buttonExploreMap, buttonStartAR, buttonViewCulture;
    private RecyclerView recyclerFeaturedRoutes;
    
    private FirebaseAuth firebaseAuth;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        firebaseAuth = FirebaseAuth.getInstance();
        
        initializeViews(view);
        setupClickListeners();
        checkPermissions();
        
        return view;
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateWelcomeText();
        loadFeaturedRoutes();
    }
    
    private void initializeViews(View view) {
        textWelcome = view.findViewById(R.id.text_welcome);
        buttonExploreMap = view.findViewById(R.id.button_explore_map);
        buttonStartAR = view.findViewById(R.id.button_start_ar);
        buttonViewCulture = view.findViewById(R.id.button_view_culture);
        recyclerFeaturedRoutes = view.findViewById(R.id.recycler_featured_routes);
        
        // Configurar RecyclerView
        recyclerFeaturedRoutes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }
    
    private void setupClickListeners() {
        buttonExploreMap.setOnClickListener(v -> {
            // Navegar al fragmento del mapa
            if (getActivity() != null) {
                // TODO: Implementar navegación al MapFragment
                Toast.makeText(getContext(), "Navegando al mapa...", Toast.LENGTH_SHORT).show();
            }
        });
        
        buttonStartAR.setOnClickListener(v -> {
            // Verificar permisos de cámara antes de iniciar AR
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                // TODO: Implementar navegación al ARFragment
                Toast.makeText(getContext(), "Iniciando realidad aumentada...", Toast.LENGTH_SHORT).show();
            } else {
                requestCameraPermission();
            }
        });
        
        buttonViewCulture.setOnClickListener(v -> {
            // Navegar al fragmento de cultura
            if (getActivity() != null) {
                // TODO: Implementar navegación al CultureFragment
                Toast.makeText(getContext(), "Navegando a contenido cultural...", Toast.LENGTH_SHORT).show();
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
        // TODO: Implementar carga de rutas destacadas desde Firebase
        // Por ahora mostrar un mensaje
        if (getContext() != null) {
            Toast.makeText(getContext(), "Cargando rutas destacadas...", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void checkPermissions() {
        Dexter.withContext(requireContext())
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.CAMERA
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            // Todos los permisos concedidos
                            enableLocationFeatures();
                        } else {
                            // Algunos permisos denegados
                            Toast.makeText(getContext(), "Algunos permisos son necesarios para todas las funcionalidades", Toast.LENGTH_LONG).show();
                        }
                    }
                    
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }
    
    private void requestCameraPermission() {
        Dexter.withContext(requireContext())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new com.karumi.dexter.listener.single.PermissionListener() {
                    @Override
                    public void onPermissionGranted(com.karumi.dexter.listener.single.PermissionGrantedResponse response) {
                        Toast.makeText(getContext(), "Permiso de cámara concedido", Toast.LENGTH_SHORT).show();
                    }
                    
                    @Override
                    public void onPermissionDenied(com.karumi.dexter.listener.single.PermissionDeniedResponse response) {
                        Toast.makeText(getContext(), "Permiso de cámara necesario para AR", Toast.LENGTH_SHORT).show();
                    }
                    
                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }
    
    private void enableLocationFeatures() {
        // Habilitar funciones que requieren ubicación
        // TODO: Implementar funcionalidades basadas en ubicación
    }
}