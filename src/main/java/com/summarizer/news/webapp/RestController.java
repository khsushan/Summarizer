package com.summarizer.news.webapp;

/**
 * Created by ushan on 3/24/16.
 */
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.summarizer.news.data.api.API_Client;
import com.summarizer.news.data.html.HtmlReader;
import com.summarizer.news.sentence.algorithm.SentenceScoreCalculator;
import com.summarizer.news.sentence.algorithm.Vector;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Path("/controller")
public class RestController {
    @POST
    @Path("/test")
    @Consumes("text/plain")
    public Response getMessage(String url) {
        StringBuilder output = new StringBuilder();
        //readHTML("http://www.dailynews.lk/?q=2016/03/09/local/thai-deputy-prime-minister-meets-foreign-minister");
        StringBuilder htmlContent = null;
        try {
            htmlContent = HtmlReader.readHTML(url);
            //readHTML("http://www.dailynews.lk/?q=2016/03/09/local/thai-deputy-prime-minister-meets-foreign-minister");
        } catch (IOException e) {
            e.printStackTrace();
        }
        SentenceScoreCalculator sentenceScoreCalculator = new SentenceScoreCalculator(new StringBuilder[]{htmlContent});
        double[] lexScore = sentenceScoreCalculator.getLexScore();

        Vector.printVector(sentenceScoreCalculator.getLexScore());
        List<String> allSentence = sentenceScoreCalculator.getAllSentence();
        for (int i = 0; i < allSentence.size();i++){
            output.append(allSentence.get(i)+"======================="+lexScore[i]+"\n");
            output.append("\n");
        }
        return Response.status(200).entity(output.toString()).build();
    }

    @POST
    @Path("/getScoredSentences")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getScoredSentences(JSON_Response json_response){

        StringBuilder output = new StringBuilder();
        String[] requestUrls = json_response.getUrls();
        StringBuilder[] documents = new StringBuilder[requestUrls.length];
        try {
            for(int i = 0; i < documents.length;i++){
                StringBuilder  htmlContent = HtmlReader.readHTML(requestUrls[i]);
                documents[i] =  htmlContent;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        SentenceScoreCalculator sentenceScoreCalculator = new SentenceScoreCalculator(documents);
        double[] lexScore = sentenceScoreCalculator.getLexScore();

        Vector.printVector(sentenceScoreCalculator.getLexScore());
        List<String> allSentence = sentenceScoreCalculator.getAllSentence();
        for (int i = 0; i < allSentence.size();i++){
            output.append(allSentence.get(i)+"======================="+lexScore[i]+"\n");
            output.append("\n");
        }
        return Response.status(200).entity(output.toString()).build();

    }

    @POST
    @Path("/getCreatedKnowledge")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getCreatedKnowledge(JSON_Response json_response){
        StringBuilder output = new StringBuilder();
        JsonArray newsUrls = null;
        StringBuilder[] documents = null;
        try {
            String keyword = json_response.getKeyword().replace(" ","%20");
            newsUrls = API_Client.getNewsUrls(keyword);
            documents = new StringBuilder[3];
            for(int i = 0; i < 2;i++){
                JsonObject resultJOB = newsUrls.get(i).getAsJsonObject();
                String newsUrl = resultJOB.get("unescapedUrl").getAsString();
                System.out.println(newsUrl);
                StringBuilder  htmlContent = HtmlReader.readHTML(newsUrl);
                documents[i] =  htmlContent;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        SentenceScoreCalculator sentenceScoreCalculator = new SentenceScoreCalculator(documents);
        double[] lexScore = sentenceScoreCalculator.getLexScore();

        Vector.printVector(sentenceScoreCalculator.getLexScore());
        List<String> allSentence = sentenceScoreCalculator.getAllSentence();
        for (int i = 0; i < allSentence.size();i++){
            output.append("\n");
        }
        return Response.status(200).entity(output.toString()).build();

    }
}
