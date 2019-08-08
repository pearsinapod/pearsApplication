package com.fb.pearsapplication;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Forest {
    public Map<String, WordFrequencies> forestMap = new HashMap<>();
    public String phrase;
    String currentWord;
    String nextWord;
    List<String> phraseWords;
    public void processPhrase(String phrase) {

        String[] oneGrams = phrase.split(" ");
        for(int i =0; i < oneGrams.length - 1; i++) {
            String keyMap = oneGrams[0];
            for(int j = 1; j < i; j++) {
                keyMap = keyMap+ " "+ oneGrams[j];
            }

            String[] restOfWords = Arrays.copyOfRange(oneGrams, i, oneGrams.length);
            if(!forestMap.containsKey(keyMap)) {
                WordFrequencies mapForKey = new WordFrequencies();
                forestMap.put(keyMap, mapForKey);
            }
            WordFrequencies mapForKey = forestMap.get(keyMap);
            mapForKey.wordCount(restOfWords);
            forestMap.put(keyMap, mapForKey);
            System.out.println(currentWord);
            System.out.println(mapForKey);

        }
        System.out.println("printing forestMap in processPhrase");
        System.out.println(forestMap);
    }

    public List<String> phrasesFollowing(String phrase) {
        processPhrase(phrase);
        phraseWords = new ArrayList<>();
        currentWord = "hi"; //phrase.split(" ")[0];
        while(currentWord != null) {
            WordFrequencies arr = forestMap.get(currentWord);
            System.out.println("printing forestMap in phrasesFollowing");
            System.out.println(forestMap);
            nextWord = arr.getHighestKey();
            phraseWords.add(nextWord);
            currentWord=nextWord;
        }
    return phraseWords;
    }

    public static void main(String[] phrase) {
        Forest f = new Forest();
        String words = "hey hi hi hi hey";
        System.out.println(f.phrasesFollowing(words));
    }

}
