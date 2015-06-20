package com.ablikexe.thevoice;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* Procesor zliczający wystąpienia poszczególnych słów w piosenkach każdego autora i wypisujący najczęściej używane. */
public class TopProcessor extends Processor {

    public static final String regex = "^top(\\d+)$";
    private final String name;
    private final ArgumentParser args;
    private final int numberOfWords;

    public TopProcessor(String name, ArgumentParser args) {
        this.name = name;
        this.args = args;
        Matcher m = Pattern.compile(regex).matcher(name);
        if (!m.matches()) throw new RuntimeException("Illegal name for TopProcessor: " + name);
        numberOfWords = Integer.parseInt(m.group(1));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void run() {
        Source source = args.getSource();
        for (String authorName: args.getAuthors()) {
            Author author = source.getAuthor(authorName);
            if (author == null) continue;

            WordCounter counter = new WordCounter(args.getFilters());
            for (Song song: source.getSongs(author)) counter.union(song.getWordCounter());

            List<Pair<String, Integer>> res = new ArrayList<>();
            for (String word: counter.mostCommon(numberOfWords))
                res.add(new Pair<>(word, counter.get(word)));

            System.out.println(authorName + ": " + res);
        }
    }

}
