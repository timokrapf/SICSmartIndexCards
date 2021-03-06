package com.example.timokrapf.sic_smartindexcards;




import android.app.FragmentTransaction;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;


public class StartActivity extends FragmentActivity implements AddButtonFragment.OnAddButtonFragmentClicked, AddSubjectFragment.OnAddSubjectButtonClicked{


    private Button scheduleButton;
    private AddButtonFragment addButtonFragment;
    private SubjectViewModel viewModel;
    private SubjectAdapter adapter;
    private AddSubjectFragment addSubjectFragment;
    private RecyclerView recyclerView;
    private TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initAdapter();
        initUI();
        initStartFragment();
        initButtons();
        setClickListener();

    }

    /*
    https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#13 am 07.08.18
     */

    private void initAdapter() {
        recyclerView = findViewById(R.id.recyclerview);
        adapter = new SubjectAdapter(this, new SubjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Subject subject) {
                String subjectTitle = subject.getSubjectTitle();
                Intent intent = new Intent(StartActivity.this, SubjectActivity.class);
                intent.putExtra(Constants.SUBJECT_TITLE_KEY, subjectTitle);
                startActivity(intent);
            }
            @Override
            public void onItemLongClicked(Subject subject) {
                viewModel.delete(subject);
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
            if(adapter.isNewSubject(newSubject)) {
                viewModel.insert(newSubject);
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


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            return false;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            return false;
        }
}
