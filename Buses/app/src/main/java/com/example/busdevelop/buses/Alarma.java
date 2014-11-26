package com.example.busdevelop.buses;

public class Alarma {
    private String mRuta;
    private String mBus;
    private float mHora;
    public static int IDS = 0;
    private final int mId;

    public Alarma(String ruta, String bus, float hora) {
        this.mRuta = ruta;
        this.mBus = bus;
        this.mHora = hora;
        this.mId = IDS;
        IDS++;
    }

    public int getIDS() {
        return IDS;
    }

    public int getId() {
        return mId;
    }


    public String getRuta() {
        return mRuta;
    }

    public String getBus() {
        return mBus;
    }

    public float getHora() {
        return mHora;
    }
}
