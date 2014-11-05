package com.example.busdevelop.buses;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Andony on 05/11/2014.
 */
public class DibujarBus {
    GoogleMap mMap;
    Bus mBus;
    double mLatitude;
    double mLongitude;
    Marker myMarker;

    public DibujarBus(Bus bus){
        mBus = bus;
    }
    public DibujarBus(GoogleMap map, Bus bus,Location location){
        mMap = map;
        mBus = bus;
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
    }

    public void dibujar(){
        Log.e("Dibujando bus", mBus.toString() + mLatitude + mLongitude);


        if(myMarker==null){
            Log.e("3","3");
            setMarker();
        }else{
            Log.e("4","4");
            myMarker.remove();
            setMarker();
        }
    }

    private void setMarker(){
        LatLng latitudLongitud = new LatLng(mLatitude,mLongitude);
        myMarker = mMap.addMarker(new MarkerOptions()
                .position(latitudLongitud)
                .title(mBus.getPlaca())
                .snippet(
                        mBus.getPlaca())
                .draggable(false)
                .icon(BitmapDescriptorFactory.defaultMarker(
                        (float) 30.0
                )));
    }

    public Bus getBus(){
        return mBus;
    }

    @Override
    public boolean equals(Object obj) {
        DibujarBus other = (DibujarBus) obj;
        if(this.mBus==other.getBus())
        {
            return true;
        }else{
            return false;
        }
    }

}
