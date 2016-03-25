package com.summarizer.news.data.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;


/**
 * Created by ushan on 3/16/16.
 */
public class SynonymsFinderClient {

    public static void findSynonyms(String word) throws IOException {
        StringBuilder builder = API_Client.
                httpClient("http://words.bighugelabs.com/api/2/776aeb1a8c0ca084de2fec4d6d90c441/"+word+"/json");
        JsonParser jsonParser =  new JsonParser();
        JsonObject responseObj = (JsonObject) jsonParser.parse(builder.toString());
        JsonObject verbObj = responseObj.get("verb").getAsJsonObject();
        JsonArray synArray = verbObj.get("syn").getAsJsonArray();
        System.out.println(synArray.toString());
    }

    public static void main(String[] args) {
        try {
            findSynonyms("choose");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
