package com.example.busdevelop.buses;

/**
 * Crea un objeto ruta con los datos almacenados
 * en la base de datos del servidor
 */
public class Ruta {
    private String id;
    private String nombre;
    private String frecuencia;
    private String precio;
    private String horario;

    public Ruta(){

    }

    public Ruta(String id, String nombre, String frecuencia,
                String precio, String horario) {
        this.id = id;
        this.nombre = nombre;
        this.frecuencia = frecuencia;
        this.precio = precio;
        this.horario = horario;
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

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
}
