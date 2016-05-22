package com.summarizer.news.sentence.algorithm;

import com.summarizer.news.model.Word;
import com.summarizer.news.sentence.extractor.SentenceExtractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ushan on 3/16/16.
 */
public class SentenceScoreCalculator {
    private List<String[]> allWordsInDocuments = null;
    private List<String> uniqueWords = null;
    private List<String> allSentences = null;
    private StringBuilder[] documents = null;
    private SentenceExtractor sentenceExtractor;
    private double[][] cosineMaxtrics = null;
    private ArrayList<Word> words;
    private double max_occarance = 0;
    private double[] degrees = null;
    private final double treshhold = 0.1;
    private double[] lexScore;

    public SentenceScoreCalculator(StringBuilder[] documents) throws IOException, InterruptedException {
        this.documents = documents;
        sentenceExtractor = new SentenceExtractor();
        words = new ArrayList<Word>();
        init();
    }

    public double[] getLexScore() {
        return this.lexScore;
    }

    public List<String> getAllSentence() {
        return this.allSentences;
    }

    private void init() throws IOException, InterruptedException {
        for (StringBuilder document : documents) {
            sentenceExtractor.getExtractedWordInGivenDocument(document);
        }
        this.allWordsInDocuments = sentenceExtractor.getAllWordsInDocuments();
        this.uniqueWords = sentenceExtractor.getUniqueWords();
        this.allSentences = sentenceExtractor.getAllSentences();
        this.cosineMaxtrics = new double[allSentences.size()][allSentences.size()];
        this.degrees = new double[allSentences.size()];
        //calculateMaxOccarunce();
        calculateTF();
        calculateIDF();
        calculateLexRankScore();
    }

    private void calculateLexRankScore() {
        for (int i = 0; i < allSentences.size(); i++) {
            for (int j = 0; j < allSentences.size(); j++) {
                cosineMaxtrics[i][j] = compute_cosine(i,j);
                if(cosineMaxtrics[i][j] > treshhold){
                    cosineMaxtrics[i][j] = 1.0;
                    degrees[i]+=1;
                }else{
                    cosineMaxtrics[i][j] = 0.0;
                }
            }
        }

        for (int i = 0; i < allSentences.size(); i++) {
            for (int j = 0; j < allSentences.size(); j++) {
               if(degrees[i] == 0){
                    degrees[i] = 1.0;
               }
                cosineMaxtrics[i][j] = cosineMaxtrics[i][j]/degrees[i];
            }
        }
        powerMethod(0.85);
    }

    private double compute_cosine(int sentence1Index, int sentence2Index) {
        ArrayList<String> commonWords = getCommonWords(sentence1Index, sentence2Index);
        double numerator = 0.0;
        for (String word : commonWords) {
            Word wordObj = new Word();
            wordObj.setValue(word);
            if (words.contains(wordObj)) {
                wordObj = words.get(words.indexOf(wordObj));
                numerator += Math.pow(wordObj.getTf_value() * wordObj.getTf_value() * wordObj.getIdf_value(), 2);
            }
        }
        String[] sentence1 = allWordsInDocuments.get(sentence1Index);
        double sentence1Val = calculateTotalTF_IDF_Square(sentence1);

        String[] sentence2 = allWordsInDocuments.get(sentence2Index);
        double sentence2Val = calculateTotalTF_IDF_Square(sentence2);

        if(sentence1Val > 0.0 && sentence2Val > 0.0){
            return numerator/Math.sqrt(sentence1Val)*Math.sqrt(sentence2Val);
        }else{
            return  0.0;
        }
    }

    private void powerMethod(double dampFactor) {
        double magDiff = 0.85;
        double size = (double)allSentences.size();
        lexScore = new double[allSentences.size()];
        double [] lexScoreNext = new double[(int) size];
        for (int i = 0; i < allSentences.size(); i++) {
            lexScore[i] = 1 / size;
        }
        while (magDiff > treshhold) {
            for (int i = 0; i < allSentences.size(); i++) {
                lexScoreNext[i] = dampFactor / size
                        + (1 - dampFactor)
                        * Vector.dotProduct(cosineMaxtrics[i], lexScore);
            }
            magDiff = Vector.difference(lexScoreNext, lexScore);
            Vector.printVector(lexScoreNext);
            System.arraycopy(lexScoreNext, 0, lexScore, 0, (int) size);
        }
    }


    private void calculateTF() {
        double tf_value = 0.00;
        for (String word : uniqueWords) {
            Word wordObj = new Word();
            wordObj.setValue(word);
            tf_value = calculateOccarunce(word) / allWordsInDocuments.size();
            wordObj.setTf_value(tf_value);
            words.add(wordObj);
        }
    }

    private void calculateIDF() {
        double total_sentence = 0;
        double idf_value = 0;
        for (Word word : words) {
            total_sentence = calculateSentenceOccarunce(word.getValue());
            idf_value =1+ Math.log(4.0 / total_sentence);
            word.setIdf_value(idf_value);
        }
    }


    private double calculateOccarunce(String word) {
        double count = 0;
        for (String[] sentence : allWordsInDocuments) {
            for (String w : sentence) {
                if (w.equalsIgnoreCase(word)) {
                    count++;
                }
            }
        }
        return count;
    }

    private double calculateSentenceOccarunce(String word) {
        double count = 0;
        for(StringBuilder document : documents){
            if(document.toString().contains(word)){
                count++;
            }
        }
        return count;
    }

    private ArrayList<String> getCommonWords(int index1, int index2) {
        String[] sentence1 = allWordsInDocuments.get(index1);
        String[] sentence2 = allWordsInDocuments.get(index2);
        ArrayList<String> commonWords = new ArrayList<String>();
        for (int i = 0; i < sentence1.length; i++) {
            for (int j = 0; j < sentence2.length; j++) {
                if (sentence1[i].trim().equalsIgnoreCase(sentence2[j].trim())) {
                    commonWords.add(sentence1[i]);
                }
            }
        }
        return commonWords;
    }

    private double calculateTotalTF_IDF_Square(String[] sentence){
        double sentenceVal = 0.00;
        for (String word : sentence) {
            Word wordObj = new Word();
            wordObj.setValue(word);
            if (words.contains(wordObj)) {
                wordObj = words.get(words.indexOf(wordObj));
                sentenceVal += Math.pow((wordObj.getTf_value()*wordObj.getIdf_value()),2);
            }
        }
        return  sentenceVal;
    }
}
