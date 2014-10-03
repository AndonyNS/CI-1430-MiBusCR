package com.example.busdevelop.buses;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class FavoritosActivity extends ActionBarActivity {

    private GoogleMap mGoogleMap;
    ArrayList<Favoritos> mFavoritosArray = new ArrayList<Favoritos>();
    ListView mList;
    ListViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        try {
            if (mGoogleMap == null) {
                mGoogleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }

        CameraUpdate centro = CameraUpdateFactory.newLatLng(new LatLng(9.935783, -84.051375));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(14);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        mGoogleMap.moveCamera(centro);
        mGoogleMap.animateCamera(zoom);

        } catch (Exception e) {

            e.printStackTrace();
            Log.e("Mapa", "exception", e);
        }

        new HttpAsyncTask().execute("https://murmuring-anchorage-1614.herokuapp.com/  ");

        // Locate the ListView in activity_obt_rutas.xml
        mList = (ListView) findViewById(R.id.listviewFavoritos);

        // Pass results to ListViewAdapter Class
        //mAdapter = new ListViewAdapter(this, mFavoritosArray);

        // Binds the Adapter to the ListView
        mList.setAdapter(mAdapter);
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

    /**
     * Metodo que hace un request al API con la url donde
     * se pregunta por la tabla de rutas
     * @param url url que almacena las rutas
     * @return String con  el array Json
     */
    public  String GET(String url){
        InputStream inputStream = null;
        String resultado = "";
        try {

            // Crear el cliente http
            HttpClient httpclient = new DefaultHttpClient();

            // hacer el request get al API
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // recibir la respuesta en un imputStream
            inputStream = httpResponse.getEntity().getContent();

            // convertir el imputStream a String
            if(inputStream != null)
                resultado = convertInputStreamToString(inputStream);
            else
                resultado = "Error al conectar a la Base de Datos";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return resultado;
    }

    /**
     * Metodo que convierte el imput stream que se recibe del servidor
     * web a un String
     * @param inputStream
     * @return
     * @throws java.io.IOException
     */
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String linea;
        String result = "";
        while((linea = bufferedReader.readLine()) != null)
            result += linea;

        inputStream.close();
        return result;

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        /**
         * metodo que se ejecuta después de obtener la respuesta
         * al request get
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Rutas Favoritas Obtenidas!", Toast.LENGTH_LONG).show();
            try{
                // una vez recibido el string con  el json
                //  se parsea guardando en un array
                JSONArray favoritas = new JSONArray(result);
                String impr ="";
                impr +=  favoritas.length();

                /*//  cada i corresponderia a una diferente ruta favorita
                // se obtiene el objetoJson de esa posicion
                // y se le sacan los atributos que todos serian
                //  Strings. Se guarda una ruta en el arreglo de rutas
                for(int i = 0; i < favoritas.length(); i++){
                    Favoritos favoritos = new Favoritos();
                    favoritos.setId(favoritas.getJSONObject(i).getString("id"));
                    favoritos.setNombre(favoritas.getJSONObject(i).getString("nombre"));
                    favoritos.setFrecuencia(favoritas.getJSONObject(i).getString("frecuencia"));
                    favoritos.setPrecio(favoritas.getJSONObject(i).getString("precio"));
                    favoritos.setHorario(favoritas.getJSONObject(i).getString("horario"));
                    mFavoritosArray.add(favoritos);
                }*/

                /*for(int i = 0; i < mRutasArray.size(); i++){
                    impr += "\n------------------------\n";
                    impr += mRutasArray.get(i).getId() + "\n";
                    impr += mRutasArray.get(i).getNombre() + "\n";
                    impr += mRutasArray.get(i).getFrecuencia() + "\n";
                    impr += mRutasArray.get(i).getPrecio() + "\n";
                    impr += mRutasArray.get(i).getHorario() + "\n";
                }

                mResultRutas.setText(impr);*/


            }catch(JSONException e){
                e.printStackTrace();
            }
        }
    }
}
