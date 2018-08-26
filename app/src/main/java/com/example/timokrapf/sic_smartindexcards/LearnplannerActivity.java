package com.example.timokrapf.sic_smartindexcards;

import android.app.Activity;
import android.app.TimePickerDialog;
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

    private Button subjectButton, saveButton, learnplannerButton;
    private SubjectSpinnerAdapter adapter;
    private TextView time;
    private DatePicker datePicker;
    private ScheduleClient scheduleClient;
    private TimePickerDialog timePicker;
    private int hour;
    private int minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learnplaner_activity);
        initUI();
        initButtons();
        setClickListener();
        getList();
        initSpinner();
    }

    public void initUI() {
        time = (TextView) findViewById(R.id.time_id);
        datePicker = (DatePicker) findViewById(R.id.date_picker_id);
        scheduleClient = new ScheduleClient(this);
        scheduleClient.doBindService();
    }

    public void initButtons() {
        subjectButton = (Button) findViewById(R.id.subject_button_id);
        saveButton = (Button) findViewById(R.id.add_button_learnplaner_id);
        learnplannerButton = (Button) findViewById(R.id.schedule_planner_button_id);
    }

    /*
    https://abhiandroid.com/ui/timepicker#Example_of_TimePickerDialog_in_Android_Studio
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

    private void subjectButtonClicked() {
        Intent i = new Intent(LearnplannerActivity.this, StartActivity.class);
        startActivity(i);
    }

    private void learnplannerButtonClicked() {
        Intent i = new Intent(LearnplannerActivity.this, ScheduleActivity.class);
        startActivity(i);
    }

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
            startScheduleActivity(date, chosenTime);
            onDateSelectedButtonView(date, chosenTime);
        }
    }

    private void onDateSelectedButtonView(String date, String chosenTime){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        scheduleClient.setAlarmForNotification(c, adapter.getSubjectTitle(), date, chosenTime);
    }

    @Override
    protected void onStop() {
        if(scheduleClient != null) {
            scheduleClient.doUnbindService();
        }
        super.onStop();
    }

    private void startScheduleActivity(String date, String time){
        Intent i = new Intent(LearnplannerActivity.this, ScheduleActivity.class);
        i.putExtra(Constants.CHOSEN_DATE, date);
        i.putExtra(Constants.CHOSEN_TIME, time);
        i.putExtra(Constants.CHOSEN_SUBJECT, adapter.getSubjectTitle());
        startActivity(i);
    }

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

    private void initSpinner() {
        Spinner subjectSpinner = (Spinner) findViewById(R.id.spinner_chosen_subject_id);
        subjectSpinner.setAdapter(adapter);
    }
}
