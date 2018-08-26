package com.example.timokrapf.sic_smartindexcards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewSicActivity extends FragmentActivity {

    private EditText question, answer;
    private String subjectTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_sic_activity);
        initUI();
        handleIntent();
        initButtons();
        saveNewCards();
    }

    private void handleIntent() {
        Intent i = getIntent();
        if(i != null) {
            Bundle extras = i.getExtras();
            if(extras != null) {
                subjectTitle = extras.getString(Constants.SUBJECT_TITLE_KEY);
            }
        }
    }

    private void initUI(){
        question = (EditText) findViewById(R.id.question_id);
        answer = (EditText) findViewById(R.id.answer_id);

    }

    private void initButtons(){
        Button saveButton = (Button) findViewById(R.id.save_button_id);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButtonClicked();
            }
        });
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

    private void saveButtonClicked(){

    }

    private void subjectButtonClicked(){
        Intent i = new Intent(NewSicActivity.this, StartActivity.class);
        startActivity(i);
    }

    private void scheduleButtonClicked(){
        Intent i = new Intent (NewSicActivity.this, LearnplannerActivity.class);
        startActivity(i);
    }

    private void saveNewCards(){
        String finalQuestion = question.getText().toString();
        String finalAnswer = answer.getText().toString();
    }
}
