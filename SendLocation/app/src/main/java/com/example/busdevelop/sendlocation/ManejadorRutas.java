package com.example.busdevelop.sendlocation;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by jossue on 21/10/14.
 * Clase que devolvera una sola instancia
 * será sinlgeton.
 */
public class ManejadorRutas {
    ArrayList<Ruta> mRutasArray  = null; // almacenara todas las rutas
    public volatile static ManejadorRutas manejador = null;
    String urlRutas = "https://murmuring-anchorage-1614.herokuapp.com/rutas";
    String mToken;

    private ManejadorRutas(String token){
        mToken = token;
        llenarLista(GET(urlRutas),token);
    }


    /**
     * Metodo que devuelve la instancia unica de la lista de rutas
     * @return
     */
    public static ManejadorRutas getInstancia(String token){
        if(manejador == null){
            synchronized (ManejadorRutas.class){
                if (manejador == null){
                    manejador = new ManejadorRutas(token);
                }
            }
        }
        return manejador;
    }


    /**
     * Metodo que devuelve las lista con las rutas ya obtenidas
     */
    public ArrayList getListaRutas(){
        return mRutasArray;
    }



    /**
     * Metodo que hace un request al API con la url donde
     * se pregunta por la tabla de rutas
     * @param url url que almacena las rutas
     * @return String con  el array Json
     */
    public  String GET(String url){
        String resultado = ApiManager.httpGet(url,mToken);
        return resultado;
    }

    /**
     * metodo que parse el json recibido
     */

    public void llenarLista(String resultado,String token){
        try{
            // una vez recibido el string con  el json
            //  se parsea guardando en un array
            JSONArray rutas = new JSONArray(resultado);
            mRutasArray = new ArrayList<Ruta>();

            //  cada i corresponderia a una diferente ruta
            // se obtiene el objetoJson de esa posicion
            // y se le sacan los atributos que todos serian
            //  Strings. Se guarda una ruta en el arreglo de rutas
            for(int i = 0; i < rutas.length(); i++){
                Ruta ruta = new Ruta();
                ruta.setId(Integer.toString(rutas.getJSONObject(i).getInt("id")));
                ruta.setNombre(rutas.getJSONObject(i).getString("nombre"));
                ruta.setFrecuencia(rutas.getJSONObject(i).getString("frecuencia"));
                ruta.setPrecio(rutas.getJSONObject(i).getString("precio"));
                ruta.setHorario(rutas.getJSONObject(i).getString("horario"));
                ruta.setParadas(token);
                mRutasArray.add(ruta);
            }


        }catch(JSONException e){
            e.printStackTrace();
        }
    }
}
