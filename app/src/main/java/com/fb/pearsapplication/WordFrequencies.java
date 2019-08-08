package com.fb.pearsapplication;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.io.*;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.fb.pearsapplication.models.PearMessage;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class WordFrequencies {
    static String body;
    static String[] parts;
    static List<String> wordList;
    public String test;
    public int count;
    public Map<String, Integer> wordFrequencyMap = new HashMap<>();

    public Map<String, Integer> wordCount(String[] words) {
        for (String s : words) {
            if (!wordFrequencyMap.containsKey(s)) {
                this.wordFrequencyMap.put(s, 1);
            } else
                count = wordFrequencyMap.get(s);
            this.wordFrequencyMap.put(s, count + 1);
        }
        System.out.println(wordFrequencyMap);
        return wordFrequencyMap;
    }


    public String getHighestKey () {
        Map.Entry<String, Integer> maxEntry = null;

        for (Map.Entry<String, Integer> entry : wordFrequencyMap.entrySet())
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
            {
                maxEntry = entry;
            }
        }
        return maxEntry.getKey();
    }
    public void stringArray() {
        ParseQuery<PearMessage> bodyQuery = ParseQuery.getQuery(PearMessage.class);
            bodyQuery.orderByDescending("body");
            System.out.println("hi hi hi");
            bodyQuery.findInBackground(new FindCallback<PearMessage>()
            {
                @Override
                public void done (List < PearMessage > objects, ParseException e){
                    System.out.println("running method");
                    String[] words = new String[objects.size()];
                    if (objects != null && objects.size() > 0) {
                        for (int i = objects.size() - 1; i >= 0; i--) {
                            body = objects.get(i).getBody();
                            words[i] = body;
                        }
                        parts = body.split(" ");
                        System.out.println(parts);
                        for (String ss : parts) {
                            System.out.println(ss);
                        }
                    }
                    wordList = Arrays.asList(parts);
                }
            });
    }
    public static void main(String[] strings) {
        WordFrequencies f = new WordFrequencies();
        String[] words = new String[4];
        words[0]="hey";
        words[1]="hi";
        words[2]="hi";
        words[3]= "hi";
        f.wordCount(words);
//        System.out.println(f.wordFrequencyMap);
    }


}


