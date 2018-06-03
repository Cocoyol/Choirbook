package com.cocoyol.apps.choirbook.models;

import java.util.ArrayList;

public class Index {

    private String file;
    private ArrayList<Lyric> lyrics;

    public Index() {
        file = "";
        lyrics = new ArrayList<Lyric>();
    }

    public Index(String file, ArrayList<Lyric> lyrics) {
        this.file = file;
        this.lyrics = lyrics;
    }

    public Index(String file) {
        this.file = file;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
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
