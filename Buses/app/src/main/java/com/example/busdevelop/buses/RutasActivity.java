package com.example.busdevelop.buses;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class RutasActivity extends ActionBarActivity implements LocationListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LocationManager mLocationManager;
    private ProgressDialog mpd;
    private Marker mMarcadorPosicion = null;

    private static final String mFIREBASE_URL = "https://blazing-fire-9075.firebaseio.com/";
    private String mGps;
    private String mLocation;
    private double mLatitud;
    private double mLongitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutas);

        setUpMapIfNeeded();
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mpd = ProgressDialog
                .show(this, "Localización", "Esperando localización");
        // Recordatorio: en este metodo indicamos la frecuencia con la
        // que obtenemos una nueva posicion.
        // En este caso cada 5 segundos y que me haya desplazado 20 metros
        // obtendra una nueva localizacion
        mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 20, this);
        //Agrega que no solo espere GPS para obtener ubicación, funciona tambien con la red (menor precisión)
        mLocationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 0, 0, this);

        getLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        Intent intent = new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);

    }

    public void getLocation(){

        Firebase firebaseRef = new Firebase(mFIREBASE_URL);

        firebaseRef.addChildEventListener(new ChildEventListener(){

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName){

                Location location = new Location("dummyprovider");

                mGps = (String) snapshot.child("GpsID").getValue();
                mLocation = (String) snapshot.child("Location").getValue();
                String[] parts = mLocation.split(" ");
                mLatitud = Double.parseDouble(parts[0]);
                mLongitud = Double.parseDouble(parts[1]);
                location.setLatitude(mLatitud);
                location.setLongitude(mLongitud);
                onLocationChanged(location);

            }

            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName){}

            @Override
            public void onChildRemoved(DataSnapshot snapshot){}

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName){}

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }

        });



    }

    @Override
    public void onLocationChanged(Location posicion) {
        LatLng latitudLongitud = new LatLng(posicion.getLatitude(),
                posicion.getLongitude());

        // Desaparecer del interfaz de usuario el ProgressDialog
        mpd.dismiss();

        if (mMarcadorPosicion == null) {
            mMarcadorPosicion = mMap.addMarker(new MarkerOptions()
                    .position(latitudLongitud)
                    .title("Mi ubicación")
                    .snippet(
                            "lat:" + posicion.getLatitude() + ",lon:"
                                    + posicion.getLongitude())
                    .draggable(true)
                            // .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.logo)));

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latitudLongitud,
                    13));
        } else {
            mMarcadorPosicion.setPosition(latitudLongitud);

            mMap.animateCamera(CameraUpdateFactory.newLatLng(latitudLongitud));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.rutas, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.rutas, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.MnuOpc1:
                Toast.makeText(this, "You have chosen the " + getResources().getString(R.string.ruta_alajuela_ucr) + " menu option",
                        Toast.LENGTH_SHORT).show();
                return true;
            case R.id.MnuOpc2:
                Toast.makeText(this, "You have chosen the " + getResources().getString(R.string.ruta_pavas_ucr) + " menu option",
                        Toast.LENGTH_SHORT).show();
                return true;
            case R.id.MnuOpc3:
                Toast.makeText(this, "You have chosen the " + getResources().getString(R.string.ruta_tibas_ucr) + " menu option",
                        Toast.LENGTH_SHORT).show();
            case R.id.action_search:
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
