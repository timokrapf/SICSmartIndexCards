package com.example.timokrapf.sic_smartindexcards;

import android.app.ActionBar;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends FragmentActivity {

    //Activity to show schedule with planned quizes


    private String date, time, subject;
    private ScheduleAdapter adapter;
    private SubjectViewModel viewModel;
    private RecyclerView recyclerView;
    private TextView emptyText;
    private Schedule schedule;
    private ArrayList<View> views = new ArrayList<>();
    private boolean multiSelect = false;
    private ArrayList<Schedule> schedules = new ArrayList<>();
    private ActionMode mActionMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_schedule_activity);
        initUI();
        handleIntent();
        initActionBar();
        initAdapter();
        initModel();
        enterNewScheduleItem();
        initButtons();
    }

    private void initUI() {
        recyclerView = (RecyclerView) findViewById(R.id.schedule_recyclerview);
        emptyText = (TextView) findViewById(android.R.id.empty);

    }

    //initialise own adapter to set up schedules

    private void initAdapter(){
        adapter = new ScheduleAdapter(this, new ScheduleAdapter.OnItemClickListener() {
            @Override
            public void onItemLongClicked(Schedule schedule, View view) {
                multiSelect = true;
                mActionMode = startActionMode(mActionModeCallback);
                selectItem(schedule, view);
                views.add(view);

            }

            @Override
            public void onItemClicked(Schedule schedule, View itemView) {
                if(multiSelect) {
                    selectItem(schedule, itemView);
                    views.add(itemView);
                }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //initialise model to either set scchedules or text if there are no planned schedules


    private void initModel() {
        viewModel = ViewModelProviders.of(this).get(SubjectViewModel.class);
        viewModel.getSchedulesList().observe(this, new Observer<List<Schedule>>() {
            @Override
            public void onChanged(@Nullable List<Schedule> schedules) {
                adapter.setScheduleList(schedules);
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

    //initialise Buttons and set on click Listener

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

    //go to other activities via navigation buttons at bottom

    private void subjectButtonClicked(){
        Intent i = new Intent(ScheduleActivity.this, StartActivity.class);
        startActivity(i);
    }

    private void scheduleButtonClicked(){
        Intent i = new Intent (ScheduleActivity.this, LearnplannerActivity.class);
        startActivity(i);
    }

    //handle intent from Learnplanner Activity

    private void handleIntent(){
        Intent i = getIntent();
        if(i != null) {
            Bundle extras = i.getExtras();
            if (extras != null) {
               schedule = extras.getParcelable(Constants.CHOSEN_SCHEDULE);
            }
        }
    }

    //enter new item to list and send Broadcast in order to send notification

    private void enterNewScheduleItem() {
        if(schedule != null) {
            viewModel.insertSchedule(schedule);
            Intent intent = new Intent(getBaseContext(), ServiceReceiver.class);
            intent.putExtra(Constants.CHOSEN_SCHEDULE, schedule);
            intent.putExtra(Constants.RECEIVER_STATUS, Constants.START_ALARM_VALUE);
            sendBroadcast(intent);
        }
    }

    //possible to delete plannes schedules

    private void selectItem(Schedule schedule, View view) {
        if (multiSelect) {
            if (schedules.contains(schedule)) {
                schedules.remove(schedule);
                view.setBackgroundColor(Color.WHITE);
            } else {
                schedules.add(schedule);
                view.setBackgroundColor(Color.RED);
            }
        }
    }
    // set up ActionBar

    private void initActionBar(){
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.schedule_planner);
            actionBar.setIcon(R.drawable.logo_sic);
            actionBar.setDisplayHomeAsUpEnabled(true);
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
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
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
        Intent settingsIntent = new Intent(ScheduleActivity.this, SettingsActivity.class);
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
            Context context = ScheduleActivity.this;
            switch (item.getItemId()) {
                case R.id.delete_button_actionbar:
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                    dialogBuilder.setTitle(R.string.do_you_want_to_delete);
                    dialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            for(int i = 0; i < schedules.size(); i++) {
                                Schedule schedule = schedules.get(i);
                                Intent intent = new Intent(getBaseContext(), ServiceReceiver.class);
                                intent.putExtra(Constants.CHOSEN_SCHEDULE, schedule);
                                intent.putExtra(Constants.RECEIVER_STATUS, Constants.STOP_ALARM_VALUE);
                                sendBroadcast(intent);
                                viewModel.deleteSchedule(schedule);
                            }
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
                View view = views.get(j);
                view.setBackgroundColor(Color.WHITE);
            }
            schedules.clear();
            multiSelect = false;
            views.clear();
        }
    };
}




