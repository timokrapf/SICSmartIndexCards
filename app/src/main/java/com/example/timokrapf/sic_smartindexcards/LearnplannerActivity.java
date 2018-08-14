package com.example.timokrapf.sic_smartindexcards;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LearnplannerActivity extends FragmentActivity {


    private Spinner subjectSpinner;
    private DatePicker datePicker;
    private Button subjectButton, saveButton;
    private ServiceConnection serviceConnection;
    private String[] list;
    private TimePicker timePicker;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learnplaner_activity);
        getList();
        initUI();
        initButtons();
        setClickListener();
        //getSpinnerData();
        initSpinner();
    }


    private void getList(){
        Intent i = getIntent();
        if(i != null) {
            Bundle extras = i.getExtras();
            if(extras != null) {
                list = (String[]) extras.get(Constants.SPINNER_SUBJECT_KEY);
            }
        }
    }


    public void initUI() {
        TextView chosenDate = (TextView) findViewById(R.id.chosenDate_id);
        TextView chosenSubject = (TextView) findViewById(R.id.chosenSubject_id);
        datePicker = (DatePicker) findViewById(R.id.date_picker_id);
        timePicker = (TimePicker) findViewById(R.id.time_picker_id);
    }

    public void initButtons() {
        subjectButton = (Button) findViewById(R.id.subject_button_id);
        saveButton = (Button) findViewById(R.id.add_button_learnplaner_id);
        Button learnplannerButton = (Button) findViewById(R.id.schedule_planner_button_id);
        learnplannerButton.setEnabled(false);
    }

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
    }

    private void subjectButtonClicked() {
        Intent i = new Intent(LearnplannerActivity.this, StartActivity.class);
        startActivity(i);
    }

    private void saveButtonClicked() {
        //Subject selectedSubject = getSelectedSubject();

        //startLearnplannerService();
        Toast.makeText(this,  "wurde als Datum gespeichert", Toast.LENGTH_SHORT).show();
    }

    /*private void startLearnplannerService() {

        Intent intent = new Intent(this, LearnplannerService.class);
        Long selectedDate = calendarView.getDate();
        intent.putExtra("selectedDate", selectedDate);
        startService(intent);
    }

   /* private void initServiceConnection() {

        serviceConnection = new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName name) {
                System.out.println("Service disconnected");
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                System.out.println("Service connected");

                myLearnplannerService = ((LearnplannerService.LocalBinder) service).getBinder();
                if (myLearnplannerService != null)
                    myLearnplannerService.setOnEggTimerStatusChangedListener(LearnplannerActivity.this);
            }
        };
   */

    private void initSpinner() {
        subjectSpinner = (Spinner) findViewById(R.id.spinner_chosen_subject_id);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(arrayAdapter);
    }
}

