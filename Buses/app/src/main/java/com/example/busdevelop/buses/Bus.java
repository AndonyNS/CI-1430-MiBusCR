package com.example.busdevelop.buses;

/**
 * Created by Andony on 28/10/2014.
 */
public class Bus {
    private int id;
    private String placa;
    private boolean rampa;
    private String gps_id;

    public Bus(){
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

    @Override
    public String toString(){
        return ("Bus: " + this.id + " Placa: " + this.placa );
    }

}
