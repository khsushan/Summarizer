package com.summarizer.news.webapp;

/**
 * Created by ushan on 3/24/16.
 */
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.summarizer.news.data.api.API_Client;
import com.summarizer.news.data.html.HtmlReader;
import com.summarizer.news.model.Sentence;
import com.summarizer.news.sentence.algorithm.Analyzer;
import com.summarizer.news.sentence.algorithm.KnowledgeCreator;
import com.summarizer.news.sentence.algorithm.SentenceScoreCalculator;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.util.JSONPObject;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

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
    @Path("/getScoredSentences")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getScoredSentences(JSON_Request json_request){
        JsonObject errorObject = new JsonObject();
        JSONObject responceObject  = new JSONObject();
        try {
            SentenceScoreCalculator sentenceScoreCalculator =
                    new SentenceScoreCalculator(getNewsDocuments(json_request, false));
            ArrayList<Sentence> scoredSenetences =(ArrayList<Sentence>) sentenceScoreCalculator.getScoredSenetences();
            JSONArray jsonArray =  new JSONArray(new Gson().toJson(scoredSenetences));
            responceObject.put("scored_Sentences",jsonArray);
        } catch (IOException e) {
            logger.error("Error while calculating senetence score "+e.getMessage());
            errorObject.addProperty("error",true);
            errorObject.addProperty("error_message",e.getMessage());
            return Response.status(500).entity(errorObject.toString()).build();
        } catch (InterruptedException e) {
            logger.error("Error while calculating senetence score "+e.getMessage());
            errorObject.addProperty("error",true);
            errorObject.addProperty("error_message",e.getMessage());
            return Response.status(500).entity(errorObject.toString()).build();
        } catch (JSONException e) {
            errorObject.addProperty("error",true);
            errorObject.addProperty("error_message",e.getMessage());
            return Response.status(500).entity(errorObject.toString()).build();
        }
        return Response.status(200).entity(responceObject.toString()).build();
    }

    @POST
    @Path("/getCreatedKnowledgeFromKeyword")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getCreatedKnowledgeFromKeyword(JSON_Request json_request){
        JSON_Responce knowledge = null;
        JsonObject jsonObject = new JsonObject();
        try {
            logger.info(json_request.getKeyword()+"=======================");
            knowledge = createKnowledge(getNewsDocuments(json_request, true));
            jsonObject.addProperty("Created Knowledge",knowledge.getCreatedKnowledge());
        } catch (IOException e) {
            logger.error("Error while calculating senetence score "+e.getMessage());
            jsonObject.addProperty("error",true);
            jsonObject.addProperty("error_message",e.getMessage());
            return  Response.status(500).entity(jsonObject.toString()).build();
        } catch (InterruptedException e) {
            logger.error("Error while calculating senetence score "+e.getMessage());
            jsonObject.addProperty("error",true);
            jsonObject.addProperty("error_message",e.getMessage());
            return  Response.status(500).entity(jsonObject.toString()).build();
        }
        return  Response.status(200).entity(jsonObject.toString()).build();
    }

    @POST
    @Path("/getCreatedKnowledgeFromUrls")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCreatedKnowledgeFromUrls(JSON_Request json_request){
        JSON_Responce knowledge = null;
        JsonObject jsonObject = new JsonObject();
        try {
            knowledge = createKnowledge(getNewsDocuments(json_request, false));
            jsonObject.addProperty("created_knolwdge",knowledge.getCreatedKnowledge());
        } catch (IOException e) {
            logger.error("Error while calculating senetence score "+e.getMessage());
            jsonObject.addProperty("error",true);
            jsonObject.addProperty("error_message",e.getMessage());
            return  Response.status(500).entity(jsonObject.toString()).build();
        } catch (InterruptedException e) {
            logger.error("Error while calculating senetence score "+e.getMessage());
            jsonObject.addProperty("error",true);
            jsonObject.addProperty("error_message",e.getMessage());
            return  Response.status(500).entity(jsonObject.toString()).build();
        }
        return  Response.status(200).entity(jsonObject.toString()).build();
    }

    private  StringBuilder[] getNewsDocuments(JSON_Request json_request, boolean isKeyword) throws IOException, InterruptedException {
        String[] newsUrls = null;
        if(isKeyword){
            ArrayList<String> urls = new ArrayList<String>();
            JsonArray newsUrlsFromKeyword = API_Client.getNewsUrls(json_request.getKeyword());
            logger.info(newsUrlsFromKeyword.size()+" : "+json_request.getKeyword());
            for(int i = 0;i<3;i++){
                //if(Analyzer.analalyzeTitle(json_request.getKeyword(),
                  //      newsUrlsFromKeyword.get(i).getAsJsonObject().get("Title").getAsString())){
                        urls.add(newsUrlsFromKeyword.get(i).getAsJsonObject().get("Url").getAsString());
                //}
            }
            newsUrls = new String[urls.size()];
            for (int i = 0;i < newsUrls.length ; i++) {
                newsUrls[i] =  urls.get(i);
                logger.info(i+":"+newsUrls[i]);
            }
        }else{
            newsUrls =  json_request.getUrls();
        }
        StringBuilder[] documents = new StringBuilder[newsUrls.length];
        int i = 0;
        for (String url: newsUrls) {
            try {
                if(url != null) {
                    StringBuilder htmlContent = API_Client.getHTMLContent(url);
                    if (htmlContent != null) {
                        documents[i] = htmlContent;
                        i++;
                    }
                }
            } catch (IOException e) {
                logger.error("Error while reading html document "+e.getMessage());
            }
        }
        return documents;
    }

    private JSON_Responce createKnowledge(StringBuilder[] documents) throws IOException, InterruptedException {
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
        KnowledgeCreator knowledgeCreator = new KnowledgeCreator(Analyzer.analyzeSport(scoredSenetences));
        String createKnowldge = knowledgeCreator.generateKnowledge(selectedSenetences.toString());
        logger.info("Created Knowledge is :"+createKnowldge);
        return new JSON_Responce(createKnowldge);
    }


}
