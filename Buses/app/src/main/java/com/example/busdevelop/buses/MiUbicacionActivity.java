package com.example.busdevelop.buses;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


public class MiUbicacionActivity extends ActionBarActivity implements LocationListener{
    GoogleMap mMapa;
    private LocationManager mLocationManager;
    private Marker mMarcadorUpdate;
    private Usuario mUsuario;
    private ListView mListViewRutasCercanas;
    private List<Ruta> mListaRutas;
    private ArrayList<Ruta> mListaRutasCercanas;
    private ListViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_ubicacion);

        getRutas();
        try {
            if (mMapa == null) {
                // soportado en ultimas apis
                mMapa = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.mapUbicacion)).getMap();
            }

               // mMapa.setMyLocationEnabled(true);
                mMapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMapa.getUiSettings().setZoomGesturesEnabled(true);

            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 5000, 20, this);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Mapa", "exception", e);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mi_ubicacion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location posicion) {
        LatLng latitudLongitud = new LatLng(posicion.getLatitude(),
                posicion.getLongitude());

        if (mMarcadorUpdate == null) {
            mMarcadorUpdate = mMapa.addMarker(new MarkerOptions()
                    .position(latitudLongitud)
                    .title("Ubicación")
                    .snippet(
                            "lat:" + posicion.getLatitude() + ",lon:"
                                    + posicion.getLongitude())
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.persona1)));

            mMapa.animateCamera(CameraUpdateFactory.newLatLngZoom(latitudLongitud,
                    13));
        } else {
            mMarcadorUpdate.setPosition(latitudLongitud);

            mMapa.animateCamera(CameraUpdateFactory.newLatLng(latitudLongitud));
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    //Método que mueve la cámara al LatLng dado y al zoom indicado
    private void moveToLocation(LatLng ll,int zoomDistance) {
        //new LatLng(9.935783, -84.051375)
        CameraUpdate ubicacion = CameraUpdateFactory.newLatLng(ll);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(zoomDistance);
        mMapa.moveCamera(ubicacion);
        mMapa.animateCamera(zoom);
    }

    /**
     * Obtener los datos del usuario y pedir las rutas
     */
    private void getRutas(){
        mUsuario = new Usuario();
        SharedPreferences sharedPref = getSharedPreferences("MyPrefsFile", 0);
        mUsuario.setEmail(sharedPref.getString("UserEmail", ""));
        mUsuario.setEncrypted_password(sharedPref.getString("UserPass", ""));

        new HttpAsyncTaskToken(this).execute();
    }

    /**
     * Obtener el token para poder consultar rutas
     */
    private class HttpAsyncTaskToken extends AsyncTask<Void, Void, String> {
        Activity mActivity;
        private HttpAsyncTaskToken(Activity activity){
            this.mActivity = activity;
        }

        @Override
        protected String doInBackground(Void...params) {
            return mUsuario.obtenerToken();
        }
        /**
         * metodo que se ejecuta después de obtener la respuesta
         * al request post del token
         * @param resultado
         */
        @Override
        protected void onPostExecute(String resultado) {
            try {
                mUsuario.guardarTokenId(resultado);
                // una vez obtenido el token se pide las rutas
                new HttpAsyncTask(mActivity).execute();
            } catch(IllegalArgumentException i){
                Log.e("Error de argumento",""+i.getMessage());
            }
        }
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        Activity mActivity;
        private HttpAsyncTask(Activity activity){
            this.mActivity = activity;
        }

        @Override
        protected String doInBackground(String... urls) {
            ManejadorRutas manejador = ManejadorRutas.getInstancia(mUsuario.getToken());
            mListaRutas = manejador.getListaRutas();
            return "Rutas Obtenidas!";
        }

        /**
         * metodo que se ejecuta después de obtener la respuesta
         * al request get
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {

            try{
                Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
                // createListView();
                // TODO: Calcular las rutas cercanas
                mListViewRutasCercanas = (ListView) findViewById(R.id.listaDeRutas);
                // Pasar las rutas al  ListViewAdapter
                mAdapter = new ListViewAdapter(mActivity,(ArrayList) mListaRutas);

                // enlazar el adaptador con el listView
                mListViewRutasCercanas.setAdapter(mAdapter);
            } catch(IllegalArgumentException i){
                Log.e("Error de argumento",""+i.getMessage());
            }
        }
    }

}
