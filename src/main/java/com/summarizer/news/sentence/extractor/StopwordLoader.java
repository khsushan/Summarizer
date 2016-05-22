package com.summarizer.news.sentence.extractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by Ushan on 5/20/2016.
 */
public class StopwordLoader {
    private static ArrayList<String> stopwords;
    private final static String textFilePath = "src/main/resources/stopword/english/Stopword.txt";
    public static ArrayList<String> getStopWords() throws IOException, InterruptedException {
        Path path = Paths.get(textFilePath);
        return (ArrayList<String>) Files.readAllLines(path, StandardCharsets.UTF_8);
    }

}
