package com.museovivo.app.ui.content;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.museovivo.app.R;
import com.museovivo.app.pointdetail.GalleryAdapter;

import java.util.ArrayList;
import java.util.List;

public class CultureDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_culture_detail);

    // Host CultureFragment (so the fragment's RecyclerView uses item_cultural_card)
    Intent intent = getIntent();
    String category = intent != null ? intent.getStringExtra("category") : "traditions";

    androidx.fragment.app.Fragment frag = new CultureFragment();
    Bundle args = new Bundle();
    args.putString("category", category);
    frag.setArguments(args);

    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.culture_fragment_container, frag)
        .commit();
    }
}
