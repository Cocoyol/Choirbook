package com.cocoyol.apps.choirbook.models;

import java.util.ArrayList;

public class Index {

    private ArrayList<Lyric> lyrics;

    public Index() {
        lyrics = new ArrayList<Lyric>();
    }

    public Index(ArrayList<Lyric> lyrics) {
        this.lyrics = lyrics;
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
}
