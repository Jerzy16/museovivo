package com.museovivo.app;

import android.app.Application;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MuseoVivoApplication extends Application {
    
    // Firebase instances
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private static MuseoVivoApplication instance;
    
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        
        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://appmuse-default-rtdb.firebaseio.com/");
        
        // Enable Firebase offline persistence
        firebaseDatabase.setPersistenceEnabled(true);
        
        // Initialize other components
        initializeComponents();
    }
    
    private void initializeComponents() {
        // Initialize any other required components here
        // Example: Image loading libraries, crash reporting, etc.
    }
    
    public static MuseoVivoApplication getInstance() {
        return instance;
    }
    
    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }
    
    public FirebaseDatabase getFirebaseDatabase() {
        return firebaseDatabase;
    }
}