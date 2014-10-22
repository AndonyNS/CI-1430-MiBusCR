package com.example.busdevelop.buses;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by farroyo on 10/20/14.
 */
public class ApiManager {
    public static String httpGet(String url, String token){
        InputStream inputStream = null;
        String resultado = "";
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");
            httpGet.setHeader("Authorization",
                    "Token token=\""+ token +"\"");
            HttpResponse httpResponse = httpclient.execute(httpGet);
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
