package com.cocoyol.apps.choirbook.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.cocoyol.apps.choirbook.models.Index;
import com.cocoyol.apps.choirbook.models.Lyric;

import java.io.File;
import java.util.ArrayList;

import static com.cocoyol.apps.choirbook.Consts.APP_LYRICS_FOLDER;
import static com.cocoyol.apps.choirbook.Consts.LYRICS_FILES_LIST;
import static com.cocoyol.apps.choirbook.Consts.SHARED_PREFERENCES_INDEX;

public class IndexFiles {

    public static final String SEPARATOR = File.separator;
    private ArrayList<String> lyricsStringData;

    private Context context;
    private ReadWriteExternalStorage readWriteExternalStorage;
    private ReadWriteSharedPreferences sharedPreferences;

    private Index index;

    public IndexFiles(Context context) {
        this.context = context;
        this.lyricsStringData = new ArrayList<>();

        readWriteExternalStorage = new ReadWriteExternalStorage(context);
        sharedPreferences = new ReadWriteSharedPreferences(context, SHARED_PREFERENCES_INDEX);

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
            //Log.d("files", "Legible/Escribible! ");
            if(readWriteExternalStorage.exists(APP_LYRICS_FOLDER)) {
                //Log.d("files", "Carpeta existe. ");
                if(sharedPreferences.contains(LYRICS_FILES_LIST)) {
                    //Log.d("files", "Shared existe. ");
                    files = updateIndexFile(sharedPreferences.readArrayFromSharedPreferences(LYRICS_FILES_LIST));
                } else {
                    //Log.d("files", "Shared NO existe. ");
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
        //Log.d("filesSize_Update", newFiles.size() +" ");
        newFiles = Helpers.sortStringArray(newFiles);
        if(!newFiles.equals(files)) {
            sharedPreferences.writeArrayToSharedPreferences(LYRICS_FILES_LIST, files);
        }
        return newFiles;
    }

    private ArrayList<String> generateIndexFile() {
        ArrayList<String> files = readWriteExternalStorage.listFilesInDirectory(APP_LYRICS_FOLDER);
        //Log.d("filesSize_ArrL", files.size() +" ");
        files = Helpers.sortStringArray(files);

        sharedPreferences.writeArrayToSharedPreferences(LYRICS_FILES_LIST, files);

        return files;
    }

    private void loadIndex(ArrayList<String> files) {
        //Log.d("filesSize_LIdx", files.size() +" ");
        if(files != null) {
            String[] tokens;
            for (String file : files) {
                tokens = file.split("\\.(?=[^\\.]+$)");
                index.addLyric(new Lyric(tokens[0], APP_LYRICS_FOLDER + SEPARATOR + file));
                //Log.d("filesI", file);
            }
        }
    }
}
