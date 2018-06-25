package com.cocoyol.apps.choirbook.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.cocoyol.apps.choirbook.models.Index;
import com.cocoyol.apps.choirbook.models.Lyric;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.cocoyol.apps.choirbook.Consts.APP_LYRICS_FOLDER;
import static com.cocoyol.apps.choirbook.Consts.FILE_LYRICS_INDEX;

public class IndexFiles {

    public static final String SEPARATOR = File.separator;
    private ArrayList<String> lyricsStringData;

    private Context context;
    private ReadWriteExternalStorage readWriteExternalStorage;
    private ReadWriteFileManager readWriteFileManager;

    private Index index;

    public IndexFiles(Context context) {
        this.context = context;
        this.lyricsStringData = new ArrayList<>();

        readWriteExternalStorage = new ReadWriteExternalStorage(context);
        readWriteFileManager = new ReadWriteFileManager(context);

        index = new Index();
        //initializeIndex();
    }

    public Index getIndex() {
        return index;
    }

    public void setIndex(Index index) {
        this.index = index;
    }

    public void initializeIndex() {
        ArrayList<String> files = new ArrayList<>();

        if (readWriteExternalStorage.isExternalStorageAccessible()) {
            if(readWriteExternalStorage.exists(APP_LYRICS_FOLDER)) {
                if(readWriteFileManager.exists(FILE_LYRICS_INDEX)) {
                    files = updateIndexFile((ArrayList<String>) readWriteFileManager.readObjectFromFile(FILE_LYRICS_INDEX));
                } else {
                    files = generateIndexFile();
                }
            } else {
                readWriteExternalStorage.createDirectory(APP_LYRICS_FOLDER);
            }
        }

        loadIndex(files);
    }

    private ArrayList<String> updateIndexFile(ArrayList<String> files) {
        ArrayList<String> newFiles = readWriteExternalStorage.listFilesInDirectory(APP_LYRICS_FOLDER);
        newFiles = Helpers.sortStringArray(newFiles);
        if(!newFiles.equals(files)) {
            readWriteFileManager.writeObjectToFile(files, FILE_LYRICS_INDEX);
        }
        return newFiles;
    }

    private ArrayList<String> generateIndexFile() {
        ArrayList<String> files = readWriteExternalStorage.listFilesInDirectory(APP_LYRICS_FOLDER);
        files = Helpers.sortStringArray(files);

        readWriteFileManager.writeObjectToFile(files, FILE_LYRICS_INDEX);

        return files;
    }

    private void loadIndex(ArrayList<String> files) {
        if(files != null) {
            String[] tokens;
            for (int i = 0; i < files.size(); i++) {

                // Extract Names ?
                tokens = files.get(i).split("\\.(?=[^\\.]+$)");
                if(tokens[0].isEmpty())
                    tokens[0] = files.get(i);
                index.addLyric(new Lyric(tokens[0], APP_LYRICS_FOLDER + SEPARATOR + files.get(i)));

                // Fill section index
                String k = Helpers.removeAccents(Helpers.normalizeText(tokens[0].substring(0, 1))).toUpperCase();
                if(index.sectionsIndex.containsKey(k) && !k.equals("#")) {
                    putSectionIndex(k, i);
                } else if(k.matches("\\d")) {
                    putSectionIndex("#", i);
                } else {
                    putSectionIndex("*", i);
                }
            }
        }
    }

    private void putSectionIndex(String s, int i) {
        if(index.sectionsIndex.get(s) < 0 ) {
            index.sectionsIndex.put(s, i);
        }
    }
}
