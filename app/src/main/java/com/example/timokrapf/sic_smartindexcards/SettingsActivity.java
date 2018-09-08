package com.example.timokrapf.sic_smartindexcards;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SettingsActivity extends AppCompatActivity {

    //Activity were you can set individual settings

    public static final String KEY_PREF_NOTIFICATION_SWITCH = "notification_pref";
    public static final String KEY_PREF_SOUND_SWITCH = "sound_pref";
    public static final String KEY_PREF_VIBRATE_SWITCH = "vibrate_pref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
        initActionBar();
    }

    //set up ActionBar

    private void initActionBar() {
       ActionBar actionBar = getSupportActionBar();
       if(actionBar != null){
           actionBar.setTitle("Smart Index Cards");
           actionBar.setIcon(R.drawable.logo_sic);
           actionBar.setDisplayShowHomeEnabled(true);
           actionBar.setDisplayHomeAsUpEnabled(true);
       }
    }

    //init back Buttons

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
