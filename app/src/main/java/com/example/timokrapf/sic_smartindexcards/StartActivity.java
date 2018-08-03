package com.example.timokrapf.sic_smartindexcards;



import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import android.support.v4.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;


public class StartActivity extends FragmentActivity implements AddButtonFragment.OnAddButtonFragmentClicked, AddSubjectFragment.OnAddSubjectButtonClicked{

    private ListView listView;
    private Button subjectButton, scheduleButton;
    private AddSubjectFragment addSubjectFragment;
    private AddButtonFragment addButtonFragment;
    private AppDatabase database;
    private SubjectAdapter adapter;
    private List<Subject> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initUI();
        initDatabaseConnection();
        initStartFragment();
        initButtons();
        setClickListener();

    }

    private void initUI() {
      listView = (ListView) findViewById(R.id.subject_list_id);
      TextView emptyView = (TextView) findViewById(android.R.id.empty);
      listView.setEmptyView(emptyView);

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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new DeleteSubjectTask().execute(position);
                return false;
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

    private void initDatabaseConnection() {
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app-database").allowMainThreadQueries().build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                list = database.subjectDao().getSubjects();
                adapter = new SubjectAdapter(StartActivity.this, list);
                listView.setAdapter(adapter);
            }
        }).start();
    }



    @Override
    public void addButtonFragmentClicked() {
        addSubjectFragment = new AddSubjectFragment();
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
            new AddSubjectTask().execute(subjectTitle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, addButtonFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private class DeleteSubjectTask extends AsyncTask<Integer, Integer, Subject> {

        @Override
        protected Subject doInBackground(Integer... subjectPositions) {
            return list.get(subjectPositions[0]);
        }

        @Override
        protected void onPostExecute(Subject subject) {
            list.remove(subject);
            database.subjectDao().deleteSubject(subject);
            adapter.notifyDataSetChanged();
        }
    }

    private class AddSubjectTask extends AsyncTask<String, Integer, Subject> {

        @Override
        protected Subject doInBackground(String... subjectTitles) {
            Subject subject = new Subject();
            subject.setSubjectTitle(subjectTitles[0]);
            return subject;
        }

        @Override
        protected void onPostExecute(Subject subject) {
            list.add(subject);
            database.subjectDao().insertSubject(subject);
            adapter.notifyDataSetChanged();
        }
    }
}
