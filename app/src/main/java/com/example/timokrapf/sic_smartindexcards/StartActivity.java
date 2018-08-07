package com.example.timokrapf.sic_smartindexcards;



import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import android.support.v4.app.FragmentTransaction;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.Toast;

import java.util.List;


public class StartActivity extends FragmentActivity implements AddButtonFragment.OnAddButtonFragmentClicked, AddSubjectFragment.OnAddSubjectButtonClicked{


    private Button subjectButton, scheduleButton;
    private AddButtonFragment addButtonFragment;
    private SubjectViewModel viewModel;
    private SubjectAdapter adapter;
    private RecyclerView recyclerView;


/*
https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#13
 */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initAdapter();
        initViewModel();
        initStartFragment();
        initButtons();
        setClickListener();

    }

    private void initAdapter() {
        recyclerView = findViewById(R.id.recyclerview);
        adapter = new SubjectAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(SubjectViewModel.class);
        viewModel.getSubjectsList().observe(this, new Observer<List<Subject>>() {
            @Override
            public void onChanged(@Nullable List<Subject> subjects) {
                adapter.setSubjectList(subjects);
            }
        });
    }

    private void initButtons() {
        subjectButton = (Button) findViewById(R.id.subject_button_id);
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

    }

    private void initStartFragment() {
        addButtonFragment = new AddButtonFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, addButtonFragment);
        transaction.commit();
    }

    @Override
    public void addButtonFragmentClicked() {
        AddSubjectFragment addSubjectFragment = new AddSubjectFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
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
            viewModel.insert(newSubject);
            replaceWithAddButtonFragment();
        }
    }

    private void replaceWithAddButtonFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, addButtonFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            return false;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            return false;
        }
}
