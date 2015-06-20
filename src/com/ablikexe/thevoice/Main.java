package com.ablikexe.thevoice;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        ArgumentParser arguments = new ArgumentParser(args);
        List<Processor> processors = Processor.createProcessors(arguments);

        for (Processor processor: processors) {
            System.out.println(processor.getName() + ":");
            processor.run();
            System.out.println("***");
        }

    }

}
