package com.example.busdevelop.buses;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andony on 31/10/2014.
 */
public class ShowBuses {

    Usuario mUsuario;
    GoogleMap mMap;
    List<Bus> mBuses;
    List<DibujarBus> mBusesDibujados;

    public ShowBuses(Usuario usuario,GoogleMap map, ArrayList<Bus> buses) {
        this.mUsuario = usuario;
        this.mMap = map;
        this.mBuses = buses;
        mBusesDibujados = new ArrayList<DibujarBus>();

        actualizarPosBuses();
    }


    //Obtiene de firebase la ubicación de los buses
    public void actualizarPosBuses() {

        for (Bus bus : mBuses) {
            //Hacer el get a gps_s/bus.getId
            Gps gps = new Gps(bus.getGpsId());
            String resultado = gps.getFromApi("gps_s"+bus.getId(), mUsuario.getToken());
            Log.e("devuelve",resultado);
            /*Location location = null;
            DibujarBus db;
            Log.e("DA", mBusesDibujados.toString());
            if (mBusesDibujados.contains(new DibujarBus(bus))) {
                Log.e("1", "1");
                db = mBusesDibujados.get(mBusesDibujados.indexOf(new DibujarBus(bus)));
                db.dibujar();
            } else {
                Log.e("2", "2");
                db = new DibujarBus(mMap, bus, location);
                db.dibujar();
                mBusesDibujados.add(db);

            }*/
        }
    }

}

