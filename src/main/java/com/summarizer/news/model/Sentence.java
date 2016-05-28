package com.summarizer.news.model;


import java.util.List;

/**
 * Created by ushan on 3/12/16.
 */
public class Sentence {

    private String sentenceValue;
    private List<Word> words;
    private double score;

    public Sentence(String sentenceValue,double score){
        this.sentenceValue =  sentenceValue;
        this.score = score;
    }

    public String getSentenceValue() {
        return sentenceValue;
    }

    public void setSentenceValue(String sentenceValue) {
        this.sentenceValue = sentenceValue;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }


    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
