package com.ablikexe.thevoice;

import java.util.ArrayList;
import java.util.List;

public abstract class Processor {

    public static List<Processor> createProcessors(ArgumentParser args) {
        List<Processor> res = new ArrayList<>();

        for (String name : args.getProcessors()) {
            if (name.matches(CountProcessor.regex)) res.add(new CountProcessor(name, args));
            if (name.matches(TopProcessor.regex)) res.add(new TopProcessor(name, args));
            if (name.matches(LongestSongProcessor.regex)) res.add(new LongestSongProcessor(name, args));
        }

        return res;
    }

    abstract public String getName();

    abstract public void run();

}
