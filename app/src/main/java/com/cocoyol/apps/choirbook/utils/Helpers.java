package com.cocoyol.apps.choirbook.utils;

import android.content.Context;
import android.util.Pair;

import java.text.Collator;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helpers {

    public static ArrayList<String> sortStringArray(ArrayList<String> arrayList) {
        Collator coll = Collator.getInstance();
        coll.setStrength(Collator.PRIMARY);
        Collections.sort(arrayList, coll);
        return arrayList;
    }

    public static String removeAccents(String s) {
        s = s.replaceAll("[\\p{M}]", "");
        //s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        //s = s.replaceAll("[^\\p{ASCII}]", "");
        return s;
    }

    public static String normalizeText(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFKD);
        return s;
    }

    public static Pair<Pair<String, String>, List<Integer>> normalizeTextAndGenerateMap(String s){
        s = Normalizer.normalize(s, Normalizer.Form.NFKD);

        int sLength = s.length();
        int pos0;
        String tmpString;
        List<Integer> l;
        StringBuilder sb;


        Pattern p = Pattern.compile("[\\p{M}]");
        Matcher m = p.matcher(s);
        if(m.find()) {
            l = new ArrayList<>();
            sb = new StringBuilder();
            pos0 = m.start();
            for (int i = 0; i < sLength; i++) {
                if (i < pos0) {
                    l.add(i);
                    sb.append(s.charAt(i));
                } else {
                    if (i + 1 < sLength) {
                        if (m.find(i + 1))
                            pos0 = m.start();
                        else
                            pos0 = sLength;
                    }
                }
            }
            tmpString = sb.toString();
        } else {
            l = new ArrayList<>(sLength);
            tmpString = s;
            for (int i = 0; i < sLength; i++) {
                l.add(i);
            }
        }

        return new Pair<Pair<String, String>, List<Integer>>(new Pair<>(s, tmpString), l);
    }

    public static float getPxFromDP(float dpValue, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return dpValue * density + 0.5f;
    }

    public static float getDpFromPx(float pxValue, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return (pxValue - 0.5f) / density;
    }
}
