package com.ablikexe.thevoice;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/* Klasa autora. Przechowuje nazwę autora, nazwy piosenek i ich źródła (adresy URL w przypadku źródeł internetowych,
 * ścieżki do plików w przypadku danych z dysku). Dodatkowo może cache'ować już pobrane piosenki. */
public class Author {

    private String name;
    private List<Song> cachedSongs = new ArrayList<>();
    private List<Pair<String, String>> songsSources = new ArrayList<>();    // pary (nazwa piosenki, jej źródło)

    public Author(String name) { this.name = name; }

    public Author(String name, List<Pair<String, String>> songsSources) {
        this.name = name;
        this.songsSources.addAll(songsSources);
    }

    public void addCachedSong(Song s) {
        cachedSongs.add(s);
    }

    public List<Song> getCachedSongs() {
        return new ArrayList<>(cachedSongs);
    }

    public boolean areSongsCached() {
        return !cachedSongs.isEmpty();
    }

    public void addSongSource(String name, String source) {
        songsSources.add(new Pair<>(name, source));
    }

    public List<Pair<String, String>> getSongsSources() {
        return songsSources;
    }

    public String getName() {
        return name;
    }

}
