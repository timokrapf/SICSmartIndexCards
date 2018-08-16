package com.example.timokrapf.sic_smartindexcards;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
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
        if(intent.getBooleanExtra(INTENT_NOTIFY, false))
            sendNotification();
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @SuppressWarnings("deprecation")
    private void sendNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(NotifyService.this)
                        .setSmallIcon(R.drawable.logo_sic)
                        .setContentTitle("Abfrage")
                        .setContentText("deine Abfrage");
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
        // mId allows you to update the notification later on.
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






