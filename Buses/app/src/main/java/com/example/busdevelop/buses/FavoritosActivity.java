package com.example.busdevelop.buses;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Locale;


public class FavoritosActivity extends ActionBarActivity {

    private GoogleMap mGoogleMap;
    private List<Ruta> mFavoritosArray;
    private List<String> mNombreRutaArray;
    private List<Row> rows;
    private ListView mList;
    private List<LatLng> mMarkerParadas;
    private Usuario mUsuario;
    private String mUrlRuta= "https://murmuring-anchorage-1614.herokuapp.com/rutas/";
    private String mUrlFavorita= "https://murmuring-anchorage-1614.herokuapp.com/favoritas";
    private List<String> ids = new ArrayList<String>();
    private final String mPrefs_Name = "MyPrefsFile";
    



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        mMarkerParadas = new ArrayList<LatLng>();
        mFavoritosArray = new ArrayList<Ruta>();

        try {
            if (mGoogleMap == null) {
                mGoogleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }

        // Locate the ListView in activity_obt_rutas.xml
        mList = (ListView) findViewById(R.id.favoritoslist);
        mList.setVisibility(View.GONE);



        CameraUpdate centro = CameraUpdateFactory.newLatLng(new LatLng(9.935783, -84.051375));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(14);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        mGoogleMap.moveCamera(centro);
        mGoogleMap.animateCamera(zoom);
        mGoogleMap.setTrafficEnabled(true);


        mGoogleMap.setTrafficEnabled(true);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Mapa", "exception", e);
        }


