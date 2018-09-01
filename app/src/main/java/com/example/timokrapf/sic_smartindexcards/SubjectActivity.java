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
import android.widget.Toast;

import org.w3c.dom.Text;

public class SubjectActivity extends FragmentActivity implements View.OnClickListener{

    private ImageButton newSic, quizCard, overviewSic, progressCard;
    private Button subjectsButton, scheduleButton;
    private TextView activityHeading;
    private String subjectTitle;
    private Subject subject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_activity);
        initUI();
        initActionBar();
        handleIntent();
        setOnClickListeners();

    }

    private void handleIntent() {
        Intent i = getIntent();
        if(i != null) {
            Bundle extras = i.getExtras();
            if(extras != null) {
                subjectTitle = extras.getString(Constants.SUBJECT_TITLE_KEY);
                activityHeading.setText(getString(R.string.subject_activity_header) + " " + subjectTitle);
                //ActionBar title displays selected subject
                getActionBar().setTitle("Fach: " + subjectTitle);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                      SubjectRepository repository = new SubjectRepository(getApplication());
                      subject = repository.getFetchedSubject(subjectTitle);
                    }
                }).start();
            }
        }
    }
    private void initUI() {
        activityHeading = (TextView) findViewById(R.id.subject_activity_textview);
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
        if(subject != null) {
            i.putExtra(Constants.CHOSEN_SUBJECT, subject);
        }
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

    //ActionBar:
    //todo: if possible: replace initActionBar() with xml style
    private void initActionBar(){
        //title is set in handleIntent()
        getActionBar().setIcon(R.drawable.subject_icon_stripes);
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
        Intent settingsIntent = new Intent(SubjectActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }


}
