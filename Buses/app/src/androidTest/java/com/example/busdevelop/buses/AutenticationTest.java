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
        String resultado = "";
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("https://murmuring-anchorage-1614.herokuapp.com/tokens");
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("email", "Api@MiBusCR.co.cr");
            jsonObject.accumulate("password", "?$jMEyp5P_9=E7L");
            json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            if(inputStream != null){
                BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
                String linea = "";
                while( (linea = bufferedReader.readLine()) != null){
                    resultado += linea;
                }
                inputStream.close();
            }else{
                fail("Error al jalar los datos");
                resultado = "Error al guardar datos";
            }
        }catch (Exception e){
            fail("Stream error");
            Log.d("InputStream", e.getLocalizedMessage());
        }
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
        String resultado = "";
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("https://murmuring-anchorage-1614.herokuapp.com/tokens");
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("email", "Api@MiBusCR.co.cr");
            jsonObject.accumulate("password", "aaa");
            json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            if(inputStream != null){
                BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
                String linea = "";
                while( (linea = bufferedReader.readLine()) != null){
                    resultado += linea;
                }
                inputStream.close();
            }else{
                fail("Error al jalar los datos");
                resultado = "Error al guardar datos";
            }
        }catch (Exception e){
            fail("Stream error");
            Log.d("InputStream", e.getLocalizedMessage());
        }
        assertEquals(resultado, " ");
    }

    public void testAutenticacionTokenIncorrecto()  {
        InputStream inputStream = null;
        String resultado = "";
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("https://murmuring-anchorage-1614.herokuapp.com/users/1");
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");
            httpGet.setHeader("Authorization",
                    "Token token=\"tokenincorrecto\"");
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
                fail("Error al jalar los datos");
                resultado = "Error al guardar datos";
            }
        }catch (Exception e){
            fail("Stream error");
            Log.d("InputStream", e.getLocalizedMessage());
        }
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
        String resultado = "";
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("https://murmuring-anchorage-1614.herokuapp.com/users/1");
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");
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
                fail("Error al jalar los datos");
                resultado = "Error al guardar datos";
            }
        }catch (Exception e){
            fail("Stream error");
            Log.d("InputStream", e.getLocalizedMessage());
        }
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
        String resultado = "";
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("https://murmuring-anchorage-1614.herokuapp.com/users/1");
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");
            httpGet.setHeader("Authorization",
                    "Token token=\"b0936d7e239775e770ce002307f0acda\"");
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
                fail("Error al jalar los datos");
                resultado = "Error al guardar datos";
            }
        }catch (Exception e){
            fail("Stream error");
            Log.d("InputStream", e.getLocalizedMessage());
        }
        try{
            JSONObject token = new JSONObject(resultado);
            assertEquals("1", token.getString("id"));
            assertEquals("Api@MiBusCR.co.cr", token.getString("email"));
            assertEquals("?$jMEyp5P_9=E7L", token.getString("password"));
            assertEquals("ApiManager", token.getString("nombre"));
            assertEquals("2014-10-04", token.getString("fechaNac"));
            assertEquals("san jose", token.getString("ciudad"));
        }catch(JSONException e){
            fail("JSONObject error");
            e.printStackTrace();
        }
    }
}
