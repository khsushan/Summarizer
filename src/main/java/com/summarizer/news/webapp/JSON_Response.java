package com.summarizer.news.webapp;

/**
 * Created by Ushan on 3/28/2016.
 */

public class JSON_Response {
    private String[] urls;

    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
    }

    @Override
    public String toString()
    {
        StringBuilder str = new StringBuilder();
        for (String url: this.urls) {
            str.append(url);
            str.append("\n");
        }
        return  str.toString();
    }
}
