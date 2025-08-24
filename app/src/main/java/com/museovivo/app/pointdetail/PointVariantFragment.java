package com.museovivo.app.pointdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.museovivo.app.R;

public class PointVariantFragment extends Fragment {
    private static final String ARG_POINT_NAME = "arg_point_name";
    private static final String ARG_POINT_VARIANT = "arg_point_variant";

    public static PointVariantFragment newInstance(String pointName) {
        PointVariantFragment f = new PointVariantFragment();
        Bundle b = new Bundle();
        b.putString(ARG_POINT_NAME, pointName);
        f.setArguments(b);
        return f;
    }

    public static PointVariantFragment newInstanceWithVariant(String pointName, String variant) {
        PointVariantFragment f = new PointVariantFragment();
        Bundle b = new Bundle();
        b.putString(ARG_POINT_NAME, pointName);
        b.putString(ARG_POINT_VARIANT, variant);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String pointName = "";
        String explicitVariant = null;
        if (getArguments() != null) {
            pointName = getArguments().getString(ARG_POINT_NAME, "").toLowerCase();
            explicitVariant = getArguments().getString(ARG_POINT_VARIANT, null);
        }

        // Decide layout variant: prefer explicit variant when provided, otherwise fall back to keyword matching
        int layoutRes = R.layout.point_variant_default;
        if (explicitVariant != null) {
            switch (explicitVariant) {
                case "plaza": layoutRes = R.layout.point_variant_plaza; break;
                case "iglesia": layoutRes = R.layout.point_variant_iglesia; break;
                case "mirador": layoutRes = R.layout.point_variant_mirador; break;
                case "puente": layoutRes = R.layout.point_variant_puente; break;
                case "mercado": layoutRes = R.layout.point_variant_mercado; break;
                case "museo": layoutRes = R.layout.point_variant_museo; break;
                default: layoutRes = R.layout.point_variant_default; break;
            }
        } else {
            if (pointName.contains("plaza")) {
                layoutRes = R.layout.point_variant_plaza;
            } else if (pointName.contains("iglesia") || pointName.contains("catedral") || pointName.contains("santuario")) {
                layoutRes = R.layout.point_variant_iglesia;
            } else if (pointName.contains("mirador")) {
                layoutRes = R.layout.point_variant_mirador;
            } else if (pointName.contains("puente")) {
                layoutRes = R.layout.point_variant_puente;
            } else if (pointName.contains("mercado")) {
                layoutRes = R.layout.point_variant_mercado;
            } else if (pointName.contains("museo")) {
                layoutRes = R.layout.point_variant_museo;
            }
        }

        return inflater.inflate(layoutRes, container, false);
    }
}
