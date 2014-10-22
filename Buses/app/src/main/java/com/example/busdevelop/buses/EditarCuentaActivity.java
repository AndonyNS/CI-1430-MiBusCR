package com.example.busdevelop.buses;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 * Actividad con el formulario para editar una cuenta
 */
public class EditarCuentaActivity extends ActionBarActivity {
    EditText mEmail, mNombre ,mPassword, mFechaNac, mCiudad, mConfPass;
    Usuario mUsuarioToken;
    Usuario mUsuarioObtenido;
    Usuario mUsuarioActualizado;
    String mUrlUsuario = "https://murmuring-anchorage-1614.herokuapp.com/users/";
    String mTokenUsuario = "";
    int mIdUser;
    String mEmailShaPref = "";
    String mPassShaPref = "";
    private final String mPrefs_Name = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_cuenta);

        //obtener referencias a las vistas
        mEmail = (EditText) findViewById(R.id.emailUsuarioEdit);
        mNombre = (EditText) findViewById(R.id.nombreUsuarioEdit);
        mPassword = (EditText) findViewById(R.id.passwordUsuarioEdit);
        mConfPass = (EditText) findViewById(R.id.confPassEdit);
        mFechaNac = (EditText) findViewById(R.id.fechaNacUsuarioEdit);
        mCiudad = (EditText) findViewById(R.id.ciudadEdit);

        if(getIntent()!=null){
            String givenEmail = getIntent().getStringExtra("emailIngresado");
            mEmail.setText(givenEmail);
        }

        // obtener del shared preferences el email
        // y el password
        SharedPreferences sharedPref = getSharedPreferences(mPrefs_Name, 0);
        mEmailShaPref = sharedPref.getString("UserEmail","");
        mPassShaPref =sharedPref.getString("UserPass","");

        mEmail.setText(mEmailShaPref);
        mPassword.setText(mPassShaPref);
        //  Obtener el token
        new HttpAsyncTaskToken().execute("https://murmuring-anchorage-1614.herokuapp.com/tokens");


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
     * Metodo llamado por el boton actualizar cuenta
     */
    public void actualizar(View vista){
        boolean ejecutarPost = true;
        if(!validCamposRequeridos()){
            ejecutarPost = false;
            Toast.makeText(getBaseContext(), "Llene los campos email, nombre y contraseña",
                    Toast.LENGTH_LONG).show();
        }

        if(!validarPass()){
            ejecutarPost = false;
            Toast.makeText(getBaseContext(), "Las contraseñas no coinciden",
                    Toast.LENGTH_LONG).show();
        }

        //si los datos en el formulario estan bien crea la cuenta
        if(ejecutarPost){
            new HttpAsyncTaskPutUsuario(this).execute(mUrlUsuario);
        }

    }

    ///////////////////////////////////////////////////////////////////
    //      Metodos y clase requerida para recuperar el token       //

    public String PostToken(String url, Usuario usuario){
        String resultado = ApiManager.httpPost(url,"",usuario.getClassToken());
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

            new HttpAsyncTaskGetUsuario().execute(mUrlUsuario);
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
    public  String GetUsuario(String url){
        String resultado = ApiManager.httpGet(url,mTokenUsuario);
        return resultado;
    }

    private class HttpAsyncTaskGetUsuario extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GetUsuario(urls[0]);
        }

        /**
         * metodo que se ejecuta después de obtener la respuesta
         * al request get, llenando el formulario con los datos de la cuenta
         * del usuario
         * @param resultado
         */
        @Override
        protected void onPostExecute(String resultado) {
            Toast.makeText(getBaseContext(), "Datos de usuario obtenidos", Toast.LENGTH_LONG).show();
            //mCiudad.setText(resultado);

            try{

                //Parsear el json string a un json object para obtener los datos
                JSONObject datosUsuario = new JSONObject(resultado);
                mUsuarioObtenido = new Usuario();

                //Parsear datos de jsonObject a la instancia de usuario
                mUsuarioObtenido .setEmail(datosUsuario.getString("email"));
                //mUsuarioObtenido .setEncrypted_password(datosUsuario.getString("password"));
                mUsuarioObtenido .setNombre(datosUsuario.getString("nombre"));
                mUsuarioObtenido .setFechaNac(datosUsuario.getString("fechaNac"));
                mUsuarioObtenido .setCiudad(datosUsuario.getString("ciudad"));

                //Mostrar los datos en el formulario
                mNombre.setText(mUsuarioObtenido.getNombre());
                mFechaNac.setText(mUsuarioObtenido.getFechaNac());
                mCiudad.setText(mUsuarioObtenido.getCiudad());


            }catch(JSONException e){
                e.printStackTrace();
            }


        }
    }




    ///////////////////////////////////////////////////////////////////
    //      Metodos y clase requerida para actualizar usuario      //

    public String PutUsuario(String url, Usuario usuario){
        String resultado = ApiManager.httpPut(url,mTokenUsuario,usuario);
        return resultado;
    }

    private  class HttpAsyncTaskPutUsuario extends AsyncTask<String, Void, String> {
        Activity mActivity;
        private HttpAsyncTaskPutUsuario(Activity activity){
            this.mActivity = activity;
        }

        @Override
        protected String doInBackground(String... urls){
            //Crear usuario con los nuevos datos
            mUsuarioActualizado = new Usuario();
            mUsuarioActualizado.setEmail(mEmail.getText().toString());
            mUsuarioActualizado.setNombre(mNombre.getText().toString());
            mUsuarioActualizado.setEncrypted_password(mPassword.getText().toString());
            mUsuarioActualizado.setFechaNac(mFechaNac.getText().toString());
            mUsuarioActualizado.setCiudad(mCiudad.getText().toString());

            return PutUsuario(urls[0], mUsuarioActualizado);
        }

        /**
         * Despliega el resultado del post request
         * y guarda el token para hacer el get request luego
         */
        @Override
        protected void onPostExecute(String resultado){
            guardarUsuario();
            Toast.makeText(getBaseContext(), "Cuenta actualizada", Toast.LENGTH_LONG).show();
            mActivity.startActivity(new Intent(mActivity,MainActivity.class));
        }
    }

    /**
     * Metodo que valida que el usuario digito los campos requeridos
     * para crear la cuenta.
     */
    private boolean validCamposRequeridos(){
        if(mEmail.getText().toString().trim().equals("") ||
                mNombre.getText().toString().trim().equals("") ||
                mPassword.getText().toString().trim().equals("")){
            return false;
        }
        return true;
    }

    /**
     * Metodo que valida que la contraseña y la confirmación de
     * contraseña sean iguales
     */
    private boolean validarPass(){
        String pass = mPassword.getText().toString().trim();
        String confirmarPass = mConfPass.getText().toString().trim();
        if(pass.equals(confirmarPass)){
            return true;
        }
        return false;
    }

    /**
     * Guarda el email del usuario en shared preferences
     * para saber que ya esta logueado
     */
    private void guardarUsuario(){
        SharedPreferences settings = getSharedPreferences("MyPrefsFile", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("UserEmail",mUsuarioActualizado.getEmail());
        editor.putString("UserPass",mUsuarioActualizado.getEncrypted_password());
        editor.putBoolean("SinRegistrar",false);
        editor.commit();
    }
}
