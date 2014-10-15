package com.example.busdevelop.buses;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Manuel on 10/15/14.
 */
public class UnderConstructionActivity extends ActionBarActivity {
    private Usuario mUsuario;
    private final String mPrefs_Name = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_under_contruction);

        // obtener del shared preferences el email
        // y el password
        mUsuario = new Usuario();
        SharedPreferences sharedPref = getSharedPreferences(mPrefs_Name, 0);
        mUsuario.setEmail(sharedPref.getString("UserEmail", ""));
        mUsuario.setEncrypted_password(sharedPref.getString("UserPass", ""));
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
}
