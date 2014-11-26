package com.example.busdevelop.sendlocation;

import android.util.Log;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

/**
 * Crea un objeto ruta con los datos almacenados
 * en la base de datos del servidor
 */
public class Gps implements ClassToRest {
    private String id_gps;
    private String ruta;
    private double latitud;
    private double longitud;

    public Gps(String id){
        this.id_gps = id;
    }

    public Gps(String id_gps, double latitud, double longitud, String ruta) {
        this.id_gps = id_gps;
        this.ruta = ruta;
        this.latitud=latitud;
        this.longitud=longitud;

    }

    public String getId() {
        return id_gps;
    }
    public void setId(String id) {
        this.id_gps = id;
    }

    public String getRuta() {
        return ruta;
    }
    public void setRuta(String ruta) {
        this.ruta = ruta;
    }


    public void setUbicacion(double latitud,double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    /**
     *
     * @param end final del request http
     * @param token del usuario
     * @return devuelve un string con el request solicitado.
     */
    private String requestHttpApi(String end, String token){
        String resultado = ApiManager.httpGet("https://murmuring-anchorage-1614.herokuapp.com/" + end, token);
        return resultado;
    }

    @Override
    public StringEntity JsonAcumulator() {
        StringEntity se = null;
        try {
            String json = "";

            //Construir el objeto json
            JSONObject jsonObject = new JSONObject();

            // se acumulan los campos necesarios, el primer parametro
            // es la etiqueta json que tendran los campos de la base
            jsonObject.accumulate("id_gps", getId());
            if(ruta != null) {
                jsonObject.accumulate("ruta", this.ruta);
            }
            jsonObject.accumulate("latitud",this.latitud);
            jsonObject.accumulate("longitud",this.longitud);

            // Convertir el objeto Json a String
            json = jsonObject.toString();

            // setear json al stringEntity
            se = new StringEntity(json);

        }catch (Exception e){
            Log.d("String to json error", e.getLocalizedMessage());
        }
        return se;
    }
}