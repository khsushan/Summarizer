package com.summarizer.news.data.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by ushan on 3/16/16.
 */
public class API_Client {

    public  static StringBuilder httpClient(String endpoint) throws IOException {
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

}
