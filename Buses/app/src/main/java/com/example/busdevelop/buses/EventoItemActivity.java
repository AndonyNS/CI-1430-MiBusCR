package com.example.busdevelop.buses;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;


public class EventoItemActivity extends ActionBarActivity {

    GoogleMap mMapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_item);

        try {
            if (mMapa == null) {
               // soportado en ultimas apis
                mMapa = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.mapEvento)).getMap();
            }

            mMapa.setMyLocationEnabled(true);
            mMapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMapa.getUiSettings().setZoomGesturesEnabled(true);

            LatLng latlng;
            try{
                latlng = new LatLng(mMapa.getMyLocation().getLatitude(),mMapa.getMyLocation().getLongitude());
            }catch(NullPointerException e){
                latlng = new LatLng(9.9200652,-84.0846053);
            }
            moveToLocation(latlng, 13);

        } catch (Exception e) {

            e.printStackTrace();
            Log.e("Mapa", "exception", e);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.evento_item, menu);
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

    //Método que mueve la cámara al LatLng dado y al zoom indicado
    private void moveToLocation(LatLng ll,int zoomDistance) {
        //new LatLng(9.935783, -84.051375)
        CameraUpdate ubicacion = CameraUpdateFactory.newLatLng(ll);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(zoomDistance);
        mMapa.moveCamera(ubicacion);
        mMapa.animateCamera(zoom);
    }

}
