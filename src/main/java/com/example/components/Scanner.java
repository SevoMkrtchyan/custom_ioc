package com.example.components;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

public class Scanner {

    public Collection<String> getResources() {
        final ArrayList<String> retval = new ArrayList<>();
        final String classPath = System.getProperty("classpath:", ".");
        final String[] classPathElements = classPath.split(System.getProperty("path.separator"));
        for (final String element : classPathElements) {
            retval.addAll(getResources(element, Pattern.compile(".*")));
        }
        return retval;
    }

    private Collection<String> getResources(final String element, final Pattern pattern) {
        final ArrayList<String> retval = new ArrayList<String>();
        final File file = new File(element);
        if (file.isDirectory()) {
            retval.addAll(getResourcesFromDirectory(file, pattern));
        }
        return retval;
    }

    private Collection<String> getResourcesFromDirectory(
            final File directory,
            final Pattern pattern) {
        final ArrayList<String> retval = new ArrayList<>();
        final File[] fileList = directory.listFiles();
        for (final File file : fileList) {
            if (file.isDirectory()) {
                retval.addAll(getResourcesFromDirectory(file, pattern));
            } else {
                final String fileName;
                try {
                    fileName = file.getCanonicalPath();

                    final boolean accept = pattern.matcher(fileName).matches();
                    if (accept) {
                        if (fileName.endsWith(".xml")) {
                            retval.add(fileName);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return retval;
    }

}