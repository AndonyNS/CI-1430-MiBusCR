package com.example.busdevelop.buses;

import android.test.ActivityTestCase;

import java.util.ArrayList;

/**
 * Created by Manuel on 10/14/14.
 */
public class ParadasTest extends ActivityTestCase {
    public Ruta ruta;
    public Parada parada;


    public void testObteneParadaInicial()  {
        ruta = new Ruta();
        ruta.setId("1");
        ruta.setParadas("b0936d7e239775e770ce002307f0acda");
        parada = ruta.getParadaInicial();
        assertEquals("UCR-Alajuela", parada.getNombre());
    }

    public void testObteneParadaFinal()  {
        ruta = new Ruta();
        ruta.setId("1");
        ruta.setParadas("b0936d7e239775e770ce002307f0acda");
        parada = ruta.getParadaFinal();
        assertEquals("Alajuela-Final", parada.getNombre());
    }

    public void testObteneParadasIntermedias()  {
        ruta = new Ruta();
        ruta.setId("2");
        ruta.setParadas("b0936d7e239775e770ce002307f0acda");
        ArrayList<Parada> paradasIntermedias = ruta.getParadasIntermedias();
        parada = paradasIntermedias.get(0);
        assertEquals("Megasuper la paz", parada.getNombre());
    }
}
