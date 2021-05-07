package com.app.guinote.TorneoModel;

import java.io.Serializable;


public class Model2 implements Serializable{

    private String name;
    private String score;

    public Model2(String name, String score){
        this.name = name;
        this.score = score;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}