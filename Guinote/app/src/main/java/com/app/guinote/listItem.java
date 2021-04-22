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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParticipantes() {
        return participantes;
    }

    public void setParticipantes(String participantes) {
        this.participantes = participantes;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }
}
