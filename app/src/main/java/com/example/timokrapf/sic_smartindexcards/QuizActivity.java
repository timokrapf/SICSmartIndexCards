package com.example.timokrapf.sic_smartindexcards;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class QuizActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_subject_item);
    }
}
