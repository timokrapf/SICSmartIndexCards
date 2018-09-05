package com.example.timokrapf.sic_smartindexcards;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

    private TextView question;
    private EditText answer;
    private Subject subject;
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
        initInstruction();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if(intent != null) {
            Bundle extras = intent.getExtras();
            if(extras != null) {
                subject = extras.getParcelable(Constants.CHOSEN_SUBJECT);
            }
        }
    }


    private void initUI(){
        question = (TextView) findViewById(R.id.question_id);
        answer = (EditText) findViewById(R.id.solution_id);
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
    /*
    https://developer.android.com/reference/java/util/Random
     */
    private void setQuestionText(){
        if(subject != null) {
            viewModel = ViewModelProviders.of(this).get(SubjectViewModel.class);
            viewModel.findCardsForSubject(subject.getSubjectTitle());
            viewModel.getCards().observe(this, new Observer<List<SmartIndexCards>>() {
                @Override
                public void onChanged(@Nullable List<SmartIndexCards> cards) {
                    if(cards.isEmpty()) {
                        Intent intent = new Intent(QuizActivity.this, SubjectActivity.class);
                        intent.putExtra(Constants.SUBJECT_TITLE_KEY, subject.getSubjectTitle());
                        intent.putExtra(Constants.TOAST_FOR_NO_CARD_CREATED, getString(R.string.no_card_created));
                        startActivity(intent);
                    } else {
                        currentCards = cards;
                        setCurrentCard();
                    }
                }
            });

        }
    }

    private void setCurrentCard() {
        Random random = new Random();
        if(currentCards.size() > 0) {
            int randomInt = random.nextInt(currentCards.size());
            currentCard = currentCards.get(randomInt);
            question.setText(currentCard.getQuestion());
            question.setTextSize(25);
        } else {
            Intent intent = new Intent(QuizActivity.this, SubjectActivity.class);
            intent.putExtra(Constants.SUBJECT_TITLE_KEY, subject.getSubjectTitle());
            intent.putExtra(Constants.TOAST_FOR_All_QUESTION_ANSWERED, getString(R.string.answered_all_questions));
            startActivity(intent);
        }
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
        Button enterButton = (Button) findViewById(R.id.save_button_id);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String givenAnswer = answer.getText().toString();
                String rightAnswer = currentCard.getAnswer();
                if(givenAnswer.equals("")) {
                    Toast.makeText(QuizActivity.this, getString(R.string.no_answer_given), Toast.LENGTH_SHORT).show();
                } else if(givenAnswer.equals(rightAnswer)) {
                    Toast.makeText(QuizActivity.this, getString(R.string.right_answer_given), Toast.LENGTH_SHORT).show();
                    answer.setText("");
                    currentCards.remove(currentCard);
                    setCurrentCard();
                } else {
                    Toast.makeText(QuizActivity.this, getString(R.string.wrong_answer_given), Toast.LENGTH_SHORT).show();
                    answer.setText("");
                }
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
        Intent settingsIntent = new Intent(QuizActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }
}
