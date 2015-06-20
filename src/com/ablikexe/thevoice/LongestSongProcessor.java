package com.ablikexe.thevoice;

/* Procesor znajdujący piosenkę każdego autora, która zawiera najwięcej różnych(!) słów (trochę myląca nazwa). */
public class LongestSongProcessor extends Processor {

    public static final String regex = "^longest-song$";
    private final String name;
    private final ArgumentParser args;

    LongestSongProcessor(String name, ArgumentParser args) {
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
        for (String authorName: args.getAuthors()) {
            Author author = source.getAuthor(authorName);
            if (author == null) continue;
            String bestSong = "";
            int maxWords = 0;
            for (Song song: source.getSongs(author)) {
                int size = song.getWordCounter().size();
                if (size > maxWords) {
                    maxWords = size;
                    bestSong = song.getTitle();
                }
            }
            System.out.println(authorName + ": " + maxWords + " (" + bestSong + ")");
        }

    }
}
