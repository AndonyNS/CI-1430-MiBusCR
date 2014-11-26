package com.example.busdevelop.buses;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CrearAlarmaActivity extends Activity {
    private List<Ruta> mListaRutas;
    private List<String> mNombreRutas;
    private List<String> mNombreParadas;
    private Usuario mUsuario;
    private Spinner mRutasOpciones;
    private Spinner mParadas;
    private Spinner mTiempo;
    private Calendar mCalendar;
    private AlarmManager mAlarmMgr;
    private PendingIntent mAlarmIntent;
    private Ruta mRuta;
    private Parada mParada;
    private int mHora;
    private int mMinutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_alarmas);
        mListaRutas = new ArrayList<Ruta>();
        getRutas();

        mTiempo = (Spinner) findViewById(R.id.tiempo);

        ArrayAdapter<CharSequence> adaptadorTiempo =
                ArrayAdapter.createFromResource(getBaseContext(),
                        R.array.valores_array,
                        android.R.layout.simple_spinner_item);

        adaptadorTiempo.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        mTiempo.setAdapter(adaptadorTiempo);

        mRutasOpciones = (Spinner) findViewById(R.id.rutas);

        mParadas = (Spinner) findViewById(R.id.paradas);

        mCalendar = Calendar.getInstance();
        mHora = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinutos = mCalendar.get(Calendar.MINUTE);

        Button crear = (Button) findViewById(R.id.buttonCrear);
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Aqui se debe llamar a la funcion que calcula el tiempo de llegada del bus
                // basado en la ruta y la parada, para probar usaremos una hora determinada
                Alarma alarma = establecerAlarma(mHora, mMinutos, Integer.getInteger(mTiempo.getSelectedItem().toString()));
                Intent intent = new Intent(getApplicationContext(), AlarmasActivity.class);

                FileOutputStream fos;
                try {
                    fos = openFileOutput("" + alarma.getId(), Context.MODE_PRIVATE);
                    ObjectOutputStream os = new ObjectOutputStream(fos);
                    os.writeObject(alarma);
                    os.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });

        /*LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_alarmas, null);
        mRutasOpciones = (Spinner) findViewById(R.id.rutas);

        ArrayAdapter<String> adaptador =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, mNombreRutas);

        adaptador.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        Toast.makeText(getBaseContext(), mNombreRutas.get(1), Toast.LENGTH_SHORT).show();

        mRutasOpciones.setAdapter(adaptador);

        // 1. Instantiate an AlertDialog.Builder with its constructor
/*        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setTitle("Seleccionar la ruta y parada deseadas")
                .setView(layout)
                .setItems(rutas, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Log.i("Dialogos", "Opción elegida: " + rutas[item]);
                    }
                })
                .setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("Dialogos", "Confirmacion Cancelada.");
                        dialog.cancel();
                    }
                });
                /*.setItems(R.color.black, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                    }
                });*/

        // 3. Get the AlertDialog from create()
