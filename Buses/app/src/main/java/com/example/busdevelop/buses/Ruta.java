package com.example.busdevelop.buses;

import java.util.ArrayList;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


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
    private Parada paradaInicial;
    private Parada paradaFinal;
    private ArrayList<Parada> paradasIntermedias;

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


    /**
     * El metodo necesita del token del usuario este busca las paradas de su id y llena los atributos paradaInicial, ParadaFinal
     * paradasIntermedias, para facilitar el uso de ellas.
     * @param token
     */
    public void setParadas(String token){
        String resultado = "";
        if(id != null && id != "") {
            paradasIntermedias  = new ArrayList<Parada>();
            resultado = requestHttpApi("rutas/"+id, token);
            try{
                JSONObject ruta = new JSONObject(resultado);
                JSONArray paradas = new JSONArray(ruta.getString("ruta_parada"));
                for(int i = 0; i < paradas.length(); i++){
                    resultado = requestHttpApi("paradas/"+ paradas.getJSONObject(i).getString("parada_id"),token);
                    Parada parada = new Parada();
                    JSONObject paradaJSON = new JSONObject(resultado);
                    parada.setId(paradaJSON.getString("id"));
                    parada.setNombre(paradaJSON.getString("nombre"));
                    parada.setLatitud(paradaJSON.getString("latitud"));
                    parada.setLongitud(paradaJSON.getString("longitud"));
                    parada.setTecho(Boolean.parseBoolean(paradaJSON.getString("techo")));
                    String algo = paradas.getJSONObject(i).getString("tipo");
                    if(algo.equals("0")) {
                        paradasIntermedias.add(parada);
                    }
                    else{
                        if(algo.equals("1")) {
                            paradaInicial = parada;
                        }
                        else {
                            if(algo.equals("-1")) {
                                paradaFinal = parada;
                            }
                        }
                    }
                }

            }catch(JSONException e){
                e.printStackTrace();
            }

        }
    }

    /**
     * @return La parada inicial de la ruta
     */
    public Parada getParadaInicial(){
        return paradaInicial;
    }

    /**
     * @return La parada final de la ruta
     */
    public Parada getParadaFinal(){
        return paradaFinal;
    }

    /**
     * @return Las paradas intermedias que posee la ruta
     */
    public ArrayList<Parada> getParadasIntermedias() {
        return paradasIntermedias;
    }

    /**
     *
     * @param end final del request http
     * @param token del usuario
     * @return devuelve un string con el request solicitado.
     */
    private String requestHttpApi(String end, String token){
        String resultado = "";
        try{
            InputStream inputStream = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("https://murmuring-anchorage-1614.herokuapp.com/"+end);
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");
            httpGet.setHeader("Authorization",
                    "Token token=\"" + token +"\"");
            HttpResponse httpResponse = httpclient.execute(httpGet);
            inputStream = httpResponse.getEntity().getContent();
            if(inputStream != null){
                BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
                String linea = "";
                while( (linea = bufferedReader.readLine()) != null){
                    resultado += linea;
                }
                inputStream.close();
            }else{
                resultado = "Error al cargar datos";
            }
        }catch (Exception e){
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return resultado;
    }

}
