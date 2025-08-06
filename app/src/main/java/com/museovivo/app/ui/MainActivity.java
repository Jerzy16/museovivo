package com.museovivo.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.museovivo.app.R;
import com.museovivo.app.ui.auth.AuthActivity;
import com.museovivo.app.ui.main.HomeFragment;
import com.museovivo.app.ui.map.MapFragment;
import com.museovivo.app.ui.ar.ARFragment;
import com.museovivo.app.ui.content.CultureFragment;
import com.museovivo.app.ui.profile.ProfileFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth firebaseAuth;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Verificar autenticación
        firebaseAuth = FirebaseAuth.getInstance();
        checkUserAuthentication();
        
        initializeViews();
        setupBottomNavigation();
        
        // Mostrar fragmento inicial
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
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
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }
    
    private void setupBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }
    
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        
        int itemId = item.getItemId();
        if (itemId == R.id.nav_home) {
            fragment = new HomeFragment();
        } else if (itemId == R.id.nav_map) {
            fragment = new MapFragment();
        } else if (itemId == R.id.nav_ar) {
            fragment = new ARFragment();
        } else if (itemId == R.id.nav_culture) {
            fragment = new CultureFragment();
        } else if (itemId == R.id.nav_profile) {
            fragment = new ProfileFragment();
        }
        
        return loadFragment(fragment);
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