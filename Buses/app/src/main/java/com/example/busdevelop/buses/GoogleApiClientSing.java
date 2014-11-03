package com.example.busdevelop.buses;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Esteban on 02/11/2014.
 */
public class GoogleApiClientSing {
    private volatile static GoogleApiClientSing instanciaUnica;
    private static GoogleApiClient sGoogleApiClient;

    private GoogleApiClientSing() {}

    public static GoogleApiClientSing getInstancia() {
        if (instanciaUnica == null) {
            synchronized (GoogleApiClientSing.class) {
                if (instanciaUnica == null) {
                    instanciaUnica = new GoogleApiClientSing();
                }
            }
        }
        return instanciaUnica;

    }

    public GoogleApiClient getGoogleApiClient() {
        return sGoogleApiClient;
    }

    public void setGoogleApiClient(GoogleApiClient client) {
        this.sGoogleApiClient = client;
    }
}
