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

import com.museovivo.app.ui.ar.ARFragment;
import com.museovivo.app.ui.content.CultureFragment;
import com.museovivo.app.ui.profile.ProfileFragment;

public class MainActivity extends AppCompatActivity {
    
    private ImageButton buttonHome, buttonMap, buttonAR, buttonCulture, buttonProfile;
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
    
    private void initializeViews() {
        buttonHome = findViewById(R.id.btn_home);
        buttonMap = findViewById(R.id.btn_map);
        buttonAR = findViewById(R.id.btn_ar);
        buttonCulture = findViewById(R.id.btn_culture);
        buttonProfile = findViewById(R.id.btn_profile);
    }
    
    private void setupNavigation() {
        buttonHome.setOnClickListener(v -> {
            loadFragment(new HomeFragment());
            setActiveTab(buttonHome);
        });
        
        buttonMap.setOnClickListener(v -> {
            // Launch MapActivity instead of loading MapFragment
            Intent mapIntent = new Intent(MainActivity.this, com.museovivo.app.mapas.MapActivity.class);
            startActivity(mapIntent);
            setActiveTab(buttonMap);
        });
        
        buttonAR.setOnClickListener(v -> {
            loadFragment(new ARFragment());
            setActiveTab(buttonAR);
        });
        
        buttonCulture.setOnClickListener(v -> {
            loadFragment(new CultureFragment());
            setActiveTab(buttonCulture);
        });
        
        buttonProfile.setOnClickListener(v -> {
            loadFragment(new ProfileFragment());
            setActiveTab(buttonProfile);
        });
    }
    
    private void setActiveTab(ImageButton activeButton) {
        // Reset all buttons to normal state
        buttonHome.setSelected(false);
        buttonMap.setSelected(false);
        buttonAR.setSelected(false);
        buttonCulture.setSelected(false);
        buttonProfile.setSelected(false);
        
        // Set active button
        activeButton.setSelected(true);
    }
    
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