package com.museovivo.app.ui.content;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.museovivo.app.R;

public class CultureFragment extends Fragment {
    
    private TabLayout tabLayout;
    private RecyclerView recyclerContent;
    private String currentCategory = "traditions";
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_culture, container, false);
        
        initializeViews(view);
        setupTabs();
        loadCulturalContent(currentCategory);
        
        return view;
    }
    
    private void initializeViews(View view) {
        tabLayout = view.findViewById(R.id.tab_layout_culture);
        recyclerContent = view.findViewById(R.id.recycler_cultural_content);
        
        // Configurar RecyclerView con grid de 2 columnas
        recyclerContent.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }
    
    private void setupTabs() {
        // Configurar botones de categorías
        // Los botones ya están definidos en el layout XML
        
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        currentCategory = "traditions";
                        break;
                    case 1:
                        currentCategory = "legends";
                        break;
                    case 2:
                        currentCategory = "recipes";
                        break;
                    case 3:
                        currentCategory = "dances";
                        break;
                }
                loadCulturalContent(currentCategory);
            }
            
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
    
    private void loadCulturalContent(String category) {
        // TODO: Implementar carga desde Firebase
        Toast.makeText(getContext(), "Cargando " + category + "...", Toast.LENGTH_SHORT).show();
        
        // Aquí se cargarían los datos reales desde Firebase
        // Por ahora mostrar contenido de ejemplo
        loadSampleContent(category);
    }
    
    private void loadSampleContent(String category) {
        // TODO: Implementar adapter y datos de ejemplo
        switch (category) {
            case "traditions":
                Toast.makeText(getContext(), "Mostrando tradiciones locales", Toast.LENGTH_SHORT).show();
                break;
            case "legends":
                Toast.makeText(getContext(), "Mostrando leyendas populares", Toast.LENGTH_SHORT).show();
                break;
            case "recipes":
                Toast.makeText(getContext(), "Mostrando recetas tradicionales", Toast.LENGTH_SHORT).show();
                break;
            case "dances":
                Toast.makeText(getContext(), "Mostrando danzas folclóricas", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}