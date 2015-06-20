package com.ablikexe.thevoice;

import java.util.List;

import javafx.util.Pair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TekstySource extends ExternalSource {

    public final static String name = "teksty.org";

    public TekstySource(ArgumentParser args) { super(args); }

    @Override
    protected String getAuthorUrl(String authorName) {
        return BASE_URL + "/" + String.join("-", authorName.toLowerCase().split(" ")) + ",testy-piosenek";
    }

    @Override
    String processSongsListSite(Document site, String url, List<Pair<String, String>> list) {
        for (Element e: site.select("a[href$=\"tekst-piosenki\""))
            list.add(new Pair<>(e.attr("title"), e.attr("href")));
        Elements e = site.select("a[class=\"next-site\"");
        if (!e.isEmpty()) return BASE_URL + e.get(0).attr("href");
        return null;
    }

    @Override
    protected Song processSongSite(Document site, String songName) {
        String text = site.select("div.originalText").get(0).text();
        songName = site.select("span[class=\"song\"]").get(0).text();
        return new Song(songName, text);
    }
}
