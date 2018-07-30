package com.example.timokrapf.sic_smartindexcards;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class StartActivity extends AppCompatActivity {

    private ListView listView;
    private TextView emptyListText;
    private Button addSubjectButton, subjectButton, scheduleButton;
    private ArrayList<String> subjectList;
    private ArrayAdapter<String> arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initUI();
        initButtons();
        setClickListener();
    }

    private void initUI() {
      listView = (ListView) findViewById(R.id.subjec_list_id);
      emptyListText = (TextView) findViewById(R.id.empty_list_id);
      listView.setEmptyView(emptyListText);
    }

    private void initButtons() {
        subjectButton = (Button) findViewById(R.id.subject_button_id);
        addSubjectButton = (Button) findViewById(R.id.add_subject_button_id);
        scheduleButton = (Button) findViewById(R.id.schedule_planner_button_id);
        subjectButton.setEnabled(false);
    }

    private void setClickListener() {
        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleButtonClicked();
            }
        });
        addSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSubjectButtonClicked();
            }
        });
    }

    private void addSubjectButtonClicked() {

    }

    private void scheduleButtonClicked() {

    }
}
