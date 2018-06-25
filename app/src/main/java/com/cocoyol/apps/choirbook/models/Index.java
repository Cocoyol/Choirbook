package com.cocoyol.apps.choirbook.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Index {

    public LinkedHashMap<String, Integer> sectionsIndex;
    private ArrayList<Lyric> lyrics;

    public Index() {
        lyrics = new ArrayList<Lyric>();
        createSectionIndex();
    }

    public Index(ArrayList<Lyric> lyrics) {
        this.lyrics = lyrics;
        createSectionIndex();
    }

    public ArrayList<Lyric> getLyrics() {
        return lyrics;
    }

    public void setLyrics(ArrayList<Lyric> lyrics) {
        this.lyrics = lyrics;
    }

    public void addLyric(Lyric lyric){
        lyrics.add(lyric);
    }

    private void createSectionIndex() {
        String[] idx = {"*", "#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "Ñ", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        sectionsIndex = new LinkedHashMap<>();
        for (String i : idx) {
            sectionsIndex.put(i, -1);
        }
    }

}
