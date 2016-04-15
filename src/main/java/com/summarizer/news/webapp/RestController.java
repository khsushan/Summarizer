package com.summarizer.news.webapp;

/**
 * Created by ushan on 3/24/16.
 */
import com.google.gson.JsonArray;
import com.summarizer.news.data.api.API_Client;
import com.summarizer.news.data.html.HtmlReader;
import com.summarizer.news.sentence.algorithm.LexRank;
import com.summarizer.news.sentence.algorithm.Vector;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
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
        LexRank lexRank = new LexRank(new StringBuilder[]{htmlContent});
        double[] lexScore = lexRank.getLexScore();

        Vector.printVector(lexRank.getLexScore());
        List<String> allSentence = lexRank.getAllSentence();
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
        LexRank lexRank = new LexRank(documents);
        double[] lexScore = lexRank.getLexScore();

        Vector.printVector(lexRank.getLexScore());
        List<String> allSentence = lexRank.getAllSentence();
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
        String result = "Record entered: "+ json_response;
        StringBuilder output = new StringBuilder();
        JsonArray newsUrls = null;
        StringBuilder[] documents = null;
        try {
            newsUrls = API_Client.getNewsUrls(json_response.getKeyword());
            documents = new StringBuilder[newsUrls.size()];
            for(int i = 0; i < documents.length;i++){
                StringBuilder  htmlContent = HtmlReader.readHTML(newsUrls.get(i).getAsString());
                documents[i] =  htmlContent;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        LexRank lexRank = new LexRank(documents);
        double[] lexScore = lexRank.getLexScore();

        Vector.printVector(lexRank.getLexScore());
        List<String> allSentence = lexRank.getAllSentence();
        for (int i = 0; i < allSentence.size();i++){
            output.append(allSentence.get(i)+"======================="+lexScore[i]+"\n");
            output.append("\n");
        }
        return Response.status(200).entity(output.toString()).build();

    }
}
