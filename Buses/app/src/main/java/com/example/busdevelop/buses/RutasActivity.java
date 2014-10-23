package com.example.busdevelop.buses;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;

public class RutasActivity extends ActionBarActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Usuario mUsuario;
    private ListView listViewRutas;
    private List<Ruta> mListaRutas;
    private List<Row> rows;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutas);

        mListaRutas = new ArrayList<Ruta>();

        setUpMapIfNeeded();

        if (mMap != null) {
            // Enable MyLocation Button in the Map
            mMap.setMyLocationEnabled(true);
            // The map will be cleared on long click
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

                @Override
                public void onMapLongClick(LatLng point) {
                    // Removes all the points from Google Map
                    mMap.clear();
                }
            });

            mMap.setTrafficEnabled(true);

            getRutas();

            showBuses();
        }

    }

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
            return mUsuario.obtenerToken(mUsuario.getEmail(), mUsuario.getEncrypted_password());
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
                new HttpAsyncTask(mActivity).execute("https://murmuring-anchorage-1614.herokuapp.com/rutas");
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
            try{
                // una vez recibido el string con  el json
                //  se parsea guardando en un array
                JSONArray rutas = new JSONArray(GET(urls[0]));


                //  cada i corresponderia a una diferente ruta
                // se obtiene el objetoJson de esa posicion
                // y se le sacan los atributos que todos serian
                //  Strings. Se guarda una ruta en el arreglo de rutas
                for(int i = 0; i < rutas.length(); i++){
                    Ruta ruta = new Ruta();
                    ruta.setId(Integer.toString(rutas.getJSONObject(i).getInt("id")));
                    ruta.setNombre(rutas.getJSONObject(i).getString("nombre"));
                    ruta.setFrecuencia(rutas.getJSONObject(i).getString("frecuencia"));
                    ruta.setPrecio(rutas.getJSONObject(i).getString("precio"));
                    ruta.setHorario(rutas.getJSONObject(i).getString("horario"));
                    ruta.setParadas(mUsuario.getToken());
                    mListaRutas.add(ruta);
                }

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
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {

            try{
                Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
                createListView();
            } catch(IllegalArgumentException i){
                Log.e("Error de argumento",""+i.getMessage());
            }
        }
    }

    /**
     * Metodo que hace un request al API con la url donde
     * se pregunta por la tabla de rutas
     * @param url url que almacena las rutas
     * @return String con  el array Json
     */
    public  String GET(String url){
        String resultado = ApiManager.httpGet(url,mUsuario.getToken());
        return resultado;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void createListView(){
        // Get ListView object from xml
        listViewRutas = (ListView) findViewById(R.id.rutaslist);
        rows = new ArrayList<Row>();

        Row row = null;
        //Llena toda las filas del listview con las listas obtenidas
        for ( Ruta r : mListaRutas){
            row = new Row();
            row.setTitle(r.getNombre());
            rows.add(row);
            //nombresRutas.add(r.getNombre());
            Log.d("Prueba", r.getNombre());
        }
        if(!rows.isEmpty()) {
            listViewRutas.setVisibility(View.VISIBLE);
        }

        //Le envía al array adapter personalizado el contexto del cual va a llamarlo y el ArrayList de filas
        CustomArrayAdapter adapter = new CustomArrayAdapter(this, rows);

        listViewRutas.setAdapter(adapter);

        listViewRutas.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //Toast.makeText(getApplicationContext(), rows.get(position).getTitle(), Toast.LENGTH_SHORT).show();

                // Obtiene la ruta seleccionada
                Ruta  itemValue = mListaRutas.get(position);

                //Llama a la clase que dibuja la ruta,
                new DibujarRuta(mMap,itemValue);
            }
        });
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.rutas, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.rutas, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}

