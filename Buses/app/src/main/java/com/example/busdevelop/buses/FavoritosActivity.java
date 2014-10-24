package com.example.busdevelop.buses;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
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
import java.util.Locale;


public class FavoritosActivity extends ActionBarActivity {

    private GoogleMap mGoogleMap;
    private List<Ruta> mFavoritosArray;
    private List<String> mNombreRutaArray;
    private List<Row> rows;
    private ListView mList;
    private List<LatLng> mMarkerParadas;
    private Usuario mUsuario;
    private String mUrlFavorita= "https://murmuring-anchorage-1614.herokuapp.com/rutas/";
    private List<String> ids = new ArrayList<String>();
    private final String mPrefs_Name = "MyPrefsFile";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        mMarkerParadas = new ArrayList<LatLng>();
        mFavoritosArray = new ArrayList<Ruta>();




        // Locate the ListView in activity_obt_rutas.xml
        mList = (ListView) findViewById(R.id.favoritoslist);
        mList.setVisibility(View.GONE);



        setUpMapIfNeed();

        CameraUpdate centro = CameraUpdateFactory.newLatLng(new LatLng(9.935783, -84.051375));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(14);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        mGoogleMap.moveCamera(centro);
        mGoogleMap.animateCamera(zoom);


        getRutas();


       }


    private void getRutas(){
        mUsuario = new Usuario();
        SharedPreferences sharedPref = getSharedPreferences("MyPrefsFile", 0);
        mUsuario.setEmail(sharedPref.getString("UserEmail", ""));
        mUsuario.setEncrypted_password(sharedPref.getString("UserPass", ""));

        new HttpAsyncTaskToken(this).execute();
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

        Log.d("sali del for","");

        //Le envía al array adapter personalizado el contexto del cual va a llamarlo y el ArrayList de filas
        CustomArrayAdapter adapter = new CustomArrayAdapter(this, rows);

        mList.setAdapter(adapter);




        // ListView Item Click Listener
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Ruta itemValue = mFavoritosArray.get(position);

                Log.d("obtuve la seleccionada", itemValue.getNombre());
                new DibujarRuta(mGoogleMap, itemValue);


            }
        });
    }


    private void setUpMapIfNeed(){


            if (mGoogleMap == null) {
                mGoogleMap = ((SupportMapFragment) getSupportFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }
    }

    @Override
    protected void onResume(){

        super.onResume();
        setUpMapIfNeed();
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
    // metodo que dibuja en el mapa la ruta
   public void dibujarRuta(Ruta ruta){

        //se obtiene las paradas
        Parada paradaInicial = ruta.getParadaInicial();
        Parada paradaFinal= ruta.getParadaFinal();
        ArrayList<Parada> paradas = ruta.getParadasIntermedias();

       // se convierten las paradas en markers para agregar al mapa
       MarkerOptions options = new MarkerOptions();

        LatLng markerInicial = new LatLng(Double.parseDouble(paradaInicial.getLatitud()),
                Double.parseDouble(paradaInicial.getLongitud()));
       options.position(markerInicial);
       options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
       mGoogleMap.addMarker(options);

       LatLng marker;
       for(Parada p : paradas){

           marker = new LatLng(Double.parseDouble(p.getLatitud()),
                   Double.parseDouble(p.getLongitud()));
           mMarkerParadas.add(marker);

       }

       LatLng markerFinal = new LatLng(Double.parseDouble(paradaFinal.getLatitud()),
               Double.parseDouble(paradaFinal.getLongitud()));
       options.position(markerFinal);
       options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
       mGoogleMap.addMarker(options);

       // Getting URL to the Google Directions API
       String url = getDirectionsUrl(markerInicial, markerFinal);

       DownloadTask downloadTask = new DownloadTask();

       // Start downloading json data from Google Directions API
       downloadTask.execute(url);

   }


    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        //paradas
        String str_waypts = "waypoints=";
        for(int i = 0; i<mMarkerParadas.size(); i++){

            str_waypts += mMarkerParadas.get(i).latitude + "," + mMarkerParadas.get(i).longitude + "|";
        }

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+str_waypts+"&"+sensor;

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
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
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
            mGoogleMap.addPolyline(lineOptions);
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
                JSONArray rutas = new JSONArray(GetFavoritos(urls[0]));
                String nombre;



                //  cada i corresponderia a una diferente ruta
                // se obtiene el objetoJson de esa posicion
                // y se le sacan los atributos que todos serian
                //  Strings. Se guarda una ruta en el arreglo de rutas

                for (int i = 0; i < rutas.length(); i++) {
                    Ruta favoritos = new Ruta();
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


            // una vez obtenido el token se pide las rutas
            for(String id : ids) {
                new HttpAsyncTaskRutas(mActivity).execute("https://murmuring-anchorage-1614.herokuapp.com/rutas/"+id);
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
            try{
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
                    //favoritos.setParadas(mUsuario.getToken());
                    mFavoritosArray.add(favoritos);








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

            return mUsuario.obtenerToken(mUsuario.getEmail(), mUsuario.getEncrypted_password());
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
            new HttpAsyncTask(mActivity).execute("https://murmuring-anchorage-1614.herokuapp.com/favoritas");

        }
    }
}
