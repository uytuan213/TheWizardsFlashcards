package ca.thewizards.flashcards;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {

    final int NOTIFICATION_ID = 1;
    final int INTENT_REQUEST_CODE = 0;

    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //Toast.makeText(this, "Notification onCreate called.", Toast.LENGTH_SHORT).show();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try{
            int alarm_delay = intent.getIntExtra("DateTimeAlarm", 86400000);
            //Toast.makeText(this, "onStartCommand called. " + alarm_delay, Toast.LENGTH_SHORT).show();

            final Timer timer = new Timer(true);

            final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            final Intent intentNotification = new Intent(getApplicationContext(), MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // emulate the back button

            final PendingIntent pendingIntent = PendingIntent
                    .getActivity(getApplicationContext(), INTENT_REQUEST_CODE, intentNotification,
                            PendingIntent.FLAG_UPDATE_CURRENT);

            final Notification notification = new Notification.Builder(getApplicationContext())
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(getString(R.string.alarm_content))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .build();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    manager.notify(NOTIFICATION_ID, notification);

                    timer.cancel();

                    stopSelf(); // to destroy the Service
                }
            }, alarm_delay);
            return Service.START_STICKY_COMPATIBILITY;

        }
        catch(Exception ex){
            return Service.START_STICKY_COMPATIBILITY;
        }

        //return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        //Toast.makeText(this, "Service onDestroy called", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
}

