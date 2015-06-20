package com.ablikexe.thevoice;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

/* Klasa parsująca argumenty programu. */
public class ArgumentParser {

    private final static String PROCESSORS_ARG = "processors",
                                PROCESSORS_SPLIT = ",",
                                SOURCE_TYPE_ARG = "source-type",
                                SOURCE_DETAILS_ARG = "source",
                                FILTERS_ARG = "filters",
                                FILTERS_SPLIT = ":";

    private Map<String, String> map = new HashMap<>();  // mapa {argument podany do programu: jego wartość}
    private List<String> authors = new ArrayList<>();   // lista autorów podana w parametrach
    private Source cachedSource;    // jeśli cache'owanie nie zostało wyłączone, zachowamy pobrane dane
    private Set<String> cachedFilters;  // oraz filtrowane słowa

    public ArgumentParser(String[] args) {
        for (String arg: args) {
            if (arg.startsWith("--")) {
                String[] s = arg.substring(2).split("=");
                map.put(s[0], s[1]);
            } else authors.add(arg);
        }
    }

    void assureArgumentExists(String arg) {
        if (!map.containsKey(arg)) throw new RuntimeException("Missing argument: " + arg);
    }

    public String[] getProcessors() {
        assureArgumentExists(PROCESSORS_ARG);
        return map.get(PROCESSORS_ARG).split(PROCESSORS_SPLIT);
    }

    public Source getSource() {
        if (cachedSource != null) return cachedSource;
        assureArgumentExists(SOURCE_TYPE_ARG);
        String source = map.get(SOURCE_TYPE_ARG);
        Source res;
        switch (source) {
            case FileSource.name:
                res = new FileSource(this);
                break;
            case TekstySource.name:
                res = new TekstySource(this);
                break;
            case AZLyricsSource.name:
                res = new AZLyricsSource(this);
                break;
            case KaczmarskiSource.name:
                res = new KaczmarskiSource(this);
                break;
            default:
                throw new RuntimeException("Unknown source type: " + source);
        }
        if (getCache()) cachedSource = res;
        return res;
    }

    public String getSourceDetails() {
        assureArgumentExists(SOURCE_DETAILS_ARG);
        return map.get(SOURCE_DETAILS_ARG);
    }

    public List<String> getAuthors() { return new ArrayList<>(authors); }

    public Set<String> getFilters() {
        if (getCache() && cachedFilters != null) return cachedFilters;
        Set<String> res = new HashSet<>();
        if (!map.containsKey(FILTERS_ARG)) return res;
        for (String filterFile: map.get(FILTERS_ARG).split(FILTERS_SPLIT)) {
            try {
                byte bytes[] = Files.readAllBytes(new File(filterFile).toPath());
                for (String word : new String(bytes, StandardCharsets.UTF_8).split("[\\s,.:;?!()-]+"))
                    res.add(word.toLowerCase());
            } catch (IOException e) {
                System.err.println("Error while reading filter file: " + filterFile);
            }
        }
        if (getCache()) cachedFilters = res;
        return res;
    }

    public boolean getCache() {
        return !map.containsKey("no-cache");
    }

}
