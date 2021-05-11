package com.app.guinote;

public class Carta {
    private String id;
    private Integer valor;
    private Integer palo;
    private Integer ranking;

    public Carta(String id) {
        this.id = id;
        switch (id) {
            case "0O":
                this.valor = 11;
                this.palo = 1;
                this.ranking = 1;
                break;
            case "0E":
                this.valor = 11;
                this.palo = 2;
                this.ranking = 1;
                break;
            case "0B":
                this.valor = 11;
                this.palo = 3;
                this.ranking = 1;
                break;
            case "0C":
                this.valor = 11;
                this.palo = 4;
                this.ranking = 1;
                break;
            case "2O":
                this.valor = 10;
                this.palo = 1;
                this.ranking = 2;
                break;
            case "2E":
                this.valor = 10;
                this.palo = 2;
                this.ranking = 2;
                break;
            case "2B":
                this.valor = 10;
                this.palo = 3;
                this.ranking = 2;
                break;
            case "2C":
                this.valor = 10;
                this.palo = 4;
                this.ranking = 2;
                break;
            case "9O":
                this.valor = 4;
                this.palo = 1;
                this.ranking = 3;
                break;
            case "9E":
                this.valor = 4;
                this.palo = 2;
                this.ranking = 3;
                break;
            case "9B":
                this.valor = 4;
                this.palo = 3;
                this.ranking = 3;
                break;
            case "9C":
                this.valor = 4;
                this.palo = 4;
                this.ranking = 3;
                break;
            case "7O":
                this.valor = 3;
                this.palo = 1;
                this.ranking = 4;
                break;
            case "7E":
                this.valor = 3;
                this.palo = 2;
                this.ranking = 4;
                break;
            case "7B":
                this.valor = 3;
                this.palo = 3;
                this.ranking = 4;
                break;
            case "7C":
                this.valor = 3;
                this.palo = 4;
                this.ranking = 4;
                break;
            case "8O":
                this.valor = 2;
                this.palo = 1;
                this.ranking = 5;
                break;
            case "8E":
                this.valor = 2;
                this.palo = 2;
                this.ranking = 5;
                break;
            case "8B":
                this.valor = 2;
                this.palo = 3;
                this.ranking = 5;
                break;
            case "8C":
                this.valor = 2;
                this.palo = 4;
                this.ranking = 5;
                break;
            case "6O":
                this.valor = 0;
                this.palo = 1;
                this.ranking = 6;
                break;
            case "6E":
                this.valor = 0;
                this.palo = 2;
                this.ranking = 6;
                break;
            case "6B":
                this.valor = 0;
                this.palo = 3;
                this.ranking = 6;
                break;
            case "6C":
                this.valor = 0;
                this.palo = 4;
                this.ranking = 6;
                break;
            case "5O":
                this.valor = 0;
                this.palo = 1;
                this.ranking = 7;
                break;
            case "5E":
                this.valor = 0;
                this.palo = 2;
                this.ranking = 7;
                break;
            case "5B":
                this.valor = 0;
                this.palo = 3;
                this.ranking = 7;
                break;
            case "5C":
                this.valor = 0;
                this.palo = 4;
                this.ranking = 7;
                break;
            case "4O":
                this.valor = 0;
                this.palo = 1;
                this.ranking = 8;
                break;
            case "4E":
                this.valor = 0;
                this.palo = 2;
                this.ranking = 8;
                break;
            case "4B":
                this.valor = 0;
                this.palo = 3;
                this.ranking = 8;
                break;
            case "4C":
                this.valor = 0;
                this.palo = 4;
                this.ranking = 8;
                break;
            case "3O":
                this.valor = 0;
                this.palo = 1;
                this.ranking = 9;
                break;
            case "3E":
                this.valor = 0;
                this.palo = 2;
                this.ranking = 9;
                break;
            case "3B":
                this.valor = 0;
                this.palo = 3;
                this.ranking = 9;
                break;
            case "3C":
                this.valor = 0;
                this.palo = 4;
                this.ranking = 9;
                break;
            case "1O":
                this.valor = 0;
                this.palo = 1;
                this.ranking = 10;
                break;
            case "1E":
                this.valor = 0;
                this.palo = 2;
                this.ranking = 10;
                break;
            case "1B":
                this.valor = 0;
                this.palo = 3;
                this.ranking = 10;
                break;
            case "1C":
                this.valor = 0;
                this.palo = 4;
                this.ranking = 10;
                break;
            default:
                this.valor = 0;
                this.palo = 5;
                this.ranking = 15;
                break;
        }

        }

    public Integer getValor(){
        return this.valor;
    }
    public Integer getPalo(){
        return this.palo;
    }

    public Integer getRanking(){
        return this.ranking;
    }
    public String getId(){
        return this.id;
    }
}
