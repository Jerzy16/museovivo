package com.museovivo.app.ui.content;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.museovivo.app.R;

public class CultureFragment extends Fragment {
    
    private TabLayout tabLayout;
    // RecyclerView for culture content is local to initializeViews
    private static final String CATEGORY_TRADITIONS = "traditions";
    private static final String CATEGORY_LEGENDS = "legends";
    private static final String CATEGORY_DANCES = "dances";
    private static final String CATEGORY_ALL = "all";
    private String currentCategory = CATEGORY_TRADITIONS;
    private CulturalContentAdapter adapter;
    private java.util.List<CulturalContentAdapter.CulturalItem> masterList;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_culture, container, false);
        
        // Read initial category from arguments (if Activity passed one)
        Bundle args = getArguments();
        if (args != null) {
            String cat = args.getString("category");
            if (cat != null && !cat.isEmpty()) currentCategory = cat;
        }

        initializeViews(view);
        setupTabs();

    // Select corresponding tab after setup (guarded)
        int selectIndex = 1; // default traditions
    if (CATEGORY_LEGENDS.equals(currentCategory)) selectIndex = 2;
    else if (CATEGORY_DANCES.equals(currentCategory)) selectIndex = 3;
    else if (CATEGORY_ALL.equals(currentCategory)) selectIndex = 0;
        if (tabLayout != null) {
            com.google.android.material.tabs.TabLayout.Tab t = tabLayout.getTabAt(selectIndex);
            if (t != null) t.select();
        }

        loadCulturalContent(currentCategory);
        
        return view;
    }
    
    private void initializeViews(View view) {
        tabLayout = view.findViewById(R.id.tab_layout_culture);
        RecyclerView recyclerContent = view.findViewById(R.id.recycler_cultural_content);

        // Mostrar lista vertical (cards) para coincidir con el diseño
        if (recyclerContent != null) {
            recyclerContent.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        }
        // Populate master list with sample items and set adapter
        masterList = new java.util.ArrayList<>();
    masterList.add(new CulturalContentAdapter.CulturalItem(CATEGORY_TRADITIONS, "Tradiciones", "El arte de contar historias", "Explora la rica tradición oral de nuestra región, donde las historias se transmiten de generación en generación.", R.drawable.plazadearmas1));
    masterList.add(new CulturalContentAdapter.CulturalItem(CATEGORY_LEGENDS, "Leyendas", "La leyenda del Bosque Susurrante", "Descubra la historia mística de Whispering Woods, un lugar donde se dice que residen espíritus antiguos.", R.drawable.catedral));
    masterList.add(new CulturalContentAdapter.CulturalItem(CATEGORY_TRADITIONS, "Tradición", "La danza de las luciérnagas", "Sea testigo de la encantadora Danza de las Luciérnagas, una celebración de la belleza de la naturaleza y la magia de las noches de verano.", R.drawable.mirador));

        adapter = new CulturalContentAdapter(new java.util.ArrayList<>(masterList), item -> startDetailActivity(item.key));
        if (recyclerContent != null) {
            recyclerContent.setAdapter(adapter);
        }

        // Configurar búsqueda
        android.widget.EditText et = view.findViewById(R.id.et_search_culture);
        if (et != null) {
            et.addTextChangedListener(new android.text.TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) { /* no-op */ }
                @Override public void onTextChanged(CharSequence s, int st, int b, int c) { filter(adapter, masterList, s.toString(), currentCategory); }
                @Override public void afterTextChanged(android.text.Editable s) { /* no-op */ }
            });
        }
    }
    
    private void setupTabs() {
        // Configurar botones de categorías
        // Los botones ya están definidos en el layout XML
        
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        currentCategory = CATEGORY_ALL;
                        break;
                    case 1:
                        currentCategory = CATEGORY_TRADITIONS;
                        break;
                    case 2:
                        currentCategory = CATEGORY_LEGENDS;
                        break;
                    case 3:
                        currentCategory = CATEGORY_DANCES;
                        break;
                }
        // Apply filter using current search text
        android.widget.EditText et = getView() == null ? null : getView().findViewById(R.id.et_search_culture);
        String q = et == null ? "" : et.getText().toString();
        filter(adapter, masterList, q, currentCategory);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { /* no-op */ }
            
            @Override
            public void onTabReselected(TabLayout.Tab tab) { /* no-op */ }
        });
    }
    
    private void loadCulturalContent(String category) {
    // Placeholder: load content from remote source (Firebase) here
    Toast.makeText(getContext(), "Cargando " + category + "...", Toast.LENGTH_SHORT).show();
        // Aquí se cargarían los datos reales desde Firebase
        // Por ahora mostrar contenido de ejemplo
        loadSampleContent(category);
    }

    private void filter(CulturalContentAdapter adapter, java.util.List<CulturalContentAdapter.CulturalItem> master, String query, String categoryKey) {
        if (adapter == null || master == null) return;
        String q = (query == null) ? "" : query.trim().toLowerCase();
        java.util.List<CulturalContentAdapter.CulturalItem> filtered = new java.util.ArrayList<>();
        for (CulturalContentAdapter.CulturalItem it : master) {
            boolean matchesCategory = (categoryKey == null || categoryKey.isEmpty() || CATEGORY_ALL.equals(categoryKey)) || it.key.equals(categoryKey);
            boolean matchesQuery = q.isEmpty() || it.title.toLowerCase().contains(q) || it.excerpt.toLowerCase().contains(q) || it.category.toLowerCase().contains(q);
            if (matchesCategory && matchesQuery) filtered.add(it);
        }
        adapter.setItems(filtered);
    }
    private void loadSampleContent(String category) {
    // Placeholder: this method would request and display category-specific content
    // Currently filtered content is handled by filter(...) using masterList
    Toast.makeText(getContext(), "Cargando muestra: " + category, Toast.LENGTH_SHORT).show();
    }
    private void startDetailActivity(String category) {
        if (getContext() == null) return;
        android.content.Intent i = new android.content.Intent(getContext(), CultureDetailActivity.class);
        i.putExtra("category", category);
        startActivity(i);
    }
}