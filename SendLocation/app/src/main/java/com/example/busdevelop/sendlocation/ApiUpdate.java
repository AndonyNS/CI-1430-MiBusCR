package com.example.busdevelop.sendlocation;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Andony on 11/11/2014.
 */
public class ApiUpdate implements serverUpdate {

    String UUID;
    Gps miGps;
    private String mUrl = "https://murmuring-anchorage-1614.herokuapp.com/gps_s";
    Activity mActivity;

    public ApiUpdate(Context context,Activity activity){
        UUID = Installation.id(context);
        miGps = new Gps(UUID);
        mActivity = activity;
    }

    @Override
    public void update(double latitud,double longitud) {
        miGps.setUbicacion(latitud,longitud);

        new HttpAsyncTaskAgregar(mActivity).execute(mUrl);
    }

    @Override
    public String getProvider() {
        return "api en heroku";
    }

    public String postUbicacion(String url){
        String resultado = ApiManager.httpPost(url, "b0936d7e239775e770ce002307f0acda",this.miGps);
        return resultado;
    }


    private class HttpAsyncTaskAgregar extends AsyncTask<String, Void, String> {
        Activity mActividad;
        private HttpAsyncTaskAgregar(Activity activity){
            this.mActividad = activity;
        }

        @Override
        protected String doInBackground(String... urls) {

            try{

                String resultado = postUbicacion(urls[0]);
                Log.e(resultado, "");

            }catch(Exception e){
                e.printStackTrace();
            }
            return "Actualizando ubicación";
        }

        /**
         * metodo que se ejecuta después de obtener la respuesta
         * al request get
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(mActividad.getBaseContext(), result, Toast.LENGTH_SHORT).show();

        }


    }

}
