package com.fb.pearsapplication;

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



public class WordTree implements Comparable<Object> {

    String word;
    Integer frequency = 1;
    Map<WordTree, WordTree> map = new HashMap<>();

    static String body;
    static String[] parts;
    static List<String> wordList;

    public WordTree() {
        this.word = "";
    }

    public WordTree(String word) {
        this.word = word;
    }

    public void addToTree(List<List<String>> ngrams) {
        for (List ngram : ngrams) {
            addNgrams(ngram);
        }
    }

    private void addNgrams(List<String> ngram) {
        System.out.println(ngram); // printed
        if (ngram == null || ngram.isEmpty()) {
            return;
        }
        WordTree t = new WordTree(ngram.remove(0).toLowerCase());
        if (this.map.containsKey(t)) {
            t = this.map.get(t);
            t.frequency++;
            this.map.put(t, t);
        } else {
            this.map.put(t, t);
        }
        try {
            this.map.get(t).addNgrams(ngram);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WordTree t = new WordTree();
        t.addToTree(ngrams(1));
        System.out.println("hey pls work");
        System.out.println(t.suggestNext(chunkLastN(3, "Red-Black  tree based hello hey yer")));
    }

    public static List<List<String>> ngrams(int a) {
        ParseQuery<PearMessage> bodyQuery = ParseQuery.getQuery(PearMessage.class);
        bodyQuery.orderByDescending("body");
        System.out.println("hi hi hi");
        bodyQuery.findInBackground(new FindCallback<PearMessage>() {
            @Override
            public void done(List<PearMessage> objects, ParseException e) {
                String[] words = new String[objects.size()];
                if (objects != null && objects.size() > 0) {
                    for (int i = objects.size() - 1; i >= 0 ; i--) {
                        body = objects.get(i).getBody();
                        words[i] = body;
                    }
                    parts = body.split(" ");
                    for ( String ss : parts) {
                        System.out.println(ss);
                    }
                }
                wordList = Arrays.asList(parts);

            }
        });
        List<List<String>> newList = new ArrayList<>();
        newList.add(wordList);
        return newList;
    }

    public static List<String> chunkLastN(int n,String string){ // takes away the last n words
        List<String> chunks = new ArrayList<>();
        if(string!=null && string.lastIndexOf(" ")==-1){
            chunks.add(string);
        }
        while(string!=null && n>0 && string.lastIndexOf(" ")>0){
            chunks.add(0,string.substring(string.lastIndexOf(" ")).trim());
            string = string.substring(0, string.lastIndexOf(" "));
            System.out.println(n+" : " +string);
            n--;
        }
        System.out.println(chunks);
        return chunks;
    }

    @Override
    public int compareTo(Object o) { //comparing freq
        if (!(o instanceof WordTree)) {
            throw new ClassCastException("The object must be compared with WordTree type");
        }
        WordTree w = (WordTree) o;
        if (this.frequency < w.frequency) {
            return 1;
        } else if (this.frequency == w.frequency) {
            return  0;
        } else {
            return -1;
        }
    }

    public List<WordTree> suggestNext(List<String> chunks) {
        List<String> lst = new ArrayList<>();
        if (chunks == null || chunks.isEmpty()) {
            return Collections.EMPTY_LIST;
        } else if (chunks.size() == 1) {
            Set<WordTree> keySet = this.map.get(new WordTree(chunks.remove(0))).map.keySet();
            ArrayList<WordTree> suggestions = new ArrayList<>(keySet);
            Collections.sort(suggestions);
            return suggestions;
        } else {
                return this.map.get(new WordTree(chunks.remove(0))).suggestNext(chunks);

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + Objects.hashCode(this.word);
        return hash;
    }

    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final WordTree other = (WordTree) obj;

        // also check the equality of frequency of two words
        // since 'frequency' is also a field
        return (!"".equals(word) && word.equals(other.word)) &&
                (this.frequency == other.frequency);
    }

    public void print() {
        System.out.println("word " + this.word + " can be followed by " + this.map.keySet());
        for (Map.Entry e : this.map.entrySet()) {
            ((WordTree) e.getValue()).print();
        }

    }

    @Override
    public String toString() {
        return " \"" + word + "\":" + frequency; //To change body of generated methods, choose Tools | Templates.
    }

}
