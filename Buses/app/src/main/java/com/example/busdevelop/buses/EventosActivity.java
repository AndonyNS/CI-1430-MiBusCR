package com.example.busdevelop.buses;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class EventosActivity extends ActionBarActivity {

    String mUrlEvento = "https://murmuring-anchorage-1614.herokuapp.com/eventos";
    Usuario mUsuario;
    private final String mPrefs_Name = "MyPrefsFile";
    List<Evento> mEventosArray ;
    ListView mList;
    EventoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);
        // Obtener datos del usuario
        // obtener del shared preferences el email
        // y el password
        mUsuario = new Usuario();
        SharedPreferences sharedPref = getSharedPreferences(mPrefs_Name, 0);
        mUsuario.setEmail(sharedPref.getString("UserEmail", ""));
        mUsuario.setEncrypted_password(sharedPref.getString("UserPass", ""));

        // localizar el listview en activity_eventos.xml
        mList = (ListView) findViewById(R.id.listviewEventos);

        // obtener el token
        new HttpAsyncTaskToken(this).execute();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.eventos, menu);
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
     *Metodo que obtiene eventos de la base de datos
     */
    public String llenarEventos(String token){
        String resultado = ApiManager.httpGet(mUrlEvento, token);
        return resultado;
    }

    /**
     * Clase para manejar la obtencion de eventos
     */
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        Activity mActivity;
        private HttpAsyncTask(Activity activity){
            this.mActivity = activity;
        }


        @Override
        protected String doInBackground(String... tokens) {

            return llenarEventos(tokens[0]);
        }

        /**
         * metodo que se ejecuta después de obtener la respuesta
         * al request get
         * @param resultado
         */
        @Override
        protected void onPostExecute(String resultado) {

            Toast.makeText(getBaseContext(), "Eventos Obtenidos!", Toast.LENGTH_LONG).show();
            try{
                // una vez recibido el string con  el json
                //  se parsea guardando en un array
                JSONArray datosEvento = new JSONArray(resultado);
                mEventosArray = new ArrayList<Evento>();

                //  cada i corresponderia a una diferente ruta
                // se obtiene el objetoJson de esa posicion
                // y se le sacan los atributos que todos serian
                //  Strings. Se guarda un evento en el arreglo de eventos

                for(int i = 0; i < datosEvento.length(); i++){
                    Evento evento = new Evento();
                    evento.setNombre(datosEvento.getJSONObject(i).getString("nombre"));
                    evento.setDescripcion(datosEvento.getJSONObject(i).getString("descripcion"));
                    evento.setTipo(Integer.toString(datosEvento.getJSONObject(i).getInt("tipo")));
                    evento.setFecha(datosEvento.getJSONObject(i).getString("dia_y_hora"));
                    evento.setLugar(datosEvento.getJSONObject(i).getString("lugar"));
                    evento.setLatitud(Double.toString(datosEvento.getJSONObject(i).getDouble("latitud")));
                    evento.setLongitud(Double.toString(datosEvento.getJSONObject(i).getDouble("longitud")));
                    mEventosArray.add(evento);
                }




            }catch(JSONException e){
                e.printStackTrace();
            }
            // se pasan los eventos al adaptador
            mAdapter = new EventoAdapter(mActivity, mEventosArray);
            // se enlaza el adaptador con el ListView
            mList.setAdapter(mAdapter);




        }
    }

    /**
     * Obtener el token para poder consultar eventos
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

            // una vez obtenido el token se pide los eventos
            new HttpAsyncTask(mActivity).execute(mUsuario.getToken());

        }
    }
}