/*        AlertDialog dialog = builder.create();
        dialog.show();
*/
    }

    private void getRutas(){
        mUsuario = new Usuario();
        SharedPreferences sharedPref = getSharedPreferences("MyPrefsFile", 0);
        mUsuario.setEmail(sharedPref.getString("UserEmail", ""));
        mUsuario.setEncrypted_password(sharedPref.getString("UserPass", ""));

        new HttpAsyncTaskToken(this).execute();
    }

    private void crearListaNombreRutas(){
        mNombreRutas = new ArrayList<String>();

        // Guarda el nombre de todas las rutas
        for (int i=0; i < mListaRutas.size(); i++){
            String s = mListaRutas.get(i).getNombre();
            mNombreRutas.add(mListaRutas.get(i).getNombre());

        }

    }

    public void getParadas(Ruta ruta) {
        mNombreParadas = new ArrayList<String>();
        mNombreParadas.add(ruta.getParadaInicial().getNombre());
        for(Parada p : ruta.getParadasIntermedias()) {
            mNombreParadas.add(p.getNombre());
        }
        mNombreParadas.add(ruta.getParadaInicial().getNombre());
    }

    public Alarma establecerAlarma(int horaLlegadaBus, int minutos, int tiempoAviso) {
        mAlarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        mAlarmIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        mCalendar = Calendar.getInstance();
        // Set the alarm to start
        mCalendar.setTimeInMillis(System.currentTimeMillis());

        if(minutos - tiempoAviso <= 0) {
            mCalendar.set(Calendar.HOUR_OF_DAY, horaLlegadaBus);
            mCalendar.set(Calendar.MINUTE, minutos - tiempoAviso);
        } else {
            horaLlegadaBus--;
            minutos = 60 - (tiempoAviso - minutos);
            mCalendar.set(Calendar.HOUR_OF_DAY, horaLlegadaBus);
            mCalendar.set(Calendar.MINUTE,  minutos);
        }
        Toast.makeText(getBaseContext(), "Alarma agregada", Toast.LENGTH_SHORT).show();

        // Se establece la alarma
        mAlarmMgr.set(AlarmManager.RTC, mCalendar.getTimeInMillis(), mAlarmIntent);

        /*alarmMgr.set(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() +
                        60 * 1000, alarmIntent);*/
        return new Alarma(mRuta.getNombre(), mParada.getNombre(), horaLlegadaBus + (minutos/100));

    }

    /**
     * Obtener el token para poder consultar rutas
     */
    private class HttpAsyncTaskToken extends AsyncTask<Void, Void, String> {
        Activity mActivity;
        private HttpAsyncTaskToken(Activity activity){
            this.mActivity = activity;
        }

        @Override
        protected String doInBackground(Void...params) {
            return mUsuario.obtenerToken();
        }
        /**
         * metodo que se ejecuta después de obtener la respuesta
         * al request post del token
         * @param resultado
         */
        @Override
        protected void onPostExecute(String resultado) {
            try {
                mUsuario.guardarTokenId(resultado);
                // una vez obtenido el token se pide las rutas
                new HttpAsyncTask(mActivity).execute();
            } catch(IllegalArgumentException i){
                Log.e("Error de argumento", "" + i.getMessage());
            }
        }
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        Activity mActivity;
        private HttpAsyncTask(Activity activity){
            this.mActivity = activity;
        }

        @Override
        protected String doInBackground(String... urls) {
            ManejadorRutas manejador = ManejadorRutas.getInstancia(mUsuario.getToken());
            mListaRutas = manejador.getListaRutas();
            return "Rutas Obtenidas!";
        }

        /**
         * metodo que se ejecuta después de obtener la respuesta
         * al request get
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {

            try{
                Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
                crearListaNombreRutas();
            } catch(IllegalArgumentException i){
                Log.e("Error de argumento",""+i.getMessage());
            }

            ArrayAdapter<String> adaptador =
                    new ArrayAdapter<String>(getBaseContext(),
                            android.R.layout.simple_spinner_item, mNombreRutas);

            adaptador.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item);

            mRutasOpciones.setAdapter(adaptador);

            mRutasOpciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg, View v1, int index1, long arg4) {
                    mRuta = mListaRutas.get(index1);
                    getParadas(mRuta);
                    ArrayAdapter<String> adaptador2 =
                            new ArrayAdapter<String>(getBaseContext(),
                                    android.R.layout.simple_spinner_item, mNombreParadas);

                    adaptador2.setDropDownViewResource(
                            android.R.layout.simple_spinner_dropdown_item);
                    mParadas.setAdapter(adaptador2);
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }

            });

            mParadas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg, View v1, int index1, long arg4) {
                    if(index1 == 0) {
                        mParada = mRuta.getParadaFinal();
                    } else if(index1 == 1) {
                        mParada = mRuta.getParadaFinal();
                    }
                    else {
                        mParada = mRuta.getParadasIntermedias().get(index1 - 2);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }

            });
        }
    }
}
