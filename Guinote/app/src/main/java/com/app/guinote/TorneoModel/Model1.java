package com.app.guinote.TorneoModel;

import java.io.Serializable;
import java.util.ArrayList;


public class Model1 implements Serializable{

    public Model1(ArrayList<Model3> matches) {
        this.matches = matches;
    }

    private ArrayList<Model3> matches;

    public void setMatches(ArrayList<Model3> matches) {
        this.matches = matches;
    }

    public ArrayList<Model3> getMatches() {
        return matches;
    }
}