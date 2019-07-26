package com.fb.pearsapplication.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;

@ParseClassName("Hobby")
public class Hobby extends ParseObject {
    public static final String KEY_SUBSET = "groupSubsets";
    public static final String KEY_THRESHOLD = "supportThreshold";
    public static final String KEY_OCCURENCES = "occurences";

    public  ArrayList getSubsets() {
        return (ArrayList) getList(KEY_SUBSET);
    }

    public void newSubset(ArrayList<Group> subset) {
        put(KEY_SUBSET, subset);
    }

    public int getThreshold() {
        return getInt(KEY_THRESHOLD);
    }

    public void setThreshold(int threshold) {
        put(KEY_THRESHOLD, threshold);
    }

    public int getOccurences() {
        return getInt(KEY_OCCURENCES);
    }

    public void setOccurences(int occurence) {
        put(KEY_OCCURENCES, occurence);

    }
}
