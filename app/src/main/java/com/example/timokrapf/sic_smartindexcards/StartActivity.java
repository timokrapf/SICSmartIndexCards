package com.example.timokrapf.sic_smartindexcards;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class StartActivity extends FragmentActivity implements AddButtonFragment.OnAddButtonFragmentClicked, AddSubjectFragment.OnAddSubjectButtonClicked{


    private Button scheduleButton;
    private AddButtonFragment addButtonFragment;
    private SubjectViewModel viewModel;
    private SubjectAdapter adapter;
    private AddSubjectFragment addSubjectFragment;
    private RecyclerView recyclerView;
    private TextView emptyText;

    private String selectedSubjectTitle;
    private Subject selectedSubject;
    private boolean subjectIsSelected = false;
    private ActionMode mActionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initAdapter();
        initUI();
        initStartFragment();
        initButtons();
        setClickListener();
        initActionBar();
        initSettings();

    }


    /*
    https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#13 am 07.08.18
     */

    private void initAdapter() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        adapter = new SubjectAdapter(this, new SubjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Subject subject) {
                Intent intent = new Intent(StartActivity.this, SubjectActivity.class);
                intent.putExtra(Constants.SUBJECT_TITLE_KEY, subject.getSubjectTitle());
                startActivity(intent);
            }
            @Override
            public void onItemLongClicked(Subject subject) {
                //viewModel.deleteSubject(subject);

                //select subject (see also SubjectAdapter):
                selectedSubjectTitle = subject.getSubjectTitle();
                subjectIsSelected = true;

                //start Action Mode to display Contextual ActionBar:
                mActionMode = startActionMode(mActionModeCallback);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initUI() {
        emptyText = (TextView) findViewById(android.R.id.empty);
        viewModel = ViewModelProviders.of(this).get(SubjectViewModel.class);
        viewModel.getSubjectsList().observe(this, new Observer<List<Subject>>() {
            @Override
            public void onChanged(@Nullable List<Subject> subjects) {
                adapter.setSubjectList(subjects);
                if(adapter.getItemCount() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    emptyText.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyText.setVisibility(View.GONE);
                }
            }
        });

    }

    private void initButtons() {
        Button subjectButton = (Button) findViewById(R.id.subject_button_id);
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

    }


    private void scheduleButtonClicked() {
        Intent i = new Intent(StartActivity.this, LearnplannerActivity.class);
        startActivity(i);

    }

    private void initStartFragment() {
        addButtonFragment = new AddButtonFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, addButtonFragment);
        transaction.commit();
    }

    @Override
    public void addButtonFragmentClicked() {
        addSubjectFragment = new AddSubjectFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, addSubjectFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void addSubjectButtonClicked(String subjectTitle) {
        if(subjectTitle.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_for_no_subject), Toast.LENGTH_SHORT).show();
        } else {
            Subject  newSubject = new Subject();
            newSubject.setSubjectTitle(subjectTitle);
            newSubject.setNumberOfCards(0);
            if(adapter.isNewSubject(newSubject)) {
                viewModel.insertSubject(newSubject);
                Toast.makeText(getApplicationContext(), subjectTitle + " " + getString(R.string.toast_for_new_subject_was_inserted), Toast.LENGTH_SHORT).show();
                replaceWithAddButtonFragment();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.toast_for_subject_already_exists), Toast.LENGTH_SHORT).show();
                addSubjectFragment.deleteKeyboardEntry();
            }
        }
    }


    private void replaceWithAddButtonFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, addButtonFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void initSettings(){
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }


    //ActionBar:
    private void initActionBar(){
        ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setTitle("Smart Index Cards");
            actionBar.setIcon(R.drawable.logo_sic);
        }
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
                //delete-Button now located in Contextual ActionBar
                break;
            case R.id.settings_button_actionbar:
                //open settings activity
                settingsButtonActionbarClicked();
                break;
        }
        return true;
    }

    private void settingsButtonActionbarClicked(){
        Intent settingsIntent = new Intent(StartActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }


    //Contextual Action Bar. Activated if subject is selected:
    //retrieved form https://developer.android.com/guide/topics/ui/menus
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_context_actionbar, menu);
            //mActionMode.setTitle(selectedSubjectTitle + " ausgewählt");
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            Context context = getApplicationContext();
            switch (item.getItemId()) {
                case R.id.delete_button_actionbar:
                    //show delete dialog (does not work yet):
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                    dialogBuilder.setTitle("Willst du das Fach " + selectedSubjectTitle + " wirklich löschen?");
                    dialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //todo delete item
                            //not working. Need Thread apparently:
                            //selectedSubject = viewModel.fetchSubject(selectedSubjectTitle);
                            //viewModel.deleteSubject(selectedSubject);


                            String deleteToastText = selectedSubjectTitle + " wurde gelöscht";
                            Context toastContext = getApplicationContext();
                            Toast.makeText(toastContext, deleteToastText, Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });
                    dialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    dialogBuilder.create().show();

                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };
}
