package com.example.timokrapf.sic_smartindexcards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SubjectActivity extends FragmentActivity implements View.OnClickListener{

    private ImageButton newSic, quizCard, overviewSic, progressCard;
    private Button subjectsButton, scheduleButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_activity);
        initUI();
        setOnClickListeners();

    }

    private void initUI() {
        TextView activityHeading = (TextView) findViewById(R.id.subject_activity_textview);
        newSic = (ImageButton) findViewById(R.id.new_sic_id);
        quizCard = (ImageButton) findViewById(R.id.quiz_card_id);
        overviewSic = (ImageButton) findViewById(R.id.overview_sics_id);
        progressCard = (ImageButton) findViewById(R.id.progress_card_id);
        subjectsButton = (Button) findViewById(R.id.subject_button_id);
        scheduleButton = (Button) findViewById(R.id.schedule_planner_button_id);
    }

    private void setOnClickListeners(){
        newSic.setOnClickListener(this);
        quizCard.setOnClickListener(this);
        overviewSic.setOnClickListener(this);
        progressCard.setOnClickListener(this);
        subjectsButton.setOnClickListener(this);
        scheduleButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.new_sic_id:
                newSicButtonClicked();
                break;
            case R.id.quiz_card_id:
                quizCardButtonClicked();
                break;
            case R.id.overview_sics_id:
                overviewSicButtonClicked();
                break;
            case R.id.progress_card_id:
                progressCardButtonClicked();
                break;
            case R.id.subject_button_id:
                subjectButtonClicked();
                break;
            case R.id.schedule_planner_button_id:
                scheduleButtonClicked();
                break;
        }
    }

    private void newSicButtonClicked() {
        Intent i = new Intent(SubjectActivity.this, NewSicActivity.class);
        startActivity(i);
    }

    private void quizCardButtonClicked(){
        Intent i = new Intent(SubjectActivity.this, QuizActivity.class);
        startActivity(i);
    }

    private void overviewSicButtonClicked(){
        Intent i = new Intent (SubjectActivity.this, OverviewActivity.class);
        startActivity(i);
    }

    private void progressCardButtonClicked(){
        Intent i = new Intent (SubjectActivity.this, ProgressActivity.class);
        startActivity(i);
    }

    private void subjectButtonClicked(){
        Intent i = new Intent(SubjectActivity.this, StartActivity.class);
        startActivity(i);
    }

    private void scheduleButtonClicked(){
        Intent i = new Intent (SubjectActivity.this, LearnplannerActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }


}
