package com.summarizer.news.sentence.algorithm;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.summarizer.news.comparator.CategoryComparator;
import com.summarizer.news.data.api.API_Client;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ushan on 4/24/2016.
 */
public class KnowledgeCreator {
    //private static final  String TEMPLATE_PATH =  "src/main/resources/template";
    private String analyzedType;
    private String phrase;

    public  KnowledgeCreator(String analyzedType){
        this.analyzedType = analyzedType;
    }

    public String generateKnowledge(String phrase) throws IOException {
        this.phrase = phrase;
        HashMap<String, ArrayList<JsonObject>> categories = extractEntites(phrase);
        CategoryComparator categoryComparator = new CategoryComparator();
        for (String category : categories.keySet()) {
            ArrayList<JsonObject> entities = categories.get(category);
            Collections.sort(entities, categoryComparator);
        }
        return selectTemplate(categories);
    }

    private HashMap<String, ArrayList<JsonObject>> extractEntites(String phrase) throws IOException {
        JsonArray extractedEntities = API_Client.getExtractedEntities(phrase);
        HashMap<String, ArrayList<JsonObject>> categories = new HashMap<String, ArrayList<JsonObject>>();
        for (int i = 0; i < extractedEntities.size(); i++) {
            JsonObject entity = extractedEntities.get(i).getAsJsonObject();
            String type = entity.get("type").getAsString();
            if (categories.containsKey(type)) {
                categories.get(type).add(entity);
            } else {
                ArrayList<JsonObject> category = new ArrayList<JsonObject>();
                category.add(entity);
                categories.put(type, category);
            }

        }
        return categories;
    }

    private String selectTemplate(HashMap<String, ArrayList<JsonObject>> pickedEntities) throws IOException {

        StringBuilder output = null;
        ClassLoader classLoader = getClass().getClassLoader();
        if (pickedEntities.get("Sport") != null) {
            String type = pickedEntities.get("Sport").get(0).get("text").getAsString();
            if (type.equalsIgnoreCase("T20") || type.equalsIgnoreCase("Cricket")) {
                output = setValuesToTemplate("Cricket",pickedEntities);
            } else if (type.equalsIgnoreCase("football")) {
                output = setValuesToTemplate("Foot Ball",pickedEntities);
            }
        }else if(analyzedType.equals("Cricket")){
            output = setValuesToTemplate("Cricket",pickedEntities);
        }else  if(analyzedType.equals("Foot Ball")){
            output = setValuesToTemplate("Foot Ball",pickedEntities);
        }
        return output.toString();
    }

    private StringBuilder setValuesToTemplate(String type,HashMap<String, ArrayList<JsonObject>> pickedEntities) throws IOException {
        StringBuilder output = new StringBuilder();
        ClassLoader classLoader = getClass().getClassLoader();
        if(type.equals("Cricket")) {
            File file = new File(classLoader.getResource("template/cricket.txt").getFile());
            Path path = Paths.get(file.getAbsolutePath());
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            String firstLine = lines.get(0).replace("country1", pickedEntities.get("Country").get(0).get("text").getAsString());
            firstLine = firstLine.replace("country2", pickedEntities.get("Country").get(1).get("text").getAsString());
            output.append(firstLine);
            output.append("\n");
            String secondLine = lines.get(1);
            secondLine = secondLine.replace("country", pickedEntities.get("Country").get(0).get("text").getAsString());
            output.append(secondLine);
            output.append("\n");
            String thirdLine = lines.get(2);
            thirdLine = thirdLine.replace("person", pickedEntities.get("Person").get(0).get("text").getAsString());
            output.append(thirdLine);
            output.append("\n");
        }else if(type.equals("Foot Ball")){
            File file = new File(classLoader.getResource("template/football.txt").getFile());
            Path path = Paths.get(file.getAbsolutePath());
            String score = "";
            Pattern pattern = Pattern.compile("\\d+\\s*\\-\\s*\\d+");
            Matcher reMatcher = pattern.matcher(phrase);
            while (reMatcher.find()){
                score = reMatcher.group().toString();
                break;
            }
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            String firstLine = lines.get(0).replace("country1", pickedEntities.get("Country").get(0).get("text").getAsString());
            firstLine = firstLine.replace("country2", pickedEntities.get("Country").get(1).get("text").getAsString());
            output.append(firstLine);
            output.append("\n");
            String secondLine = lines.get(1);
            secondLine = secondLine.replace("country", pickedEntities.get("Country").get(0).get("text").getAsString());
            secondLine = secondLine.replace("goals",score);
            output.append(secondLine);
            output.append("\n");
        }

        return  output;
    }
}
