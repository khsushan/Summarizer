package com.summarizer.news.model;

/**
 * Created by ushan on 3/12/16.
 */
public class Word {
    private String value;
    private double tf_idf_value;
    private double tf_value;
    private double idf_value;
    private double centorid;
    private double count;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public double getTf_idf_value() {
        return tf_idf_value;
    }

    public void setTf_idf_value(double tf_idf_value) {
        this.tf_idf_value = tf_idf_value;
    }


    public double getIdf_value() {
        return idf_value;
    }

    public void setIdf_value(double idf_value) {
        this.idf_value = idf_value;
    }


    public double getCentorid() {
        return centorid;
    }

    public void setCentorid(double centorid) {
        this.centorid = centorid;
    }


    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }


    public double getTf_value() {
        return tf_value;
    }

    public void setTf_value(double tf_value) {
        this.tf_value = tf_value;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof  Word){
            return ((Word)obj).getValue().equals(this.getValue());
        }else{
           return  false;
        }
    }
}
