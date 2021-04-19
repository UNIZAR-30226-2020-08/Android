package com.app.guinote;

public class listItem {
    public int id;
    public String name;
    public String participantes;
    public int imagen;
    listItem(int id, String name, int imagen, String participantes){
        this.id = id;
        this.name = name;
        this.imagen = imagen;
        this.participantes=participantes;
    }
}
