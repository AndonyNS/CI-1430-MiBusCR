package com.example.busdevelop.buses;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
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

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class RutasActivity extends ActionBarActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Usuario mUsuario;
    private ListView listViewRutas;
    private List<Ruta> mListaRutas;
    private ArrayList<Ruta> mListaRutasCercanas = null;
    private List<String> mFavIds;
    private String mUrlFavorita= "https://murmuring-anchorage-1614.herokuapp.com/favoritas";
    private Location mUbicacion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutas);
        mFavIds = new ArrayList<String>();
        mListaRutas = new ArrayList<Ruta>();

        getRutas();

        mUbicacion = new Location("Aqui estoy");

        try {
            if (mMap == null) {
                mMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.mapRutas)).getMap();
            }

            // Enable MyLocation Button in the Map
            mMap.setMyLocationEnabled(true);

            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.getUiSettings().setZoomGesturesEnabled(true);

            //Obtiene la latitud y longitud de mi ubicación actual, y llama al método que mueve la cámara a una ubicación
            //UCR new LatLng(9.935783, -84.051375)
            //MiUbicacion new LatLng(mMap.getMyLocation().getLatitude(),mMap.getMyLocation().getLongitude());
            //Inicia el mapa centrado en San José
            LatLng latlng;
            try{
                latlng = new LatLng(mMap.getMyLocation().getLatitude(),mMap.getMyLocation().getLongitude());
            }catch(NullPointerException e){
                latlng = new LatLng(9.9200652,-84.0846053);
            }
            moveToLocation(latlng, 13);

            // The map will ask to see if you want to go there
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng point) {
                    mUbicacion.setLatitude(point.latitude);
                    mUbicacion.setLongitude(point.longitude);
                    confirmacionDeIr();
                }
            });

            mMap.setTrafficEnabled(true);

            Thread.sleep(5);
            Log.d("El mapa que va a escribir es",mMap.toString());
            MainActivity.currentMap = mMap;
            Log.d("Ahora current map es",MainActivity.currentMap.toString());

        } catch (Exception e) {

            e.printStackTrace();
            Log.e("Mapa", "exception", e);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getRutas(){
        mUsuario = new Usuario();
        SharedPreferences sharedPref = getSharedPreferences("MyPrefsFile", 0);
        mUsuario.setEmail(sharedPref.getString("UserEmail", ""));
        mUsuario.setEncrypted_password(sharedPref.getString("UserPass", ""));

        new HttpAsyncTaskToken(this).execute();
    }

    //Método que mueve la cámara al LatLng dado y al zoom indicado
    private void moveToLocation(LatLng ll,int zoomDistance) {
        //new LatLng(9.935783, -84.051375)
        CameraUpdate ubicacion = CameraUpdateFactory.newLatLng(ll);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(zoomDistance);
        mMap.moveCamera(ubicacion);
        mMap.animateCamera(zoom);
    }

    private void confirmacionDeIr(){
        AlertDialog.Builder confirmar = new AlertDialog.Builder(this);
        confirmar.setTitle("Navegar");
        confirmar.setMessage("¿Desea saber como llegar a este punto?");


        //Poner la ubicacion
        confirmar.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("SI","va a calcular");
                        new HttpAsyncTaskRutasCercanas().execute();
                    }
                }
                );

        confirmar.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("NO","va a cerrar");

                        dialogInterface.dismiss();
                    }
                }
                );


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
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,0));
        //Hace el zoom 1 para "afuera" porque sino quedan las paradas en el borde de la cámara
        mMap.animateCamera(CameraUpdateFactory.zoomTo(mMap.getCameraPosition().zoom-1));
    }

    private void createListView(ListView listView, final List<Ruta> listRutas){
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.rutaslist);
        List<Row> rows = new ArrayList<Row>();

        Row row;
        //Llena toda las filas del listview con las listas obtenidas
        for ( Ruta r : listRutas){
            row = new Row();
            row.setTitle(r.getNombre());
            if(mFavIds.contains(r.getId())){
                row.setChecked(true);
            }
            rows.add(row);
            //nombresRutas.add(r.getNombre());
            Log.d("Prueba", r.getNombre());
        }
        if(!rows.isEmpty()) {
            listView.setVisibility(View.VISIBLE);
        }

        //Le envía al array adapter personalizado el contexto del cual va a llamarlo y el ArrayList de filas
        CustomArrayAdapter adapter = new CustomArrayAdapter(this, rows,this,listRutas,mUsuario);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), rows.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                view.setSelected(true);
                // Obtiene la ruta seleccionada
                Ruta rutaSeleccionada = listRutas.get(position);

                //Llama a la clase que dibuja la ruta,
                new DibujarRuta(mUsuario,mMap, rutaSeleccionada);

                moveToBounds(rutaSeleccionada.getParadaInicial(), rutaSeleccionada.getParadaFinal());

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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
            try{
                JSONArray rutas = new JSONArray(ApiManager.httpGet(mUrlFavorita,mUsuario.getToken()));
                for (int i = 0; i < rutas.length(); i++) {

                    mFavIds.add(rutas.getJSONObject(i).getString("ruta_id"));

                }

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
                createListView(listViewRutas,mListaRutas);
            } catch(IllegalArgumentException i){
                Log.e("Error de argumento",""+i.getMessage());
            }
        }
    }

    private class HttpAsyncTaskRutasCercanas extends AsyncTask<String, Void, String> {
        private HttpAsyncTaskRutasCercanas(){
        }

        @Override
        protected String doInBackground(String... urls) {
            ManejadorRutas manejador = ManejadorRutas.getInstancia(mUsuario.getToken());
            mListaRutas = manejador.getListaRutas();
            return "Calculando Rutas cercanas";
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
                // por cada ruta se fija en las paradas cercanas
                double distancia;

                //mUbicacionActual = new Location("Evento Ubicacion");
                //mUbicacionActual.setLatitude(Double.parseDouble(mEvento.getLatitud()));
                //mUbicacionEvento.setLongitude(Double.parseDouble(mEvento.getLongitud()));

                // Guardara la informacion de la parada a verificar
                Location paradaCercana = new Location("Evaluar Parada");
                for(Ruta ruta : mListaRutas){
                    // mListaRutasCercanas.add(ruta);
                    /**************************************
                     * Verificar ruta inicial
                     **************************************/
                    Parada mParadaInicial = ruta.getParadaInicial();
                    paradaCercana.setLatitude(Double.parseDouble(mParadaInicial.getLatitud()));
                    paradaCercana.setLongitude(Double.parseDouble(mParadaInicial.getLongitud()));
                    distancia = mUbicacion.distanceTo(paradaCercana);

                    if (distancia<= 500){
                        mListaRutasCercanas.add(ruta);
                        continue; // como ya agrego esta ruta no es necesario evaluar mas paradas
                    }


                    /**************************************
                     * Si no verifica parada final
                     **************************************/
                    Parada mParadaFinal = ruta.getParadaFinal();
                    paradaCercana.setLatitude(Double.parseDouble(mParadaFinal.getLatitud()));
                    paradaCercana.setLongitude(Double.parseDouble(mParadaFinal.getLongitud()));
                    distancia = mUbicacion.distanceTo(paradaCercana);
                    if (distancia< 500){
                        mListaRutasCercanas.add(ruta);
                        continue; // como ya agrego esta ruta no es necesario evaluar mas paradas
                    }

                    /****************************************************
                     * El útimo caso sería verificar paradas intermedias
                     ****************************************************/
                    List<Parada> mParadasIntermedias= ruta.getParadasIntermedias();
                    for(Parada parada: mParadasIntermedias ){
                        paradaCercana.setLatitude(Double.parseDouble(parada.getLatitud()));
                        paradaCercana.setLongitude(Double.parseDouble(parada.getLongitud()));
                        distancia = mUbicacion.distanceTo(paradaCercana);
                        if (distancia< 500){
                            mListaRutasCercanas.add(ruta);
                            break; // como ya agrego esta ruta no es necesario evaluar mas paradas
                        }
                    }
                }


                listViewRutas = (ListView) findViewById(R.id.rutaslist);

                /**
                 * Si hay rutas cercanas las muestra en un listview
                 * Si no le informa al usuario que no hay rutas cercanas
                 * a su ubicación
                 */
                if(mListaRutasCercanas != null){
                    listViewRutas.setVisibility(View.VISIBLE);
                    createListView(listViewRutas,mListaRutas);
                }else{
                    Toast.makeText(getBaseContext(), "No hay rutas cercanas para llegar al evento", Toast.LENGTH_LONG).show();
                }


            } catch(IllegalArgumentException i){
                Log.e("Error de argumento",""+i.getMessage());
            }
        }
    }
}

