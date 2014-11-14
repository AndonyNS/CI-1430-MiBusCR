package com.example.busdevelop.buses;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MiUbicacionActivity extends ActionBarActivity implements LocationListener{
    GoogleMap mMapa;
    private LocationManager mLocationManager;
    private Marker mMarcadorUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_ubicacion);

        try {
            if (mMapa == null) {
                // soportado en ultimas apis
                mMapa = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.mapUbicacion)).getMap();
            }

               // mMapa.setMyLocationEnabled(true);
                mMapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMapa.getUiSettings().setZoomGesturesEnabled(true);

            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 5000, 20, this);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Mapa", "exception", e);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mi_ubicacion, menu);
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

    @Override
    public void onLocationChanged(Location posicion) {
        LatLng latitudLongitud = new LatLng(posicion.getLatitude(),
                posicion.getLongitude());

        if (mMarcadorUpdate == null) {
            mMarcadorUpdate = mMapa.addMarker(new MarkerOptions()
                    .position(latitudLongitud)
                    .title("Ubicación")
                    .snippet(
                            "lat:" + posicion.getLatitude() + ",lon:"
                                    + posicion.getLongitude())
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.persona1)));

            mMapa.animateCamera(CameraUpdateFactory.newLatLngZoom(latitudLongitud,
                    13));
        } else {
            mMarcadorUpdate.setPosition(latitudLongitud);

            mMapa.animateCamera(CameraUpdateFactory.newLatLng(latitudLongitud));
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    //Método que mueve la cámara al LatLng dado y al zoom indicado
    private void moveToLocation(LatLng ll,int zoomDistance) {
        //new LatLng(9.935783, -84.051375)
        CameraUpdate ubicacion = CameraUpdateFactory.newLatLng(ll);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(zoomDistance);
        mMapa.moveCamera(ubicacion);
        mMapa.animateCamera(zoom);
    }

}
