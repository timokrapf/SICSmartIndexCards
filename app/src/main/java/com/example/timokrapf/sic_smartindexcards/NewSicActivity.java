package com.example.timokrapf.sic_smartindexcards;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class NewSicActivity extends FragmentActivity {

    private EditText question, answer;
    private Subject subject;
    private String emptyText = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_sic_activity);
        initUI();
        initActionBar();
        handleIntent();
        initButtons();
    }


    private void handleIntent() {
        Intent i = getIntent();
        if(i != null) {
            Bundle extras = i.getExtras();
            if(extras != null) {
                subject = extras.getParcelable(Constants.CHOSEN_SUBJECT);
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
        if(subject != null) {
            String questionString = question.getText().toString();
            String answerString = answer.getText().toString();
            if(questionString.equals(emptyText) || answerString.equals(emptyText)) {
               Toast.makeText(this, getString(R.string.no_complete_card), Toast.LENGTH_SHORT).show();
            } else {
                SubjectRepository repository = new SubjectRepository(getApplication());
                SmartIndexCards cards = new SmartIndexCards();
                cards.setAnswer(answerString);
                cards.setQuestion(questionString);
                cards.setSubject(subject.getSubjectTitle());
                repository.insertCard(cards);
                answer.setText(emptyText);
                question.setText(emptyText);
                Toast.makeText(this, getString(R.string.new_card), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void subjectButtonClicked(){
        Intent i = new Intent(NewSicActivity.this, StartActivity.class);
        startActivity(i);
    }

    private void scheduleButtonClicked(){
        Intent i = new Intent (NewSicActivity.this, LearnplannerActivity.class);
        startActivity(i);
    }

    //ActionBar:
    //todo: if possible: replace initActionBar() with xml style
    private void initActionBar(){
        getActionBar().setTitle(R.string.new_sic);
        getActionBar().setIcon(R.drawable.karteikarte);
        getActionBar().setDisplayHomeAsUpEnabled(true);
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
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
        }
        return true;
    }

    private void settingsButtonActionbarClicked(){
        Intent settingsIntent = new Intent(NewSicActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }
}
