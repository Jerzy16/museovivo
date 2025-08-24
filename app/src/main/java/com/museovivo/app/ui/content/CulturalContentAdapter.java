package com.museovivo.app.ui.content;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.museovivo.app.R;

import java.util.List;

public class CulturalContentAdapter extends RecyclerView.Adapter<CulturalContentAdapter.ViewHolder> {

    public interface Callback { void onItemClicked(CulturalItem item); }

    public static class CulturalItem {
        // key is a machine-friendly category key (e.g. "traditions"), category is display label
        public final String key;
        public final String category;
        public final String title;
        public final String excerpt;
        public final int imageRes;

        public CulturalItem(String key, String category, String title, String excerpt, int imageRes) {
            this.key = key; this.category = category; this.title = title; this.excerpt = excerpt; this.imageRes = imageRes;
        }
    }

    private final List<CulturalItem> items;
    private final Callback callback;

    public CulturalContentAdapter(List<CulturalItem> items, Callback callback) {
        this.items = items; this.callback = callback;
    }

    public void setItems(List<CulturalItem> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    public List<CulturalItem> getItems() { return items; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cultural_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CulturalItem it = items.get(position);
        holder.tvCategory.setText(it.category);
        holder.tvTitle.setText(it.title);
        holder.tvExcerpt.setText(it.excerpt);
        holder.imgThumb.setImageResource(it.imageRes);
        holder.container.setOnClickListener(v -> { if (callback != null) callback.onItemClicked(it); });
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final View container;
        final TextView tvCategory;
        final TextView tvTitle;
        final TextView tvExcerpt;
        final ImageView imgThumb;
//        final ImageView imgBookmark
        ViewHolder(View v) {
            super(v);
            container = v;
            tvCategory = v.findViewById(R.id.tv_category);
            tvTitle = v.findViewById(R.id.tv_title);
            tvExcerpt = v.findViewById(R.id.tv_excerpt);
            imgThumb = v.findViewById(R.id.img_thumb);
        }
    }
}
