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

public class LearnplannerActivity extends Activity {


    private Spinner subjectSpinner;
    private Button subjectButton, saveButton;
    private ServiceConnection serviceConnection;
    private String[] list;
    private EditText time;
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
        TextView chosenDate = (TextView) findViewById(R.id.chosen_date_id);
        TextView chosenSubject = (TextView) findViewById(R.id.chosenSubject_id);
        time = (EditText) findViewById(R.id.time_id);
        datePicker = (DatePicker) findViewById(R.id.date_picker_id);
        scheduleClient = new ScheduleClient(this);
        scheduleClient.doBindService();
    }

    public void initButtons() {
        subjectButton = (Button) findViewById(R.id.subject_button_id);
        saveButton = (Button) findViewById(R.id.add_button_learnplaner_id);
        Button learnplannerButton = (Button) findViewById(R.id.schedule_planner_button_id);
        learnplannerButton.setEnabled(false);
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
                timePicker = new TimePickerDialog(LearnplannerActivity.this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time.setText(selectedHour + ":" + selectedMinute);
                        hour = selectedHour;
                        minute = selectedMinute;
                    }
                }, 0, 0, true);
                timePicker.setTitle(Constants.SELECT_DATE);
                timePicker.show();
            }
        });
    }

    private void subjectButtonClicked() {
        Intent i = new Intent(LearnplannerActivity.this, StartActivity.class);
        startActivity(i);
    }

    private void saveButtonClicked() {
        //Subject selectedSubject = getSelectedSubject();


        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();
        String date = String.valueOf(day) + "." + String.valueOf(month) + "." + String.valueOf(year);
        String chosenTime = time.getText().toString();
        if (date.isEmpty() || chosenTime.isEmpty() || subjectSpinner.getSelectedItem() == null){
            Toast.makeText(this, "fehlerhafte Eingabe", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "am " + date + " um " + chosenTime + " Uhr wirst du in "
                    + subjectSpinner.getSelectedItem() + " ausgefragt", Toast.LENGTH_LONG).show();
            startScheduleActivity(date, chosenTime);
            onDateSelectedButtonView();
        }
    }

    private void onDateSelectedButtonView(){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        scheduleClient.setAlarmForNotification(c);
        Toast.makeText(this, "Erinnerung am "+ day +"/"+ (month+1) +"/"+ year, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        if(scheduleClient != null)
            scheduleClient.doUnbindService();
        super.onStop();
    }

    private void startScheduleActivity(String date, String time){
        Intent i = new Intent(LearnplannerActivity.this, ScheduleActivity.class);
        i.putExtra(Constants.CHOSEN_DATE, date);
        i.putExtra(Constants.CHOSEN_TIME, time);
        i.putExtra(Constants.CHOSEN_SUBJECT, subjectSpinner.getSelectedItem().toString());
        startActivity(i);
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



    private void getList() {
        Intent i = getIntent();
        if (i != null) {
            Bundle extras = i.getExtras();
            if (extras != null) {
                list = (String[]) extras.get(Constants.SPINNER_SUBJECT_KEY);
            }
        }
    }

    private void initSpinner() {
        subjectSpinner = (Spinner) findViewById(R.id.spinner_chosen_subject_id);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(arrayAdapter);

    }

}

