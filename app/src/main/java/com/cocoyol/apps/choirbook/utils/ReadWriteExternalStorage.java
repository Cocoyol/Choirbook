package com.cocoyol.apps.choirbook.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

public class ReadWriteExternalStorage {

    private Context context;

    private String SEPARATOR;

    public ReadWriteExternalStorage(Context context) {
        this.context = context;

        this.SEPARATOR = File.separator;
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public boolean createDirectory(String dirName) {
        File dir = new File(Environment.getExternalStorageDirectory() + SEPARATOR + dirName);
        boolean result = false;
        if (!dir.exists()) {
            try {
                result = dir.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(context, Environment.getExternalStorageDirectory().toString() + SEPARATOR + dirName + " -> " + result, Toast.LENGTH_LONG).show();
        return result;
    }
}
