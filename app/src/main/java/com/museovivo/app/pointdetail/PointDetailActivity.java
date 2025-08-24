package com.museovivo.app.pointdetail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.museovivo.app.R;

import java.util.ArrayList;
import java.util.List;

public class PointDetailActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_IMAGE = 101;
    private ArrayList<String> comments = new ArrayList<>();
    private ArrayList<Integer> userImages = new ArrayList<>(); // Para demo, usar drawables. Para producción, usar Uris.
    private boolean isFavorite = false;
    GalleryAdapter galleryAdapter;
    private SharedPreferences sharedPreferences;
    private float currentRating = 4.5f;
    private int ratingCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_detail);
        
        try {
            // Obtener datos del intent
            String pointName = getIntent().getStringExtra("point_name");
            String pointDescription = getIntent().getStringExtra("point_description");
            String pointHistory = getIntent().getStringExtra("point_history");
            String pointSchedule = getIntent().getStringExtra("point_schedule");
            String pointEntryFee = getIntent().getStringExtra("point_entry_fee");
            String pointContact = getIntent().getStringExtra("point_contact");
            double latitude = getIntent().getDoubleExtra("latitude", 0.0);
            double longitude = getIntent().getDoubleExtra("longitude", 0.0);
            int imageResource = getIntent().getIntExtra("image_resource", R.drawable.plazadearmas1);
            
            // Validar datos requeridos
            if (pointName == null || pointName.isEmpty()) {
                Toast.makeText(this, "Error: Datos del punto incompletos", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            // Asignar valores por defecto si algún campo es null
            if (pointDescription == null) pointDescription = "Sin descripción disponible.";
            if (pointHistory == null) pointHistory = "Sin historia disponible.";
            if (pointSchedule == null) pointSchedule = "Horario no disponible.";
            if (pointEntryFee == null) pointEntryFee = "No especificado.";
            if (pointContact == null) pointContact = "No disponible.";
            // Configurar vistas
            setupViews(pointName, pointDescription, pointHistory, pointSchedule, 
                      pointEntryFee, pointContact, latitude, longitude, imageResource);
        } catch (Exception e) {
            android.util.Log.e("PointDetailActivity", "Error al cargar detalles", e);
            StringBuilder sb = new StringBuilder();
            sb.append(e.toString());
            for (StackTraceElement el : e.getStackTrace()) {
                sb.append("\n  at ").append(el.toString());
            }
            Toast.makeText(this, "Error al cargar detalles: " + sb.toString(), Toast.LENGTH_LONG).show();
            finish();
        }
    }
    private void setupViews(String name, String description, String history, 
                           String schedule, String entryFee, String contact,
                           double latitude, double longitude, int imageResource) {

        // Configurar imagen
        ImageView ivPointImage = findViewById(R.id.iv_point_image);
        ivPointImage.setImageResource(imageResource);
        // Nuevo: abrir imagen en pantalla completa al tocarla
        ivPointImage.setOnClickListener(v -> {
            Intent intent = new Intent(PointDetailActivity.this, FullscreenImageActivity.class);
            intent.putExtra("imageResId", imageResource);
            startActivity(intent);
        });

        // Configurar textos
        TextView tvPointName = findViewById(R.id.tv_point_name);
        tvPointName.setText(name);

        TextView tvPointDescription = findViewById(R.id.tv_point_description);
        tvPointDescription.setText(description);

        TextView tvHistory = findViewById(R.id.history);
        tvHistory.setText(history);

        TextView tvSchedule = findViewById(R.id.schedule);
        tvSchedule.setText(schedule);

        TextView tvEntryFee = findViewById(R.id.entry_fee);
        tvEntryFee.setText(entryFee);

        TextView tvContact = findViewById(R.id.contact);
        tvContact.setText(contact);
        // Configurar preferencias compartidas
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isFavorite = sharedPreferences.getBoolean("favorite_" + name, false);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        TextView tvRatingValue = findViewById(R.id.tv_rating_value);
        ratingBar.setRating(currentRating);
        tvRatingValue.setText(String.format("%.1f", currentRating));
        ratingBar.setOnRatingBarChangeListener((bar, rating, fromUser) -> {
            if (fromUser) {
                // Promedio simple para demo
                currentRating = (currentRating * ratingCount + rating) / (ratingCount + 1);
                ratingCount++;
                ratingBar.setRating(currentRating);
                tvRatingValue.setText(String.format("%.1f", currentRating));
                Toast.makeText(this, "¡Gracias por tu valoración!", Toast.LENGTH_SHORT).show();
            }
        });
        EditText etComment = findViewById(R.id.et_comment);
        RecyclerView recyclerComments = findViewById(R.id.recycler_comments);
        FloatingActionButton btnAddComment = findViewById(R.id.btn_add_comment);
        CommentsAdapter commentsAdapter = new CommentsAdapter(comments);
        recyclerComments.setAdapter(commentsAdapter);
        recyclerComments.setLayoutManager(new LinearLayoutManager(this));
        btnAddComment.setOnClickListener(v -> {
            String comment = etComment.getText().toString().trim();
            if (!TextUtils.isEmpty(comment)) {
                comments.add(comment);
                commentsAdapter.notifyItemInserted(comments.size() - 1);
                etComment.setText("");
                recyclerComments.smoothScrollToPosition(comments.size() - 1);
            }
        });

        // Configurar galería interactiva
        RecyclerView recyclerGallery = findViewById(R.id.recycler_gallery);
        List<Integer> imageList = new ArrayList<>();
        imageList.add(R.drawable.plazadearmas1);
        imageList.add(R.drawable.plazadearmas1);
        imageList.add(R.drawable.plazadearmas1);
        imageList.add(R.drawable.plazadearmas1);
        imageList.add(R.drawable.plazadearmas1);
        // Agregar imágenes subidas por el usuario
        imageList.addAll(userImages);
        galleryAdapter = new GalleryAdapter(imageList, imageResId -> {
            Intent intent = new Intent(PointDetailActivity.this, FullscreenImageActivity.class);
            intent.putExtra("imageResId", imageResId);
            startActivity(intent);
        });
        recyclerGallery.setAdapter(galleryAdapter);
        recyclerGallery.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Configurar botón de regreso
        FloatingActionButton fabBack = findViewById(R.id.fab_back);
        fabBack.setOnClickListener(v -> finish());

        // Configurar botón de navegación
        MaterialButton btnNavigate = findViewById(R.id.btn_navigate);
        btnNavigate.setOnClickListener(v -> {
            try {
                // Crear URI para Google Maps
                String uri = "geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude + "(" + name + ")";
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                mapIntent.setPackage("com.google.android.apps.maps");

                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    // Si no hay Google Maps, abrir en navegador
                    String webUri = "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUri));
                    startActivity(webIntent);
                }
            } catch (Exception e) {
                Toast.makeText(this, "Error al abrir la navegación", Toast.LENGTH_SHORT).show();
            }
        });

        // Insertar fragmento con variante específica según el nombre del punto
        try {
            androidx.fragment.app.FragmentManager fm = getSupportFragmentManager();
            androidx.fragment.app.Fragment existing = fm.findFragmentById(R.id.variant_container);
            if (existing == null) {
                // Prefer explicit variant sent via intent
                String explicitVariant = getIntent().getStringExtra("point_variant");
                PointVariantFragment variant;
                if (explicitVariant != null) {
                    variant = PointVariantFragment.newInstanceWithVariant(name, explicitVariant);
                } else {
                    variant = PointVariantFragment.newInstance(name);
                }
                fm.beginTransaction()
                        .replace(R.id.variant_container, variant)
                        .commitAllowingStateLoss();
            }
        } catch (Exception e) {
            android.util.Log.w("PointDetailActivity", "No se pudo cargar variante: " + e.getMessage());
        }

    }
}
