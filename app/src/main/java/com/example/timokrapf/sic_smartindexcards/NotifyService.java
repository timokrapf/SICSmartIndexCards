package com.example.timokrapf.sic_smartindexcards;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.List;

public class NotifyService extends Service {

    public static final String INTENT_NOTIFY = "com.blundell.tut.service.INTENT_NOTIFY";
    private final IBinder iBinder = new ServiceBinder();
    private NotificationManager notifManager;
    private static final String NOTIFICATION_ID = "channel_id";
    private static final String NOTIFICATION_CHANNEL_NAME = "channel_name";
    private SubjectRepository repository;


    public class ServiceBinder extends Binder {
        NotifyService getService() {
            return NotifyService.this;
        }
    }

    @Override
    public void onCreate() {
        Log.i("NotifyService", "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        if (intent.getBooleanExtra(INTENT_NOTIFY, false)) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                String subjectTitle = extras.getString(Constants.SUBJECT_TITLE_KEY);
                int requestcode = extras.getInt(Constants.CHOSEN_REQUESTCODE);
                if (subjectTitle != null) {
                    SharedPreferences sharedPref =
                            PreferenceManager.getDefaultSharedPreferences(this);
                    Boolean switchPref = sharedPref.getBoolean
                            (SettingsActivity.KEY_PREF_NOTIFICATION_SWITCH, false);
                    if (switchPref) {
                        createNotification(subjectTitle, requestcode);
                    }
                }
            }
        }
        return START_NOT_STICKY;
    }

    /*
    @Override
    public boolean stopService (Intent service){
        Bundle extras = service.getExtras();
        if(service.getBooleanExtra("enabled", true)){
            return true;
        } else{
            return false;
        }

    }
    */

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    /*
    http://www.brevitysoftware.com/how-to-get-heads-up-notifications-in-android/
    */
/*
    @SuppressWarnings("deprecation")
    private void sendNotification(Schedule schedule) {
            createNotificationChannel();
            NotificationCompat.Builder mBuilder;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder = new NotificationCompat.Builder(NotifyService.this, NOTIFICATION_ID);
                mBuilder.setSmallIcon(R.drawable.logo_sic);
                mBuilder.setContentTitle("Abfrage");
                mBuilder.setContentText("deine Abfrage in " + schedule.getSubjectTitle());
                mBuilder.setDefaults(Notification.DEFAULT_ALL);

            } else {
                mBuilder = new NotificationCompat.Builder(NotifyService.this);
                mBuilder.setSmallIcon(R.drawable.logo_sic);
                mBuilder.setContentTitle("Abfrage");
                mBuilder.setContentText("deine Abfrage in " + schedule.getSubjectTitle());
                mBuilder.setDefaults(Notification.DEFAULT_ALL);
                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                mBuilder.setSound(uri);
                mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
            }
            Intent resultIntent = new Intent(this, SubjectActivity.class);
            resultIntent.putExtra(Constants.SUBJECT_TITLE_KEY, schedule.getSubjectTitle());
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
            mBuilder.setVibrate(new long[]{0, 50, 100, 50, 100, 50, 100, 400, 100, 300, 100, 350, 50, 200, 100, 100, 50, 600});
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setVisibility(Notification.VISIBILITY_SECRET);
        }
        if (notificationManager != null) {
                notificationManager.notify(schedule.getRequestCode(), mBuilder.build());
        }
        stopSelf();
        }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(NOTIFICATION_ID);
            if(notificationChannel == null) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                CharSequence channelName = NOTIFICATION_CHANNEL_NAME;
                notificationChannel = new NotificationChannel(NOTIFICATION_ID, channelName, importance);
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{0, 50, 100, 50, 100, 50, 100, 400, 100, 300, 100, 350, 50, 200, 100, 100, 50, 600});
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
    }
*/
    public void createNotification(String subjectTitle, int requestcode) {
        Intent intent;
        PendingIntent pendingIntent;
        Notification.Builder builder;
        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);
        if (notifManager == null) {
            notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(NOTIFICATION_ID);
            if (mChannel == null) {
                mChannel = new NotificationChannel(NOTIFICATION_ID, NOTIFICATION_CHANNEL_NAME, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new Notification.Builder(this, NOTIFICATION_ID);
            intent = new Intent(this, SubjectActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra(Constants.SUBJECT_TITLE_KEY, subjectTitle);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            builder.setContentTitle("Abfrage")  // required
                    .setSmallIcon(R.drawable.logo_sic) // required
                    .setContentText("deine Abfrage in " + subjectTitle)  // required
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker("Abfrage")
                    .setChannelId(NOTIFICATION_ID)
                    .setPriority(Notification.PRIORITY_MAX);
            Boolean vibrateSwitchPref = sharedPref.getBoolean
                    (SettingsActivity.KEY_PREF_VIBRATE_SWITCH, false);
            if (vibrateSwitchPref) {
                builder.setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            }
            Boolean soundSwitchPref = sharedPref.getBoolean
                    (SettingsActivity.KEY_PREF_SOUND_SWITCH, false);
            if(soundSwitchPref){
                builder.setDefaults(Notification.DEFAULT_SOUND);
            }
        } else {
            builder = new Notification.Builder(this);
            intent = new Intent(this, SubjectActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra(Constants.SUBJECT_TITLE_KEY, subjectTitle);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            builder.setContentTitle("Abfrage")                           // required
                    .setSmallIcon(R.drawable.logo_sic) // required
                    .setContentText("deine Abfrage in " + subjectTitle)  // required
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker("Abfrage")
                    .setPriority(Notification.PRIORITY_MAX);
            Boolean switchPref = sharedPref.getBoolean
                    (SettingsActivity.KEY_PREF_VIBRATE_SWITCH, false);
            if (switchPref) {
                builder.setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            }
            Boolean soundSwitchPref = sharedPref.getBoolean
                    (SettingsActivity.KEY_PREF_SOUND_SWITCH, false);
            if(soundSwitchPref){
                builder.setDefaults(Notification.DEFAULT_SOUND);
            }
        }
        repository = new SubjectRepository(getApplication());
        repository.removeScheduleByRequestCode(requestcode);
        Notification notification = builder.build();
        notifManager.notify(requestcode, notification);
        stopSelf();
    }
}








