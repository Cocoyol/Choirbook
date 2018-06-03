package com.cocoyol.apps.choirbook.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cocoyol.apps.choirbook.R;
import com.cocoyol.apps.choirbook.models.Lyric;

import java.util.List;

public class ElementAdapter extends RecyclerView.Adapter<ElementAdapter.ViewHolder> {

    private List<Lyric> lyrics;
    private int layout;
    private Activity activity;
    private OnItemClickListener listener;

    public ElementAdapter(List<Lyric> lyrics, int layout, Activity activity, OnItemClickListener listener) {
        this.lyrics = lyrics;
        this.layout = layout;
        this.activity = activity;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(lyrics.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return lyrics.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewSongName;
        public ImageView imageViewSongLogo;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewSongName = itemView.findViewById(R.id.textViewSongName);
        }

        public void bind(final Lyric lyric, final OnItemClickListener listener) {
            this.textViewSongName.setText(lyric.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(lyric, getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Lyric lyric, int position);
    }
}
