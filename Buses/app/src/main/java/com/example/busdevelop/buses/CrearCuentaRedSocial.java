package com.example.busdevelop.buses;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.io.InputStream;

public class CrearCuentaRedSocial {
    Usuario mUsuario; //crear objeto que se manda por json
    String mEmailUsuario ,mNombreUsuario, mPasswordUsuario, mFechaNacUsuario, mCiudad;
    Context context;

    public CrearCuentaRedSocial(String emailUsuario, String nombreUsuario,
                                String passwordUsuario, Context context) {
        this.mEmailUsuario = emailUsuario;
        this.mNombreUsuario = nombreUsuario;
        this.mPasswordUsuario = passwordUsuario;
        this.context = context;

    }

    public void crearUsuario() {
        new HttpAsyncTask().execute("https://murmuring-anchorage-1614.herokuapp.com/users");

    }

    /**
     * Recibe la url a la que hacer el Post y el usuario a
     * insertar en la base de datos para convertirlo en un
     * objeto json
     */
    public String Post(String url, Usuario usuario) {
        InputStream inputStream = null;
        String resultado = ApiManager.httpPost(url, "b0936d7e239775e770ce002307f0acda", usuario);
        return resultado;
    }

    /**
     * Clase que ayuda a crear la conexion en un tread separado para que
     * no se congele la UI
     */
    private  class HttpAsyncTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls){
            mUsuario = new Usuario();
            mUsuario.setEmail(mEmailUsuario);
            mUsuario.setNombre(mNombreUsuario);
            mUsuario.setEncrypted_password(mPasswordUsuario);
            mUsuario.setFechaNac("");
            mUsuario.setCiudad("");
/*            if(mFechaNacUsuario != null) {
                mUsuario.setFechaNac(mFechaNacUsuario);
            } else {
                mUsuario.setFechaNac("");
            }

            if(mCiudad != null) {
                mUsuario.setCiudad(mCiudad);
            }  else {
                mUsuario.setCiudad("");
            }
*/
            return Post(urls[0],mUsuario);
        }

        @Override
        protected void onPostExecute(String resultado){
            guardarUsuario();

        }

    }

    private void guardarUsuario(){
        SharedPreferences settings = context.getSharedPreferences("MyPrefsFile", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("UserEmail",mUsuario.getEmail());
        editor.putString("UserPass",mUsuario.getEncrypted_password());
        editor.putBoolean("SinRegistrar",false);
        editor.commit();
    }

}
