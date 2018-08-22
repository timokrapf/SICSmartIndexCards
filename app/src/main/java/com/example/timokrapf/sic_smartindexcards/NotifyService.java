package com.example.timokrapf.sic_smartindexcards;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NotifyService extends Service {

    private static final int NOTIFICATION = 123;
    public static final String INTENT_NOTIFY = "com.blundell.tut.service.INTENT_NOTIFY";
    private final IBinder iBinder = new ServiceBinder();

    public class ServiceBinder extends Binder {
        NotifyService getService() {
            return NotifyService.this;
        }
    }

    @Override
    public void onCreate() {
        Log.i("NotifyService", "onCreate()");
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        if(intent.getBooleanExtra(INTENT_NOTIFY, false)) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                String subject = extras.getString("Subject");
            sendNotification(subject);
        }
        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    /*
    http://www.brevitysoftware.com/how-to-get-heads-up-notifications-in-android/
    */

    @SuppressWarnings("deprecation")
    private void sendNotification(String subject) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(NotifyService.this);
                        mBuilder.setSmallIcon(R.drawable.logo_sic);
                        mBuilder.setContentTitle("Abfrage");
                        mBuilder.setContentText("deine Abfrage in " + subject);
                        mBuilder.setDefaults(Notification.DEFAULT_ALL);
                        mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        Intent resultIntent = new Intent(this, SubjectActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(SubjectActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(mNotificationManager != null) {
            mNotificationManager.notify(NOTIFICATION, mBuilder.build());
            long[] pattern = {0,50,100,50,100,50,100,400,100,300,100,350,50,200,100,100,50,600};
            ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(pattern, -1);
        }



        /*CharSequence title = "Alarm!!";
        int icon = R.drawable.logo_sic;
        CharSequence text = "Your notification time is upon us.";
        long time = System.currentTimeMillis();
        Notification notification = new Notification(icon, text, time);
        PendingIntent contentIntent = PendingIntent.getActivity(NotifyService.this,
                0, new Intent(NotifyService.this, SubjectActivity.class), 0);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(NOTIFICATION, notification);
        stopSelf();
        */
    }
}






