package com.summarizer.news.sentence.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ushan on 3/15/16.
 */
public class TF_IDF_Calculator {

    /**
     * This will calculate the TF value of the word in given document
     * @param document - document which is need to calculate the TF value of the
     * @param wordToCheck  - word which is need to calculate TF value
     *
     * */

    public int calculateTF(String[] document,String wordToCheck){
        int count = 0;
        for (String  word:document) {
            if(word.equalsIgnoreCase(wordToCheck)){
                count++;
            }
        }
        return  count/document.length;
    }

    /**
     * This will calculate the IDF value of the word in given documents
     *@param documents - documents which are need to calculate IDF value for given word
     *@param wordToCheck  - word which is need to calculate IDF value.
     *
     * */
    public double calculateIDF(List<String[]> documents,String wordToCheck){
        double count = 0;
        for (String[] ss : documents) {
            for (String s : ss) {
                if (s.equalsIgnoreCase(wordToCheck)) {
                    count++;
                    break;
                }
            }
        }
        return Math.log(documents.size() / 1+count);
    }

    public ArrayList<Double[]> calculateTF_IDF_valueOfAllDocuments(List<String[]> documents){
        double tf = 0;
        double idf = 0;
        int count = 0 ;
        ArrayList<Double[]> tdidfValuesForDocuments = new ArrayList<Double[]>();
        for(String[] document:documents){
            Double[] tdidfValuesForDocument = new Double[document.length];
            for(String word : document){
                tf = calculateTF(document,word);
                idf = calculateIDF(documents,word);
                tdidfValuesForDocument[count] = tf * idf;
            }
            tdidfValuesForDocuments.add(tdidfValuesForDocument);
        }
       return tdidfValuesForDocuments;
    }

}
