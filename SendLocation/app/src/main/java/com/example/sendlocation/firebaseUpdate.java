package com.example.sendlocation;

import android.content.Context;

import com.firebase.client.Firebase;

public class firebaseUpdate implements serverUpdate {
    Firebase firebaseRef;
    String UUID;

    public firebaseUpdate(Context context){
        //Obtiene el número único para cada dispositivo
        UUID = Installation.id(context);
        firebaseRef = new Firebase("https://blazing-fire-9075.firebaseio.com/Device-"+ UUID);
    }
    public void update(String currentLocation){
        //Pone en firebase el id del dispositivo con su ubicación actual, que se llama con el onLocationChanged
        firebaseRef.child("GpsID").setValue(UUID);
        firebaseRef.child("Location").setValue(currentLocation);
    }

    public String getProvider(){
        return "firebase";
    }

}
