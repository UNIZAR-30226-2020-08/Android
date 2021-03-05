package com.app.guinote;

public class rank {
    public int id;
    public String name;
    public int imagen;
    public String puntuacion;

    public rank(int id, String name, int imagen, String puntuacion) {
        this.id = id;
        this.name = name;
        this.imagen = imagen;
        this.puntuacion = puntuacion;
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

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public String getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(String puntuacion) {
        this.puntuacion = puntuacion;
    }
}
