package com.summarizer.news.sentence.algorithm;

import com.summarizer.news.model.Sentence;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ushan on 5/29/2016.
 */
public class Analyzer {

    public static boolean analalyzeTitle(String keyword,String title){
        int count = 0;
        String[] split = keyword.split(" ");
        for (String word:split) {
            if(title.contains(word.toLowerCase()) || title.contains(word)){
                count++;
            }
        }

        if (count >= 2) return true;
        else return  false;
    }

    public static String analyzeSport(List<Sentence> sentences){
        int cricketCount = 0;
        int footBallCount = 0;
        for (Sentence sentence: sentences) {
            if(sentence.getSentenceValue().contains("cricket") ||
                    sentence.getSentenceValue().contains("T20")
                    ||sentence.getSentenceValue().contains("bowler")
                    ||sentence.getSentenceValue().contains("wicket")){
                cricketCount++;
            }else if(sentence.getSentenceValue().contains("goal") ||
                    sentence.getSentenceValue().contains("Goal")
                    || sentence.getSentenceValue().contains("Foot Ball")
                    ){
                footBallCount++;
            }
        }

        if(cricketCount > footBallCount)
            return "Cricket";
        else
            return  "Foot Ball";

    }

}
