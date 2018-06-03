package com.cocoyol.apps.choirbook.models;

public class Lyric {

    private String name;
    private String file;

    public Lyric(String name, String file) {
        this.name = name;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
