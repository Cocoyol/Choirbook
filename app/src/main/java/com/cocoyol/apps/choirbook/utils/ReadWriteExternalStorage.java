package com.cocoyol.apps.choirbook.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

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

    public boolean isExternalStorageAccessible() {
        return (isExternalStorageReadable() && isExternalStorageWritable());
    }

    public boolean exists(String fileName) {
        File file = new File(Environment.getExternalStorageDirectory() + SEPARATOR + fileName);
        return file.exists();
    }

    public boolean isDirectory (String dirName) {
        File file = new File(Environment.getExternalStorageDirectory() + SEPARATOR + dirName);
        return file.isDirectory();
    }

    public boolean isEmptyDirectory (String dirName) {
        File file = new File(Environment.getExternalStorageDirectory() + SEPARATOR + dirName);
        return file.list().length == 0;
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
        return result;
    }

    public ArrayList<String> listFilesInDirectory(String dirName) {
        ArrayList<String> files = new ArrayList<String>();
        File dir = new File(Environment.getExternalStorageDirectory() + SEPARATOR + dirName);
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

    public boolean clearDirectory(String dirName) {
        File dir = new File(Environment.getExternalStorageDirectory() + SEPARATOR + dirName);
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

    public String readFromTextFile(String fileName) {
        FileInputStream fileInputStream;
        InputStreamReader inputStreamReader;
        char[] inputBuffer;
        String fileContent = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory() + SEPARATOR + fileName);
            if (file.exists()) {
                inputBuffer = new char[(int) file.length()];

                fileInputStream = new FileInputStream(file);

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

    public boolean writeToTextFile(byte[] data, String fileName) {
        FileOutputStream fileOutputStream;
        boolean result = false;
        try {
            File file = new File(Environment.getExternalStorageDirectory() + SEPARATOR + fileName);
            fileOutputStream = new FileOutputStream(file, false);
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

    public <T> ArrayList<T> readArrayFromFile(String fileName) {
        FileInputStream fileInputStream;
        ArrayList<T> fileContent = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory() + SEPARATOR + fileName);
            if (file.exists()) {
                fileInputStream = new FileInputStream(file);
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

    public <T> boolean writeArrayToFile(ArrayList<T> data, String fileName) {
        FileOutputStream fileOutputStream;
        boolean result = false;
        try {
            File file = new File(Environment.getExternalStorageDirectory() + SEPARATOR + fileName);
            fileOutputStream = new FileOutputStream(file, false);
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

    public boolean delete(String fileName) {
        boolean result = true;
        try {
            File file = new File(Environment.getExternalStorageDirectory() + SEPARATOR + fileName);
            result = file.delete();
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }

        return result;
    }
}
