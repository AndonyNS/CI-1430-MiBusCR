package com.example.busdevelop.buses;
import android.test.ActivityTestCase;
import android.test.InstrumentationTestCase;

import java.util.ArrayList;

/**
 * Created by jossue on 15/10/14.
 */
public class BuscarRutasTest extends InstrumentationTestCase {


    public void testObtenerRutasBuscadas(){
        ArrayList<Ruta> rutas  = new ArrayList<Ruta>();
        ObtRutasActivity obtRutas = new ObtRutasActivity();

        //Llenar de rutas
        Ruta ruta = new Ruta();

        ruta.setId("1");
        ruta.setNombre("UCR-Alajuela");
        ruta.setFrecuencia("1 hora");
        ruta.setPrecio("740");
        ruta.setHorario("7-10");
        rutas.add(ruta);

        Ruta ruta2 = new Ruta();
        ruta2.setId("2");
        ruta2.setNombre("UCR-Hatillo");
        ruta2.setFrecuencia("30 min");
        ruta2.setPrecio("330");
        ruta2.setHorario("7-10");
        rutas.add(ruta2);

        ListViewAdapter adaptador = new ListViewAdapter(this.getInstrumentation().getContext(),rutas);
        System.out.println(adaptador.mRutasArray.get(0).getNombre());

        adaptador.filter("Ha");
        assertEquals("UCR-Hatillo", adaptador.mRutasArray.get(0).getNombre());


    }


}
