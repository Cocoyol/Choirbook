package com.cocoyol.apps.choirbook.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.cocoyol.apps.choirbook.R;
import com.cocoyol.apps.choirbook.models.Lyric;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ElementAdapter extends RecyclerView.Adapter<ElementAdapter.ViewHolder> implements SectionIndexer{

    private Context context;

    private List<Lyric> lyrics;
    private List<Integer> sectionsPositions;
    private LinkedHashMap<String, Integer> sectionsPositionsMap;
    private LinkedHashMap<Integer, String> inverseSectionsPositionsMap;
    private int layout;
    private Activity activity;
    private OnItemClickListener listener;

    public ElementAdapter(Context context, List<Lyric> lyrics, LinkedHashMap<String, Integer> sectionsPositionsMap, int layout, Activity activity, OnItemClickListener listener) {
        this.context = context;
        this.lyrics = lyrics;
        this.layout = layout;
        this.activity = activity;
        this.listener = listener;

        this.sectionsPositionsMap = sectionsPositionsMap;
        this.inverseSectionsPositionsMap = inverseSectionsPositions();
    }

    private LinkedHashMap<Integer, String> inverseSectionsPositions() {
        inverseSectionsPositionsMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> sectionPosition : sectionsPositionsMap.entrySet()) {
            inverseSectionsPositionsMap.put(sectionPosition.getValue(), sectionPosition.getKey());
        }
        return inverseSectionsPositionsMap;
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
        // Set Section title
        boolean showSectionTitle = false;
        String sectionTitle = "";
        if(inverseSectionsPositionsMap.containsKey(position)) {
            showSectionTitle = true;
            sectionTitle = inverseSectionsPositionsMap.get(position);
        }

        holder.bind(lyrics.get(position), showSectionTitle, sectionTitle, listener);
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

        private TextView textViewSongName;
        private TextView textViewSectionTitle;
        private ImageView imageViewSongLogo;

        private ViewHolder(View itemView) {
            super(itemView);
            textViewSongName = itemView.findViewById(R.id.textViewSongName);
            textViewSectionTitle = itemView.findViewById(R.id.textViewSectionTitle);
        }

        private void bind(final Lyric lyric, boolean showSectionTitle, String sectionTitle, final OnItemClickListener listener) {
            this.textViewSongName.setText(lyric.getName());

            textViewSectionTitle.setText(sectionTitle);
            int topMargin = showSectionTitle ? ((int) context.getResources().getDimension(R.dimen.song_list_section_separations)) : 0;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(itemView.getLayoutParams());
            layoutParams.setMargins(0, topMargin, 0, 0);
            itemView.setLayoutParams(layoutParams);

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
