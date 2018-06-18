package com.cocoyol.apps.choirbook.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ReadWriteSharedPreferences {

    private Context context;
    private SharedPreferences sharedPreferences;
    //SharedPreferences.Editor editor;

    public ReadWriteSharedPreferences(Context context, String name) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        //editor = sharedPreferences.edit();
    }

    public boolean contains(String name) {
        return sharedPreferences.contains(name);
    }

    public void writeArrayToSharedPreferences(String name, ArrayList<String> array) {
        Set<String> set = new HashSet<>(array);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(name, set);
        editor.apply();
    }

    public ArrayList<String> readArrayFromSharedPreferences(String name) {
        ArrayList<String> array = new ArrayList<>();
        if(contains(name)) {
            array = new ArrayList<String>(sharedPreferences.getStringSet(name, new HashSet<String>(array)));
        }
        return array;
    }
}
