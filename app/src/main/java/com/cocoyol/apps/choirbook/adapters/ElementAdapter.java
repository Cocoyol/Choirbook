package com.cocoyol.apps.choirbook.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.cocoyol.apps.choirbook.R;
import com.cocoyol.apps.choirbook.models.Lyric;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ElementAdapter extends RecyclerView.Adapter<ElementAdapter.ViewHolder> implements SectionIndexer{

    private List<Lyric> lyrics;
    private List<Integer> sectionsPositions;
    private LinkedHashMap<String, Integer> sectionsPositionsMap;
    private int layout;
    private Activity activity;
    private OnItemClickListener listener;

    public ElementAdapter(List<Lyric> lyrics, LinkedHashMap<String, Integer> sectionsPositionsMap, int layout, Activity activity, OnItemClickListener listener) {
        this.lyrics = lyrics;
        this.layout = layout;
        this.activity = activity;
        this.listener = listener;

        this.sectionsPositionsMap = sectionsPositionsMap;
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

    @Override
    public Object[] getSections() {
        List<String> sections = new ArrayList<>();
        sectionsPositions = new ArrayList<>();
        for (Map.Entry<String, Integer> sectionPosition : sectionsPositionsMap.entrySet()) {
            if(sectionPosition.getValue() >= 0) {
                sections.add(sectionPosition.getKey());
                sectionsPositions.add(sectionPosition.getValue());
            }
        }
        return sections.toArray(new String[0]);
    }

    @Override
    public int getPositionForSection(int i) {
        return sectionsPositions.get(i);
    }

    @Override
    public int getSectionForPosition(int i) {
        return 0;
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
