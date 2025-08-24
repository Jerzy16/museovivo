package com.museovivo.app.pointdetail;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.museovivo.app.R;

public class FullscreenImageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image);

        ImageView photoView = findViewById(R.id.photo_view);
        int imageResId = getIntent().getIntExtra("imageResId", -1);
        if (imageResId != -1) {
            photoView.setImageResource(imageResId);
        }
        photoView.setOnClickListener(v -> finish());
    }
}