        getRutas();
        showBuses();


       }


    private void getRutas(){
        mUsuario = new Usuario();
        SharedPreferences sharedPref = getSharedPreferences("MyPrefsFile", 0);
        mUsuario.setEmail(sharedPref.getString("UserEmail", ""));
        mUsuario.setEncrypted_password(sharedPref.getString("UserPass", ""));

        new HttpAsyncTaskToken(this).execute();
    }

    /* TODO: Muestra los buses*/
    public void showBuses() {
        /*Firebase firebaseRef = new Firebase(mFIREBASE_URL);

        firebaseRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {



                Location location = new Location("dummyprovider");

                mGps = (String) snapshot.child("GpsID").getValue();
                mLocation = (String) snapshot.child("Location").getValue();
                String[] parts = mLocation.split(" ");
                mLatitud = Double.parseDouble(parts[0]);
                mLongitud = Double.parseDouble(parts[1]);
                location.setLatitude(mLatitud);
                location.setLongitude(mLongitud);
                onLocationChanged(location);
                mMarcadorBus = mMarcadorUpdate;

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

        });*/
    }

    private void createListView(){

        mList = (ListView) findViewById(R.id.favoritoslist);
        rows = new ArrayList<Row>();
        //mNombreRutaArray = new ArrayList<String>();
        Row row = null;

        for ( Ruta r : mFavoritosArray){
            row = new Row();
            row.setTitle(r.getNombre());
            rows.add(row);
            Log.d("hasta aqui voy bien", r.getNombre());
        }
        if(!rows.isEmpty()){
            mList.setVisibility(View.VISIBLE);

            Log.e("mensaje","lista no vacia");

        }

        //Le envía al array adapter personalizado el contexto del cual va a llamarlo y el ArrayList de filas
        CustomArrayAdapter adapter = new CustomArrayAdapter(this, rows,this,mFavoritosArray,mUsuario);

        mList.setAdapter(adapter);


     // ListView Item Click Listener
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Ruta itemValue = mFavoritosArray.get(position);

                Log.d("obtuve la seleccionada", itemValue.getNombre());

                try {
                    if (mGoogleMap == null) {
                        mGoogleMap = ((MapFragment) getFragmentManager().
                                findFragmentById(R.id.map)).getMap();
                    }
                    //itemValue.setParadas(mUsuario.getToken());
                    //Llama a la clase que dibuja la ruta
                    new DibujarRuta(mGoogleMap, itemValue);

                    moveToBounds(itemValue.getParadaInicial(), itemValue.getParadaFinal());

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Mapa", "exception", e);
                }

                }
        });
    }

    /* Método que mueve la cámara a los límites establecidos por
    * dos paradas, el método LatLngBounds funciona formando un cuadrado
    * en orden SO -> NE por lo que si la latitud de una parada está
    * después que la latitud de la segunda, quedará un "cuadrado invertido"
    * y esto es un error para el mapa
    * */
    private void moveToBounds(Parada parada1,Parada parada2){
        //Debido a que el orden es Sur Oeste
        Double sur = Double.parseDouble(parada1.getLatitud());
        Double oeste = Double.parseDouble(parada1.getLongitud());
        Double norte = Double.parseDouble(parada2.getLatitud());
        Double este = Double.parseDouble(parada2.getLongitud());

        /*Si el sur es mayor que el norte, significa que la
        * parada1 está más arriba que la parada2, entonces
        * hay que intercambiarlas */
        if(sur>norte){
            Double temp = sur;
            sur = norte;
            norte = temp;
        }

        /* Si el oeste es mayor que el este, significa que la
        * parada1 está a la derecha de la parada, entonces
        * hay que intercambiarlas*/
        if(oeste>este){
            Double temp = oeste;
            oeste = este;
            este = temp;
        }

        LatLngBounds bounds = new LatLngBounds(
                new LatLng(sur,oeste),
                new LatLng(norte,este)
        );

        //Mueve la cámara al cuadrado creado por LatLngBounds
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,0));
        //Hace el zoom 1 para "afuera" porque sino quedan las paradas en el borde de la cámara
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(mGoogleMap.getCameraPosition().zoom-1));
    }


    @Override
    protected void onResume() {

        super.onResume();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.favoritos, menu);
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







    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        Activity mActivity;
        ManejadorRutas manejador;
        private HttpAsyncTask(Activity activity){
            this.mActivity = activity;
        }

        @Override
        protected String doInBackground(String... urls) {
            try{
                // una vez recibido el string con  el json
                //  se parsea guardando en un array
                JSONArray rutas = new JSONArray(GetFavoritos(urls[0]));
                String nombre;



                //  cada i corresponderia a una diferente ruta
                // se obtiene el objetoJson de esa posicion
                // y se le sacan los atributos que todos serian
                //  Strings. Se guarda una ruta en el arreglo de rutas

                for (int i = 0; i < rutas.length(); i++) {
                   
                    ids.add(Integer.toString(rutas.getJSONObject(i).getInt("ruta_id")));


                   // favoritos.setNombre(rutas.getJSONObject(i).getString("nombre"));
                   // nombre = rutas.getJSONObject(i).getString("nombre");
                   // favoritos.setFrecuencia(rutas.getJSONObject(i).getString("frecuencia"));
                   // favoritos.setPrecio(rutas.getJSONObject(i).getString("precio"));
                   // favoritos.setHorario(rutas.getJSONObject(i).getString("horario"));
                   // favoritos.setParadas(mUsuario.getToken());
                   // mFavoritosArray.add(favoritos);
                   // Toast.makeText(getBaseContext(), nombre, Toast.LENGTH_LONG).show();
                }
                manejador = ManejadorRutas.getInstancia(mUsuario.getToken());

                //llamar al otro asyntask





                // mResultRutas.setText(mRutasArray.get(1).getFrecuencia());

                // Pasar las rutas al  ListViewAdapter
                //mAdapter = new ListViewAdapter(mActivity, mRutasArray);

                // enlazar el adaptador con el listView
                //mList.setAdapter(mAdapter);


            }catch(JSONException e){
                e.printStackTrace();
            }
            return "Rutas Obtenidas!";
        }

        /**
         * metodo que se ejecuta después de obtener la respuesta
         * al request get
         * @param resultado
         */
        @Override
        protected void onPostExecute(String resultado) {

            
            ArrayList<Ruta> auxiliar = manejador.getListaRutas(); 
            // una vez obtenido el token se pide las rutas
            for(String id : ids) {
                mFavoritosArray.add(auxiliar.get(Integer.parseInt(id)-1));
            }
        }


    }

    /**
     * Metodo que hace un request al API con la url donde
     * se pregunta por la tabla de favoritos
     * @param url url que almacena las rutas favoritas
     * @return String con  el array Json
     */
    public  String GetFavoritos(String url){
        String resultado = ApiManager.httpGet(url,mUsuario.getToken());

        return resultado;
    }

    public  String GET(String url){
        String resultado = ApiManager.httpGet(url,mUsuario.getToken());

        return resultado;
    }

    private class HttpAsyncTaskRutas extends AsyncTask<String, Void, String> {
        Activity mActivity;
        private HttpAsyncTaskRutas(Activity activity){
            this.mActivity = activity;
        }

        @Override
        protected String doInBackground(String... urls) {

            ManejadorRutas manejador = ManejadorRutas.getInstancia(mUsuario.getToken());
            mFavoritosArray = manejador.getListaRutas();
            /*try{
                // una vez recibido el string con  el json
                //  se parsea guardando en un array
                JSONObject rutas = new JSONObject(GET(urls[0]));




                //  cada i corresponderia a una diferente ruta
                // se obtiene el objetoJson de esa posicion
                // y se le sacan los atributos que todos serian
                //  Strings. Se guarda una ruta en el arreglo de rutas


                    Ruta favoritos = new Ruta();
                    favoritos.setId(Integer.toString(rutas.getInt("id")));
                    favoritos.setNombre(rutas.getString("nombre"));
                    favoritos.setFrecuencia(rutas.getString("frecuencia"));
                    favoritos.setPrecio(rutas.getString("precio"));
                    favoritos.setHorario(rutas.getString("horario"));
                    favoritos.setParadas(mUsuario.getToken());
                    mFavoritosArray.add(favoritos);








                // mResultRutas.setText(mRutasArray.get(1).getFrecuencia());

                // Pasar las rutas al  ListViewAdapter
                //mAdapter = new ListViewAdapter(mActivity, mRutasArray);

                // enlazar el adaptador con el listView
                //mList.setAdapter(mAdapter);


            }catch(JSONException e){
                e.printStackTrace();
            }*/
            return "Rutas Obtenidas!";
        }

        /**
         * metodo que se ejecuta después de obtener la respuesta
         * al request get
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
            createListView();
        }


    }

    /**
     * Obtener el token para consultar rutas favoritas
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
            mUsuario.guardarTokenId(resultado);

            // una vez obtenido el token se pide las rutas
            new HttpAsyncTask(mActivity).execute(mUrlFavorita);

        }
    }
}
