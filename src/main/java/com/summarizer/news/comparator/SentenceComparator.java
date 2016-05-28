package com.summarizer.news.comparator;

import com.summarizer.news.model.Sentence;

import java.util.Comparator;

/**
 * Created by Ushan on 5/28/2016.
 */
public class SentenceComparator implements Comparator<Sentence> {
    public int compare(Sentence o1, Sentence o2) {
        if(o1.getScore() < o2.getScore() )
            return 1;
        else
            return -1;

    }
}
