package com.example.timokrapf.sic_smartindexcards;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SettingsActivity extends PreferenceActivity  {

    private Preference notificationPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        initActionBar();

        /*
        notificationPref = findPreference(getString(R.string.notification_pref_key));
        notificationPref.setOnPreferenceChangeListener(this);
        */
    }


    /*
    @Override
    public boolean onPreferenceChange(Preference preference, Object value){

        Intent notificationPrefIntent = new Intent (SettingsActivity.this, NotifyService.class);

        if (preference == notificationPref){
            if (notificationPref.isEnabled()){
                Toast.makeText(this, "Benachrichtigungen aus", Toast.LENGTH_SHORT).show();
                notificationPrefIntent.putExtra("enabled", true);

            } else{
                Toast.makeText(this, "Benachrichtigungen ein", Toast.LENGTH_SHORT).show();
                notificationPrefIntent.putExtra("enabled", false);
            }
        }

        return true;
    }
    */




    //----------------------------------------------------------------------
    //ActionBar:
    //todo: if possible: replace initActionBar() with xml style
    private void initActionBar(){
        getActionBar().setTitle(R.string.settings);
        getActionBar().setIcon(R.drawable.settings_button_gear);
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
        Intent settingsIntent = new Intent(SettingsActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

}
