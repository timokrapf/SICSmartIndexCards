package com.example.timokrapf.sic_smartindexcards;

import android.app.Activity;
import android.app.Fragment;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ProgressActivity extends FragmentActivity {

    private String subjectTitle;
    private ArrayList<SmartIndexCards> rightCards;
    private TextView progressText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_activity);
        handleIntent();
        initUI();
        initActionBar();
        initButtons();
        setValuesForProgressText();

    }

    private void handleIntent() {
        Intent intent = getIntent();
        if(intent != null) {
            Bundle extras = intent.getExtras();
            if(extras != null) {
                subjectTitle = extras.getString(Constants.SUBJECT_TITLE_KEY);
            }
        }
    }

    private void setValuesForProgressText() {
        rightCards = new ArrayList<>();
        SubjectViewModel model = ViewModelProviders.of(this).get(SubjectViewModel.class);
        if(subjectTitle != null) {
            model.findCardsForSubject(subjectTitle);
            model.getCards().observe(this, new Observer<List<SmartIndexCards>>() {
                @Override
                public void onChanged(@Nullable List<SmartIndexCards> cards) {
                    if(!cards.isEmpty()) {
                        for(int i = 0; i < cards.size(); i++) {
                            SmartIndexCards currentCard = cards.get(i);
                            if(currentCard.isWasRightAnswer()) {
                                rightCards.add(currentCard);
                            }
                        }
                        progressText.setText("Du hast bereits " + rightCards.size() +  " von " + cards.size() + " Karten richtig beantwortet");
                    }
                }
            });
        }

    }

    private void initUI(){
        progressText = (TextView)findViewById(R.id.progress_textview_id);
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
        Intent i = new Intent(ProgressActivity.this, StartActivity.class);
        startActivity(i);
    }

    private void scheduleButtonClicked(){
        Intent i = new Intent (ProgressActivity.this, LearnplannerActivity.class);
        startActivity(i);
    }

    //ActionBar:
    //todo: if possible: replace initActionBar() with xml style
    private void initActionBar(){
        getActionBar().setTitle(R.string.progress);
        getActionBar().setIcon(R.drawable.lernerfolg_karte);
        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_button_actionbar:
                //open settings activity
                Toast.makeText(this, "Einstellungen", Toast.LENGTH_SHORT).show();
                settingsButtonActionbarClicked();
                break;
            case android.R.id.home:
                Intent intent = new Intent(ProgressActivity.this, SubjectActivity.class);
                intent.putExtra(Constants.SUBJECT_TITLE_KEY, subjectTitle);
                startActivity(intent);
                break;
        }

        return true;
    }

    private void settingsButtonActionbarClicked(){
        Intent settingsIntent = new Intent(ProgressActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }
}
