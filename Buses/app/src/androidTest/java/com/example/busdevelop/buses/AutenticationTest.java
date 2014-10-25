package com.example.busdevelop.buses;


import android.test.ActivityTestCase;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Manuel Arroyo on 10/5/14.
 */
public class AutenticationTest extends ActivityTestCase {



    public void testObtenerToken()  {
        InputStream inputStream = null;
        String resultado = ApiManager.httpPost("https://murmuring-anchorage-1614.herokuapp.com/tokens","",new Token("Api@MiBusCR.co.cr","?$jMEyp5P_9=E7L"));
        try{
            JSONObject token = new JSONObject(resultado);
            assertEquals("b0936d7e239775e770ce002307f0acda", token.getString("token"));
            assertEquals("1", token.getString("id"));
        }catch(JSONException e){
            fail("JSONObject error");
            e.printStackTrace();
        }
    }

    public void testObtenerTokenPasswordIncorrecto()  {
        InputStream inputStream = null;
        String resultado = ApiManager.httpPost("https://murmuring-anchorage-1614.herokuapp.com/tokens","",new Token("Api@MiBusCR.co.cr","aaa"));
        assertEquals(resultado, " ");
    }

    public void testAutenticacionTokenIncorrecto()  {
        InputStream inputStream = null;
        String resultado = ApiManager.httpGet("https://murmuring-anchorage-1614.herokuapp.com/users/1","tokenincorrecto");
        try{
            JSONObject token = new JSONObject(resultado);
            assertEquals("Invalid API Token", token.getString("message"));
        }catch(JSONException e){
            fail("JSONObject error");
            e.printStackTrace();
        }
    }

    public void testAutenticacionSinToken()  {
        InputStream inputStream = null;
        String resultado = ApiManager.httpGet("https://murmuring-anchorage-1614.herokuapp.com/users/1","");
        try{
            JSONObject token = new JSONObject(resultado);
            assertEquals("Invalid API Token", token.getString("message"));
        }catch(JSONException e){
            fail("JSONObject error");
            e.printStackTrace();
        }
    }


    public void testAutenticacionToken()  {
        InputStream inputStream = null;
        String resultado = ApiManager.httpGet("https://murmuring-anchorage-1614.herokuapp.com/users/1","b0936d7e239775e770ce002307f0acda");
        try{
            JSONObject token = new JSONObject(resultado);
            assertEquals("1", token.getString("id"));
            assertEquals("Api@MiBusCR.co.cr", token.getString("email"));
            assertEquals("ApiManager", token.getString("nombre"));
            assertEquals("2014-10-04", token.getString("fechaNac"));
            assertEquals("san jose", token.getString("ciudad"));
        }catch(JSONException e){
            fail("JSONObject error");
            e.printStackTrace();
        }
    }
}
