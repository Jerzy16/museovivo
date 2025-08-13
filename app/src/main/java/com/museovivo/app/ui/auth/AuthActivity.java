package com.museovivo.app.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.museovivo.app.R;
import com.museovivo.app.data.auth.AuthRepository;
import com.museovivo.app.ui.MainActivity;

public class AuthActivity extends AppCompatActivity {
    
    private EditText editTextEmail, editTextPassword, editTextDisplayName;
    private Button buttonLogin, buttonRegister;
    private TextView textForgotPassword, textToggleMode;
    private ProgressBar progressBar;
    
    private AuthRepository authRepository;
    private boolean isLoginMode = true;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        
        authRepository = new AuthRepository();
        
        initializeViews();
        setupClickListeners();
        observeAuthState();
    }
    
    private void initializeViews() {
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        editTextDisplayName = findViewById(R.id.edit_text_display_name);
        buttonLogin = findViewById(R.id.button_login);
        textToggleMode = findViewById(R.id.text_toggle_mode);
        textForgotPassword = findViewById(R.id.text_forgot_password);
        progressBar = findViewById(R.id.progress_bar);
        
        updateUIForMode();
    }
    
    private void setupClickListeners() {
        buttonLogin.setOnClickListener(v -> {
            if (isLoginMode) {
                performLogin();
            } else {
                performRegister();
            }
        });
        
        textToggleMode.setOnClickListener(v -> {
            isLoginMode = !isLoginMode;
            updateUIForMode();
        });
        
        textForgotPassword.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                editTextEmail.setError("Ingresa tu email");
                return;
            }
            
            showProgress(true);
            authRepository.resetPassword(email)
                    .addOnCompleteListener(task -> {
                        showProgress(false);
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Email de recuperación enviado", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
    
    private void observeAuthState() {
        authRepository.getUserLiveData().observe(this, firebaseUser -> {
            if (firebaseUser != null) {
                // Usuario autenticado, ir a MainActivity
                Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        
        authRepository.getErrorLiveData().observe(this, error -> {
            if (error != null) {
                showProgress(false);
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void performLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        
        if (!validateInput(email, password, null)) {
            return;
        }
        
        showProgress(true);
        authRepository.loginWithEmail(email, password)
                .addOnCompleteListener(task -> {
                    showProgress(false);
                    if (!task.isSuccessful()) {
                        String error = task.getException() != null ? task.getException().getMessage() : "Error de autenticación";
                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    
    private void performRegister() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String displayName = editTextDisplayName.getText().toString().trim();
        
        if (!validateInput(email, password, displayName)) {
            return;
        }
        
        showProgress(true);
        authRepository.registerWithEmail(email, password, displayName)
                .addOnCompleteListener(task -> {
                    showProgress(false);
                    if (!task.isSuccessful()) {
                        String error = task.getException() != null ? task.getException().getMessage() : "Error de registro";
                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    
    private boolean validateInput(String email, String password, String displayName) {
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Email requerido");
            return false;
        }
        
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Contraseña requerida");
            return false;
        }
        
        if (password.length() < 6) {
            editTextPassword.setError("La contraseña debe tener al menos 6 caracteres");
            return false;
        }
        
        if (!isLoginMode && TextUtils.isEmpty(displayName)) {
            editTextDisplayName.setError("Nombre requerido");
            return false;
        }
        
        return true;
    }
    
    private void updateUIForMode() {
        if (isLoginMode) {
            buttonLogin.setText("Iniciar Sesión");
            textToggleMode.setText("¿No tienes cuenta? Regístrate");
            findViewById(R.id.layout_display_name).setVisibility(View.GONE);
            textForgotPassword.setVisibility(View.VISIBLE);
        } else {
            buttonLogin.setText("Registrarse");
            textToggleMode.setText("¿Ya tienes cuenta? Inicia sesión");
            findViewById(R.id.layout_display_name).setVisibility(View.VISIBLE);
            textForgotPassword.setVisibility(View.GONE);
        }
    }
    
    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        buttonLogin.setEnabled(!show);
        textToggleMode.setEnabled(!show);
    }
}