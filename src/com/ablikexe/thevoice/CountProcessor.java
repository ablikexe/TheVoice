package com.ablikexe.thevoice;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* Procesor zliczający różne słowa użyte przez każdego artystę. */
public class CountProcessor extends Processor {

    public static final String regex = "^count$";
    private final String name;
    private final ArgumentParser args;

    public CountProcessor(String name, ArgumentParser args) {
        this.name = name;
        this.args = args;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void run() {
        Source source = args.getSource();
        List<Pair<String, Integer>> res = new ArrayList<>();
        for (String authorName: args.getAuthors()) {
            Author author = source.getAuthor(authorName);
            if (author == null) continue;
            WordCounter counter = new WordCounter();
            for (Song song: source.getSongs(author)) counter.union(song.getWordCounter());
            res.add(new Pair<>(authorName, counter.size()));
        }

        Collections.sort(res, (a, b) -> Integer.compare(b.getValue(), a.getValue()));
        for (Pair<String, Integer> authorWords: res)
            System.out.println(authorWords.getKey() + " " + authorWords.getValue());
    }

}
