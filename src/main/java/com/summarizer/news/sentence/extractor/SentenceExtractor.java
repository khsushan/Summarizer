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
    private List<String[]> wordsInDocuments = new ArrayList<String[]>(); //this will hold all the words in document
    private List<String> allWords =
            new ArrayList<String>(); //this all words set will hold all the unique words in wordsInDocuments
    private List<String> allSentences = new ArrayList<String>();
    private static ArrayList<String> stopwords;

    public  void getExtractedWordInGivenDocument(StringBuilder builder) throws IOException {
        if(builder != null){
            Reader reader = new StringReader(builder.toString());
            DocumentPreprocessor dp = new DocumentPreprocessor(reader);
            for (List<HasWord> sentence : dp) {
                String sentenceString = Sentence.listToString(sentence);
                allSentences.add(sentenceString.toString());
                String[] tokenizedTerms = sentenceString.toString().
                        replaceAll("[\\W&&[^\\s]]", "").split("\\W+");//to get individual terms
                for (String term : tokenizedTerms) {
                    if (!allWords.contains(term) && !getStopWords().contains(term)) {  //avoid duplicate entry
                        allWords.add(term);
                    }

                }
                wordsInDocuments.add(tokenizedTerms);
            }
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

    public static ArrayList getStopWords() throws IOException {

        if(stopwords == null){
            stopwords = new ArrayList<String>();
            File stopword = new File("src/main/resources/stopword/english/Stopword.txt");
            BufferedReader stopWordBufferReader =  new BufferedReader(new FileReader(stopword));
            while (stopWordBufferReader.readLine() != null){
                stopwords.add(stopWordBufferReader.readLine());
            }
            return stopwords;
        }else {
            return stopwords;
        }
    }


}
