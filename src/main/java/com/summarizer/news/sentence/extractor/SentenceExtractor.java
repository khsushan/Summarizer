package com.summarizer.news.sentence.extractor;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.process.DocumentPreprocessor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ushan on 3/5/16.
 */
public class SentenceExtractor {
    private List<String[]> allWordsInDocuments = new ArrayList<String[]>(); //this will hold all the words in document
    private List<String> uniqueWords =
            new ArrayList<String>(); //this all words set will hold all the unique words in allWordsInDocuments
    private List<String> allSentences = new ArrayList<String>();
    private List<String> stopWords = null;

    public void getExtractedWordInGivenDocument(StringBuilder builder) throws IOException, InterruptedException {
        if (builder != null) {
            Reader reader = new StringReader(builder.toString());
            DocumentPreprocessor dp = new DocumentPreprocessor(reader);
            for (List<HasWord> sentence : dp) {
                String sentenceString = Sentence.listToString(sentence);
                allSentences.add(sentenceString.toString());
                String[] tokenizedTerms = sentenceString.toString().
                        replaceAll("[\\W&&[^\\s]]", "").split("\\W+");//to get individual terms
                stopWords = StopwordLoader.getStopWords();
                for (String term : tokenizedTerms) {
                    //avoid duplicate entry & stop words
                    if (!uniqueWords.contains(term) && !stopWords.contains(term)) {
                        uniqueWords.add(term);
                    }
                }
                allWordsInDocuments.add(tokenizedTerms);
            }
        }
    }

    public List<String[]> getAllWordsInDocuments() { return this.allWordsInDocuments; }

    public List<String> getUniqueWords() {
        return this.uniqueWords;
    }

    public List<String> getAllSentences() {
        return this.allSentences;
    }
}
