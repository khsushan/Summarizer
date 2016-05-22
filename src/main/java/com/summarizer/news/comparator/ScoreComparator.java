package com.summarizer.news.comparator;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by Ushan on 5/20/2016.
 */
public class ScoreComparator implements Comparator<String> {

    Map<String, Double> base;

    public ScoreComparator(Map<String, Double> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with
    // equals.
    public int compare(String a, String b) {
        if (base.get(a) <= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }

}

