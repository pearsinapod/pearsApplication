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
        System.out.println("about to insert the ngrams");
        System.out.println(ngrams);
        for (List ngram : ngrams) {
            System.out.println("about to add an ngram set");
            System.out.println(ngram);
            addNgrams(ngram);
        }
    }

    private void addNgrams(List<String> ngram) {
        System.out.println("adding");
        System.out.println(ngram);
        System.out.println("done adding");

        System.out.println(ngram); // printed
        if (ngram == null || ngram.isEmpty()) {
            System.out.println("no ngrams found");
            return;
        }
        for ( String word: ngram) {

        }
        String word = ngram.remove(0).toLowerCase();
        System.out.println("word being evaluated");
        System.out.println(word);
        WordTree t = new WordTree(word);

        if (this.map.containsKey(t)) {
            System.out.println("-------NOT adding to map");
            System.out.println(this.map);
            t = this.map.get(t);
            t.frequency++;
            this.map.put(t, t);
        } else {

            System.out.println("-------adding to map");
            System.out.println("before");
            System.out.println(this.map);
            this.map.put(t, t);
            System.out.println("after");
            System.out.println(this.map);
        }
        System.out.println("map printing again!!!!!!!");
        System.out.println(this.map.get(t));
        try {
            WordTree tree = this.map.get(t);
            System.out.println(ngram);
            this.addNgrams(ngram);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WordTree t = new WordTree();
        List<List<String>> listOfngrams = ngrams(1);
        t.addToTree(listOfngrams);

        System.out.println("hey pls work");
        System.out.println(t.map);
        System.out.println(t.suggestNext(chunkLastN(3, "hi i'm jaleel")));
    }

    public static List<List<String>> ngrams(int a) {
        ParseQuery<PearMessage> bodyQuery = ParseQuery.getQuery(PearMessage.class);
        bodyQuery.orderByDescending("body");
        System.out.println("hi hi hi");
        bodyQuery.findInBackground(new FindCallback<PearMessage>() {
            @Override
            public void done(List<PearMessage> objects, ParseException e) {
                System.out.println("running method");
                String[] words = new String[objects.size()];
                if (objects != null && objects.size() > 0) {
                    for (int i = objects.size() - 1; i >= 0 ; i--) {
                        body = objects.get(i).getBody();
                        words[i] = body;
                    }
                    parts = body.split(" ");
                    System.out.println(parts);
                    for ( String ss : parts) {
                        System.out.println(ss);
                    }
                }
                wordList = Arrays.asList(parts);


            }
        });
        wordList = new ArrayList<String>();
        wordList.add("Hello");
        wordList.add("good");
        List<List<String>> newList = new ArrayList<>();
        if(wordList.size() != 0 && wordList != null) {
            newList.add(wordList);
        }
        return newList;
    }

    public static List<String> chunkLastN(int n,String nGramMinus){ // takes away the last n words
        List<String> chunks = new ArrayList<>();
        if(nGramMinus!=null && nGramMinus.lastIndexOf(" ")==-1){
            chunks.add(nGramMinus);
        }
        while(nGramMinus!=null && n>0 && nGramMinus.lastIndexOf(" ")>0){
            chunks.add(0,nGramMinus.substring(nGramMinus.lastIndexOf(" ")).trim());
            nGramMinus = nGramMinus.substring(0, nGramMinus.lastIndexOf(" "));
            System.out.println(n+" : " +nGramMinus);
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
        System.out.println("Comparing words");
        return this.word.compareTo(w.word);
    }

    public List<WordTree> suggestNext(List<String> chunks) {
        List<String> lst = new ArrayList<>();
        if (chunks == null || chunks.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        if (chunks.size() == 1) {
            Set<WordTree> keySet = this.map.get(new WordTree(chunks.remove(0))).map.keySet();
            ArrayList<WordTree> suggestions = new ArrayList<>(keySet);
            Collections.sort(suggestions);
            return suggestions;
        } else {
            WordTree tree = new WordTree(chunks.remove(0));
            WordTree nextTree = this.map.get(tree);
            System.out.println("printing map");
            System.out.println(this.map);
            if(nextTree == null) {
                System.out.println("null next tree");
            } else {
                System.out.println("not null next tree");
            }

            return nextTree.suggestNext(chunks);
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
        return " \"" + word + "\":" + frequency;
    }

}
