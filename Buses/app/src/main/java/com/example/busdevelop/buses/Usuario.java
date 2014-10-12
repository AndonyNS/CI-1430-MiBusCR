package com.example.busdevelop.buses;

import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * Clase que guardara los datos de un usuario
 * para poder enviar objetos JSON o recibirlos
 * y manejarlos de mejor manera
 */
public class Usuario {
    private String email;
    private String nombre;
    private String encrypted_password;
    private String fechaNac;
    private String ciudad;
    private int id;

    //Constructor por omision
    public Usuario(){

    }

    public Usuario(String email,String nombre, String password,
                   String fechaNac, String ciudad){
        this.email = email;
        this.nombre = nombre;
        this.encrypted_password = password;
        this.fechaNac = fechaNac;
        this.ciudad = ciudad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEncrypted_password() {
        return encrypted_password;
    }

    public String getFechaNac() {
        return fechaNac;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEncrypted_password(String encrypted_password) {
        this.encrypted_password = encrypted_password;
    }

    public void setFechaNac(String fechaNac) {
        this.fechaNac = fechaNac;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    /**
     * Covierte lo que recibe de la API a un string
     * @param inputStream
     * @return
     * @throws java.io.IOException
     */
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String linea = "";
        String resultado = "";
        while( (linea = bufferedReader.readLine()) != null){
            resultado += linea;
        }
        inputStream.close();
        return resultado;
    }


    public String getUsuario(String token, String url){
        InputStream inputStream = null;
        String resultado = "";
        try {

            // Crear el cliente http
            HttpClient httpclient = new DefaultHttpClient();

            //Preparar el request y agregarle los header necesarios
            HttpGet request = new HttpGet(url);
            request.setHeader("Authorization",
                    "Token token=\""+ token + "\"");
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

    public void parsearDatosUsuario(String resultado){

        try{

            //Parsear el json string a un json object para obtener los datos
            JSONObject datosUsuario = new JSONObject(resultado);

            //Parsear datos de jsonObject a la instancia de usuario
            this.setId(datosUsuario.getInt("id"));
            this.setEmail(datosUsuario.getString("email"));
            this.setEncrypted_password(datosUsuario.getString("password"));
            this.setNombre(datosUsuario.getString("nombre"));
            this.setFechaNac(datosUsuario.getString("fechaNac"));
            this.setCiudad(datosUsuario.getString("ciudad"));

        }catch(JSONException e){
            e.printStackTrace();
        }

    }

    public String actualizarUsuario(String token, String url){
        InputStream inputStream = null;
        String resultado = "";
        try{

            //Crear cliente
            HttpClient httpclient = new DefaultHttpClient();

            //Hacer el request para un POST a la url
            HttpPut httpPost = new HttpPut(url);

            String json = "";

            //Construir el objeto json
            JSONObject jsonObject = new JSONObject();

            // se acumulan los campos necesarios, el primer parametro
            // es la etiqueta json que tendran los campos de la base
            jsonObject.accumulate("email", this.getEmail());
            jsonObject.accumulate("password", this.getEncrypted_password());
            jsonObject.accumulate("nombre", this.getNombre());
            jsonObject.accumulate("fechaNac", this.getFechaNac());
            jsonObject.accumulate("ciudad", this.getCiudad());


            // Convertir el objeto Json a String
            json = jsonObject.toString();

            // setear json al stringEntity
            StringEntity se = new StringEntity(json);

            // setear la Entity de httpPost
            httpPost.setEntity(se);


            // incluir los headers para que el Api sepa que es json
            // y mandar el token
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("Authorization",
                    "Token token=\""+ token + "\"");


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

}