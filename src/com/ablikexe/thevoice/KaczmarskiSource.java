package com.ablikexe.thevoice;

import javafx.util.Pair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class KaczmarskiSource extends ExternalSource {

    public final static String name = "kaczmarski.art.pl";

    public KaczmarskiSource(ArgumentParser args) {
        super(args);
    }

    @Override
    protected String getAuthorUrl(String authorName) {
        if (!authorName.equals("Jacek Kaczmarski")) return null;
        return BASE_URL + "/tworczosc/wiersze_alfabetycznie/index.php";
    }

    @Override
    String processSongsListSite(Document site, String url, List<Pair<String, String>> list) {
        int lastSeparator = url.lastIndexOf('/');
        String currentHref = url.substring(lastSeparator + 1);
        String currentDir = url.substring(0, lastSeparator + 1);
        for (Element e: site.select("ul li a")) {
            list.add(new Pair<>(e.text(), currentDir + e.attr("href")));
        }
        boolean getNext = true;
        String nextPage = null;
        Elements menu = site.select("div.alfabet a");
        if (menu.isEmpty()) menu = site.select("div.menu1 a");
        for (Element e: menu) {
            if (getNext) nextPage = currentDir + e.attr("href");
            getNext = e.attr("href").equals(currentHref);
        }
        if (getNext) return null;
        return nextPage;
    }

    @Override
    protected Song processSongSite(Document site, String songName) {
        StringBuilder text = new StringBuilder();
        for (Element e: site.select("td:not([width]) p"))
            text.append(e.text()).append("\n");
        return new Song(songName, text.toString());
    }

}
