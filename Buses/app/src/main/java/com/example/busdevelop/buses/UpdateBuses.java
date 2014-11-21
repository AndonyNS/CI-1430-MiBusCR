package com.example.busdevelop.buses;

import android.location.Location;
import android.util.Log;

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

        Iterator it = MainActivity.busesActuales.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pares = (Map.Entry)it.next();
            Bus bus = (Bus)pares.getKey();

            Log.e("Aqui","1");
            GetBusLocation gbl = new GetBusLocation(bus,mToken);
            gbl.updateLocation();
            Log.e("Aqui","2");
            Location location = gbl.getLastKnown();
            //if(location.getLatitude()+location.getLongitude()!=0.0){
                Log.e("Aqui","3");
                MainActivity.busesActuales.put(bus,location);
            //}
            it.next();
        }
    }
}
