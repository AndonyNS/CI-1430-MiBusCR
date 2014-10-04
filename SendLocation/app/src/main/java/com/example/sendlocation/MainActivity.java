package com.example.sendlocation;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.firebase.client.Firebase;


public class MainActivity extends Activity {

    private final String mPrefs_Name = "MyPrefsFile";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        SharedPreferences settings = getSharedPreferences(mPrefs_Name, 0);
        continueButtonListener();

        //Si no ha marcado que quiere dejar de recibir la notificación al principio, seguirá apareciendo cada vez que inicie
        if (settings.getBoolean("show_agreement", true)){
            View v = (View)findViewById(R.id.warningDialog);
            v.setVisibility(View.VISIBLE);
        }

        //Especifíca el locationManager y le indica que use tanto GPS como el proveedor de servicio (internet móvil)
		LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		LocationListener mlocListener = new MyLocationListener();
		mlocManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);
		mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

		stopButtonListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    /*Termina la aplicación al darle el botón de cerrar. Aunque no es lo ideal para android, se realiza
    * debido a que la aplicación va a estar constantemente actualizada si no se cierra, entonces
    * por seguridad se agrega este botón */
	private void stopButtonListener(){
    	Button button = (Button) findViewById(R.id.stopButton);
    	 
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Installation.end();
				finish();
			}
		});
    }

    //Si selecciona no mostrar de nuevo, guarda el SharedPreference
    public void itemClicked(View v){
        if(findViewById(R.id.checkBox)==v){
            CheckBox cb = (CheckBox)v;
            if(cb.isChecked()){
                SharedPreferences settings = getSharedPreferences(mPrefs_Name, 0);
                settings.edit().putBoolean("show_agreement", false).commit();
            }
        }
    }

    private void continueButtonListener(){
        Button button = (Button) findViewById(R.id.continueButton);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                View v = (View)findViewById(R.id.warningDialog);
                v.setVisibility(View.INVISIBLE);
            }
        });
    }
	
    
	public class MyLocationListener implements LocationListener
	{
    //Este es el número único generado por cada dispositivo android
	String UUID = Installation.id(getApplicationContext());
    
    //Llama (o crea) la instancia de Firebase para el dispositivo
	Firebase firebaseRef = new Firebase("https://blazing-fire-9075.firebaseio.com/Device-"+UUID);
	
	@Override
	public void onLocationChanged(Location loc)
	{	
	  	/*String Text = "Mi ubicación actual es: " + "Latitud = " + loc.getLatitude() +
	  	" Longitud = " + loc.getLongitude();*/

		Toast.makeText(getApplicationContext(),"Actualizando ubicación",Toast.LENGTH_SHORT).show();

		String location = loc.getLatitude() + " " + loc.getLongitude();
		//firebaseRef.setValue(location);
        //Pone en firebase el id del teléfono con su ubicación actual, que se llama con el onLocationChanged
		firebaseRef.child("GpsID").setValue(UUID);
		firebaseRef.child("Location").setValue(location);

	}

	@Override
	public void onProviderDisabled(String provider)
	{
		/*if(provider==LocationManager.NETWORK_PROVIDER){
			TextView t = (TextView)findViewById(R.id.textView1);
			t.setText("Favor active \ntráfico de datos \npoder compartir su ubicación");
		}
		if(provider==LocationManager.GPS_PROVIDER){
			Toast.makeText( getApplicationContext(), "GPS Desactivado",Toast.LENGTH_SHORT ).show();
		}*/
	
	}

	@Override
	public void onProviderEnabled(String provider)
	{
		/*if(provider==LocationManager.NETWORK_PROVIDER){
			TextView t = (TextView)findViewById(R.id.textView1);
			t.setText("Enviando \nubicaci�n a \nServidor");
			Toast.makeText( getApplicationContext(),"WIFI Activado",Toast.LENGTH_SHORT).show();
		}
		if(provider==LocationManager.GPS_PROVIDER){
			Toast.makeText( getApplicationContext(), "GPS Activado",Toast.LENGTH_SHORT ).show();
		}*/
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras){}

	} //End mylocationlistener
}