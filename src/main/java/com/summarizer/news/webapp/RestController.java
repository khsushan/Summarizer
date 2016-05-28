package com.summarizer.news.webapp;

/**
 * Created by ushan on 3/24/16.
 */
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.summarizer.news.comparator.SentenceComparator;
import com.summarizer.news.data.api.API_Client;
import com.summarizer.news.data.html.HtmlReader;
import com.summarizer.news.model.Sentence;
import com.summarizer.news.sentence.algorithm.KnowledgeCreator;
import com.summarizer.news.sentence.algorithm.SentenceScoreCalculator;
import com.summarizer.news.sentence.algorithm.Vector;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.jsoup.HttpStatusException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Path("/controller")
public class RestController {
    static {
        BasicConfigurator.configure();
    }

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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        double[] lexScore = sentenceScoreCalculator.getLexScore();
//
//        Vector.printVector(sentenceScoreCalculator.getLexScore());
//        List<String> allSentence = sentenceScoreCalculator.getAllSentence();
//        for (int i = 0; i < allSentence.size();i++){
//            output.append(allSentence.get(i)+"======================="+lexScore[i]+"\n");
//            output.append("\n");
//        }
        return Response.status(200).entity(output.toString()).build();
    }

    @POST
    @Path("/getScoredSentences")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getScoredSentences(JSON_Request json_request){

//        StringBuilder output = new StringBuilder();
//        String[] requestUrls = json_request.getUrls();
//        StringBuilder[] documents = new StringBuilder[requestUrls.length];
//        try {
//            for(int i = 0; i < documents.length;i++){
//                StringBuilder  htmlContent = HtmlReader.readHTML(requestUrls[i]);
//                documents[i] =  htmlContent;
//            }
//        } catch (IOException e) {
//            logger.error("Error while reading html content"+e.getMessage());
//        }
//        SentenceScoreCalculator sentenceScoreCalculator = null;
//        try {
//            sentenceScoreCalculator = new SentenceScoreCalculator(documents);
//        } catch (IOException e) {
//            logger.error("Error while calculating senetence score "+e.getMessage());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        double[] lexScore = sentenceScoreCalculator.getLexScore();
//
//        Vector.printVector(sentenceScoreCalculator.getLexScore());
//        List<String> allSentence = sentenceScoreCalculator.getAllSentence();
//        for (int i = 0; i < allSentence.size();i++){
//            output.append(allSentence.get(i)+"======================="+lexScore[i]+"\n");
//            output.append("\n");
//        }
        return Response.status(200).entity("").build();

    }

    @POST
    @Path("/getCreatedKnowledgeFromKeyword")
    @Consumes(MediaType.APPLICATION_JSON)
    public JSON_Responce getCreatedKnowledge(JSON_Request json_request){
        JSON_Responce knowledge = null;
        try {
            knowledge = createKnowledge(getNewsUrls(json_request, true));
        } catch (IOException e) {
            logger.error("Error while calculating senetence score "+e.getMessage());
        } catch (InterruptedException e) {
            logger.error("Error while calculating senetence score "+e.getMessage());
        }
        return  knowledge;
    }

    @POST
    @Path("/getCreatedKnowledgeFromUrls")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCreatedKnowledgeFromUrls(JSON_Request json_request){
        JSON_Responce knowledge = null;
        JsonObject jsonObject = new JsonObject();
        try {
            knowledge = createKnowledge(getNewsUrls(json_request, false));
            jsonObject.addProperty("created_knolwdge",knowledge.getCreatedKnowledge());
        } catch (IOException e) {
            logger.error("Error while calculating senetence score "+e.getMessage());
        } catch (InterruptedException e) {
            logger.error("Error while calculating senetence score "+e.getMessage());
        }
        return  Response.status(200).entity(jsonObject.toString()).build();
    }

    private  String[]  getNewsUrls(JSON_Request json_request, boolean isKeyword) throws IOException {
        StringBuilder output = new StringBuilder();
        String[] newsUrls = null;
        StringBuilder[] documents = null;
        if(isKeyword){
            JsonArray newsUrlsFromKeyword = API_Client.getNewsUrls(json_request.getKeyword());
            newsUrls = new String[newsUrlsFromKeyword.size()];
            for(int i = 0;i<newsUrlsFromKeyword.size();i++){
                newsUrls[i] = newsUrlsFromKeyword.get(i).getAsJsonObject().get("Url").getAsString();
            }
        }else{
            newsUrls =  json_request.getUrls();
        }
        return newsUrls;
    }

    private JSON_Responce createKnowledge(String[] urls) throws IOException, InterruptedException {
        StringBuilder[] documents = new StringBuilder[urls.length];
        int i = 0;
        for (String url: urls) {
            try {
                StringBuilder htmlContent = API_Client.getHTMLContent(url);
                if(htmlContent !=  null){
                    documents[i] = htmlContent;
                    i++;
                }
            } catch (IOException e) {
                logger.error("Error while reading html document "+e.getMessage());
            }
        }
        SentenceScoreCalculator  sentenceScoreCalculator = new SentenceScoreCalculator(documents);
        List<Sentence> scoredSenetences = sentenceScoreCalculator.getScoredSenetences();
        int incrementer  = 0;
        StringBuilder selectedSenetences = new StringBuilder();
        for (Sentence sentence : scoredSenetences) {
            if (incrementer == 20) {
                break;
            } else {
                selectedSenetences.append(sentence.getSentenceValue()+"\n");
                System.out.println(incrementer + " : " + sentence.getSentenceValue());
                incrementer++;
            }
        }
        KnowledgeCreator knowledgeCreator = new KnowledgeCreator();
        String createKnowldge = knowledgeCreator.generateKnowledge(selectedSenetences.toString());
        logger.info(createKnowldge+"=================");
        return new JSON_Responce(createKnowldge);
    }
}
