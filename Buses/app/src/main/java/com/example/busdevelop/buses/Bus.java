package com.example.busdevelop.buses;

import android.util.Log;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

/**
 * Created by Andony on 28/10/2014.
 */
public class Bus implements ClassToRest{
    private int id;
    private String placa;
    private boolean rampa;
    private String gps_id;
    private boolean dibujado = false;

    public Bus(){
        id = 0;
    }

    public Bus(int id, String placa, boolean rampa, String gps_id){
        this.id = id;
        this.placa = placa;
        this.rampa = rampa;
        this.gps_id = gps_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public boolean getRampa() {
        return rampa;
    }

    public void setRampa(boolean rampa) {
        this.rampa = rampa;
    }

    public String getGpsId() {
        return gps_id;
    }

    public void setGpsId(String gps_id) {
        this.gps_id = gps_id;
    }

    public void setDibujado(){
        dibujado = true;
    }

    public boolean fueDibujado(){
        return dibujado;
    }

    @Override
    public String toString(){
        return ("Bus: " + this.id + " Placa: " + this.placa );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Bus other = (Bus) obj;
        if (this.id == 0) {
            if (other.getId() != 0)
                return false;
        } else if (this.id != other.getId())
            return false;
        return true;
    }

    @Override
    public StringEntity JsonAcumulator() {
        StringEntity se = null;
        try {
            String json = "";

            //Construir el objeto json
            JSONObject jsonObject = new JSONObject();

            // se acumulan los campos necesarios, el primer parametro
            // es la etiqueta json que tendran los campos de la base
            jsonObject.accumulate("id", getId());
            jsonObject.accumulate("placa",getPlaca());

            // Convertir el objeto Json a String
            json = jsonObject.toString();

            // setear json al stringEntity
            se = new StringEntity(json);

        }catch (Exception e){
            Log.d("String to json error", e.getLocalizedMessage());
        }
        return se;
    }

}
