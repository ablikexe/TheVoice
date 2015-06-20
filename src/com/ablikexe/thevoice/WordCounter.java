package com.ablikexe.thevoice;

import java.util.*;

/* Klasa zliczająca słowa. Obsługuje łączenie dwóch obiektów tej klasy i wybranie najczęściej występujących słów. */
public class WordCounter {

    private Map<String, Integer> counter = new HashMap<>();
    private Set<String> filters = new HashSet<>();

    public WordCounter() {}
    public WordCounter(Set<String> filters) {
        this.filters.addAll(filters);
    }

    private void add(String word, int val) {
        if (!filters.contains(word)) counter.put(word, get(word) + val);
    }

    public void add(String word) { add(word, 1); }

    public int get(String word) {
        return counter.getOrDefault(word, 0);
    }

    public void union(WordCounter w) {
        for (String word: w.getWords())
            add(word, w.get(word));
    }

    public Set<String> getWords() { return counter.keySet(); }

    private int compareWords(String a, String b) {
        return 2 * Integer.signum(counter.get(b).compareTo(counter.get(a))) + Integer.signum(b.compareTo(a));
    }

    public List<String> mostCommon(int x) {
        List<String> sortedWords = new ArrayList<>(getWords());
        Collections.sort(sortedWords, this::compareWords);
        return sortedWords.subList(0, Math.min(x, sortedWords.size()));
    }

    public int size() { return counter.size(); }

}
