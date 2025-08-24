package com.museovivo.app.mapas;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.museovivo.app.R;

import androidx.core.content.ContextCompat;

public class MapLayersBottomSheetFragment extends BottomSheetDialogFragment {

    public interface OnMapLayerSelectedListener {
        void onMapTypeSelected(int mapType);
    }

    private OnMapLayerSelectedListener listener;
    private int currentMapType = 1; // 1: Normal, 2: Satélite, 3: Terreno, 4: Híbrido

    public MapLayersBottomSheetFragment(int currentMapType, boolean trafficEnabled, boolean buildingsEnabled) {
        this.currentMapType = currentMapType;
    }

    public MapLayersBottomSheetFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnMapLayerSelectedListener) {
            listener = (OnMapLayerSelectedListener) context;
        }
    }
// Este fragmento es un BottomSheet que permite al usuario seleccionar el tipo de mapa
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_map_layers, container, false);

        ImageButton btnClose = view.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(v -> dismiss());
        // Tipos de mapa
        GridLayout gridMapTypes = view.findViewById(R.id.grid_map_types);
        String[] mapTypeNames = {"Estándar", "Satélite", "Relieve", "Híbrido"};
        int[] mapTypeIcons = {R.drawable.ic_map, R.drawable.ic_satellite_placeholder, R.drawable.ic_terrain_placeholder, R.drawable.ic_layers};
        int[] mapTypeValues = {1, 2, 3, 4};

        gridMapTypes.removeAllViews();
        for (int i = 0; i < mapTypeNames.length; i++) {
            View item = inflater.inflate(R.layout.item_map_type, gridMapTypes, false);
            ImageView icon = item.findViewById(R.id.iv_map_type_icon);
            TextView label = item.findViewById(R.id.tv_map_type_label);
            icon.setImageResource(mapTypeIcons[i]);
            label.setText(mapTypeNames[i]);
            if (currentMapType == mapTypeValues[i]) {
                icon.setBackgroundResource(R.drawable.circle_background);
                label.setTextColor(ContextCompat.getColor(getContext(), R.color.primary_color));
            } else {
                icon.setBackgroundResource(R.drawable.fab_background);
                label.setTextColor(ContextCompat.getColor(getContext(), R.color.primary_text));
            }
            int finalI = i;
            item.setOnClickListener(v -> {
                if (listener != null) listener.onMapTypeSelected(mapTypeValues[finalI]);
                dismiss();
            });
            gridMapTypes.addView(item);
        }

        return view;
    }
}