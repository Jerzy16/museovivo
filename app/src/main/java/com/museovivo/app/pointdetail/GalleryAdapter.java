package com.museovivo.app.pointdetail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.museovivo.app.R;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {
    private List<Integer> imageList;
    private OnImageClickListener listener;

    public interface OnImageClickListener {
        void onImageClick(int imageResId);
    }

    public GalleryAdapter(List<Integer> imageList, OnImageClickListener listener) {
        this.imageList = imageList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery_image, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        int imageResId = imageList.get(position);
        holder.imageView.setImageResource(imageResId);
        holder.imageView.setOnClickListener(v -> listener.onImageClick(imageResId));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    static class GalleryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        GalleryViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_gallery);
        }
    }
}
