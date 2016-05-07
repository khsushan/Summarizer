package com.summarizer.news.sentence.algorithm;

import java.util.HashMap;

/**
 * Created by Ushan on 4/24/2016.
 */
public class KnowledgeCreator {

    public String generateKnowledge(String[] senetcnces){
        HashMap<String,String[]> categories = extractEntites(senetcnces);
        HashMap<String,Integer> entitiyCount = new HashMap<String, Integer>();
        HashMap<String,String> pickedEntities = new HashMap<String, String>();
        for (String category: categories.keySet()) {
            String[] entities  = categories.get(category);
            for (String entity: entities) {
                if(entitiyCount.containsKey(entity)){
                    entitiyCount.put(entity,(entitiyCount.get(entity)+1));
                }else{
                    entitiyCount.put(entity,1);
                }
            }
            pickedEntities.put(category,getTheMaxOccaranceEntity(entitiyCount));
            entitiyCount.clear();
        }
        return  assignToTemplate(pickedEntities);
    }

    private HashMap<String,String[]> extractEntites(String[] senetces){

        return null;
    }

    private String assignToTemplate(HashMap<String,String> pickedEntities){

        return null;
    }

    private String getTheMaxOccaranceEntity(HashMap<String,Integer> entitiyCount){
        return null;
    }

}
