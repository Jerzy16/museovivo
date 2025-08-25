package com.museovivo.app.ui.content;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.museovivo.app.R;

public class CulturalDetailActivity extends AppCompatActivity {
    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_DESC = "extra_desc";
    public static final String EXTRA_IMAGE_URL = "extra_image_url";
    public static final String PREFS_NAME = "user_stats";
    public static final String KEY_VISITED = "places_visited";
    public static final String KEY_POINTS = "points_gained";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cultural_detail);

        ImageView image = findViewById(R.id.image_cultural);
        TextView title = findViewById(R.id.text_title);
        TextView desc = findViewById(R.id.text_desc);
        Button btnVisit = findViewById(R.id.button_visit);

        Intent intent = getIntent();
        String t = intent.getStringExtra(EXTRA_TITLE);
        String d = intent.getStringExtra(EXTRA_DESC);
        // Try to get image as resource ID (int) or as URL
        int imgRes = intent.getIntExtra(EXTRA_IMAGE_URL, -1);
        if (imgRes != -1) {
            image.setImageResource(imgRes);
        } else {
            String img = intent.getStringExtra(EXTRA_IMAGE_URL);
            if (img != null) Glide.with(this).load(img).into(image);
        }
        title.setText(t);
        desc.setText(d);

        btnVisit.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            int visited = prefs.getInt(KEY_VISITED, 0) + 1;
            int points = prefs.getInt(KEY_POINTS, 0) + 10;
            prefs.edit().putInt(KEY_VISITED, visited).putInt(KEY_POINTS, points).apply();
            btnVisit.setText("¡Visitado!");
            btnVisit.setEnabled(false);
        });
    }
}
