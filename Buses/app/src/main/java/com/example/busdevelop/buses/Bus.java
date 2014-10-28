package com.example.busdevelop.buses;

/**
 * Created by Andony on 28/10/2014.
 */
public class Bus {
    private String id;
    private String nombre;
    private boolean rampa;
    private String caracteristicas;
    private String latitud;
    private String longitud;

    public Bus(){
    }
    public Bus(String id, String nombre, boolean rampa, String caracteristicas, String latitud, String longitud){
        this.id = id;
        this.nombre = nombre;
        this.rampa = rampa;
        this.caracteristicas = caracteristicas;
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

    public boolean getRampa() {
        return rampa;
    }

    public void setRampa(boolean rampa) {
        this.rampa = rampa;
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

    public String getCaracteristicas() { return caracteristicas; }

    public void setCaracteristicas(String caracteristicas){ this.caracteristicas = caracteristicas; }

}
