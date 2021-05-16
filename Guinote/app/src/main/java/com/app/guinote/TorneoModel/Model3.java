package com.app.guinote.TorneoModel;

import java.io.Serializable;

/**
 * Created by Emil on 21/10/17.
 */

public class Model3 implements Serializable{

    private Model2 competitorOne;
    private Model2 competitorTwo;
    private String competicion;
    private int height;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Model3(Model2 competitorOne, Model2 competitorTwo, String partida) {
        this.competitorOne = competitorOne;
        this.competitorTwo = competitorTwo;
        competicion=partida;
    }

    public Model2 getCompetitorTwo() {
        return competitorTwo;
    }

    public String getCompeticion(){
        return competicion;
    }

    public void setCompetitorTwo(Model2 competitorTwo) {
        this.competitorTwo = competitorTwo;
    }

    public Model2 getCompetitorOne() {

        return competitorOne;
    }

    public void setCompetitorOne(Model2 competitorOne) {
        this.competitorOne = competitorOne;
    }
}
