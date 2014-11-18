package com.example.busdevelop.buses;

import android.location.Location;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Andony on 18/11/2014.
 */
public class UpdateBuses {
    String mToken;
    public UpdateBuses(String token){
        mToken = token;
    }

    public void update(){

        Iterator it = MainActivity.busesActuales.values().iterator();
        while(it.hasNext()){
            Map.Entry pares = (Map.Entry)it.next();
            Bus bus = (Bus)pares.getKey();

            GetBusLocation gbl = new GetBusLocation(bus,mToken);
            gbl.updateLocation();

            Location location = gbl.getLastKnown();
            if(location.getLatitude()+location.getLongitude()!=0.0){
                MainActivity.busesActuales.put(bus,location);
            }
            it.remove();
        }
    }
}
