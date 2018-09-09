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

    //Service to sent Notification to User on chosen Date and Time

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

    //start Service if subject and request code are correct,
    /*
     don't send if user chose not to get notification in settingsActivity
     From https://developer.android.com/guide/topics/ui/settings
     Changes made.
     */


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

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    /*
    create Notification for users with different SDK-Level
    From http://www.brevitysoftware.com/how-to-get-heads-up-notifications-in-android/
    Changes were made.
    If User switches off sound and vibration, notification won't make sound or vibrate
    */

    public void createNotification(String subjectTitle, int requestcode) {
        Intent intent;
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
            intent.putExtra(Constants.SUBJECT_TITLE_KEY, subjectTitle);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(SubjectActivity.class);
            stackBuilder.addNextIntent(intent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            builder.setContentTitle("Abfrage")
                    .setSmallIcon(R.drawable.logo_sic)
                    .setContentText("deine Abfrage in " + subjectTitle)
                    .setAutoCancel(true)
                    .setContentIntent(resultPendingIntent)
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
            intent.putExtra(Constants.SUBJECT_TITLE_KEY, subjectTitle);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(SubjectActivity.class);
            stackBuilder.addNextIntent(intent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            builder.setContentTitle("Abfrage")
                    .setSmallIcon(R.drawable.logo_sic)
                    .setContentText("deine Abfrage in " + subjectTitle)
                    .setAutoCancel(true)
                    .setContentIntent(resultPendingIntent)
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








