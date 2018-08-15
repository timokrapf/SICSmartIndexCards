package com.example.timokrapf.sic_smartindexcards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ScheduleActivity extends Activity {


    private String date, time, subject;
    private ArrayList<String> schedule_items = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_schedule_activity);
        initUI();
        getListData();
        fillList();
    }

    public void initUI(){
        TextView plannedSchedule = (TextView) findViewById(R.id.schedule_heading);
    }

    public void getListData(){
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if (extras != null) {
            date = extras.getString(Constants.CHOSEN_DATE);
            time = extras.getString(Constants.CHOSEN_TIME);
            subject = extras.getString(Constants.CHOSEN_SUBJECT);
        }
    }

    public void fillList(){
        ListView scheduleList = (ListView) findViewById(R.id.schedule_listview_id);
        ArrayAdapter<String> schedule_adapter;
        schedule_items.add(date + " " + time + " " + subject);
        schedule_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, schedule_items);
        scheduleList.setAdapter(schedule_adapter);
    }
}
