package com.cocoyol.apps.choirbook.utils;

import android.content.Context;

import com.cocoyol.apps.choirbook.models.Index;
import com.cocoyol.apps.choirbook.models.Lyric;

import java.io.File;
import java.util.ArrayList;

public class IndexFiles {
    public static final String DIRECTORY = "songs";
    public static final String SEPARATOR = File.separator;
    public static final String INDEX_FILE = "index.idx";
    private static final String INDEX_FULL_PATH = DIRECTORY + SEPARATOR+ INDEX_FILE;

    private Context context;
    private ReadWriteFileManager readWriteFileManager;

    private Index index;

    public IndexFiles(Context context) {
        this.context = context;

        readWriteFileManager = new ReadWriteFileManager();

        index = new Index();
        init();

    }

    public Index getIndex() {
        return index;
    }

    public void setIndex(Index index) {
        this.index = index;
    }

    private void init() {
        ArrayList<String> files;

        if (!readWriteFileManager.exists(context, DIRECTORY)) {
            readWriteFileManager.createDirectory(context, DIRECTORY);
        }

        if(readWriteFileManager.exists(context, INDEX_FULL_PATH)) {
            files = readWriteFileManager.readArrayFromFile(context, INDEX_FULL_PATH);
            //Log.d("MENSAJE00.-1", "Índice Leido");
        } else {
            files = generateIndexFile();
            //Log.d("MENSAJE00.-2", "Índice generado");
        }

        if(files != null) {
            //Log.d("MENSAJE00.", String.valueOf(files.size()));
            for (String s : files) {
                //Log.d("MENSAJE00", s);
            }
        }

        createIndex(files);
    }

    private ArrayList<String> generateIndexFile() {

        ArrayList<String> files = readWriteFileManager.listFilesInDirectory(context, DIRECTORY);
        files = Helpers.sortStringArray(files);

        readWriteFileManager.writeArrayToFile(context, files, INDEX_FULL_PATH);

        return files;
    }

    private void createIndex(ArrayList<String> files) {
        index.setFile(INDEX_FULL_PATH);

        if(files != null) {
            String[] tokens;
            //Log.d("MENSAJE01.", "---");
            for (String file : files) {
                tokens = file.split("\\.(?=[^\\.]+$)");
                index.addLyric(new Lyric(tokens[0], DIRECTORY + SEPARATOR + file));
                //Log.d("MENSAJE01", tokens[0]);
            }
        }
    }
}
