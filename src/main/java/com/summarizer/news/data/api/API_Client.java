package com.summarizer.news.data.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by ushan on 3/16/16.
 */
public class API_Client {

    private   static StringBuilder httpClient(String endpoint) throws IOException {
        URL url = new URL(endpoint);
        URLConnection connection = url.openConnection();
        String line;
        StringBuilder response = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        while((line = reader.readLine()) != null) {
            response.append(line);
        }
        return response;
    }

    /**
     * This method is use as client for google news api
     * @param keyword- keyword which is need to search in google news api
     *
     * */
    public static JsonArray getNewsUrls(String keyword) throws IOException {
        StringBuilder builder = httpClient("https://ajax.googleapis.com/ajax/services/search/news?" +
                "v=1.0&q="+keyword+"&userip=INSERT-USER-IP");
        JsonParser jsonParser =  new JsonParser();
        JsonObject responseObj = (JsonObject) jsonParser.parse(builder.toString());
        JsonObject responseDataObj = responseObj.get("responseData").getAsJsonObject();
        JsonArray results = responseDataObj.get("results").getAsJsonArray();
        return results;
    }


    public static void findSynonyms(String word) throws IOException {
        StringBuilder builder =
                httpClient("http://words.bighugelabs.com/api/2/776aeb1a8c0ca084de2fec4d6d90c441/"+word+"/json");
        JsonParser jsonParser =  new JsonParser();
        JsonObject responseObj = (JsonObject) jsonParser.parse(builder.toString());
        JsonObject verbObj = responseObj.get("verb").getAsJsonObject();
        JsonArray synArray = verbObj.get("syn").getAsJsonArray();
        System.out.println(synArray.toString());
    }

}
