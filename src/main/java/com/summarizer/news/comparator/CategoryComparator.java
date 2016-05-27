package com.summarizer.news.comparator;

import com.google.gson.JsonObject;

import java.util.Comparator;

/**
 * Created by Ushan on 5/25/2016.
 */
public class CategoryComparator implements Comparator<JsonObject> {

    public int compare(JsonObject o1, JsonObject o2) {
        double relevance1 = o1.get("relevance").getAsDouble();
        double relevance2 = o2.get("relevance").getAsDouble();
        if(relevance1 < relevance2)
            return 1;
        else
            return -1;
    }
}
