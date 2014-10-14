package com.example.sendlocation;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    private final String mPrefs_Name = "MyPrefsFile";
    String currentLocation;
    long lastUpdate;
    long updateFrequency;
    serverUpdate server;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        lastUpdate = System.nanoTime();
        //Frecuencia de actualización, cada 3 minutos
        updateFrequency = min2nano(3);

        //Crea la instancia de firebase, es de tipo server, para que pueda ser usado con otros servidores en caso de que sea desee cambiar
        server = new firebaseUpdate(getApplicationContext());
        continueButtonListener();
        stopButtonListener();

        SharedPreferences settings = getSharedPreferences(mPrefs_Name, 0);
        checkSettings(settings);
        configureServiceManager();
	}

    /*Debido a que se trabaja con nanoTime, que devuelve nanosegundos, este método es para convertir
     minutos a nanosegundos*/
    public long min2nano(double minutes){
        long conv = (6 * (long)Math.pow(10,6));
        return (long)minutes * conv;
    }

    private void checkSettings(SharedPreferences settings){
        //Si no ha marcado que quiere dejar de recibir la notificación al principio, seguirá apareciendo cada vez que inicie
        if (settings.getBoolean("show_agreement", true)){
            View v = (View)findViewById(R.id.warningDialog);
            v.setVisibility(View.VISIBLE);
        }
    }

    private void configureServiceManager(){
        //Especifíca el locationManager y le indica que use tanto GPS como el proveedor de servicio (internet móvil)
        LocationManager mLocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener mLocListener = new MyLocationListener();
        mLocManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, mLocListener);
        mLocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mLocListener);
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

    public MyLocationListener(){
        currentLocation = null;
    }
	@Override
	public void onLocationChanged(Location loc)
	{
	  	/*String Text = "Mi ubicación actual es: " + "Latitud = " + loc.getLatitude() +
	  	" Longitud = " + loc.getLongitude();*/
		currentLocation = loc.getLatitude() + " " + loc.getLongitude();
        attemptUpdate();
	}

     public void attemptUpdate(){
         /* Si el tiempo actual menos la ultima actualización
         * es mayor que la frecuencia de actualización indicada, se actualiza
         */
         long currentTime = System.nanoTime();
         if(currentTime-lastUpdate> updateFrequency){
             server.update(currentLocation);
             Toast.makeText(getApplicationContext(), getResources().getString(R.string.update_message), Toast.LENGTH_SHORT).show();
             lastUpdate = System.nanoTime();
         }
     }

    //Si el internet está deshabilitado
	@Override
	public void onProviderDisabled(String provider){
        if(!isNetworkAvailable(getApplicationContext())){
            TextView t = (TextView)findViewById(R.id.textView1);
            t.setText(getResources().getString(R.string.activate));

        }
    }

    //Si se habilita
	@Override
	public void onProviderEnabled(String provider){
        TextView t = (TextView)findViewById(R.id.textView1);
        t.setText(getResources().getString(R.string.app_text));
    }

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras){}

    //Revisa que haya una red disponible (tenga los datos activos)
    public boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting())
            return true;

        return false;
    }

    } //End mylocationlistener
}