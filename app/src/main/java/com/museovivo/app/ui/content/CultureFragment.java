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
    masterList.add(new CulturalContentAdapter.CulturalItem(CATEGORY_TRADITIONS, "Tradiciones", "Fiesta de San Juan", "Es muy celebrada, sobre todo en el distrito de Pachaconas (Antabamba), donde se organizan ferias agropecuarias en las que se exhiben los mejores productos de la región. Esta feria tiene reconocimiento oficial. La fiesta de San Juan está a cargo de un mayordomo que allí se denomina carguyocc, quien corre con todos los gastos.", R.drawable.sanjuan));
    masterList.add(new CulturalContentAdapter.CulturalItem(CATEGORY_TRADITIONS, "Tradiciónes", "Carnaval Abanquino", "El Carnaval abanquino-apurimeño es una importante, colorida y pícara celebración que se lleva a cabo en todo el departamento de Apurímac. En esta celebración el entusiasmo prima y nadie puede escapar de la alegría y entretenimiento propios del carnaval.", R.drawable.carnalabancay));
    masterList.add(new CulturalContentAdapter.CulturalItem(CATEGORY_LEGENDS, "Leyendas", "El Ampay: Guardián eterno", "El nevado Ampay es considerado un “apus”, o espíritu protector dentro de las creencias andinas locales. La leyenda cuenta cómo este guardián eterno se erige sobre Abancay velando por sus habitantes día y noche contra cualquier amenaza natural o sobrenatural. Los pobladores le ofrecen respeto mediante rituales ancestrales para mantener su protección y asegurar cosechas abundantes.", R.drawable.ampay));
    masterList.add(new CulturalContentAdapter.CulturalItem(CATEGORY_LEGENDS, "Leyendas", "Los túneles secretos coloniales subterráneos","Bajo Abancay correrían laberintos ocultos construidos durante la época colonial española utilizados tanto para escapar ante ataques indígenas como para transportar metales preciosos extraídos ilegalmente evadiendo impuestos reales . A pesar de muchas investigaciones aún estos túneles mantienen sus entradas selladas o perdidas , alimentando fantasías sobre tesoros olvidados aún esperando ser descubiertos por algún aventurero intrépido.", R.drawable.tuneles));
    masterList.add(new CulturalContentAdapter.CulturalItem(CATEGORY_LEGENDS, "Leyendas", "La leyenda del Puente Pachachaca", "El Puente Pachachaca, construido en el siglo XVIII, es un testimonio de la ingeniería colonial. Según la leyenda local, el puente fue construido por un grupo de esclavos que, al ser liberados, juraron protegerlo para siempre. Se dice que en las noches de luna llena, los espíritus de estos esclavos aún vigilan el puente, asegurándose de que nadie lo dañe.", R.drawable.puentepachachaca));
    masterList.add(new CulturalContentAdapter.CulturalItem(CATEGORY_LEGENDS, "Leyendas", "La sirenas en el río Yaku Mayo", "A lo largo del caudaloso río Yaku Mayo hay historias sobre encuentros con encantadoras pero peligrosas sirenas cuya belleza atrapa a los incautos pescadores o viajeros nocturnos. Se dice que estas criaturas poseen tal encanto musical que quienes escuchan sus cantos quedan embelesados para luego ser arrastrados al fondo acuático desde donde jamás regresan.", R.drawable.siremas));
    masterList.add(new CulturalContentAdapter.CulturalItem(CATEGORY_DANCES, "Danzas", "Lazo tinkay", "Es una danza ritual que se consagra a los apus, que son las montañas que protegen al poblado, antes y después de la corrida de toros o toro pukllay. Se bendicen los lazos que utilizan los laceadores presentes en la corrida de toros.\n" +
            "\n" + "Las mujeres visten faldas rojas y blancas, blusa blanca y chalina roja con sombrero negro. Los hombres visten pantalón, chaleco y sombrero negros, y camisa a cuadros.\n" +
            "\n" + "Uno o dos hombres se disfrazan de toro. Los participantes se encuentran en la plaza donde se celebra la corrida portando un dios Kuntur que colocan en el lomo del toro.", R.drawable.tinkay));
    masterList.add(new CulturalContentAdapter.CulturalItem(CATEGORY_DANCES, "Danzas", "Sara yapuy", "la acción de sembrar el maíz. Mediante movimientos que siguen el ritmo de la música, el dueño y su peón encabezan el grupo llevando las herramientas.\n" +
            "\n" + "Detrás van llegando más personas que comienzan a sembrar. Después del descanso llegan las mujeres con las meriendas.\n" +
            "\n" + "Luego de comer, se baila en parejas y se finaliza con un coro de mujeres llamado wankaska. Los varones repiten cada estrofa. Al final, todos caminan alegres rumbo a sus casas.\n" +
            "\n" + "Los hombres utilizan un traje mestizo: pantalón blanco, camisa a cuadros, chalina y sombrero. Las mujeres visten faldas de colores, blusa blanca y sombrero negro.", R.drawable.sarayapuy));
    masterList.add(new CulturalContentAdapter.CulturalItem(CATEGORY_DANCES, "Danzas","Wicuña chaqoy","Es una antigua danza preincaica que representa el trasquilado de la vicuña. Mediante esta danza se defiende y ensalza el valor de este animal andino.Al igual que hacían los incas, la idea es proteger la vida de este animal para que no se extinga.",R.drawable.wicuna));
    adapter = new CulturalContentAdapter(new java.util.ArrayList<>(masterList), item -> startDetailActivity(item));
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
    private void startDetailActivity(CulturalContentAdapter.CulturalItem item) {
        if (getContext() == null) return;
        android.content.Intent i = new android.content.Intent(getContext(), com.museovivo.app.ui.content.CulturalDetailActivity.class);
        i.putExtra(com.museovivo.app.ui.content.CulturalDetailActivity.EXTRA_TITLE, item.title);
        i.putExtra(com.museovivo.app.ui.content.CulturalDetailActivity.EXTRA_DESC, item.excerpt);
        i.putExtra(com.museovivo.app.ui.content.CulturalDetailActivity.EXTRA_IMAGE_URL, item.imageRes);
        startActivity(i);
    }
}