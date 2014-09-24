package com.example.busdevelop.buses;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class SingleItemView extends Activity {
    // Declare Variables
    TextView txtnombre;
    TextView txtfrecuencia;
    TextView txtprecio;
    TextView txthorario;
    String nombre;
    String frecuencia;
    String precio;
    String horario;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_item_view);
        // Hide the keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        // Retrieve data from MainActivity on item click event
        Intent i = getIntent();
        // Get the results of rank
        nombre = i.getStringExtra("nombre");
        // Get the results of country
        frecuencia = i.getStringExtra("frecuencia");
        // Get the results of population
        precio = i.getStringExtra("precio");
        horario = i.getStringExtra("horario");

        // Locate the TextViews in singleitemview.xml
        txtnombre = (TextView) findViewById(R.id.nombreR);
        txtfrecuencia = (TextView) findViewById(R.id.frecuenciaR);
        txtprecio = (TextView) findViewById(R.id.precioR);
        txthorario = (TextView) findViewById(R.id.horarioR);

        // Load the results into the TextViews
        txtnombre.setText(nombre);
        txtfrecuencia.setText(frecuencia);
        txtprecio.setText(precio);
        txthorario.setText(horario);
    }
}