package com.example.busdevelop.buses;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import  java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;


public class CrearCuentaActivity extends ActionBarActivity {
    Usuario mUsuario; //crear objeto que se manda por json
    EditText mEmailUsuario,mNombreUsuario,mPasswordUsuarioN,mFechaNacUsuarioN,mCiudad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        //obtener referencias a las vistas
        mEmailUsuario = (EditText) findViewById(R.id.emailUsuario);
        mNombreUsuario = (EditText) findViewById(R.id.nombreUsuario);
        mPasswordUsuarioN = (EditText) findViewById(R.id.passwordUsuarioN);
        mFechaNacUsuarioN = (EditText) findViewById(R.id.fechaNacUsuarioN);
        mCiudad = (EditText) findViewById(R.id.ciudad);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.crear_cuenta, menu);

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
     * Recibe la url a la que hacer el Post y el usuario a
     * insertar en la base de datos para convertirlo en un
     * objeto json
     */
    public String Post(String url, Usuario usuario){
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
            jsonObject.accumulate("nombre", usuario.getNombre());
            jsonObject.accumulate("fechaNac", usuario.getFechaNac());
            jsonObject.accumulate("ciudad", usuario.getCiudad());

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

    /**
     * Metodo llamado por el boton crear cuenta
     */
    public void registrar(View vista){
        //TODO: Validar campos
        new HttpAsyncTask().execute("http://murmuring-anchorage-1614.herokuapp.com/users");
    }

    /**
     * Clase que ayuda a crear la conexion en un tread separado para que
     * no se congele la UI
     */
    private  class HttpAsyncTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls){
            mUsuario = new Usuario();
            mUsuario.setEmail(mEmailUsuario.getText().toString());
            mUsuario.setNombre(mNombreUsuario.getText().toString());
            mUsuario.setEncrypted_password(mPasswordUsuarioN.getText().toString());
            mUsuario.setFechaNac(mFechaNacUsuarioN.getText().toString());
            mUsuario.setCiudad(mCiudad.getText().toString());

            return Post(urls[0],mUsuario);
        }

        /**
         * Despliega el resultado del post request
         */
        @Override
        protected void onPostExecute(String resultado){
            Toast.makeText(getBaseContext(), "Cuenta Creada", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Covierte lo que recibe de la API a un string
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String linea = "";
        String resultado = "";
        while( (linea = bufferedReader.readLine()) != null){
            resultado += linea;
        }
        inputStream.close();
        return resultado;
    }


}