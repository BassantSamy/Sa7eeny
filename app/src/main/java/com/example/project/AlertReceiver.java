package com.example.project;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;


public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlertReceiver","i'm here");
        int intId = Integer.parseInt(intent.getExtras().getString("intentid"));
        Log.d("intentID", String.valueOf(intId));

        NotificationHelper notificationHelper = new NotificationHelper(context);

        context.startService(new Intent(context, RingtonePlayingService.class));

        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();

        int cnid = (int)System.currentTimeMillis();
        Intent dismissIntent = new Intent(context, RingtonePlayingService.class);
        dismissIntent.putExtra("cancelid",String.valueOf(intId));
        PendingIntent pendingIntent1 = PendingIntent.getService(context, cnid, dismissIntent, 0);
        nb.addInvisibleAction(android.R.drawable.ic_lock_idle_alarm, "DISMISS", pendingIntent1);

        int nid = (int)System.currentTimeMillis();
//        Intent intent1 = new Intent();
//        PendingIntent pIntent = PendingIntent.getActivity(context, intId, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
//        nb.setContentIntent(pIntent);

        notificationHelper.getManager().notify(intId, nb.build());

        Vibrator v = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        v.vibrate(5000);
    }

}

