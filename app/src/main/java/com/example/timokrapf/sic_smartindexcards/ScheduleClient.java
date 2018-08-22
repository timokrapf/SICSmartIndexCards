package com.example.timokrapf.sic_smartindexcards;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.Calendar;

/*
http://blog.blundellapps.co.uk/notification-for-a-user-chosen-time/
 */
public class ScheduleClient {

    private LearnplannerService mLearnplannerService;
    private Context context;
    private boolean mIsBound;

    public ScheduleClient(Context context){
        this.context = context;
    }

    public void doBindService(){
        context.bindService(new Intent(context, LearnplannerService.class), connection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mLearnplannerService = ((LearnplannerService.ServiceBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mLearnplannerService = null;
        }
    };

    public void setAlarmForNotification(Calendar c, String subject){
        mLearnplannerService.setAlarm(c, subject);
    }

    public void setSubject(String subject){
        mLearnplannerService.setGivenSubject(subject);
    }

    public void doUnbindService() {
        if (mIsBound) {
            context.unbindService(connection);
            mIsBound = false;
        }
    }
}
