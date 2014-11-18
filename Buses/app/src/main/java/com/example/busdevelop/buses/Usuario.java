package com.example.busdevelop.buses;

import android.util.Log;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *
 * Clase que guardara los datos de un usuario
 * para poder enviar objetos JSON o recibirlos
 * y manejarlos de mejor manera
 */
public class Usuario implements ClassToRest {
    private String nombre;
    private String fechaNac;
    private String ciudad;
    private Token token;
    private int id;


    //Constructor por omision
    public Usuario() {
        token = new Token();
    }

    public Usuario(String email, String nombre, String password,
                   String fechaNac, String ciudad) {
        token = new Token(email,password);
        this.nombre = nombre;
        this.fechaNac = fechaNac;
        this.ciudad = ciudad;
        this.id = -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
            return token.getToken();
    }

    public Token getClassToken() {
        return token;
    }

    public void setToken(String token) {
        this.token.setToken(token);
    }

    public String getEncrypted_password() {
        return token.getPassword();
    }

    public String getFechaNac() {
        return fechaNac;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return token.getEmail();
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setEmail(String email) {
        token.setEmail(email);
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEncrypted_password(String encrypted_password) {
        token.setPassword(encrypted_password);
    }

    public void setFechaNac(String fechaNac) {
        this.fechaNac = fechaNac;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }


    /**
     * Metodo que hace un get request al api para obtener el string
     * con el json de los datos de la cuenta del usuario
     *
     * @param token
     * @param url
     * @return
     */
    public String getUsuario(String token, String url) {
        String resultado = ApiManager.httpGet(url, token);
        return resultado;
    }

    /**
     * Metodo que recibe el string retornado por un request hecho al API
     * y lo parsea a los atributos de usuario
     *
     * @param resultado
     */
    public void parsearDatosUsuario(String resultado) {

        try {

            //Parsear el json string a un json object para obtener los datos
            JSONObject datosUsuario = new JSONObject(resultado);

            //Parsear datos de jsonObject a la instancia de usuario
            this.setId(datosUsuario.getInt("id"));
            this.setEmail(datosUsuario.getString("email"));
            this.setEncrypted_password(datosUsuario.getString("password"));
            this.setNombre(datosUsuario.getString("nombre"));
            this.setFechaNac(datosUsuario.getString("fechaNac"));
            this.setCiudad(datosUsuario.getString("ciudad"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Metodo que hace un put request para actualizar datos del usuario
     *
     * @param token
     * @param url
     * @return
     */
    public String actualizarUsuario(String token, String url) {
        String resultado = ApiManager.httpPut(url,token,this);
        return resultado;
    }

    /**
     * Metodo para hacer un post al API y obtener un token
     *
     * @return
     */
    public String obtenerToken() {
        String urlToken = "https://murmuring-anchorage-1614.herokuapp.com/tokens";
        String resultado = ApiManager.httpPost(urlToken,"",token);
        this.guardarTokenId(resultado);
        return this.getToken();
    }


    /**
     * Metodo que recibe la hilera json del post request de un token
     * y lo parsea, si despues del parse el id es -1 o el token
     * es "" entonces quiere decir que el usuario no existe
     *
     * @param resultado
     */
    public void guardarTokenId(String resultado) {
        try {
            // una vez recibido el string con  el json
            //  se parsea sacando las variables id y token del usuario
            JSONObject usuarioToken = new JSONObject(resultado);
            this.setId(usuarioToken.getInt("id"));
            this.setToken(usuarioToken.getString("token"));

            //Url a la que el usuario tiene que pedir sus datos
            //mUrlUsuario +=  Integer.toString(mIdUser);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public StringEntity JsonAcumulator() {
        StringEntity se = null;
        try {
            String json = "";

            //Construir el objeto json
            JSONObject jsonObject = new JSONObject();

            // se acumulan los campos necesarios, el primer parametro
            // es la etiqueta json que tendran los campos de la base
            jsonObject.accumulate("email", getEmail());
            jsonObject.accumulate("password", getEncrypted_password());
            jsonObject.accumulate("nombre", getNombre());
            jsonObject.accumulate("fechaNac", getFechaNac());
            jsonObject.accumulate("ciudad", getCiudad());

            // Convertir el objeto Json a String
            json = jsonObject.toString();

            // setear json al stringEntity
            se = new StringEntity(json);

        }catch (Exception e){
            Log.d("String to json error", e.getLocalizedMessage());
        }
        return se;
    }
}