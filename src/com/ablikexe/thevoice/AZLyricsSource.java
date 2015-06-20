package com.ablikexe.thevoice;

import javafx.util.Pair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;

public class AZLyricsSource extends ExternalSource {

    public final static String name = "azlyrics.com";

    public AZLyricsSource(ArgumentParser args) {
        super(args);
    }

    protected String getAuthorUrl(String authorName) {
        return BASE_URL + "/" + authorName.toLowerCase().charAt(0) + "/" +
                authorName.toLowerCase().replace(" ", "") + ".html";
    }

    @Override
    String processSongsListSite(Document site, String url, List<Pair<String, String>> list) {
        for (Element e: site.select("a[href^=\"../lyrics/\""))
            list.add(new Pair<>(e.text(), BASE_URL + e.attr("href").substring(2)));
        return null;
    }

    @Override
    protected Song processSongSite(Document site, String songName) {
        String text = site.select("div div.row div.text-center div:not([class])").get(0).text();
        return new Song(songName, text);
    }

}
