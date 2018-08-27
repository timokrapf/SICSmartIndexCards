package com.example.timokrapf.sic_smartindexcards;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class AlarmTask implements Runnable {

    private final Calendar calendar;
    private final String subject, date, time;
    private final AlarmManager alarmManager;
    private final Context context;


    public AlarmTask(Context context, Calendar calendar, String subject, String date, String time){
        this.context = context;
        this.calendar = calendar;
        this.subject = subject;
        this.date = date;
        this.time = time;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public void run() {
        Intent intent = new Intent(context, NotifyService.class);
        intent.putExtra(NotifyService.INTENT_NOTIFY, true);
        intent.putExtra(Constants.CHOSEN_SUBJECT, subject);
        intent.putExtra(Constants.CHOSEN_DATE, date);
        intent.putExtra(Constants.CHOSEN_TIME, time);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
    }
}
