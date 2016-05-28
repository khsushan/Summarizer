package com.summarizer.news.sentence.extractor;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.process.DocumentPreprocessor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ushan on 3/5/16.
 */
public class SentenceExtractor {
    private static List<String[]> allWordsInDocuments = new ArrayList<String[]>(); //this will hold all the words in document
    private static List<String> uniqueWords =
            new ArrayList<String>(); //this all words set will hold all the unique words in allWordsInDocuments
    private static List<String> allSentences = new ArrayList<String>();
    private static List<String> stopWords = null;
    private static int documentCount = 0;

    public void getExtractedWordInGivenDocument(StringBuilder htmlContent) throws IOException, InterruptedException {
        if (htmlContent != null) {
            Pattern re = Pattern.compile("[^.!?\\s][^.!?]*(?:[.!?](?!['\"]?\\s|$)[^.!?]*)*[.!?]?['\"]?(?=\\s|$)",
                    Pattern.MULTILINE | Pattern.COMMENTS);
            Matcher reMatcher = re.matcher(htmlContent.toString());
            while (reMatcher.find()) {
                allSentences.add(reMatcher.group().toString());
                String[] tokenizedTerms = reMatcher.group().toString().
                        replaceAll("[\\W&&[^\\s]]", "").split("\\W+");//to get individual terms
                stopWords = StopwordLoader.getStopWords();
                for (String term : tokenizedTerms) {
                    //avoid duplicate entry & stop words
                    if (!uniqueWords.contains(term) && !stopWords.contains(term.toLowerCase())) {
                        uniqueWords.add(term);
                    }
                }
                allWordsInDocuments.add(tokenizedTerms);
            }
        }
        documentCount++;
        System.out.println("Senetence size : "+allSentences.size()+" and document count is : " +documentCount);
    }

    public List<String[]> getAllWordsInDocuments() { return this.allWordsInDocuments; }

    public List<String> getUniqueWords() { return this.uniqueWords; }

    public List<String> getAllSentences() { return this.allSentences; }
}
