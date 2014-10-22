package com.example.busdevelop.buses;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by farroyo on 10/20/14.
 */
public class ApiManager {
    public static String httpGet(String url, String token){
        String resultado = "";
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-type", "application/json");
        httpGet.setHeader("Authorization",
                "Token token=\""+ token +"\"");
        HttpResponse httpResponse = null;
        try{
            // ejecutar el request de post en la url
            httpResponse = httpclient.execute(httpGet);
        }catch (Exception e){
            Log.d("InputStream", e.getLocalizedMessage());
        }
        resultado = StreamToString(httpResponse);
        return resultado;
    }

    public static String httpPost(String url, String token, ClassToRest objeto){
        String resultado = "";

        //Crear cliente
        HttpClient httpclient = new DefaultHttpClient();

        //Hacer el request para un POST a la url
        HttpPost httpPost = new HttpPost(url);

        // setear la Entity de httpPost
        httpPost.setEntity(objeto.JsonAcumulator());

        //  incluir header con el token del usuario general
        //  para crear usuario de manera segura
        httpPost.setHeader("Authorization",
                "Token token=\""+ token +"\"");

        // incluir los headers para que el Api sepa que es json
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        HttpResponse httpResponse = null;
        try{
            // ejecutar el request de post en la url
             httpResponse = httpclient.execute(httpPost);
        }catch (Exception e){
            Log.d("InputStream", e.getLocalizedMessage());
        }
        resultado = StreamToString(httpResponse);
        return resultado;
    }

    public static String httpPut(String url, String token, ClassToRest objeto){
        String resultado = "";

        //Crear cliente
        HttpClient httpclient = new DefaultHttpClient();

        //Hacer el request para un POST a la url
        HttpPut httpPut = new HttpPut(url);

        // setear la Entity de httpPost
        httpPut.setEntity(objeto.JsonAcumulator());

        //  incluir header con el token del usuario general
        //  para crear usuario de manera segura
        httpPut.setHeader("Authorization",
                "Token token=\""+ token +"\"");

        // incluir los headers para que el Api sepa que es json
        httpPut.setHeader("Accept", "application/json");
        httpPut.setHeader("Content-type", "application/json");
        HttpResponse httpResponse = null;
        try{
            // ejecutar el request de post en la url
            httpResponse = httpclient.execute(httpPut);
        }catch (Exception e){
            Log.d("InputStream", e.getLocalizedMessage());
        }
        resultado = StreamToString(httpResponse);
        return resultado;
    }


    private static String StreamToString(HttpResponse httpResponse){
        InputStream inputStream = null;
        String resultado="";
        try{
            inputStream = httpResponse.getEntity().getContent();
            if(inputStream != null){
                BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
                String linea = "";
                while( (linea = bufferedReader.readLine()) != null){
                    resultado += linea;
                }
                inputStream.close();
            }else{
                resultado = "Error al guardar datos";
            }
        }catch (Exception e){
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return resultado;
    }
}


