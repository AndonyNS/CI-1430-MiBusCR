package com.example.busdevelop.buses;

import android.util.Log;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Crea un objeto ruta con los datos almacenados
 * en la base de datos del servidor
 */
public class Ruta implements ClassToRest{
    private String id;
    private String nombre;
    private String frecuencia;
    private String precio;
    private String horario;
    private Parada paradaInicial;
    private Parada paradaFinal;
    private ArrayList<Parada> paradasIntermedias;
    private ArrayList<Bus> listaDeBuses;

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
        ListaParadas listParadas = ListaParadas.getInstancia();
        Parada parada;
        if(id != null && id != "") {
            paradasIntermedias = new ArrayList<Parada>();
            resultado = requestHttpApi("rutas/"+id, token);
            try{
                JSONObject ruta = new JSONObject(resultado);
                JSONArray paradas = new JSONArray(ruta.getString("ruta_parada"));
                for(int i = 0; i < paradas.length(); i++){
                    parada = listParadas.getParada(paradas.getJSONObject(i).getString("parada_id"), token);
                    String tipo = paradas.getJSONObject(i).getString("tipo");
                    if(tipo.equals("0")) {
                        paradasIntermedias.add(parada);
                    }
                    else{
                        if(tipo.equals("1")) {
                            paradaInicial = parada;
                        }
                        else {
                            if(tipo.equals("-1")) {
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


    public void setBuses(String token){
        String resultado = "";
        if(id != null && id != "") {
            listaDeBuses = new ArrayList<Bus>();
            resultado = requestHttpApi("rutas/"+id, token);
            try{
                JSONObject ruta = new JSONObject(resultado);
                JSONArray buses = new JSONArray(ruta.getString("bus"));
                for(int i = 0; i < buses.length(); i++){
                    resultado = requestHttpApi("buses/"+ buses.getJSONObject(i).getString("id"),token);
                    Bus bus = new Bus();
                    JSONObject busJSON = new JSONObject(resultado);
                    Log.d("Devuelve:",busJSON.toString());
                    bus.setId(busJSON.getInt("id"));
                    bus.setPlaca(busJSON.getString("placa"));
                    JSONObject gpsJSON = busJSON.getJSONObject("gps");
                    bus.setGpsId(gpsJSON.getString("id"));
                    //bus.setRampa(Boolean.parseBoolean(busJSON.getString("rampa")));
                    listaDeBuses.add(bus);
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Bus> getBuses() {
        return listaDeBuses;
    }

    /**
     *
     * @param end final del request http
     * @param token del usuario
     * @return devuelve un string con el request solicitado.
     */
    private String requestHttpApi(String end, String token){
        String resultado = ApiManager.httpGet("https://murmuring-anchorage-1614.herokuapp.com/"+end,token);
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
            jsonObject.accumulate("ruta_id", getId());


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