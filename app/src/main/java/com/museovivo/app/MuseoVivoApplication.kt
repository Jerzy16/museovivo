package com.museovivo.app

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MuseoVivoApplication : Application() {
    
    // Firebase instances
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance("https://appmuse-default-rtdb.firebaseio.com/")
        
        // Enable Firebase offline persistence
        firebaseDatabase.setPersistenceEnabled(true)
        
        // Initialize other components
        initializeComponents()
    }
    
    private fun initializeComponents() {
        // Initialize any other required components here
        // Example: Image loading libraries, crash reporting, etc.
    }
    
    companion object {
        private lateinit var instance: MuseoVivoApplication
        
        fun getInstance(): MuseoVivoApplication {
            return instance
        }
    }
    
    init {
        instance = this
    }
}