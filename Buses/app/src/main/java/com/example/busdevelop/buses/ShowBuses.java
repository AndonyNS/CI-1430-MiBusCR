package com.example.busdevelop.buses;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Andony on 31/10/2014.
 */
public class ShowBuses {

    GoogleMap mMap;

    public ShowBuses(GoogleMap map) {
        this.mMap = map;

        actualizarPosBuses();
    }
    //Obtiene la ubicación de los buses
    public void actualizarPosBuses() {

        Map currentBuses = MainActivity.busesActuales;
        Iterator it = currentBuses.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pares = (Map.Entry)it.next();
            Bus bus = (Bus)pares.getKey();
            Log.d("Procesando bus",bus.toString());

            Location location = (Location)pares.getValue();
            //if(location.getLongitude()+location.getLatitude()!=0.0) {
                DibujarBus db = new DibujarBus(mMap, bus, location);
                db.dibujar();
            //}

            it.remove();
            /*GetBusLocation busLocation = new GetBusLocation(bus,mUsuario.getToken());
            busLocation.updateLocation();*(

            /*
            Solución parcial al siguiente problema:
            updateLocation tiene el get de la base de datos, el problema es que el get
            dura más de lo que dura la ejecución del código, por lo tanto, el do
            while "le da tiempo" al get de ejecutarse en su totalidad

            Location location;
            do {
                location = busLocation.getLastKnown();
            }while(location.getLatitude()==0.0&&location.getLongitude()==0.0);
                Log.e("devuelve", ""+location.getLatitude());

                DibujarBus db = new DibujarBus(mMap,bus,location);
                db.dibujar();

                mBusesDibujados.put(db,location);*/

        }
    }

}

