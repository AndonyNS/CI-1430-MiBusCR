package com.example.busdevelop.buses;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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


public class ObtRutasActivity extends ActionBarActivity {

    TextView mResultRutas;
    ArrayList<Ruta> mRutasArray  = new ArrayList<Ruta>();
    ListView mList;
    ListViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obt_rutas);

        //referencia a la vista
        //mResultRutas = (TextView) findViewById(R.id.resultRutas);
        new HttpAsyncTask().execute("https://murmuring-anchorage-1614.herokuapp.com/rutas");

        // Locate the ListView in activity_obt_rutas.xml
        mList = (ListView) findViewById(R.id.listviewRutas);

        // Pass results to ListViewAdapter Class
        mAdapter = new ListViewAdapter(this, mRutasArray);

        // Binds the Adapter to the ListView
        mList.setAdapter(mAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.obt_rutas, menu);
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
     * @throws IOException
     */
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
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
            Toast.makeText(getBaseContext(), "Rutas Obtenidas!", Toast.LENGTH_LONG).show();
            try{
                // una vez recibido el string con  el json
                //  se parsea guardando en un array
                JSONArray rutas = new JSONArray(result);
                String impr ="";
                impr +=  rutas.length();

                //  cada i corresponderia a una diferente ruta
                // se obtiene el objetoJson de esa posicion
                // y se le sacan los atributos que todos serian
                //  Strings. Se guarda una ruta en el arreglo de rutas
                for(int i = 0; i < rutas.length(); i++){
                    Ruta ruta = new Ruta();
                    ruta.setId(rutas.getJSONObject(i).getString("id"));
                    ruta.setNombre(rutas.getJSONObject(i).getString("nombre"));
                    ruta.setFrecuencia(rutas.getJSONObject(i).getString("frecuencia"));
                    ruta.setPrecio(rutas.getJSONObject(i).getString("precio"));
                    ruta.setHorario(rutas.getJSONObject(i).getString("horario"));
                    mRutasArray.add(ruta);
                }

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