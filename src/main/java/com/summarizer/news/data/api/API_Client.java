package com.summarizer.news.data.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.codehaus.jettison.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Base64;

/**
 * Created by ushan on 3/16/16.
 */
public class API_Client {
    private static final String AlCHMEY_API_KEY = "be6e8644de0fbe81586ddb2d7fe30b1f5d8c1c1c";
    private static final String BING_API_KEY = "9KbdApiBCyeSbEJPhFmPf7yD8lnTj/CdDrxFl4G4ZEc";
    private static final String DIFFBOT_API_KEY = "3e370abab3d362393716cfba235c0bf1";

    private   static StringBuilder httpClient(String endpoint, String authenticationKey) throws IOException {
        URL url = new URL(endpoint);
        URLConnection connection = url.openConnection();
        if (authenticationKey != null) {
            String accountKeyEnc =Base64.getEncoder().encodeToString((authenticationKey+ ":"
                    + authenticationKey).getBytes());
            connection.setRequestProperty("Authorization","Basic "+accountKeyEnc);
        }
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
//        StringBuilder builder = httpClient("https://ajax.googleapis.com/ajax/services/search/news?" +
//                "v=1.0&q="+keyword+"&userip=INSERT-USER-IP");
        String bingUrlPattern = "https://api.datamarket.azure.com/Bing/Search/Web?Query=%%27%s%%27&$format=JSON";
        String query = URLEncoder.encode(keyword, Charset.defaultCharset().name());
        String url =   String.format(bingUrlPattern, query);
        StringBuilder builder = httpClient(url,
               BING_API_KEY);

        JsonParser jsonParser =  new JsonParser();
        JsonObject responseObj = (JsonObject) jsonParser.parse(builder.toString());
        System.out.println(responseObj.toString());
        JsonObject responseDataObj = responseObj.get("d").getAsJsonObject();
        JsonArray results = responseDataObj.get("results").getAsJsonArray();
        return results;
    }


    public static void findSynonyms(String word) throws IOException {
        StringBuilder builder =
                httpClient("http://words.bighugelabs.com/api/2/776aeb1a8c0ca084de2fec4d6d90c441/"+word+"/json",null);
        JsonParser jsonParser =  new JsonParser();
        JsonObject responseObj = (JsonObject) jsonParser.parse(builder.toString());
        JsonObject verbObj = responseObj.get("verb").getAsJsonObject();
        JsonArray synArray = verbObj.get("syn").getAsJsonArray();
    }

    public static JsonArray getExtractedEntities(String phrase) throws IOException {
        String urlEncoder = URLEncoder.encode(phrase,"UTF-8");
        StringBuilder builder = httpClient("http://gateway-a.watsonplatform.net/calls/text/TextGetRankedNamedEntities?" +
                "apikey="+AlCHMEY_API_KEY+"&text="+urlEncoder+"&outputMode=json",null);
        JsonParser jsonParser =  new JsonParser();
        JsonObject responseObj = (JsonObject) jsonParser.parse(builder.toString());
        return  responseObj.get("entities").getAsJsonArray();

    }

    public static StringBuilder getHTMLContent(String url) throws IOException {
        String encodedUrl = URLEncoder.encode(url,"UTF-8");
        StringBuilder builder = httpClient("http://api.diffbot.com/v3/article?token="+DIFFBOT_API_KEY+"" +
                "&url="+encodedUrl,null);
        JsonParser jsonParser =  new JsonParser();
        JsonObject responseObj = (JsonObject) jsonParser.parse(builder.toString());
        return  new StringBuilder(responseObj.get("objects").
                getAsJsonArray().get(0).getAsJsonObject().get("text").getAsString());
    }

}
