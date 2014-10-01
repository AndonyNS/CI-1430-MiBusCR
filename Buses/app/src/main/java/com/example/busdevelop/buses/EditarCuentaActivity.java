package com.example.busdevelop.buses;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Esteban on 30/09/2014.
 * Actividad con el formulario para editar una cuenta
 */
public class EditarCuentaActivity extends ActionBarActivity {
    EditText mEmail, mNombre ,mPassword, mFechaNac, mCiudad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_cuenta);

        //obtener referencias a las vistas
        mEmail = (EditText) findViewById(R.id.emailUsuarioEdit);
        mNombre = (EditText) findViewById(R.id.nombreUsuarioEdit);
        mPassword = (EditText) findViewById(R.id.passwordUsuarioEdit);
        mFechaNac = (EditText) findViewById(R.id.fechaNacUsuarioEdit);
        mCiudad = (EditText) findViewById(R.id.ciudadEdit);

        if(getIntent()!=null){
            String givenEmail = getIntent().getStringExtra("emailIngresado");
            mEmail.setText(givenEmail);
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
     * Metodo llamado por el boton crear cuenta
     */
    public void actualizar(View vista){
        //TODO: Validar campos
        //new HttpAsyncTask().execute("https://murmuring-anchorage-1614.herokuapp.com/users");

    }

}
