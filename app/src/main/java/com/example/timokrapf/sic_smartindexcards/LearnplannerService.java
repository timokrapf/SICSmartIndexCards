package com.example.timokrapf.sic_smartindexcards;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Calendar;


public class LearnplannerService extends Service {

    /*
    http://blog.blundellapps.co.uk/notification-for-a-user-chosen-time/
    */

    private static final int myNotificationID = 12345;

    private final IBinder iBinder = new ServiceBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("ScheduleService", "Received start id " + startId + ": " + intent);

        return START_STICKY;
    }

    public void setAlarm(Calendar c, String subject) {
        new AlarmTask(this, c, subject).run();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    public class ServiceBinder extends Binder {
        LearnplannerService getService() {
            return LearnplannerService.this;
        }
    }

}
