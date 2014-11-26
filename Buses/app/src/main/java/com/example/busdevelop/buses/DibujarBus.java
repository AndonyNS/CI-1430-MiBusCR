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
    private Marker myMarker;

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
        if(mMap == null){
            Log.e("El mapa es nulo","mejor suerte la proxima");
        }else {
            Log.e("Dibujando bus", mBus.toString() + mLatitude + mLongitude);
            try {
                myMarker.remove();
                Log.e("Modifica uno nuevo", mBus.toString());
            } catch (NullPointerException e) {
                Log.e("Agrega uno nuevo", mBus.toString());
            }
            setMarker();
        }
    }

    public void setLocation(Location location){
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
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

    public Marker getMarker(){
        return myMarker;
    }

    @Override
    public boolean equals(Object obj) {
        DibujarBus other = (DibujarBus) obj;
        if(this.myMarker==other.getMarker())
        {
            return true;
        }else{
            return false;
        }
    }

}
