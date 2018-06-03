package com.cocoyol.apps.choirbook.utils;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ReadWriteFileManager {
    public ReadWriteFileManager() {
    }

    public boolean exists(Context context, String fileName) {
        File file = new File(context.getFilesDir().getPath() + File.separator + fileName);
        return file.exists();
    }

    public String readFromTextFile(Context context, String fileName) {

        FileInputStream fileInputStream;
        InputStreamReader inputStreamReader;

        char[] inputBuffer;
        String fileContent = null;

        try {
            File file = new File(context.getFilesDir().getPath() + File.separator + fileName);
            if (file.exists()) {
                inputBuffer = new char[(int) file.length()];

                fileInputStream = new FileInputStream(file);
                //fileInputStream = context.openFileInput(fileName);

                inputStreamReader = new InputStreamReader(fileInputStream);
                inputStreamReader.read(inputBuffer);

                fileContent = new String(inputBuffer);

                try {
                    inputStreamReader.close();
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            fileContent = null;
        }

        return fileContent;
    }

    public boolean writeToTextFile(Context context, byte[] data, String fileName) {

        FileOutputStream fileOutputStream;
        boolean result = false;

        try {
            File file = new File(context.getFilesDir().getPath() + File.separator + fileName);
            fileOutputStream = new FileOutputStream(file, false);
            //fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fileOutputStream.write(data);

            result = true;
            try {
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }

        return result;
    }

    public <T> ArrayList<T> readArrayFromFile(Context context, String fileName) {
        FileInputStream fileInputStream;
        ArrayList<T> fileContent = null;

        try {
            File file = new File(context.getFilesDir().getPath() + File.separator + fileName);
            if (file.exists()) {
                fileInputStream = new FileInputStream(file);
                //fileInputStream = context.openFileInput(fileName);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                fileContent = (ArrayList<T>) objectInputStream.readObject();

                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fileContent = null;
        }

        return fileContent;
    }

    public <T> boolean writeArrayToFile(Context context, ArrayList<T> data, String fileName) {

        FileOutputStream fileOutputStream;
        boolean result = false;
        try {
            File file = new File(context.getFilesDir().getPath() + File.separator + fileName);
            fileOutputStream = new FileOutputStream(file, false);
            //fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(data);

            result = true;
            try {
                objectOutputStream.flush();
                objectOutputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            result = false;
        }

        return result;
    }

    public boolean delete(Context context, String fileName) {

        boolean result = true;
        try {
            File file = new File(context.getFilesDir().getPath() + File.separator + fileName);
            result = file.delete();
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }

        return result;
    }

    public boolean createDirectory(Context context, String dirName) {
        File dir = new File(context.getFilesDir().getPath() + File.separator + dirName);
        boolean result = false;
        if (!dir.exists()) {
            try {
                result = dir.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public ArrayList<String> listFilesInDirectory(Context context, String dirName) {

        ArrayList<String> files = new ArrayList<String>();
        File dir = new File(context.getFilesDir().getPath() + File.separator + dirName);
        //Log.d("MENSAJE10", context.getFilesDir().getPath() + File.separator + dirName);
        if (dir.exists()) {
            for (File file : dir.listFiles()) {
                try {
                    files.add(file.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return files;
    }

    public boolean clearDirectory(Context context, String dirName) {

        File dir = new File(context.getFilesDir().getPath() + File.separator + dirName);

        boolean result = false;
        if (dir.exists()) {
            for (File file : dir.listFiles()) {
                try {
                    result = file.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
