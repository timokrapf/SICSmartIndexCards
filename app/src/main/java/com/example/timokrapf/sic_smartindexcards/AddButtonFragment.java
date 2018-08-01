package com.example.timokrapf.sic_smartindexcards;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AddButtonFragment extends Fragment {

    OnAddButtonFragmentClicked mCallback;

    public AddButtonFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_button, container, false);
        Button button = (Button) v.findViewById(R.id.add_subject_button_id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.addButtonFragmentClicked();
            }
        });
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnAddButtonFragmentClicked) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public interface OnAddButtonFragmentClicked {
         void addButtonFragmentClicked();
    }
}
