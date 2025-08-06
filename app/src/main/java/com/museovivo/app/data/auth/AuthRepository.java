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
    
    public AuthRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://appmuse-default-rtdb.firebaseio.com/");
        usersRef = database.getReference("users");
        userLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
        
        // Observar cambios en el estado de autenticación
        firebaseAuth.addAuthStateListener(firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            userLiveData.setValue(user);
        });
    }
    
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
    
    public void logout() {
        firebaseAuth.signOut();
    }
    
    public Task<Void> resetPassword(String email) {
        return firebaseAuth.sendPasswordResetEmail(email)
                .addOnFailureListener(e -> {
                    errorLiveData.setValue(e.getMessage());
                });
    }
    
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
    
    private void createUserProfile(User user) {
        usersRef.child(user.getUid()).setValue(user)
                .addOnFailureListener(e -> {
                    errorLiveData.setValue("Error creating user profile: " + e.getMessage());
                });
    }
    
    private void updateLastLogin(String userId) {
        usersRef.child(userId).child("lastLoginAt").setValue(System.currentTimeMillis());
    }
    
    public void updateUserProfile(User user) {
        if (getCurrentUser() != null) {
            usersRef.child(user.getUid()).setValue(user)
                    .addOnFailureListener(e -> {
                        errorLiveData.setValue("Error updating profile: " + e.getMessage());
                    });
        }
    }
}