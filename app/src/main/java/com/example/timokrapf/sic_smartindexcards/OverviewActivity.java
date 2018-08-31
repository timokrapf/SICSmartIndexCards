package com.example.timokrapf.sic_smartindexcards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

public class OverviewActivity extends FragmentActivity {

    private GridView gridView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview_activity);

        initActionBar();
        initUI();
        initButtons();
    }

    private void initUI(){
        initGridView();
    }

    //LAYOUT STEHT THEORETISCH, MUSS NUR ZWECKS BEFÜLLUNG DER GRIDVIEW AUF FERTIGUNG DER NEWSIC WARTEN
    private void initGridView(){
        sic sic_data[] = new sic[]{
                new sic(R.drawable.karteikarte)
        };

        SicAdapter adapter = new SicAdapter(this, R.layout.gridview_item, sic_data);
        gridView = (GridView) findViewById(R.id.gridView_id);
        gridView.setAdapter(adapter);
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


    //ActionBar:
    //todo: if possible: replace initActionBar() with xml style
    private void initActionBar(){
        getActionBar().setTitle(R.string.overview_actionbar);
        getActionBar().setIcon(R.drawable.kartenuebersicht);
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
        }
        return true;
    }

    private void settingsButtonActionbarClicked(){
        Intent settingsIntent = new Intent(OverviewActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }
}
