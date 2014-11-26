package com.example.busdevelop.buses;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    private static final int MY_NOTIFICATION_ID = 1;
    NotificationManager notificationManager;
    Notification notification;

    @Override
    public void onReceive(Context c, Intent i) {
        PendingIntent pi = PendingIntent.getBroadcast(c, 0, new Intent("com.example.busdevelop.buses"),0 );
        notification = new NotificationCompat.Builder(c)
                .setContentTitle("MiBusCR")
                .setContentText("El bus está cerca!")
                        //.setWhen(System.currentTimeMillis())
                .setContentIntent(pi)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.logo)
                .build();

        notificationManager = (NotificationManager)c.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(MY_NOTIFICATION_ID, notification);

    }
}