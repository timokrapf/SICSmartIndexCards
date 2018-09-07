package com.example.timokrapf.sic_smartindexcards;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class OverviewActivity extends FragmentActivity {

    private TextView currentCardView;
    private TextView emptyText;
    private String subjectTitle;
    private RecyclerView view;
    private SubjectViewModel model;
    private SicAdapter adapter;
    private ActionMode mActionMode;
    private ArrayList<SmartIndexCards> cards = new ArrayList<>();
    private ArrayList<TextView> views = new ArrayList<>();
    private boolean multiSelect = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview_activity);
        initActionBar();
        handleIntent();
        initUI();
        initAdapter();
        initViewModel();
        setSwipeListener();
        initButtons();
    }

    private void setSwipeListener() {
        View view = (View) findViewById(R.id.overview_parent);
        view.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeTop() {
                super.onSwipeLeft();
                SmartIndexCards currentCard = adapter.getCurrentCard();
                if(currentCard != null) {
                    if(currentCard.getQuestion().equals(currentCardView.getText().toString())) {
                        currentCardView.setText(currentCard.getAnswer());
                    } else {
                        currentCardView.setText(currentCard.getQuestion());
                    }
                }
            }
        });
    }

    private void initViewModel() {
        if(subjectTitle != null) {
            model = ViewModelProviders.of(this).get(SubjectViewModel.class);
            model.findCardsForSubject(subjectTitle);
            model.getCards().observe(this, new Observer<List<SmartIndexCards>>() {
                @Override
                public void onChanged(@Nullable List<SmartIndexCards> cards) {
                    adapter.setCardsList(cards);
                    updateSubject();
                    if(adapter.getItemCount() == 0) {
                        view.setVisibility(View.GONE);
                        currentCardView.setVisibility(View.GONE);
                        emptyText.setVisibility(View.VISIBLE);
                    } else {
                        view.setVisibility(View.VISIBLE);
                        currentCardView.setVisibility(View.VISIBLE);
                        emptyText.setVisibility(View.GONE);
                    }
                }
            });
        }
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

    private void initAdapter() {
        adapter = new SicAdapter(this, new SicAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(SmartIndexCards card, TextView view) {
                if(multiSelect) {
                    selectItem(card, view);
                    views.add(view);
                } else {
                    currentCardView.setText(card.getQuestion());
                    adapter.setCurrentCard(card);
                }
            }

            @Override
            public void onItemLongClicked(SmartIndexCards card, TextView view) {
                multiSelect = true;
                mActionMode = startActionMode(mActionModeCallback);
                selectItem(card, view);
                views.add(view);
            }
        });
        view.setAdapter(adapter);
        view.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initUI(){
        currentCardView = (TextView) findViewById(R.id.overview_card);
        view = (RecyclerView) findViewById(R.id.recyclerview_overview);
        emptyText = (TextView) findViewById(android.R.id.empty);

    }

    private void updateSubject() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Subject subject = model.fetchSubject(subjectTitle);
                subject.setNumberOfCards(adapter.getItemCount());
                model.updateSubject(subject);
            }
        }).start();
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
        Intent i = new Intent(OverviewActivity.this, StartActivity.class);
        startActivity(i);
    }

    private void scheduleButtonClicked(){
        Intent i = new Intent (OverviewActivity.this, LearnplannerActivity.class);
        startActivity(i);
    }
    private void selectItem(SmartIndexCards card, TextView view) {
        if (multiSelect) {
            if (cards.contains(card)) {
                cards.remove(card);
                view.setBackground(getDrawable(R.drawable.karteikarte));
            } else {
                cards.add(card);
                view.setBackgroundColor(Color.RED);
            }
        }
    }
    //ActionBar:
    //todo: if possible: replace initActionBar() with xml style
    private void initActionBar(){
        getActionBar().setTitle(R.string.overview_actionbar);
        getActionBar().setIcon(R.drawable.kartenuebersicht);
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
            case android.R.id.home:
                Intent intent = new Intent(OverviewActivity.this, SubjectActivity.class);
                intent.putExtra(Constants.SUBJECT_TITLE_KEY, subjectTitle);
                startActivity(intent);
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
        Intent settingsIntent = new Intent(OverviewActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {


        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_context_actionbar, menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
            Context context = OverviewActivity.this;
            switch (item.getItemId()) {
                case R.id.delete_button_actionbar:
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                    dialogBuilder.setTitle(R.string.do_you_want_to_delete);
                    dialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            for(int i = 0; i < cards.size(); i++) {
                                SmartIndexCards card = cards.get(i);
                                model.deleteCard(card);
                            }
                            model.findCardsForSubject(subjectTitle);
                            mode.finish();
                            dialog.cancel();
                        }
                    });
                    dialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mode.finish();
                            dialog.cancel();
                        }
                    });
                    dialogBuilder.create().show();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            adapter.setChooseModeIsOn(false);
            for(int j = 0; j < views.size(); j++) {
                TextView view = views.get(j);
                view.setBackground(getDrawable(R.drawable.karteikarte));
            }
            cards.clear();
            multiSelect = false;
            views.clear();
        }
    };
}

