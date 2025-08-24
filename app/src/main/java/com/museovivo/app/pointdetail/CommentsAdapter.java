package com.museovivo.app.pointdetail;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// NUEVO: Adapter simple para comentarios
class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {
    private final ArrayList<String> comments;
    CommentsAdapter(ArrayList<String> comments) { this.comments = comments; }
    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView tv = new TextView(parent.getContext());
        tv.setTextSize(15);
        tv.setPadding(16, 8, 16, 8);
        return new CommentViewHolder(tv);
    }
    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        ((TextView) holder.itemView).setText(comments.get(position));
    }
    @Override
    public int getItemCount() { return comments.size(); }
    static class CommentViewHolder extends RecyclerView.ViewHolder {
        CommentViewHolder(@NonNull View itemView) { super(itemView); }
    }
}
