package com.example.busdevelop.buses;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andony on 31/10/2014.
 */
public class ShowBuses {

    Usuario mUsuario;
    GoogleMap mMap;
    List<Bus> mBuses;
    Map<DibujarBus,Location> mBusesDibujados;

    public ShowBuses(Usuario usuario,GoogleMap map, ArrayList<Bus> buses) {
        this.mUsuario = usuario;
        this.mMap = map;
        this.mBuses = buses;
        mBusesDibujados = new HashMap<DibujarBus,Location>();

        actualizarPosBuses();
    }


    //Obtiene la ubicación de los buses
    public void actualizarPosBuses() {

        for (Bus bus : mBuses) {
            Log.d("Procesando bus",bus.toString());
            GetBusLocation busLocation = new GetBusLocation(bus,mUsuario.getToken());
            busLocation.updateLocation();

            /*
            Solución parcial al siguiente problema:
            updateLocation tiene el get de la base de datos, el problema es que el get
            dura más de lo que dura la ejecución del código, por lo tanto, el do
            while "le da tiempo" al get de ejecutarse en su totalidad
             */
            Location location;
            do {
                location = busLocation.getLastKnown();
            }while(location.getLatitude()==0.0&&location.getLongitude()==0.0);
                Log.e("devuelve", ""+location.getLatitude());

                DibujarBus db = new DibujarBus(mMap,bus,location);
                db.dibujar();

                mBusesDibujados.put(db,location);

        }
    }

}

