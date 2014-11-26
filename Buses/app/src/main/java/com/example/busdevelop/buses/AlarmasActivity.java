package com.example.busdevelop.buses;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

public class AlarmasActivity extends Activity {
    private List<Alarma> mAlarmas = new ArrayList<Alarma>();
    private List<String> mAlarmasString = new ArrayList<String>();
    private ListView mListAlarmas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmas);

        for(int i = 0; i < Alarma.IDS; i++) {
            try {
                FileInputStream fis = openFileInput("" + i);
                ObjectInputStream is = new ObjectInputStream(fis);
                Alarma alarma = (Alarma) is.readObject();
                is.close();
                mAlarmas.add(alarma);
                mAlarmasString.add(alarma.getHora()+ ", " + alarma.getRuta() + ", " + alarma.getBus());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (OptionalDataException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mListAlarmas = (ListView) findViewById(R.id.listView);

        /*ArrayAdapter<String> adaptador =
                new ArrayAdapter<String>(getBaseContext(),
                        android.R.layout.simple_spinner_item, mAlarmas);*/

        ArrayAdapter<String> adaptador =
                new ArrayAdapter<String>(getBaseContext(),
                        android.R.layout.simple_spinner_item);

        adaptador.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        mListAlarmas.setAdapter(adaptador);

        /*Bundle bundle = this.getIntent().getExtras();
        if(bundle.containsKey("rutaAlarma")) {
            //Recuperar info enviada en el intent

            Alarma alarma = new Alarma(bundle.getString("rutaAlarma"), bundle.getString("busAlarma"), bundle.getFloat("horaAlarma"));
            /*SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(bundle.getString("rutaAlarma"), alarma.getId());
            editor.commit();
            editor.putInt(bundle.getString("busAlarma"), alarma.getId());
            editor.commit();
            editor.putInt(bundle.getString("horaAlarma"), alarma.getId());
            editor.commit();
        }
    }*/
    }


    public void seleccionarRutaParada(View view) {
        Intent i = new Intent(this, CrearAlarmaActivity.class);
        startActivity(i);

    }

    public void cancelarAlarma(AlarmManager alarma, PendingIntent alarmIntent) {
        // If the alarm has been set, cancel it.
        if (alarma!= null) {
            alarma.cancel(alarmIntent);
        }
    }

    public void crearNotificacion(int tiempo) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("MiBusCR")
                        .setContentText("El bus pasa en " + tiempo + " minutos!");

        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }


}
