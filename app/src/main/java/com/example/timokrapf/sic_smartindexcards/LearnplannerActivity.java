package com.example.timokrapf.sic_smartindexcards;

import android.app.ActionBar;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.graphics.drawable.Icon;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.lifecycle.Observer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.support.v7.widget.Toolbar;

public class LearnplannerActivity extends FragmentActivity{

    //Activity where user can choose when to learn specific subject

    private Button subjectButton, saveButton, learnplannerButton;
    private SubjectSpinnerAdapter adapter;
    private TextView time;
    private DatePicker datePicker;
    private TimePickerDialog timePicker;
    private int hour;
    private int minute;
    private Schedule schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learnplaner_activity);
        initUI();
        initActionBar();
        initButtons();
        setClickListener();
        getList();
        initSpinner();

    }

    //set up User Interface

    public void initUI() {
        time = (TextView) findViewById(R.id.time_id);
        datePicker = (DatePicker) findViewById(R.id.date_picker_id);
        datePicker.setMinDate(System.currentTimeMillis() - 1000);
    }

    //set up Buttons

    public void initButtons() {
        subjectButton = (Button) findViewById(R.id.subject_button_id);
        saveButton = (Button) findViewById(R.id.add_button_learnplaner_id);
        learnplannerButton = (Button) findViewById(R.id.schedule_planner_button_id);
    }

    /*
     set click-Listeners for Buttons
     set up time Picker
     From https://abhiandroid.com/ui/timepicker#Example_of_TimePickerDialog_in_Android_Studio
     Minor changes were done
    */

    private void setClickListener() {
        subjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subjectButtonClicked();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButtonClicked();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentTime = Calendar.getInstance();
                int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
                int currentMinute = currentTime.get(Calendar.MINUTE);
                timePicker = new TimePickerDialog(LearnplannerActivity.this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        setTimeText(selectedHour, selectedMinute);
                        hour = selectedHour;
                        minute = selectedMinute;
                    }
                }, currentHour, currentMinute, true);
                timePicker.setTitle(Constants.SELECT_DATE);
                timePicker.show();
            }
        });
        learnplannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                learnplannerButtonClicked();
            }
        });
    }

    //show time in common way

    private void setTimeText(int selectedHour, int selectedMinute) {
        String hourString = Integer.toString(selectedHour);
        String minuteString = Integer.toString(selectedMinute);
        if(selectedHour < 10) {
            hourString = "0" + selectedHour;
        }
        if(selectedMinute < 10) {
            minuteString = "0" + selectedMinute;
        }
        time.setText(hourString + ":" + minuteString);
    }

    //go to other activities via navigation buttons at bottom

    private void subjectButtonClicked() {
        Intent i = new Intent(LearnplannerActivity.this, StartActivity.class);
        startActivity(i);
    }

    private void learnplannerButtonClicked() {
        Intent i = new Intent(LearnplannerActivity.this, ScheduleActivity.class);
        startActivity(i);
    }

    //start schedule activity save chosen date,time and subject after pressing save button

    private void saveButtonClicked() {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();
        String date = String.valueOf(day) + "." + String.valueOf(month) + "." + String.valueOf(year);
        String chosenTime = time.getText().toString();
        if (date.isEmpty() || adapter.getSubjectTitle().equals(getString(R.string.no_subject_was_chosen)) || chosenTime.isEmpty()){
            Toast.makeText(this, "Fehlerhafte Eingabe", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Am " + date + " um " + chosenTime + " Uhr wirst du in "
                    + adapter.getSubjectTitle() + " ausgefragt", Toast.LENGTH_LONG).show();
            onDateSelectedButtonView(date, chosenTime);
            startScheduleActivity();
        }
    }

    //save chosen data as calender to make notification

    private void onDateSelectedButtonView(String date, String chosenTime){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        schedule = new Schedule();
        schedule.setSubjectTitle(adapter.getSubjectTitle());
        schedule.setTime(chosenTime);
        schedule.setDate(date);
        schedule.setAlarmTime(c.getTimeInMillis());
        int requestCode = (int) c.getTimeInMillis() + schedule.getScheduleId();
        schedule.setRequestCode(requestCode);
    }

    //start ScheduleActivity via Intent

    private void startScheduleActivity(){
        Intent i = new Intent(LearnplannerActivity.this, ScheduleActivity.class);
        i.putExtra(Constants.CHOSEN_SCHEDULE, schedule);
        startActivity(i);
    }

    //get subjects to fill Spinner, using own SubjectSpinnerAdapter

    private void getList() {
        adapter = new SubjectSpinnerAdapter(this);
        SubjectViewModel model = ViewModelProviders.of(this).get(SubjectViewModel.class);
        model.getSubjectsList().observe(this, new Observer<List<Subject>>() {
            @Override
            public void onChanged(@Nullable List<Subject> subjects) {
                adapter.setSubjectList(subjects);
            }
        });
    }

    //fill subjectSpinner with data from getList

    private void initSpinner() {
        Spinner subjectSpinner = (Spinner) findViewById(R.id.spinner_chosen_subject_id);
        subjectSpinner.setAdapter(adapter);
    }


    //ActionBar

    private void initActionBar(){
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.learnplaner);
            actionBar.setIcon(R.drawable.learnplanner_icon_calendar);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        return true;
    }

    // handle settings-button in Actionbar being pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings_button_actionbar){
            settingsButtonActionbarClicked();
        }
        return true;
    }

    private void settingsButtonActionbarClicked(){
        Intent settingsIntent = new Intent(LearnplannerActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }
}
