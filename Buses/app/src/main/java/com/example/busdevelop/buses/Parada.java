package com.example.busdevelop.buses;

/**
 * Created by Manuel on 10/14/14.
 */
public class Parada {
    private String id;
    private String nombre;
    private boolean techo;
    private String latitud;
    private String longitud;

    public Parada(){
    }
    public Parada(String id, String nombre, boolean techo, String latitud, String longitud){
        this.id = id;
        this.nombre = nombre;
        this.techo = techo;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean getTecho() {
        return techo;
    }

    public void setTecho(boolean techo) {
        this.techo = techo;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

}
