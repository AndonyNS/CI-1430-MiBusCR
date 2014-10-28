package com.example.busdevelop.buses;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jossue on 28/10/14.
 */
public class Evento {
    private String nombre;
    private String descripcion;
    private String tipo;
    private String fecha;
    private String lugar;
    private String latitud;
    private String longitud;

    public Evento(){

    }

    public Evento(String nombre, String descripcion, String tipo, String fecha, String lugar,
                  String latitud, String longitud) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.fecha = fecha;
        this.lugar = lugar;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
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

    /**
     * Metodo que parsea los datos de un json string a los campos
     * del objeto Evento
     * @param jsonString
     */
    public void parsearDatosEvento(String jsonString){

        try {
            //Parsear el json string a un json object para obtener los datos
            JSONObject datosEvento = new JSONObject(jsonString);

            //Parsear datos de jsonObject a la instancia de Evento
            this.setNombre(datosEvento.getString("nombre"));
            this.setDescripcion(datosEvento.getString("descripcion"));
            this.setTipo(Integer.toString(datosEvento.getInt("tipo")));
            this.setFecha(datosEvento.getString("dia_y_hora"));
            this.setLugar(datosEvento.getString("lugar"));
            this.setLatitud(Integer.toString(datosEvento.getInt("latitud")));
            this.setLongitud(Integer.toString(datosEvento.getInt("longitud")));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
