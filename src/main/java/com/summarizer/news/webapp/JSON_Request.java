package com.summarizer.news.webapp;

/**
 * Created by Ushan on 3/28/2016.
 */

public class JSON_Request {
    private String[] urls;
    private String keyword;

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
        for (String url: this.getUrls()) {
            str.append(url);
            str.append("\n");
        }
        return  str.toString();
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
