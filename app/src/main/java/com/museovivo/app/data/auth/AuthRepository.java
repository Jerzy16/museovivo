package com.museovivo.app.data.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.museovivo.app.domain.model.User;

public class AuthRepository {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersRef;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<String> errorLiveData;
    // Constructor
    public AuthRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://museovivo-7f141-default-rtdb.firebaseio.com/");
        usersRef = database.getReference("users");
        userLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
        
        // Observar cambios en el estado de autenticación
        firebaseAuth.addAuthStateListener(firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            userLiveData.setValue(user);
        });
    }
    // Iniciar sesión con Google
    public Task<AuthResult> loginWithEmail(String email, String password) {
        return firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = authResult.getUser();
                    if (user != null) {
                        updateLastLogin(user.getUid());
                    }
                })
                .addOnFailureListener(e -> {
                    errorLiveData.setValue(e.getMessage());
                });
    }
    // Registro de usuario con correo electrónico y contraseña
    public Task<AuthResult> registerWithEmail(String email, String password, String displayName) {
        return firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser firebaseUser = authResult.getUser();
                    if (firebaseUser != null) {
                        // Crear perfil de usuario en la base de datos
                        User user = new User(firebaseUser.getUid(), email, displayName);
                        createUserProfile(user);
                    }
                })
                .addOnFailureListener(e -> {
                    errorLiveData.setValue(e.getMessage());
                });
    }
    // Cerrar sesión
    public void logout() {
        firebaseAuth.signOut();
    }
    
    public Task<Void> resetPassword(String email) {
        return firebaseAuth.sendPasswordResetEmail(email)
                .addOnFailureListener(e -> {
                    errorLiveData.setValue(e.getMessage());
                });
    }
    // Obtener el usuario actual
    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }
    
    public boolean isUserLoggedIn() {
        return getCurrentUser() != null;
    }
    
    public LiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }
    
    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
    // Crear el perfil de usuario en la base de datos
    private void createUserProfile(User user) {
        usersRef.child(user.getUid()).setValue(user)
                .addOnFailureListener(e -> {
                    errorLiveData.setValue("\n" + "Error al crear el perfil de usuario: " + e.getMessage());
                });
    }
   // Actualizar la fecha del último inicio de sesión
    private void updateLastLogin(String userId) {
        usersRef.child(userId).child("lastLoginAt").setValue(System.currentTimeMillis());
    }
    /// Actualizar el perfil del usuario
    public void updateUserProfile(User user) {
        if (getCurrentUser() != null) {
            usersRef.child(user.getUid()).setValue(user)
                    .addOnFailureListener(e -> {
                        errorLiveData.setValue("Error al actualizar el perfil:" + e.getMessage());
                    });
        }
    }
}