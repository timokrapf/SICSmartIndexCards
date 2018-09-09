package com.example.timokrapf.sic_smartindexcards;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/*
ServiceReveiver to set/stop AlarmManager and send Notification or not depending on int status
From https://www.thepolyglotdeveloper.com/2014/10/use-broadcast-receiver-background-services-android/
Changes were made
 */
public class ServiceReceiver extends BroadcastReceiver {

    private static final int START_ALARM_CODE = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        Schedule schedule = intent.getExtras().getParcelable(Constants.CHOSEN_SCHEDULE);
        int status = intent.getExtras().getInt(Constants.RECEIVER_STATUS);
        if (schedule != null) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(context, NotifyService.class);
            i.putExtra(Constants.SUBJECT_TITLE_KEY, schedule.getSubjectTitle());
            i.putExtra(Constants.CHOSEN_REQUESTCODE, schedule.getRequestCode());
            i.putExtra(NotifyService.INTENT_NOTIFY, true);
            PendingIntent pendingIntent = PendingIntent.getService(context, schedule.getRequestCode(), i, PendingIntent.FLAG_UPDATE_CURRENT);
            if (alarmManager != null) {
                if (status == START_ALARM_CODE) {
                    alarmManager.set(AlarmManager.RTC, schedule.getAlarmTime(), pendingIntent);
                } else {
                    alarmManager.cancel(pendingIntent);
                    pendingIntent.cancel();
                }
            }
        }
    }
}
