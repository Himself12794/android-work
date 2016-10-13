package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private final List<String> words = new ArrayList<String>();
    private final Map<String, List<String>> lettersToWord = new HashMap<>();
    private final Set<String> wordSet = new HashSet<>();

    public AnagramDictionary(InputStream wordListStream) throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while((line = in.readLine()) != null) {

            String word = line.trim();

            words.add(word);
            wordSet.add(word);

            String sorted = sortLetters(word);

            if (!lettersToWord.containsKey(sorted)) {
                List<String> inside = new ArrayList<String>();
                inside.add(word);
                lettersToWord.put(sorted, inside);
            } else {
                lettersToWord.get(sorted).add(word);
            }

        }

        for (String word : words) {

            List<String> temp = getAnagramsWithOneMoreLetter(word);
            lettersToWord.get(sortLetters(word)).addAll(temp);

        }

    }

    public boolean areAnagrams(String word1, String word2) {
        return word1.length() == word2.length() && sortLetters(word1).equals(sortLetters(word2));
    }

    public String sortLetters(String word) {
        char[] t = word.toCharArray();
        Arrays.sort(t);
        return new String(t);
    }

    public boolean isGoodWord(String word, String base) { return !word.contains(base) && wordSet.contains(word); }

    public List<String> getAnagrams(String targetWord) {
        List<String> result = new ArrayList<String>();
        String sorted = sortLetters(targetWord);

        if (lettersToWord.containsKey(sorted)) result.addAll(lettersToWord.get(sorted));

        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        List<String> result = new ArrayList<String>();

        for (char c = 'a'; c <= 'z'; ++c) {
            String sorted = sortLetters(word + c);
            List<String> extra;
            if (lettersToWord.containsKey(sorted)) extra = lettersToWord.get(sorted);
            else extra = new ArrayList<>();

            for (String w : extra) {
                if (w.length() == sorted.length()) result.add(w);
            }

        }

        return result;
    }

    public String pickGoodStarterWord() {

        String word = words.get(random.nextInt(words.size()));
        boolean valid;

        do {
            valid = getAnagrams(word).size() >= MIN_NUM_ANAGRAMS;

            if (!valid) {
                word = words.get(random.nextInt(words.size()));
            }

        } while (!valid);

        return word;
    }
}
