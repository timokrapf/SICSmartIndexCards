package com.example.timokrapf.sic_smartindexcards;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ScheduleActivity extends FragmentActivity {


    private String date, time, subject;
    private int day, year, month, hour, minute;
    private ScheduleAdapter adapter;
    private SubjectViewModel viewModel;
    private RecyclerView recyclerView;
    private TextView emptyText;
    private Calendar calendar;
    private ScheduleClient client;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_schedule_activity);
        initUI();
        initActionBar();
        initAdapter();
        initModel();
        getListData();
        enterNewScheduleItem();
        initButtons();
    }

    private void initUI() {
        recyclerView = (RecyclerView) findViewById(R.id.schedule_recyclerview);
        emptyText = (TextView) findViewById(android.R.id.empty);

    }



    private void initAdapter(){
        adapter = new ScheduleAdapter(this, new ScheduleAdapter.OnItemClickListener() {
            @Override
            public void onItemLongClicked(Schedule schedule) {
                viewModel.deleteSchedule(schedule);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initModel() {
        viewModel = ViewModelProviders.of(this).get(SubjectViewModel.class);
        viewModel.getSchedulesList().observe(this, new Observer<List<Schedule>>() {
            @Override
            public void onChanged(@Nullable List<Schedule> schedules) {
                adapter.setScheduleList(schedules);
                if(adapter.getItemCount() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    emptyText.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyText.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initButtons(){
        Button subjectButton = (Button) findViewById(R.id.subject_button_id);
        subjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subjectButtonClicked();
            }
        });
        Button scheduleButton = (Button) findViewById(R.id.schedule_planner_button_id);
        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleButtonClicked();
            }
        });
    }

    private void subjectButtonClicked(){
        Intent i = new Intent(ScheduleActivity.this, StartActivity.class);
        startActivity(i);
    }

    private void scheduleButtonClicked(){
        Intent i = new Intent (ScheduleActivity.this, LearnplannerActivity.class);
        startActivity(i);
    }

    private void getListData(){
        Intent i = getIntent();
        if(i != null) {
            Bundle extras = i.getExtras();
            if (extras != null) {
                date = extras.getString(Constants.CHOSEN_DATE);
                time = extras.getString(Constants.CHOSEN_TIME);
                subject = extras.getString(Constants.CHOSEN_SUBJECT);
            }
        }
    }

    private void enterNewScheduleItem() {
        if(date != null && time != null && subject != null) {
            Schedule schedule = new Schedule();
            schedule.setSubjectTitle(subject);
            schedule.setDate(date);
            schedule.setTime(time);
            viewModel.insertSchedule(schedule);
        }
    }

    //ActionBar:
    private void initActionBar(){
        getActionBar().setTitle(R.string.schedule_planner);
        getActionBar().setIcon(R.drawable.logo_sic);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar_other, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_button_actionbar:
                //todo delete item
                Toast.makeText(this, "Löschen", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings_button_actionbar:
                //open settings activity
                Toast.makeText(this, "Einstellungen", Toast.LENGTH_SHORT).show();
                settingsButtonActionbarClicked();
                break;
        }
        return true;
    }

    private void settingsButtonActionbarClicked(){
        Intent settingsIntent = new Intent(ScheduleActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }
}




