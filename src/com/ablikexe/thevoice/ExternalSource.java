package com.ablikexe.thevoice;

import javafx.util.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract public class ExternalSource extends Source {

    protected final boolean CACHE_ON;
    protected final String BASE_URL;
    protected Map<String, Author> cachedAuthors = new HashMap<>();

    ExternalSource(ArgumentParser args) {
        BASE_URL = args.getSourceDetails();
        CACHE_ON = args.getCache();
    }

    abstract protected String getAuthorUrl(String authorName);
    abstract String processSongsListSite(Document site, String url, List<Pair<String, String>> list);
    protected abstract Song processSongSite(Document site, String songName);

    protected List<Pair<String, String>> getSongsSourcesFromUrl(String url) {
        List<Pair<String, String>> res = new ArrayList<>();
        String nextPage;
        Document songsListSite;
        try {
            songsListSite = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.err.println("Error while downloading site: " + url);
            return res;
        }
        nextPage = processSongsListSite(songsListSite, url, res);
        if (nextPage != null) res.addAll(getSongsSourcesFromUrl(nextPage));
        return res;
    }

    @Override
    public Author getAuthor(String authorName) {
        if (CACHE_ON && cachedAuthors.containsKey(authorName)) return cachedAuthors.get(authorName);
        String url = getAuthorUrl(authorName);
        if (url == null) {
            System.err.println("Unknown author: " + authorName);
            return null;
        }
        Author author = new Author(authorName, getSongsSourcesFromUrl(url));
        if (CACHE_ON) cachedAuthors.put(authorName, author);
        return author;
    }

    @Override
    public List<Song> getSongs(Author author) {
        if (CACHE_ON && author.areSongsCached()) return author.getCachedSongs();
        List<Song> songs = new ArrayList<>();
        for (Pair<String, String> songSource: author.getSongsSources()) {
            String songName = songSource.getKey();
            String url = songSource.getValue();
            Document site;
            try {
                site = Jsoup.connect(url).get();
            } catch (IOException e) {
                System.err.println("Error while downloading song: " + url);
                continue;
            }
            Song song = processSongSite(site, songName);
            if (song == null) {
                System.err.println("Error while processing song: " + songName);
                continue;
            }
            songs.add(song);
            author.addCachedSong(song);
        }
        return songs;
    }

}
