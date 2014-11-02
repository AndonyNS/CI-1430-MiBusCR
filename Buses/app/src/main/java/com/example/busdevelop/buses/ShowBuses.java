package com.example.busdevelop.buses;

import android.location.Location;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Andony on 31/10/2014.
 */
public class ShowBuses {

    GoogleMap mMap;
    List<Bus> mBuses;
    List<Float> mColores;
    Map<Bus,Float> mBusesDibujados;
    int mColorActual = -1;
    final String mFirebaseUrl = "https://blazing-fire-9075.firebaseio.com/";

    public ShowBuses(GoogleMap map,ArrayList<Bus> buses) {
        this.mMap = map;
        this.mBuses = buses;
        mBusesDibujados = new TreeMap<Bus,Float>();

        //Lista de cada uno de los colores existentes para el marker
        mColores = new ArrayList<Float>();

        //Colores AZURE,BLUE,CYAN,GREEN,MAGENTA,ORANGE,RED,ROSE,VIOLET,YELLOW
        mColores.add((float)210.0);
        mColores.add((float)240.0);
        mColores.add((float)180.0);
        mColores.add((float)120.0);
        mColores.add((float)300.0);
        mColores.add((float)30.0);
        mColores.add((float)0.0);
        mColores.add((float)330.0);
        mColores.add((float)270.0);
        mColores.add((float) 60.0);

        getBusesLocation();
    }

    /* Método que dibuja la ubicación del bus
    * @params: El bus que quiere dibujar y la ubicación del mismo
     */
    private void dibujarBus(Bus bus, Location location){
        LatLng latitudLongitud = new LatLng(location.getLatitude(),location.getLongitude());
        //Si ya está el bus dentro de los dibujados, lo agrega en su nueva ubicación, con el marker que tenía
        if(mBusesDibujados.containsKey(bus)){
            mMap.addMarker(new MarkerOptions()
                    .position(latitudLongitud)
                    .title(bus.getPlaca())
                    .snippet(
                            bus.getPlaca())
                    .draggable(false)
                    .icon(BitmapDescriptorFactory.defaultMarker(mBusesDibujados.get(bus))));
        //Sino, lo agrega al mapa con un nuevo color de marker
        }else{
            ++mColorActual;
            if(mColorActual ==mColores.size()){
                mColorActual = 0;
            }
            mMap.addMarker(new MarkerOptions()
                    .position(latitudLongitud)
                    .title(bus.getPlaca())
                    .snippet(
                            bus.getPlaca())
                    .draggable(false)
                    .icon(BitmapDescriptorFactory.defaultMarker(
                            mColores.get(mColorActual)
                    )));
            mBusesDibujados.put(bus,mColores.get(mColorActual));
        }
    }

    //Obtiene de firebase la ubicación de los buses
    public void getBusesLocation() {

        Firebase firebaseRef = new Firebase(mFirebaseUrl);

        firebaseRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {

                Location location = new Location("dummyprovider");

                String id = (String) snapshot.child("GpsID").getValue();
                for(Bus bus: mBuses){
                    if(id==bus.getGpsId()){
                        String ubicacionBus = (String) snapshot.child("Location").getValue();
                        String[] parts = ubicacionBus.split(" ");
                        double latitud = Double.parseDouble(parts[0]);
                        double longitud = Double.parseDouble(parts[1]);
                        location.setLatitude(latitud);
                        location.setLongitude(longitud);
                        dibujarBus(bus,location);
                    }
                }
            }

            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }

        });


    }





}