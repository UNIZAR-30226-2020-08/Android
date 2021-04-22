package com.app.guinote;

public class MensajeDeTexto {
    private String id;
    private String mensaje;
    private int tipoMensaje;
    private String Horadelmensaje;

    public MensajeDeTexto(String id, String mensaje, int tipoMensaje, String horadelmensaje) {
        this.id = id;
        this.mensaje = mensaje;
        this.tipoMensaje = tipoMensaje;
        Horadelmensaje = horadelmensaje;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getTipoMensaje() {
        return tipoMensaje;
    }

    public void setTipoMensaje(int tipoMensaje) {
        this.tipoMensaje = tipoMensaje;
    }

    public String getHoradelmensaje() {
        return Horadelmensaje;
    }

    public void setHoradelmensaje(String horadelmensaje) {
        Horadelmensaje = horadelmensaje;
    }
}
