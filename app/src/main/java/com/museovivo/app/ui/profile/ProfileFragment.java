package com.museovivo.app.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.museovivo.app.R;
import com.museovivo.app.ui.auth.AuthActivity;

public class ProfileFragment extends Fragment {
    
    private ImageView imageProfile;
    private TextView textUserName, textUserEmail, textTotalPoints, textVisitedPlaces;
    private RecyclerView recyclerAchievements;
    private Button buttonEditProfile, buttonLogout;
    
    private FirebaseAuth firebaseAuth;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        firebaseAuth = FirebaseAuth.getInstance();
        
        initializeViews(view);
        setupClickListeners();
        loadUserData();
        
        return view;
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadAchievements();
    }
    
    private void initializeViews(View view) {
        imageProfile = view.findViewById(R.id.image_profile);
        textUserName = view.findViewById(R.id.text_user_name);
        textUserEmail = view.findViewById(R.id.text_user_email);
        textTotalPoints = view.findViewById(R.id.text_total_points);
        textVisitedPlaces = view.findViewById(R.id.text_visited_places);
        recyclerAchievements = view.findViewById(R.id.recycler_achievements);
    buttonEditProfile = view.findViewById(R.id.button_edit_profile);
    buttonLogout = view.findViewById(R.id.button_logout);
        
        // Configurar RecyclerView para logros
        recyclerAchievements.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }
    
    private void setupClickListeners() {
        buttonEditProfile.setOnClickListener(v -> {
            // Abrir EditProfileActivity para editar nombre/foto
            if (getActivity() != null) {
                startActivity(new android.content.Intent(getActivity(), EditProfileActivity.class));
            }
        });
        
        buttonLogout.setOnClickListener(v -> {
            performLogout();
        });
        
        imageProfile.setOnClickListener(v -> {
            // TODO: Cambiar foto de perfil
            Toast.makeText(getContext(), "Cambiar foto de perfil", Toast.LENGTH_SHORT).show();
        });
    }
    
    private void loadUserData() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            // Cargar datos básicos del usuario
            textUserName.setText(user.getDisplayName() != null ? user.getDisplayName() : "Usuario");
            textUserEmail.setText(user.getEmail());

            // Prefer local saved image path
            android.content.SharedPreferences prefs = getActivity() != null ? getActivity().getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE) : null;
            String localPath = prefs != null ? prefs.getString("profile_image_path", null) : null;
            if (localPath != null) {
                java.io.File f = new java.io.File(localPath);
                if (f.exists()) {
                    Glide.with(this).load(f).circleCrop().placeholder(R.drawable.ic_profile).into(imageProfile);
                    return;
                }
            }

            // Cargar foto de perfil desde FirebaseAuth si no hay imagen local
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .circleCrop()
                        .placeholder(R.drawable.ic_profile)
                        .into(imageProfile);
            }
            
            // TODO: Cargar estadísticas desde Firebase
            loadUserStatistics(user.getUid());
        }
    }
    
    private void loadUserStatistics(String userId) {
        // TODO: Implementar carga desde Firebase Realtime Database
        // Por ahora mostrar datos de ejemplo
        textTotalPoints.setText("0 puntos");
        textVisitedPlaces.setText("0 lugares");
        
        Toast.makeText(getContext(), "Cargando estadísticas...", Toast.LENGTH_SHORT).show();
    }
    
    private void loadAchievements() {
        // TODO: Implementar carga de logros desde Firebase
        // Por ahora mostrar mensaje
        Toast.makeText(getContext(), "Cargando logros...", Toast.LENGTH_SHORT).show();
    }
    
    private void performLogout() {
        firebaseAuth.signOut();
        
        // Navegar a pantalla de autenticación
        Intent intent = new Intent(getActivity(), AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Recargar datos cuando el fragmento vuelve a ser visible
        loadUserData();
    }
}