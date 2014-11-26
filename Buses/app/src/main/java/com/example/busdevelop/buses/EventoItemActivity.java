package com.example.busdevelop.buses;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.List;


public class EventoItemActivity extends ActionBarActivity {

    GoogleMap mMapa;
    Evento mEvento;
    MarkerOptions mMarcadorEvento;
    ImageView mImagenEvento;
    TextView mNombreEvento;
    TextView mTipoEvento;
    TextView mFechaEvento;
    TextView mDescripcionEvento;
    int position;
    LinearLayout mLinearLayout;
    ListView mLista;
    Button mIr;

    // obtener rutas
    private Usuario mUsuario;
    private ListView mListViewRutasCercanas;
    private List<Ruta> mListaRutas;
    private ListViewAdapter mAdapter;

    // para obtener paradas a analizar de cada ruta
    private Parada mParadaInicial;
    private Parada mParadaFinal;
    private ArrayList<Parada> mParadasIntermedias;
    private Location mUbicacionEvento;  // coordenadas de donde se encuentra el usuario
    private ArrayList<Ruta> mListaRutasCercanas = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_item);
         // recuperar los datos del evento que manda al
        // hacer click en un elemento de la lista
        mListaRutasCercanas = new ArrayList<Ruta>();

        Intent i = getIntent();
        mEvento = new Evento();
        mEvento.setNombre(i.getStringExtra("nombre"));
        mEvento.setDescripcion(i.getStringExtra("descripcion"));
        mEvento.setTipo(i.getStringExtra("tipo"));
        mEvento.setFecha(i.getStringExtra("fecha"));
        mEvento.setLugar(i.getStringExtra("lugar"));
        mEvento.setLatitud(i.getStringExtra("latitud"));
        mEvento.setLongitud(i.getStringExtra("longitud"));

        position = Integer.parseInt(i.getStringExtra("position"));

        // Localizar los vie en activity_evento_item.xml
        mImagenEvento = (ImageView) findViewById(R.id.imagenEvento);
        mNombreEvento = (TextView) findViewById(R.id.nombreEvento);
        mTipoEvento = (TextView) findViewById(R.id.tipoEvento);
        mFechaEvento = (TextView) findViewById(R.id.fechaEvento);
        mDescripcionEvento = (TextView) findViewById(R.id.descripcionEvento);
        mLinearLayout  = (LinearLayout) findViewById(R.id.linearLayoutEvento);
        //mLista = (ListView) findViewById(R.id.rutasCercanasEvento);
        mIr = (Button) findViewById(R.id.btnIr);
        //getRutas();
        try {
            if (mMapa == null) {
               // soportado en ultimas apis
                mMapa = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.mapEvento)).getMap();
            }

            mMapa.setMyLocationEnabled(true);
            mMapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMapa.getUiSettings().setZoomGesturesEnabled(true);

            LatLng latlng;
            try{
                latlng = new LatLng(mMapa.getMyLocation().getLatitude(),mMapa.getMyLocation().getLongitude());
            }catch(NullPointerException e){
                latlng = new LatLng(9.9200652,-84.0846053);
            }
            moveToLocation(latlng, 13);

            // moverse a la localizacion del evento
            latlng = new LatLng(Double.parseDouble(mEvento.getLatitud()) ,
                    Double.parseDouble(mEvento.getLongitud()));

            moveToLocation(latlng, 15);

            // agregar marcador a mapa en localización de evento
            mMarcadorEvento = new MarkerOptions();
            mMarcadorEvento.position(latlng);
            mMarcadorEvento.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mMarcadorEvento.title(mEvento.getLugar());
            mMapa.addMarker(mMarcadorEvento);

        } catch (Exception e) {

            e.printStackTrace();
            Log.e("Mapa", "exception", e);
        }

        // Mostrar Infromación del evento
        datosEvento();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.evento_item, menu);
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

    //Método que mueve la cámara al LatLng dado y al zoom indicado
    private void moveToLocation(LatLng ll,int zoomDistance) {
        //new LatLng(9.935783, -84.051375)
        CameraUpdate ubicacion = CameraUpdateFactory.newLatLng(ll);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(zoomDistance);
        mMapa.moveCamera(ubicacion);
        mMapa.animateCamera(zoom);
    }


    /**
     * Metodo que llena los datos del evento y los despliega en layout
     * encima del mapa
     */

    public void datosEvento(){
        mNombreEvento.setText(mEvento.getNombre());
        mFechaEvento.setText("Fecha: " + mEvento.getFecha());
        mDescripcionEvento.setText("Descripción: " + mEvento.getDescripcion());

        // switch para agregar el tipo
        switch (Integer.parseInt(mEvento.getTipo())){
            case 1: // Artistico
                mTipoEvento.setText("Tipo de Evento: Artístico");
                if(position  == 0){
                    mImagenEvento.setImageResource(R.drawable.arte2);
                } else{
                    mImagenEvento.setImageResource(R.drawable.arte1);
                }

                break;
            case 2: // Deportivo
                mTipoEvento.setText("Tipo de Evento: Deportivo");
                mImagenEvento.setImageResource(R.drawable.deportes2);
                break;
            case 3: // Fiesta
                mTipoEvento.setText("Tipo de Evento: Entretenimiento, Fiesta");
                if(position % 2 == 0){
                    mImagenEvento.setImageResource(R.drawable.fiesta2);
                }else{
                    mImagenEvento.setImageResource(R.drawable.fiesta3);
                }

                break;
            case 4: // Concierto
                mTipoEvento.setText("Tipo de Evento: Concierto");
                if(position % 2 != 0) {
                    mImagenEvento.setImageResource(R.drawable.concierto2);
                }else{
                    mImagenEvento.setImageResource(R.drawable.concierto4);
                }
                break;
            case 5: // Exposicion
                mTipoEvento.setText("Tipo de Evento: Exposicion, Charla");
                mImagenEvento.setImageResource(R.drawable.arte2);
                break;
        }
    }

    public void irAEvento(View view){
        mLinearLayout.setVisibility(View.GONE);
        //mLista.setVisibility(View.VISIBLE);
        // Se obtienen las rutas
        getRutas();


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
                // TODO: Calcular las rutas cercanas
                // por cada ruta se fija en las paradas cercanas
                double distancia;

                mUbicacionEvento = new Location("Evento Ubicacion");
                mUbicacionEvento.setLatitude(Double.parseDouble(mEvento.getLatitud()));
                mUbicacionEvento.setLongitude(Double.parseDouble(mEvento.getLongitud()));

                // Guardara la informacion de la parada a verificar
                Location paradaCercana = new Location("Evaluar Parada");
                for(Ruta ruta : mListaRutas){
                     // mListaRutasCercanas.add(ruta);
                    /**************************************
                     * Verificar ruta inicial
                     **************************************/
                    mParadaInicial = ruta.getParadaInicial();
                    paradaCercana.setLatitude(Double.parseDouble(mParadaInicial.getLatitud()));
                    paradaCercana.setLongitude(Double.parseDouble(mParadaInicial.getLongitud()));
                    distancia = mUbicacionEvento.distanceTo(paradaCercana);

                    if (distancia<= 500){
                        mListaRutasCercanas.add(ruta);
                        continue; // como ya agrego esta ruta no es necesario evaluar mas paradas
                    }


                    /**************************************
                     * Si no verifica parada final
                     **************************************/
                    mParadaFinal = ruta.getParadaFinal();
                    paradaCercana.setLatitude(Double.parseDouble(mParadaFinal.getLatitud()));
                    paradaCercana.setLongitude(Double.parseDouble(mParadaFinal.getLongitud()));
                    distancia = mUbicacionEvento.distanceTo(paradaCercana);
                    if (distancia< 500){
                        mListaRutasCercanas.add(ruta);
                        continue; // como ya agrego esta ruta no es necesario evaluar mas paradas
                    }

                    /****************************************************
                     * El útimo caso sería verificar paradas intermedias
                     ****************************************************/
                    mParadasIntermedias= ruta.getParadasIntermedias();
                    for(Parada parada: mParadasIntermedias ){
                        paradaCercana.setLatitude(Double.parseDouble(parada.getLatitud()));
                        paradaCercana.setLongitude(Double.parseDouble(parada.getLongitud()));
                        distancia = mUbicacionEvento.distanceTo(paradaCercana);
                        if (distancia< 500){
                            mListaRutasCercanas.add(ruta);
                            break; // como ya agrego esta ruta no es necesario evaluar mas paradas
                        }
                    }
                }


                mListViewRutasCercanas = (ListView) findViewById(R.id.rutasCercanasEvento);

                /**
                 * Si hay rutas cercanas las muestra en un listview
                 * Si no le informa al usuario que no hay rutas cercanas
                 * a su ubicación
                 */
                if(mListaRutasCercanas != null){
                    mListViewRutasCercanas.setVisibility(View.VISIBLE);
                    // Pasar las rutas al  ListViewAdapter
                    // mAdapter = new ListViewAdapter(mActivity,(ArrayList) mListaRutas);
                    mAdapter = new ListViewAdapter(mActivity,mListaRutasCercanas);

                    // enlazar el adaptador con el listView
                    mListViewRutasCercanas.setAdapter(mAdapter);
                }else{
                    Toast.makeText(getBaseContext(), "No hay rutas cercanas para llegar al evento", Toast.LENGTH_LONG).show();
                }


            } catch(IllegalArgumentException i){
                Log.e("Error de argumento",""+i.getMessage());
            }
        }
    }
}
