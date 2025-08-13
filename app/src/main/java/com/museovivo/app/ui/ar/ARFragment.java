package com.museovivo.app.ui.ar;

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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.ar.core.ArCoreApk;
import com.google.ar.core.Config;
import com.google.ar.core.Session;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException;

import com.museovivo.app.R;

public class ARFragment extends Fragment {
    
    private TextView textARStatus;
    private Button buttonStartAR, buttonScanQR;
    private Session arSession;
    private boolean isARSupported = false;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ar, container, false);
        
        initializeViews(view);
        setupClickListeners();
        checkARSupport();
        
        return view;
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkCameraPermission();
    }
    
    private void initializeViews(View view) {
        textARStatus = view.findViewById(R.id.text_ar_status);
        buttonStartAR = view.findViewById(R.id.button_start_ar);
        buttonScanQR = view.findViewById(R.id.button_scan_qr);
    }
    
    private void setupClickListeners() {
        buttonStartAR.setOnClickListener(v -> {
            if (isARSupported && arSession != null) {
                startARExperience();
            } else {
                Toast.makeText(getContext(), "AR no está disponible en este dispositivo", Toast.LENGTH_SHORT).show();
            }
        });
        
        buttonScanQR.setOnClickListener(v -> {
            // TODO: Implementar escáner QR
            Toast.makeText(getContext(), "Iniciando escáner QR...", Toast.LENGTH_SHORT).show();
        });
    }
    
    private void checkARSupport() {
        ArCoreApk.Availability availability = ArCoreApk.getInstance().checkAvailability(requireContext());
        if (availability == ArCoreApk.Availability.SUPPORTED_INSTALLED) {
            isARSupported = true;
            textARStatus.setText("✅ AR disponible - Listo para usar");
            textARStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.success));
            buttonStartAR.setEnabled(true);
        } else if (availability == ArCoreApk.Availability.SUPPORTED_APK_TOO_OLD ||
                   availability == ArCoreApk.Availability.SUPPORTED_NOT_INSTALLED) {
            textARStatus.setText("⚠️ AR disponible - Requiere actualización");
            textARStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.warning));
            buttonStartAR.setEnabled(false);
        } else {
            isARSupported = false;
            textARStatus.setText("❌ AR no compatible con este dispositivo");
            textARStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.error));
            buttonStartAR.setEnabled(false);
        }
    }
    
    private void checkCameraPermission() {
        // Simplificado - sin verificación de permisos por ahora
        initializeARSession();
    }
    
    private void initializeARSession() {
        if (!isARSupported) return;
        
        try {
            arSession = new Session(requireContext());
            Config config = new Config(arSession);
            config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE);
            arSession.configure(config);
            
            Toast.makeText(getContext(), "Sesión AR inicializada correctamente", Toast.LENGTH_SHORT).show();
            
        } catch (UnavailableArcoreNotInstalledException e) {
            Toast.makeText(getContext(), "Por favor instala ARCore", Toast.LENGTH_LONG).show();
            buttonStartAR.setEnabled(false);
        } catch (UnavailableApkTooOldException e) {
            Toast.makeText(getContext(), "Por favor actualiza ARCore", Toast.LENGTH_LONG).show();
            buttonStartAR.setEnabled(false);
        } catch (UnavailableSdkTooOldException e) {
            Toast.makeText(getContext(), "Por favor actualiza la aplicación", Toast.LENGTH_LONG).show();
            buttonStartAR.setEnabled(false);
        } catch (UnavailableDeviceNotCompatibleException e) {
            Toast.makeText(getContext(), "Dispositivo no compatible con AR", Toast.LENGTH_LONG).show();
            buttonStartAR.setEnabled(false);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error al inicializar AR: " + e.getMessage(), Toast.LENGTH_LONG).show();
            buttonStartAR.setEnabled(false);
        }
    }
    
    private void startARExperience() {
        if (arSession == null) {
            initializeARSession();
            return;
        }
        
        try {
            arSession.resume();
            Toast.makeText(getContext(), "Experiencia AR iniciada", Toast.LENGTH_SHORT).show();
            
            // TODO: Aquí se implementaría la interfaz AR completa
            // Por ahora solo mostramos un mensaje
            
        } catch (CameraNotAvailableException e) {
            Toast.makeText(getContext(), "Cámara no disponible", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    public void onPause() {
        super.onPause();
        if (arSession != null) {
            arSession.pause();
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (arSession != null) {
            arSession.close();
            arSession = null;
        }
    }
}