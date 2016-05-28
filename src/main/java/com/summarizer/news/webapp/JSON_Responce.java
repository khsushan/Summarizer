package com.summarizer.news.webapp;

import com.summarizer.news.model.Sentence;

/**
 * Created by Ushan on 5/28/2016.
 */
public class JSON_Responce {

    private String createdKnowledge;
    private Sentence[] sentences;

    public  JSON_Responce(String createdKnowledge){
        this.createdKnowledge = createdKnowledge;
    }

    public String getCreatedKnowledge() {
        return createdKnowledge;
    }

    public void setCreatedKnowledge(String createdKnowledge) {
        this.createdKnowledge = createdKnowledge;
    }

    public Sentence[] getSentences() {
        return sentences;
    }

    public void setSentences(Sentence[] sentences) {
        this.sentences = sentences;
    }
}
