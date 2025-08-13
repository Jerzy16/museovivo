package com.museovivo.app.mapas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.museovivo.app.R;

public class MapLayersBottomSheetFragment extends DialogFragment {

    public interface OnMapLayerSelectedListener {
        void onMapLayerSelected(int mapType);
    }
    private OnMapLayerSelectedListener listener;
    private ChipGroup chipGroupLayers;
    private MaterialButton btnCloseLayers;

    public static MapLayersBottomSheetFragment newInstance() {
        return new MapLayersBottomSheetFragment();
    }

    public void setOnMapLayerSelectedListener(OnMapLayerSelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_layers_bottom_sheet, container, false);
        chipGroupLayers = view.findViewById(R.id.chip_group_layers);
        btnCloseLayers = view.findViewById(R.id.btn_close_layers);
        setupListeners();
        return view;
    }

    private void setupListeners() {
        if (chipGroupLayers != null) {
            // Asumiendo que quieres singleSelection para los tipos de mapa (solo uno puede estar activo)
            // Asegúrate que tu ChipGroup en fragment_map_layers_bottom_sheet.xml tenga app:singleSelection="true"
            chipGroupLayers.setOnCheckedChangeListener((group, checkedId) -> {
                // checkedId es el int ID del chip seleccionado, o View.NO_ID si se deselecciona
                // y app:selectionRequired="false"

                if (checkedId == View.NO_ID) {
                    // Si permites la deselección y quieres un comportamiento por defecto
                    // o simplemente no hacer nada y retornar.
                    // Si siempre debe haber una capa seleccionada, esta condición podría no alcanzarse
                    // si app:selectionRequired="true" (que es el default para singleSelection).
                    return;
                }

                int mapType = 1; // Default to Normal (GoogleMap.MAP_TYPE_NORMAL)

                // Asumo que tienes un chip con id "chip_normal" o similar para el tipo normal,
                // si no, el default se aplicará si ninguno de los otros coincide.
                if (checkedId == R.id.chip_normal) { // CAMBIAR R.id.chip_normal_layer al ID real de tu chip "Normal"
                    mapType = 1; // GoogleMap.MAP_TYPE_NORMAL
                } else if (checkedId == R.id.chip_satellite) { // CAMBIAR R.id.chip_satellite_layer al ID real de tu chip "Satélite"
                    mapType = 2; // GoogleMap.MAP_TYPE_SATELLITE
                } else if (checkedId == R.id.chip_terrain) { // CAMBIAR R.id.chip_terrain_layer al ID real de tu chip "Terreno"
                    mapType = 3; // GoogleMap.MAP_TYPE_TERRAIN
                } else if (checkedId == R.id.chip_hybrid) { // CAMBIAR R.id.chip_hybrid_layer al ID real de tu chip "Híbrido"
                    mapType = 4; // GoogleMap.MAP_TYPE_HYBRID
                }
                // Nota: Si el checkedId no coincide con ninguno de los anteriores,
                // y no tenías un chip "Normal" explícito, mapType seguirá siendo 1 (Normal).

                if (listener != null) {
                    listener.onMapLayerSelected(mapType);
                }

                dismiss(); // Cierra el BottomSheet después de la selección
            });
        }

        if (btnCloseLayers != null) {
            btnCloseLayers.setOnClickListener(v -> dismiss());
        }
    }
}
