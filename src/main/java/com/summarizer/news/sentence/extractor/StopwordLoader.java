package com.summarizer.news.sentence.extractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Ushan on 5/20/2016.
 */
public class StopwordLoader {
    private static ArrayList<String> stopwords;
    private final static String textFilePath = "src/main/resources/stopword/english/Stopword.txt";
    public static ArrayList<String> getStopWords() throws IOException, InterruptedException {
        //if(stopwords == null){
            stopwords = new ArrayList<String>();
            File stopword = new File(textFilePath);
            BufferedReader stopWordBufferReader =  new BufferedReader(new FileReader(stopword));
            while (stopWordBufferReader.readLine() != null){
                if(stopWordBufferReader.readLine() != null) {
                    //Thread.sleep(500);
                    stopwords.add(stopWordBufferReader.readLine());
                }
            }
            return stopwords;
        //}else {
          //  return stopwords;
        //}
    }

//    public static void main(String[] args) {
//        try {
//            ArrayList<String> stopwords = getStopWords();
//            for (String stopword: stopwords) {
//                System.out.println(stopword+"============================");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//    }

}
