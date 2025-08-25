package com.museovivo.app.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

// removed FirebaseStorage import - using local storage instead
import com.museovivo.app.R;

public class EditProfileActivity extends AppCompatActivity {

    private static final int REQUEST_PICK_IMAGE = 1001;

    private ImageView imageProfile;
    private EditText editName;
    private ProgressBar progressBar;
    private Uri imageUri;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

    firebaseAuth = FirebaseAuth.getInstance();

        imageProfile = findViewById(R.id.image_profile_edit);
    editName = findViewById(R.id.edit_name);
        progressBar = findViewById(R.id.progress_edit);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            editName.setText(user.getDisplayName());
            // Load local image if available, otherwise Firebase photo or default
            android.content.SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            String localPath = prefs.getString("profile_image_path", null);
            if (localPath != null && new File(localPath).exists()) {
                Glide.with(this).load(new File(localPath)).circleCrop().into(imageProfile);
            } else if (user.getPhotoUrl() != null) {
                Glide.with(this).load(user.getPhotoUrl()).circleCrop().into(imageProfile);
            } else {
                imageProfile.setImageResource(R.drawable.ic_profile);
            }
        }

        imageProfile.setOnClickListener(v -> openImagePicker());
        findViewById(R.id.button_save_profile).setOnClickListener(v -> saveProfile());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Seleccionar imagen"), REQUEST_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            if (imageUri != null) {
                Glide.with(this).load(imageUri).circleCrop().into(imageProfile);
            }
        }
    }

    private void saveProfile() {
    String name = editName.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            editName.setError("El nombre no puede estar vacío");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }

        // If an image was picked, save it locally and use that file:// URI
        if (imageUri != null) {
            try {
                String uid = user.getUid();
                File dir = new File(getFilesDir(), "profile_images");
                if (!dir.exists()) dir.mkdirs();
                File outFile = new File(dir, uid + ".jpg");

                // Delete previous file if exists to ensure a clean overwrite
                if (outFile.exists()) {
                    outFile.delete();
                }

                try (InputStream in = getContentResolver().openInputStream(imageUri);
                     FileOutputStream out = new FileOutputStream(outFile)) {
                    byte[] buffer = new byte[8192];
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }
                }

                Uri localUri = Uri.fromFile(outFile);
                // persist local path in preferences for ProfileFragment
                SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                prefs.edit().putString("profile_image_path", outFile.getAbsolutePath()).apply();

                updateUserProfile(user, name, localUri);
                // Return success to caller
                setResult(RESULT_OK);
            } catch (Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Error al guardar imagen localmente", Toast.LENGTH_SHORT).show();
            }

        } else {
            // No image change
            updateUserProfile(user, name, null);
        }
    }

    private void updateUserProfile(FirebaseUser user, String name, @Nullable Uri photoUri) {
        UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder().setDisplayName(name);
        if (photoUri != null) {
            builder.setPhotoUri(photoUri);
        }

        user.updateProfile(builder.build()).addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al actualizar perfil", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
