package com.ablikexe.thevoice;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class Song {

    private String title;
    private String lyrics;
    private WordCounter words;

    public Song(String title, String lyrics) {
        this.title = title;
        this.lyrics = lyrics;
    }

    public Song(String title, File file) throws IOException {
        this.title = title;
        byte bytes[] = Files.readAllBytes(file.toPath());
        lyrics = new String(bytes, StandardCharsets.UTF_8);
    }

    public String getTitle() { return title; }

    public WordCounter getWordCounter() {
        if (words == null) {
            words = new WordCounter();
            for (String word: lyrics.split("[\\s,.:;?!()-]+"))
                words.add(word.toLowerCase());
        }
        return words;
    }

}
