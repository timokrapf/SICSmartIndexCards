package com.example.timokrapf.sic_smartindexcards;



import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import android.support.v4.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
    private boolean isNewSubject;


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
            public boolean onItemLongClick(AdapterView<?> parent, View view,  int position, long id) {
                Subject subject = list.get(position);
                isNewSubject = false;
                handleDatabase(subject);
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
            final Subject  newSubject = new Subject();
            newSubject.setSubjectTitle(subjectTitle);
            isNewSubject = true;
            handleDatabase(newSubject);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, addButtonFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private void handleDatabase(final Subject subject) {
        new Thread(new Runnable() {
            @Override
            public void run() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   if(isNewSubject) {
                       list.add(subject);
                       database.subjectDao().insertSubject(subject);
                       adapter.notifyDataSetChanged();
                       Intent intent = new Intent(StartActivity.this, SubjectActivity.class);
                       startActivity(intent);
                   }  else {
                      list.remove(subject);
                      database.subjectDao().deleteSubject(subject);
                      adapter.notifyDataSetChanged();
                   }

                }
            });
          }
        }).start();
    }
/*
    class SubjectTask extends AsyncTask<Subject, Integer, String> {

        @Override
        protected String doInBackground(Subject... subjects) {
            Subject subject = subjects[0];
            if (isNewSubject) {
                list.add(subject);
                database.subjectDao().insertSubject(subject);

            } else {
                list.remove(subject);
                database.subjectDao().deleteSubject(subject);
            }
            return subject.getSubjectTitle();
        }

        @Override
        protected void onPostExecute(String subjectTitle) {
            adapter.notifyDataSetChanged();
            if (isNewSubject) {
                Toast.makeText(getApplicationContext(), "Du hast das Fach " + subjectTitle + " hinzugefügt", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Du hast das Fach " + subjectTitle + " gelöscht", Toast.LENGTH_SHORT).show();
            }
        }
    }
*/

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            return false;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            return false;
        }
}
