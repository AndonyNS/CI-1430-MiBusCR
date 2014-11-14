package com.example.busdevelop.buses;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Andony on 12/11/2014.
 */
public class GetBusLocation {
    private String mUrl = "https://murmuring-anchorage-1614.herokuapp.com/gps_s/";
    String mToken;
    Location ubicacion;

    public GetBusLocation(Bus bus,String token){

        mUrl += bus.getGpsId();
        mToken = token;
        ubicacion = new Location("dummyprovider");
    }

    public void updateLocation(){
        new HttpAsyncTask().execute();
        //Log.d("ubicacion",ubicacion.toString());
    }

    public Location getLastKnown(){
        return ubicacion;
    }

    /**
     * @return devuelve un string con el request solicitado.
     */
    public String getFromApi(){
        String resultado = ApiManager.httpGet(mUrl, mToken);

        return resultado;
    }

    private class HttpAsyncTask extends AsyncTask<Void, Void, String> {
        private HttpAsyncTask(){
        }

        @Override
        protected String doInBackground(Void... urls) {
            String resultado = getFromApi();
            try {
                JSONObject locationJSON = new JSONObject(resultado);

                ubicacion.setLatitude(locationJSON.getDouble("latitud"));
                ubicacion.setLongitude(locationJSON.getDouble("longitud"));
            }catch(JSONException e){
                Log.e("Error",e.getMessage());
            }
            return "Exito!";
        }

        /**
         * metodo que se ejecuta después de obtener la respuesta
         * al request get
         * @param resultado
         */
        @Override
        protected void onPostExecute(String resultado) {
                Log.d("Obtenido",resultado);
        }
    }





}
