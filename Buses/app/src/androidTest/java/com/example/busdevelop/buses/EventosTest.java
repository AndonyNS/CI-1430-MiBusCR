package com.example.busdevelop.buses;

import android.test.InstrumentationTestCase;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by jossue on 02/11/14.
 */
public class EventosTest extends InstrumentationTestCase {

    public void testHayEventos(){
        String token = "b0936d7e239775e770ce002307f0acda";
        EventosActivity ea = new EventosActivity();

        String resultado = ea.llenarEventos(token);

        JSONArray datosEvento = null;
        try {
            datosEvento = new JSONArray(resultado);

            ea.mEventosArray = new ArrayList<Evento>();

            //  cada i corresponderia a una diferente ruta
            // se obtiene el objetoJson de esa posicion
            // y se le sacan los atributos que todos serian
            //  Strings. Se guarda un evento en el arreglo de eventos

            for(int i = 0; i < datosEvento.length(); i++){
                Evento evento = new Evento();
                evento.setNombre(datosEvento.getJSONObject(i).getString("nombre"));
                evento.setDescripcion(datosEvento.getJSONObject(i).getString("descripcion"));
                evento.setTipo(Integer.toString(datosEvento.getJSONObject(i).getInt("tipo")));
                evento.setFecha(datosEvento.getJSONObject(i).getString("dia_y_hora"));
                evento.setLugar(datosEvento.getJSONObject(i).getString("lugar"));
                evento.setLatitud(Integer.toString(datosEvento.getJSONObject(i).getInt("latitud")));
                evento.setLongitud(Integer.toString(datosEvento.getJSONObject(i).getInt("longitud")));
                ea.mEventosArray.add(evento);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        assertTrue(ea.mEventosArray.size() > 0);


    }

    public void testInformacionEvento(){
        String token = "b0936d7e239775e770ce002307f0acda";
        EventosActivity ea = new EventosActivity();

        String resultado = ea.llenarEventos(token);

        JSONArray datosEvento = null;
        try {
            datosEvento = new JSONArray(resultado);

            ea.mEventosArray = new ArrayList<Evento>();

            //  cada i corresponderia a una diferente ruta
            // se obtiene el objetoJson de esa posicion
            // y se le sacan los atributos que todos serian
            //  Strings. Se guarda un evento en el arreglo de eventos

            for(int i = 0; i < datosEvento.length(); i++){
                Evento evento = new Evento();
                evento.setNombre(datosEvento.getJSONObject(i).getString("nombre"));
                evento.setDescripcion(datosEvento.getJSONObject(i).getString("descripcion"));
                evento.setTipo(Integer.toString(datosEvento.getJSONObject(i).getInt("tipo")));
                evento.setFecha(datosEvento.getJSONObject(i).getString("dia_y_hora"));
                evento.setLugar(datosEvento.getJSONObject(i).getString("lugar"));
                evento.setLatitud(Integer.toString(datosEvento.getJSONObject(i).getInt("latitud")));
                evento.setLongitud(Integer.toString(datosEvento.getJSONObject(i).getInt("longitud")));
                ea.mEventosArray.add(evento);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        assertEquals("Exposicion garabito", ea.mEventosArray.get(0).getNombre());
        assertEquals("1",ea.mEventosArray.get(0).getTipo() );
        assertEquals("Exposicion de arte garabito en el paseo de las flores",
                ea.mEventosArray.get(0).getDescripcion());
        assertEquals("11.08.2014 14:00", ea.mEventosArray.get(0).getFecha());
        assertEquals("Paseo de las flores", ea.mEventosArray.get(0).getLugar());


    }
}
