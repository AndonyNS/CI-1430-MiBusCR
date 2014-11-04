package com.example.busdevelop.buses;

import android.test.AndroidTestCase;

import java.util.ArrayList;

/**
 * Created by jossue on 03/11/14.
 */
public class RutaTest extends AndroidTestCase {

    /**
     * Prueba que en la base de datos hayan rutas
     */
    public void testHayRutas(){
        String token = "b0936d7e239775e770ce002307f0acda";
        ArrayList<Ruta> mListaRutas;
        ManejadorRutas manejador = ManejadorRutas.getInstancia(token);
        mListaRutas = manejador.getListaRutas();
        assertTrue(mListaRutas.size() > 0);
    }

    /**
     * Prueba que se cumpla efectivamente el singleton
     */
    public void testInstanciaUnicaDeRutas(){
        String token = "b0936d7e239775e770ce002307f0acda";
        ManejadorRutas manejador1 = ManejadorRutas.getInstancia(token);
        ManejadorRutas manejador2 = ManejadorRutas.getInstancia(token);
        assertSame(manejador1,manejador2);
    }

    /**
     * Prueba que se devuelva bien la información de una ruta
     */
    public void testInfoDeRuta(){
        String token = "b0936d7e239775e770ce002307f0acda";
        ArrayList<Ruta> mListaRutas;
        ManejadorRutas manejador = ManejadorRutas.getInstancia(token);
        mListaRutas = manejador.getListaRutas();

        assertEquals("UCR-Alajuela", mListaRutas.get(0).getNombre());
        assertEquals("1 hora", mListaRutas.get(0).getFrecuencia());
        assertEquals("740", mListaRutas.get(0).getPrecio());
        assertEquals("7-10", mListaRutas.get(0).getHorario());
    }

}
