package com.example.busdevelop.buses;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class CrearCuentaActivity extends ActionBarActivity {
    Usuario mUsuario; //crear objeto que se manda por json
    EditText mEmailUsuario,mNombreUsuario,mPasswordUsuarioN,mFechaNacUsuarioN,mCiudad,
            mConfPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        //obtener referencias a las vistas
        mEmailUsuario = (EditText) findViewById(R.id.emailUsuario);
        mNombreUsuario = (EditText) findViewById(R.id.nombreUsuario);
        mPasswordUsuarioN = (EditText) findViewById(R.id.passwordUsuarioN);
        mConfPass = (EditText) findViewById(R.id.confPass);
        mFechaNacUsuarioN = (EditText) findViewById(R.id.fechaNacUsuarioN);
        mCiudad = (EditText) findViewById(R.id.ciudad);

        if(getIntent()!=null){
            String givenEmail = (String) getIntent().getStringExtra("emailIngresado");
            mEmailUsuario.setText(givenEmail);
        }



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
        String resultado = ApiManager.httpPost(url, "b0936d7e239775e770ce002307f0acda", usuario);
        return resultado;
    }

    /**
     * Metodo llamado por el boton crear cuenta
     */
    public void registrar(View vista){
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
            new HttpAsyncTask(this).execute("https://murmuring-anchorage-1614.herokuapp.com/users");
        }

    }

    /**
     * Clase que ayuda a crear la conexion en un tread separado para que
     * no se congele la UI
     */
    private  class HttpAsyncTask extends AsyncTask<String, Void, String>{
        Activity mActivity;
        private HttpAsyncTask(Activity activity){
            this.mActivity = activity;
        }

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
         * y guarda en el Shared Preferences el email de la cuenta creada
         */
        @Override
        protected void onPostExecute(String resultado){
            guardarUsuario();

            Toast.makeText(getBaseContext(), "Cuenta Creada", Toast.LENGTH_LONG).show();
            mActivity.startActivity(new Intent(mActivity,MainActivity.class));

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

    /**
     * Metodo que valida que el usuario digito los campos requeridos
     * para crear la cuenta.
     */
    private boolean validCamposRequeridos(){
        if(mEmailUsuario.getText().toString().trim().equals("") ||
                mNombreUsuario.getText().toString().trim().equals("") ||
                mPasswordUsuarioN.getText().toString().trim().equals("")){
            return false;
        }
        return true;
    }

    /**
     * Metodo que valida que la contraseña y la confirmación de
     * contraseña sean iguales
     */
    private boolean validarPass(){
        String pass = mPasswordUsuarioN.getText().toString().trim();
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
        editor.putString("UserEmail",mUsuario.getEmail());
        editor.putString("UserPass",mUsuario.getEncrypted_password());
        editor.putBoolean("SinRegistrar",false);
        editor.commit();
    }

}