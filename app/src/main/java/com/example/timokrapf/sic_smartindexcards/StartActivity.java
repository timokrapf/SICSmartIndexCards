package com.example.timokrapf.sic_smartindexcards;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity {

    private ListView listView;
    private TextView emptyListText;
    private Button addSubjectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initUI();
    }

    private void initUI() {
      listView = (ListView) findViewById(R.id.subjec_list_id);
      emptyListText = (TextView) findViewById(R.id.empty_list_id);
      listView.setEmptyView(emptyListText);
    }
}
