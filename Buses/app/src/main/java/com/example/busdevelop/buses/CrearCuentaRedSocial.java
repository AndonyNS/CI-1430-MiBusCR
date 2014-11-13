package com.example.busdevelop.buses;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

public class CrearCuentaRedSocial {
    Usuario mUsuario; //crear objeto que se manda por json
    String mEmailUsuario ,mNombreUsuario, mPasswordUsuario, mFechaNacUsuario, mCiudad;
    Context context;

    public CrearCuentaRedSocial(String emailUsuario, String nombreUsuario,
                                String passwordUsuario, String fechaNacUsuarioN, String ciudad, Context context) {
        this.mEmailUsuario = emailUsuario;
        this.mNombreUsuario = nombreUsuario;
        this.mPasswordUsuario = passwordUsuario;
        this.mFechaNacUsuario = fechaNacUsuarioN;
        this.mCiudad = ciudad;
        this.context = context;

    }

    public Usuario crearUsuario() {
        new HttpAsyncTask().execute("https://murmuring-anchorage-1614.herokuapp.com/users");
        return mUsuario;

    }

    /**
     * Recibe la url a la que hacer el Post y el usuario a
     * insertar en la base de datos para convertirlo en un
     * objeto json
     */
    public String Post(String url, Usuario usuario) {
        String resultado = ApiManager.httpPost(url, "b0936d7e239775e770ce002307f0acda", usuario);
        return resultado;
    }

    private void guardarUsuario(){
        SharedPreferences settings = context.getSharedPreferences("MyPrefsFile", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("UserEmail",mUsuario.getEmail());
        editor.putString("UserPass",mUsuario.getEncrypted_password());
        editor.putBoolean("SinRegistrar",false);
        editor.commit();
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
            mUsuario.setFechaNac(mFechaNacUsuario);
            mUsuario.setCiudad(mCiudad);
            guardarUsuario();

            return Post(urls[0],mUsuario);
        }

    }

}
