package com.example.busdevelop.buses;

import android.test.ActivityTestCase;

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
        assertEquals("3", parada.getId());
        assertEquals("UCR-Alajuela", parada.getNombre());
    }
}
