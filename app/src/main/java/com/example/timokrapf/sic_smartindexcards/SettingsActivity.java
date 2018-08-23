package com.example.timokrapf.sic_smartindexcards;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SettingsActivity extends Activity {

    private TextView settings1;
    private TextView settings2;
    private TextView settings3;
    private Switch settingsSwitch1;
    private ToggleButton settingsToggle1;
    private RadioButton settingsRadio1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // back-button
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initUI();

    }

    private void initUI() {
        settings1 = findViewById(R.id.settings_test1);
        settings2 = findViewById(R.id.settings_test2);
        settings3 = findViewById(R.id.settings_test3);
        settingsSwitch1 = findViewById(R.id.settings_test1_switch);
        settingsToggle1 = findViewById(R.id.settings_test2_toggle);
        settingsRadio1 = findViewById(R.id.settings_test3_radio);
    }



}
