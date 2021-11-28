package com.example.components;

import org.reflections.Reflections;

public class Scanner {

    private final Reflections scanner;

    public Scanner(String packageToScan) {
        scanner = new Reflections(packageToScan);
    }

    public Reflections getScanner() {
        return scanner;
    }
}
