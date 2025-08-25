package com.museovivo.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.museovivo.app.R;
import com.museovivo.app.ui.auth.AuthActivity;
import com.museovivo.app.ui.main.HomeFragment;

import com.museovivo.app.ui.content.CultureFragment;
import com.museovivo.app.ui.profile.ProfileFragment;

public class MainActivity extends AppCompatActivity {
    
    private ImageButton buttonHome, buttonMap, buttonCulture, buttonProfile;
    private FirebaseAuth firebaseAuth;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Verificar autenticación
        firebaseAuth = FirebaseAuth.getInstance();
        checkUserAuthentication();
        initializeViews();
        setupNavigation();
        
        // Mostrar fragmento inicial
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            setActiveTab(buttonHome);
        }
    }
    // Verificar si el usuario está autenticado
    private void checkUserAuthentication() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            // Usuario no autenticado, ir a AuthActivity
            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);
            finish();
            return;
        }
    }
    // Inicializar vistas
    private void initializeViews() {
        buttonHome = findViewById(R.id.btn_home);
        buttonMap = findViewById(R.id.btn_map);
        buttonCulture = findViewById(R.id.btn_culture);
        buttonProfile = findViewById(R.id.btn_profile);
    }
    // Configurar navegación
    private void setupNavigation() {
        buttonHome.setOnClickListener(v -> {
            loadFragment(new HomeFragment());
            setActiveTab(buttonHome);
        });
        // Mapa abren actividades completas
        buttonMap.setOnClickListener(v -> {
            // Launch MapActivity instead of loading MapFragment
            Intent mapIntent = new Intent(MainActivity.this, com.museovivo.app.mapas.MapActivity.class);
            startActivity(mapIntent);
            setActiveTab(buttonMap);
        });
        // Cultura abre actividad completa
        buttonCulture.setOnClickListener(v -> {
            loadFragment(new com.museovivo.app.ui.content.CultureFragment());
            setActiveTab(buttonCulture);
        });
        // Perfil abre fragmento
        buttonProfile.setOnClickListener(v -> {
            loadFragment(new ProfileFragment());
            setActiveTab(buttonProfile);
        });
    }
    // Resaltar pestaña activa
    private void setActiveTab(ImageButton activeButton) {
        // Resetear todas las pestañas
    buttonHome.setSelected(false);
    buttonMap.setSelected(false);
    buttonCulture.setSelected(false);
    buttonProfile.setSelected(false);
        
        // Set active button
        activeButton.setSelected(true);
    }
    // Cargar fragmento en el contenedor
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
    // Verificar autenticación al iniciar actividad
    @Override
    protected void onStart() {
        super.onStart();
        // Verificar si el usuario sigue autenticado
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);
            finish();
        }
    }
}