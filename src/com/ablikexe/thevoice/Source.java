package com.ablikexe.thevoice;

import java.util.List;

abstract public class Source {

    public abstract Author getAuthor(String authorName);
    public abstract List<Song> getSongs(Author author);

}
