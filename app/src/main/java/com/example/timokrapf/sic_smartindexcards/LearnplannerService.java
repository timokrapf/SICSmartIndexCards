package com.example.timokrapf.sic_smartindexcards;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class LearnplannerService extends Service {

    /*
    http://blog.blundellapps.co.uk/notification-for-a-user-chosen-time/
    * */
    private static final int myNotificationID = 12345;

    private IBinder iBinder;

    @Override
    public void onCreate() {
        iBinder = new LocalBinder();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }







    class LocalBinder extends Binder {

        LearnplannerService getBinder() {
            return LearnplannerService.this;
        }
    }
}
