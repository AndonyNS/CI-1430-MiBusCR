package com.example.busdevelop.buses;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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

public class MiUbicacionActivity extends ActionBarActivity implements LocationListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LocationManager mLocationManager;
    private ProgressDialog mpd;
    private Marker mMarcadorPosicion = null;
    private Marker mMarcadorBus = null;
    private Marker mMarcadorUpdate = null;
    private static final String mFIREBASE_URL = "https://blazing-fire-9075.firebaseio.com/";
    private String mGps;
    private String mLocation;
    private double mLatitud;
    private double mLongitud;
    private Usuario mUsuario;
    private ListView listViewRutas;

    private List<Ruta> mListaRutas;
    private List<LatLng> mMarkerParadas;

    // Marcadores de uuna ruta al dibujarse
    private List<Marker> mMarkerRuta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutas);

        mMarkerParadas = new ArrayList<LatLng>();
        mListaRutas = new ArrayList<Ruta>();
        mMarkerRuta = new ArrayList<Marker>();

        setUpMapIfNeeded();
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mpd = ProgressDialog
                .show(this, "Localización", "Esperando localización");
        // Recordatorio: en este metodo indicamos la frecuencia con la
        // que obtenemos una nueva posicion.
        // En este caso cada 5 segundos y que me haya desplazado 20 metros
        // obtendra una nueva localizacion
        mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 20, this);
        //Agrega que no solo espere GPS para obtener ubicación, funciona tambien con la red (menor precisión)
        mLocationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 0, 0, this);

        getRutas();

        getLocation();

        showBuses();

        if (mMap != null) {

            // Enable MyLocation Button in the Map
            mMap.setMyLocationEnabled(true);

            // The map will be cleared on long click
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

                @Override
                public void onMapLongClick(LatLng point) {
                    // Removes all the points from Google Map
                    mMap.clear();

                    // Removes all the points in the ArrayList
                    mMarkerParadas.clear();
                }
            });
        }

        listViewRutas = (ListView) findViewById(R.id.rutaslist);
        listViewRutas.setVisibility(View.GONE);
    }

    private void getRutas(){
        mUsuario = new Usuario();
        SharedPreferences sharedPref = getSharedPreferences("MyPrefsFile", 0);
        mUsuario.setEmail(sharedPref.getString("UserEmail", ""));
        mUsuario.setEncrypted_password(sharedPref.getString("UserPass", ""));

        new HttpAsyncTaskToken(this).execute();
    }

    private String getDirectionsUrl(){

        // Origin of route
        String str_origin = "origin="+mMarkerParadas.get(0).latitude+","+
                mMarkerParadas.get(0).longitude;

        // Destination of route
        String str_dest = "destination="+mMarkerParadas.get(1).latitude+","+
                mMarkerParadas.get(1).longitude;

        //paradas
        String str_waypts = "";
        for(int i = 2; i<mMarkerParadas.size(); i++){
            if(i==2) {
                str_waypts = "&waypoints=";
            }
            str_waypts += mMarkerParadas.get(i).latitude + "," + mMarkerParadas.get(i).longitude + "|";
        }

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+str_waypts+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
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
        String resultado = ApiManager.httpGet(url, mUsuario.getToken());
        return resultado;
    }


    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            try{
                super.onPostExecute(result);

                ParserTask parserTask = new ParserTask();

                // Invokes the thread for parsing the JSON data
                parserTask.execute(result);
            } catch(IllegalArgumentException i){
                Log.e("Error de argumento",""+i.getMessage());
            }
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            try{
                ArrayList<LatLng> points = null;
                PolylineOptions lineOptions = null;

                // Traversing through all the routes
                for(int i=0;i<result.size();i++){
                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for(int j=0; j<path.size(); j++){
                        HashMap<String,String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(2);
                    lineOptions.color(Color.RED);
                }

                // Drawing polyline in the Google Map for the i-th route
                mMap.addPolyline(lineOptions);
            } catch(IllegalArgumentException i){
                Log.e("Error de argumento",""+i.getMessage());
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void createListView(){
        // Get ListView object from xml
        listViewRutas = (ListView) findViewById(R.id.rutaslist);

        List<String> nombresRutas = new ArrayList<String>();
        for ( Ruta r : mListaRutas){
            nombresRutas.add(r.getNombre());
            Log.d("Prueba",r.getNombre());
            Log.d("Prueba",r.getNombre());
        }
        if(!nombresRutas.isEmpty()) {
            listViewRutas.setVisibility(View.VISIBLE);
        }
        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, nombresRutas);


        // Assign adapter to ListView
        listViewRutas.setAdapter(adapter);

        // ListView Item Click Listener
        listViewRutas.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                /*for(Marker marker: mMarkerRuta){
                    marker.remove();
                }
                mMarkerRuta.clear();*/

                // ListView Clicked item value
                Ruta  itemValue = mListaRutas.get(position);
                //Toast.makeText(getBaseContext(), "Rutas Obtenidas!", Toast.LENGTH_LONG).show();

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

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        /* No es necesario, debido a que funciona sin GPS
        Intent intent = new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);*/
        Toast.makeText(this, "Se recomienda encender el GPS para mejorar la precisión",
                Toast.LENGTH_LONG).show();
    }

    public void getLocation() {

        Firebase firebaseRef = new Firebase(mFIREBASE_URL);

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
                mMarcadorUpdate = mMarcadorPosicion;

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

        });


    }

    /* Muestra el otro bus*/
    public void showBuses() {
        Firebase firebaseRef = new Firebase(mFIREBASE_URL);

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

        });
    }



    @Override
    public void onLocationChanged(Location posicion) {
        LatLng latitudLongitud = new LatLng(posicion.getLatitude(),
                posicion.getLongitude());

        // Desaparecer del interfaz de usuario el ProgressDialog
        mpd.dismiss();

        if (mMarcadorUpdate == null) {
            mMarcadorUpdate = mMap.addMarker(new MarkerOptions()
                    .position(latitudLongitud)
                    .title("Ubicación")
                    .snippet(
                            "lat:" + posicion.getLatitude() + ",lon:"
                                    + posicion.getLongitude())
                    .draggable(true)
                            // .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.logo)));

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latitudLongitud,
                    13));
        } else {
            mMarcadorUpdate.setPosition(latitudLongitud);

            mMap.animateCamera(CameraUpdateFactory.newLatLng(latitudLongitud));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

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


