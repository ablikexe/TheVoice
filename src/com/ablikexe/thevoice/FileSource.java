package com.ablikexe.thevoice;

import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* Obsługa wczytywania piosenek z katalogów. */
public class FileSource extends Source {

    private final boolean CACHE_ON;
    public final static String name = "file";
    private final String path;
    private Map<String, Author> cachedAuthors = new HashMap<>();

    public FileSource(ArgumentParser args) {
        path = args.getSourceDetails();
        CACHE_ON = args.getCache();
    }

    @Override
    public Author getAuthor(String authorName) {
        if (CACHE_ON && cachedAuthors.containsKey(authorName)) return cachedAuthors.get(authorName);
        File authorDir = new File(path, authorName);
        File[] filesList = authorDir.listFiles();
        if (filesList == null) {
            System.err.println("Couldn't find author: " + authorName);
            return null;
        }
        Author author = new Author(authorName);
        for (File songFile: filesList)
            if (songFile.isFile() && songFile.getName().endsWith(".txt")) {
                String songName = songFile.getName();
                songName = songName.substring(0, songName.length()-4);
                author.addSongSource(songName, songFile.getPath());
            }
        if (CACHE_ON) cachedAuthors.put(authorName, author);
        return author;
    }

    @Override
    public List<Song> getSongs(Author author) {
        if (CACHE_ON && author.areSongsCached()) return author.getCachedSongs();
        List<Song> res = new ArrayList<>();
        for (Pair<String, String> songSource: author.getSongsSources()) {
            Song song;
            try {
                song = new Song(songSource.getKey(), new File(songSource.getValue()));
                res.add(song);
                if (CACHE_ON) author.addCachedSong(song);
            } catch (IOException e) {
                System.err.println("Error while reading song from file: " + songSource.getValue() + ". Omitting song.");
            }
        }
        return res;
    }
}
