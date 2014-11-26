package com.example.busdevelop.sendlocation;

/**
 * Created by Manuel on 11/2/14.
 */

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;
import java.util.HashMap;


public class ListaParadas {
    Map<Integer, Parada> mParadasArray; // almacenara todas las paradas
    public volatile static ListaParadas manejador = null;
    String urlParadas = "https://murmuring-anchorage-1614.herokuapp.com/paradas/";
    String mToken;

    private ListaParadas(){
        mParadasArray  = new HashMap<Integer, Parada>();
    }


    /**
     * Metodo que devuelve la instancia unica de la lista de paradas
     * @return
     */
    public static ListaParadas getInstancia(){
        if(manejador == null){
            synchronized (ListaParadas.class){
                if (manejador == null){
                    manejador = new ListaParadas();
                }
            }
        }
        return manejador;
    }


    /**
     * Metodo que devuelve la parada solicitada
     */
    public Parada getParada(String id, String token){
        int idInt = Integer.parseInt(id);
        Parada parada = mParadasArray.get(idInt);
        if(parada == null){
            try{
                parada = new Parada();
                String resultado = ApiManager.httpGet(urlParadas + id ,token);
                JSONObject paradaJSON = new JSONObject(resultado);
                parada.setId(paradaJSON.getString("id"));
                parada.setNombre(paradaJSON.getString("nombre"));
                parada.setLatitud(paradaJSON.getString("latitud"));
                parada.setLongitud(paradaJSON.getString("longitud"));
                parada.setTecho(Boolean.parseBoolean(paradaJSON.getString("techo")));
                mParadasArray.put(idInt,parada);

            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        return parada;
    }
}

