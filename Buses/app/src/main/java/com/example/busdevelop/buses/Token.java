package com.example.busdevelop.buses;

import android.util.Log;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

/**
 * Created by farroyo on 10/22/14.
 */
public class Token implements  ClassToRest{
    private String email;
    private String token;
    private String password;

    public Token(){
    }

    public Token(String email, String password){
        this.email = email;
        this.password = password;

    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setToken(String token) {
        this.token = token;
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
            jsonObject.accumulate("password", getPassword());

            // Convertir el objeto Json a String
            json = jsonObject.toString();

            // setear json al stringEntity
            se = new StringEntity(json);

        }catch (Exception e){
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return se;
    }
}
