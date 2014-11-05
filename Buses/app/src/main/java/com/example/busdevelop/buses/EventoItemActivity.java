package com.example.busdevelop.buses;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class EventoItemActivity extends ActionBarActivity {

    GoogleMap mMapa;
    Evento mEvento;
    MarkerOptions mMarcadorEvento;
    ImageView mImagenEvento;
    TextView mNombreEvento;
    TextView mTipoEvento;
    TextView mFechaEvento;
    TextView mDescripcionEvento;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_item);
         // recuperar los datos del evento que manda al
        // hacer click en un elemento de la lista
        Intent i = getIntent();
        mEvento = new Evento();
        mEvento.setNombre(i.getStringExtra("nombre"));
        mEvento.setDescripcion(i.getStringExtra("descripcion"));
        mEvento.setTipo(i.getStringExtra("tipo"));
        mEvento.setFecha(i.getStringExtra("fecha"));
        mEvento.setLugar(i.getStringExtra("lugar"));
        mEvento.setLatitud(i.getStringExtra("latitud"));
        mEvento.setLongitud(i.getStringExtra("longitud"));

        position = Integer.parseInt(i.getStringExtra("position"));

        // Localizar los vie en activity_evento_item.xml
        mImagenEvento = (ImageView) findViewById(R.id.imagenEvento);
        mNombreEvento = (TextView) findViewById(R.id.nombreEvento);
        mTipoEvento = (TextView) findViewById(R.id.tipoEvento);
        mFechaEvento = (TextView) findViewById(R.id.fechaEvento);
        mDescripcionEvento = (TextView) findViewById(R.id.descripcionEvento);

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

            // moverse a la localizacion del evento
            latlng = new LatLng(Double.parseDouble(mEvento.getLatitud()) ,
                    Double.parseDouble(mEvento.getLongitud()));

            moveToLocation(latlng, 15);

            // agregar marcador a mapa en localización de evento
            mMarcadorEvento = new MarkerOptions();
            mMarcadorEvento.position(latlng);
            mMarcadorEvento.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mMarcadorEvento.title(mEvento.getLugar());
            mMapa.addMarker(mMarcadorEvento);

        } catch (Exception e) {

            e.printStackTrace();
            Log.e("Mapa", "exception", e);
        }

        // Mostrar Infromación del evento
        datosEvento();

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


    /**
     * Metodo que llena los datos del evento y los despliega en layout
     * encima del mapa
     */

    public void datosEvento(){
        mNombreEvento.setText(mEvento.getNombre());
        mFechaEvento.setText("Fecha: " + mEvento.getFecha());
        mDescripcionEvento.setText("Descripción: " + mEvento.getDescripcion());

        // switch para agregar el tipo
        switch (Integer.parseInt(mEvento.getTipo())){
            case 1: // Artistico
                mTipoEvento.setText("Tipo de Evento: Artístico");
                if(position  == 0){
                    mImagenEvento.setImageResource(R.drawable.arte2);
                } else{
                    mImagenEvento.setImageResource(R.drawable.arte1);
                }

                break;
            case 2: // Deportivo
                mTipoEvento.setText("Tipo de Evento: Deportivo");
                mImagenEvento.setImageResource(R.drawable.deportes2);
                break;
            case 3: // Fiesta
                mTipoEvento.setText("Tipo de Evento: Entretenimiento, Fiesta");
                if(position % 2 == 0){
                    mImagenEvento.setImageResource(R.drawable.fiesta2);
                }else{
                    mImagenEvento.setImageResource(R.drawable.fiesta3);
                }

                break;
            case 4: // Concierto
                mTipoEvento.setText("Tipo de Evento: Concierto");
                if(position % 2 != 0) {
                    mImagenEvento.setImageResource(R.drawable.concierto2);
                }else{
                    mImagenEvento.setImageResource(R.drawable.concierto4);
                }
                break;
            case 5: // Exposicion
                mTipoEvento.setText("Tipo de Evento: Exposicion, Charla");
                mImagenEvento.setImageResource(R.drawable.arte2);
                break;
        }
    }
}
