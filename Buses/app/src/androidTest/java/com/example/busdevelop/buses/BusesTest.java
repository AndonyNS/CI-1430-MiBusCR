package com.example.busdevelop.buses;

import android.test.ActivityTestCase;

import java.util.ArrayList;

/**
 * Created by Andony on 29/10/2014.
 */
public class BusesTest extends ActivityTestCase{
    public Ruta ruta;
    //public Bus bus;

    public void testGetBuses(){
        ruta = new Ruta();
        ruta.setId("1");
        ruta.setBuses("b0936d7e239775e770ce002307f0acda");
        ArrayList<Bus> buses = ruta.getBuses();
        assertEquals("Bus: 1 Placa: AB-201", buses.get(0).toString());
    }
}
