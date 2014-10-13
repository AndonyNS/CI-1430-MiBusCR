package com.example.busdevelop.buses;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class FavoritosActivity extends ActionBarActivity {

    private GoogleMap mGoogleMap;
    ArrayList<Ruta> mFavoritosArray = new ArrayList<Ruta>();
    ListView mList;
    ListViewAdapter mAdapter;

    Usuario mUsuarioToken;
    Usuario mUsuarioObtenido;
    String mUrlUsuario = "https://murmuring-anchorage-1614.herokuapp.com/users/";
    String mUrlFavorito ="https://murmuring-anchorage-1614.herokuapp.com/favoritas/";
    String mTokenUsuario = "";
    int mIdUser;
    String mEmailShaPref = "";
    String mPassShaPref = "";
    private final String mPrefs_Name = "MyPrefsFile";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        // obtener del shared preferences el email
        // y el password
        SharedPreferences sharedPref = getSharedPreferences(mPrefs_Name, 0);
        mEmailShaPref = sharedPref.getString("UserEmail","");
        mPassShaPref =sharedPref.getString("UserPass","");

        //  Obtener el token
        new HttpAsyncTaskToken().execute("https://murmuring-anchorage-1614.herokuapp.com/tokens");

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

        // Locate the ListView in activity_obt_rutas.xml
        mList = (ListView) findViewById(R.id.listviewFavoritos);

        // Pass results to ListViewAdapter Class
        //mAdapter = new ListViewAdapter(this, mFavoritosArray);

        // Binds the Adapter to the ListView
        mList.setAdapter(mAdapter);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id){
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        //aqui se pasa al fragment supuestamente
                    }
                });

            }
        });
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

    ///////////////////////////////////////////////////////////////////
    //      Metodos y clase requerida para recuperar el token       //

    public String PostToken(String url, Usuario usuario){
        InputStream inputStream = null;
        String resultado = "";
        try{

            //Crear cliente
            HttpClient httpclient = new DefaultHttpClient();

            //Hacer el request para un POST a la url
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            //Construir el objeto json
            JSONObject jsonObject = new JSONObject();

            // se acumulan los campos necesarios, el primer parametro
            // es la etiqueta json que tendran los campos de la base
            jsonObject.accumulate("email", usuario.getEmail());
            jsonObject.accumulate("password", usuario.getEncrypted_password());


            // Convertir el objeto Json a String
            json = jsonObject.toString();

            // setear json al stringEntity
            StringEntity se = new StringEntity(json);

            // setear la Entity de httpPost
            httpPost.setEntity(se);


            // incluir los headers para que el Api sepa que es json
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // ejecutar el request de post en la url
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // recibir la respuesta como un inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convertir el inputStream a String si tiene valor null
            // quiere decir que el post no sirvio
            if(inputStream != null){
                resultado = convertInputStreamToString(inputStream);

            }else{
                resultado = "Error al guardar datos";

            }

        }catch (Exception e){
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return resultado;
    }


    private  class HttpAsyncTaskToken extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls){
            mUsuarioToken = new Usuario();
            mUsuarioToken.setEmail(mEmailShaPref);
            mUsuarioToken.setEncrypted_password(mPassShaPref);
            return PostToken(urls[0], mUsuarioToken);
        }

        /**
         * Despliega el resultado del post request
         * y guarda el token para hacer el get request luego
         */
        @Override
        protected void onPostExecute(String resultado){
            Toast.makeText(getBaseContext(), "Token Recuperado", Toast.LENGTH_LONG).show();
            //  Obtener los datos del usuario

            try{
                // una vez recibido el string con  el json
                //  se parsea sacando las variables id y token del usuario
                JSONObject usuarioToken = new JSONObject(resultado);
                mIdUser = usuarioToken.getInt("id");
                mTokenUsuario = usuarioToken.getString("token");

                //Url a la que el usuario tiene que pedir sus datos
                mUrlUsuario +=  Integer.toString(mIdUser);

            }catch(JSONException e){
                e.printStackTrace();
            }

            new HttpAsyncTaskGetFavoritos().execute(mUrlFavorito);
        }
    }

    ///////////////////////////////////////////////////////////////////
    //      Metodos y clase requerida para cargar datos del usuario  //
    /**
     * Metodo que hace un request al API con la url donde
     * se pregunta por los datos del usuario
     * @param url para obtener la cuenta del usuario
     * @return String con  el array Json
     */
    public  String GetFavoritos(String url){
        InputStream inputStream = null;
        String resultado = "";
        try {

            // Crear el cliente http
            HttpClient httpclient = new DefaultHttpClient();

            //Preparar el request y agregarle los header necesarios
            HttpGet request = new HttpGet(url);
            request.setHeader("Authorization",
                    "Token token=\""+mTokenUsuario + "\"");
            request.setHeader("Content-type", "application/json");

            // hacer el request get al API
            HttpResponse httpResponse = httpclient.execute(request);

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


    private class HttpAsyncTaskGetFavoritos extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GetFavoritos(urls[0]);
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

                //  cada i corresponderia a una diferente ruta favorita
                // se obtiene el objetoJson de esa posicion
                // y se le sacan los atributos que todos serian
                //  Strings. Se guarda una ruta en el arreglo de rutas
                for(int i = 0; i < favoritas.length(); i++){
                    Ruta favoritos = new Ruta();
                    favoritos.setId(favoritas.getJSONObject(i).getString("id"));
                    favoritos.setNombre(favoritas.getJSONObject(i).getString("nombre"));
                    favoritos.setFrecuencia(favoritas.getJSONObject(i).getString("frecuencia"));
                    favoritos.setPrecio(favoritas.getJSONObject(i).getString("precio"));
                    favoritos.setHorario(favoritas.getJSONObject(i).getString("horario"));
                    mFavoritosArray.add(favoritos);
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
