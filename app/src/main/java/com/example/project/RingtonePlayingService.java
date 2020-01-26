package com.example.project;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;

public class RingtonePlayingService extends Service {
    private static final String TAG = RingtonePlayingService.class.getSimpleName();
    private static final String URI_BASE = RingtonePlayingService.class.getName() + ".";
    public static final String ACTION_DISMISS = URI_BASE + "ACTION_DISMISS";


    private Ringtone ringtone;

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("wselt","true");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("inRingCreate", "in onCreate");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("inRing", "in onStartCommand");
//        int intentid = Integer.parseInt(intent.getExtras().getString("intent_id"));
//        Log.d("inRingID",String.valueOf(intentid));

        if (intent == null) {
            Log.d(TAG, "The intent is null.");
            return START_REDELIVER_INTENT;
        }

        if ("action.cancel.notification".equalsIgnoreCase(intent.getAction())){
            Log.d("InDismiss", "Stop ringtone");
            dismissRingtone();
        }
        else {

            Uri ring = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            ringtone = RingtoneManager.getRingtone(getApplicationContext(), ring);
            ringtone.play();
        }

        return START_NOT_STICKY;
    }

    public void dismissRingtone() {
        // stop the alarm rigntone
        Intent i = new Intent(this, RingtonePlayingService.class);
        stopService(i);

        // also dismiss the alarm to ring again or trigger again
        AlarmManager aManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        aManager.cancel(pendingIntent);

        // Canceling the current notification
        NotificationManager notificationManager =
                (NotificationManager)getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }

    @Override
    public void onDestroy() {
        Log.d("inOnDestroy", "stop ringtone");
        if(ringtone != null){
            ringtone.stop();
        }
    }

}