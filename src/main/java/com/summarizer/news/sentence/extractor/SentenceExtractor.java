package com.summarizer.news.sentence.extractor;

import com.summarizer.news.data.html.HtmlReader;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.process.DocumentPreprocessor;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by ushan on 3/5/16.
 */
public class SentenceExtractor {
    private List<String[]> wordsInDocuments = new ArrayList<String[]>(); //this will hold all the words in document
    private List<String> allWords =
            new ArrayList<String>(); //this all words set will hold all the unique words in wordsInDocuments
    private List<String> allSentences = new ArrayList<String>();

    public  void getExtractedWordInGivenDocument(StringBuilder builder){
        Reader reader = new StringReader(builder.toString());
        DocumentPreprocessor dp = new DocumentPreprocessor(reader);
        for (List<HasWord> sentence : dp) {
            String sentenceString = Sentence.listToString(sentence);
            allSentences.add(sentenceString.toString());
            String[] tokenizedTerms = sentenceString.toString().
                    replaceAll("[\\W&&[^\\s]]", "").split("\\W+");//to get individual terms
            for (String term : tokenizedTerms) {
                if (!allWords.contains(term)) {  //avoid duplicate entry
                    allWords.add(term);
                }

            }
            wordsInDocuments.add(tokenizedTerms);
        }
    }

    public  List<String[]> getWordsInDocuments(){
        return  this.wordsInDocuments;
    }

    public  List<String> getAllWords(){
        return  this.allWords;
    }

    public  List<String> getAllSentences(){
        return  this.allSentences;
    }

}
