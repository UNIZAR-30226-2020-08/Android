package com.app.guinote;

public class Carta {
    private int id;
    private int valor;
    private int palo;
    private int ranking;

    public Carta(int id, int valor, int palo, int ranking) {
        this.id = id;
        this.valor = valor;
        this.palo = palo;
        this.ranking = ranking;
    }

    public int getValor(){
        return this.valor;
    }
    public int getPalo(){
        return this.palo;
    }

    public int getRanking(){
        return this.ranking;
    }
    public int getId(){
        return this.id;
    }
}
