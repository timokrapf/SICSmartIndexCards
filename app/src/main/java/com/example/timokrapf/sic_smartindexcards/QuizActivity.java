package com.example.timokrapf.sic_smartindexcards;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends FragmentActivity {

    private TextView question;
    private RelativeLayout topLevelLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_acivity);
        initUI();
        initActionBar();
        initButtons();
        initInstruction();

    }



    private void initUI(){
        question = (TextView) findViewById(R.id.question_id);
        setQuestionText();
    }

    private void initInstruction(){
        topLevelLayout = findViewById(R.id.instruction_layer_id);
        if (isFirstTime()) {
            topLevelLayout.setVisibility(View.INVISIBLE);
        }
    }

    /*
    http://www.christianpeeters.com/android-tutorials/android-tutorial-overlay-with-user-instructions/
     */

    @SuppressLint("ClickableViewAccessibility")
    private boolean isFirstTime()
    {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.commit();
            topLevelLayout.setVisibility(View.VISIBLE);
            topLevelLayout.setOnTouchListener(new View.OnTouchListener(){

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    topLevelLayout.setVisibility(View.INVISIBLE);
                    return false;
                }
            });
        }
        return ranBefore;
    }

    private void setQuestionText(){

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
        Intent i = new Intent(QuizActivity.this, StartActivity.class);
        startActivity(i);
    }

    private void scheduleButtonClicked(){
        Intent i = new Intent (QuizActivity.this, LearnplannerActivity.class);
        startActivity(i);
    }


    //ActionBar:
    //todo: if possible: replace initActionBar() with xml style
    private void initActionBar(){
        getActionBar().setTitle(R.string.quiz);
        getActionBar().setIcon(R.drawable.abfrage_karte);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
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
        Intent settingsIntent = new Intent(QuizActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }
}
