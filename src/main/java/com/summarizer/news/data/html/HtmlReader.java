package com.summarizer.news.data.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;

/**
 * This class is use to read html content in given link
 */
public class HtmlReader {

    public static StringBuilder readHTML(String link) throws IOException {
        Document document = Jsoup.connect(link).timeout(3000).get();
        String title = document.title();
        Elements paragraphs = document.body().select("p");
        StringBuilder content = new StringBuilder();
        for (Element element : paragraphs) {
            //removing advertisement tag
            if (!element.text().equals("Advertisement") &&
                    !element.text().contains("|") &&
                    !element.text().contains(":") &&
                    !(element.text().trim().split(" ").length == 1) &&
                    !(element.text().trim().contains("Home Page"))) {
                content.append(element.text());
                content.append("\n");
            }

        }
        return content;
    }
}
