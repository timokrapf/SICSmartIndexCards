package com.example.timokrapf.sic_smartindexcards;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuizActivity extends FragmentActivity {

    //activity to check if user knows answers to his cards

    private TextView question;
    private EditText answer;

    private String subjectTitle;
    private RelativeLayout topLevelLayout;
    private List<SmartIndexCards> currentCards;
    private SmartIndexCards currentCard;
    private SubjectViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_acivity);
        initUI();
        handleIntent();
        initActionBar();
        setQuestionText();
        initButtons();
        setListener();
        initInstruction();
    }

    //set Listeners for swiping when next card should appear

    private void setListener() {
        View  layout = (View) findViewById(R.id.frame_layout);
        layout.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
            }
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                if(question.getText().equals(currentCard.getQuestion())) {
                    question.setText(currentCard.getAnswer());
                } else if(currentCards.size() == 1){
                    Intent intent = new Intent(QuizActivity.this, SubjectActivity.class);
                    intent.putExtra(Constants.SUBJECT_TITLE_KEY, subjectTitle);
                    intent.putExtra(Constants.TOAST_FOR_ALMOST, getString(R.string.almost_everything_right));
                    startActivity(intent);
                } else {
                    currentCards.remove(currentCard);
                    setCurrentCard();
                }

            }
        });
    }

    //get intent from newsic Activity

    private void handleIntent() {
        Intent intent = getIntent();
        if(intent != null) {
            Bundle extras = intent.getExtras();
            if(extras != null) {
                subjectTitle = extras.getString(Constants.SUBJECT_TITLE_KEY);
            }
        }
    }


    private void initUI(){
        question = (TextView) findViewById(R.id.question_id);
        answer = (EditText) findViewById(R.id.solution_id);
    }

    /*
    Method to show instruction when user opens activity for very first time
    From http://www.christianpeeters.com/android-tutorials/android-tutorial-overlay-with-user-instructions/
    Changes were made.
     */


    private void initInstruction(){
        topLevelLayout = findViewById(R.id.instruction_layer_id);
        if (isFirstTime()) {
            topLevelLayout.setVisibility(View.INVISIBLE);
        }
    }



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
    /*
    Activity to fill question on indexcard
    From https://developer.android.com/reference/java/util/Random
    Minor changes were made
     */

    private void setQuestionText(){
        if(subjectTitle != null) {
            viewModel = ViewModelProviders.of(this).get(SubjectViewModel.class);
            viewModel.findCardsForSubject(subjectTitle);
            viewModel.getCards().observe(this, new Observer<List<SmartIndexCards>>() {
                @Override
                public void onChanged(@Nullable List<SmartIndexCards> cards) {
                    currentCards = cards;
                    setCurrentCard();
                    }
            });

        }
    }

    // method to set question on index card

    private void setCurrentCard() {
        Random random = new Random();
        if(currentCards.size() > 0) {
            int randomInt = random.nextInt(currentCards.size());
            currentCard = currentCards.get(randomInt);
            question.setText(currentCard.getQuestion());
            question.setTextSize(40);

        } else {
            Intent intent = new Intent(QuizActivity.this, SubjectActivity.class);
            intent.putExtra(Constants.SUBJECT_TITLE_KEY, subjectTitle);
            intent.putExtra(Constants.TOAST_FOR_All_QUESTION_ANSWERED, getString(R.string.answered_all_questions));
            startActivity(intent);
        }
    }

     /*
     initialise Buttons
     From https://stackoverflow.com/questions/17973964/how-to-compare-two-strings-in-java-without-considering-spaces
     Minor changes made.
      */

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
        Button enterButton = (Button) findViewById(R.id.save_button_id);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String givenAnswer = answer.getText().toString();
                String rightAnswer = currentCard.getAnswer();
                if(givenAnswer.equals("")) {
                    Toast.makeText(QuizActivity.this, getString(R.string.no_answer_given), Toast.LENGTH_SHORT).show();
                } else if(givenAnswer.replaceAll("\\s+","").compareToIgnoreCase(rightAnswer.replaceAll("\\s+","")) == 0) {
                    answerIsCorrect();
                } else {
                    answerIsWrong();
                }
            }
        });
    }
    /*
    If user's answer is correct, play sound and get next card
    Sound from https://freesound.org/people/Higgs01/sounds/428156/ under creative common 0 license
     */

    private void answerIsCorrect(){
        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);
        Boolean soundSwitchPref = sharedPref.getBoolean
                (SettingsActivity.KEY_PREF_SOUND_SWITCH, false);
        if(soundSwitchPref) {
            MediaPlayer correct = MediaPlayer.create(QuizActivity.this, R.raw.yay);
            correct.start();
        }
        Toast.makeText(QuizActivity.this, getString(R.string.right_answer_given), Toast.LENGTH_SHORT).show();
        answer.setText("");
        currentCard.setWasRightAnswer(true);
        viewModel.updateCard(currentCard);
        currentCards.remove(currentCard);
        setCurrentCard();
    }

    /*If user's answer is incorrect, play sound and let user try again
    Sound from https://freesound.org/search/?q=wrong&f=&s=score+desc&advanced=0&g=1 under creative common 0
    Sound can be turned off in SettingsActivity
    */

    private void answerIsWrong(){
        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);
        Boolean soundSwitchPref = sharedPref.getBoolean
                (SettingsActivity.KEY_PREF_SOUND_SWITCH, false);
        if(soundSwitchPref) {
            MediaPlayer wrong = MediaPlayer.create(QuizActivity.this, R.raw.wrong);
            wrong.start();
        }
        Toast.makeText(QuizActivity.this, getString(R.string.wrong_answer_given), Toast.LENGTH_SHORT).show();
        answer.setText("");
    }

    //go to other activities via navigation buttons at bottom

    private void subjectButtonClicked(){
        Intent i = new Intent(QuizActivity.this, StartActivity.class);
        startActivity(i);
    }

    private void scheduleButtonClicked(){
        Intent i = new Intent (QuizActivity.this, LearnplannerActivity.class);
        startActivity(i);
    }


    // ActionBar

    private void initActionBar(){
        ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setTitle(R.string.quiz);
            actionBar.setIcon(R.drawable.abfrage_karte);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        return true;
    }

    // handle back-button and settings-button in Actionbar being pressed

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_button_actionbar:
                //open settings activity
                Toast.makeText(this, "Einstellungen", Toast.LENGTH_SHORT).show();
                settingsButtonActionbarClicked();
                break;

            case android.R.id.home:
                Intent intent = new Intent(QuizActivity.this, SubjectActivity.class);
                intent.putExtra(Constants.SUBJECT_TITLE_KEY, subjectTitle);
                startActivity(intent);
                break;
        }
        return true;
    }

    private void settingsButtonActionbarClicked(){
        Intent settingsIntent = new Intent(QuizActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }
}
