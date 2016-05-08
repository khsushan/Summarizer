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
import org.apache.log4j.Logger;
import org.jsoup.HttpStatusException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;


@Path("/controller")
public class RestController {
    private Logger logger = Logger.getLogger("RestController");
    @POST
    @Path("/test")
    @Consumes("text/plain")
    public Response getMessage(String url) {
        StringBuilder output = new StringBuilder();
        StringBuilder htmlContent = null;
        try {
            htmlContent = HtmlReader.readHTML(url);
        } catch (IOException e) {
            logger.error("Error while reading html content "+e.getMessage());
        }
        SentenceScoreCalculator sentenceScoreCalculator = null;
        try {
            sentenceScoreCalculator = new SentenceScoreCalculator(new StringBuilder[]{htmlContent});
        } catch (IOException e) {
            logger.error("Error while calculating score "+e.getMessage());
        }
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
            logger.error("Error while reading html content"+e.getMessage());
        }
        SentenceScoreCalculator sentenceScoreCalculator = null;
        try {
            sentenceScoreCalculator = new SentenceScoreCalculator(documents);
        } catch (IOException e) {
            logger.error("Error while calculating senetence score "+e.getMessage());
        }
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
            newsUrls = API_Client.getNewsUrls(json_response.getKeyword());
            documents = new StringBuilder[3];
            int limit =4;
            for(int i = 0; i < limit;i++){
                JsonObject resultJOB = newsUrls.get(i).getAsJsonObject();
                String newsUrl = resultJOB.get("Url").getAsString();
                logger.info(newsUrl);
                try {
                    StringBuilder htmlContent = HtmlReader.readHTML(newsUrl);
                    documents[i] = htmlContent;
                }catch (HttpStatusException ex){
                    logger.error("Error while reading html link "+newsUrl+" "+ex.getMessage());
                    limit++;
                }
            }
        } catch (IOException e) {
            logger.error("Error while reading html document "+e.getMessage());
        }
        SentenceScoreCalculator sentenceScoreCalculator = null;
        try {
            sentenceScoreCalculator = new SentenceScoreCalculator(documents);
        } catch (IOException e) {
            logger.error("Error while calculating senetence score "+e.getMessage());
        }
        double[] lexScore = sentenceScoreCalculator.getLexScore();

        Vector.printVector(sentenceScoreCalculator.getLexScore());
        List<String> allSentence = sentenceScoreCalculator.getAllSentence();
        for (int i = 0; i < allSentence.size();i++){
            output.append("\n");
        }
        return Response.status(200).entity(output.toString()).build();

    }
}
