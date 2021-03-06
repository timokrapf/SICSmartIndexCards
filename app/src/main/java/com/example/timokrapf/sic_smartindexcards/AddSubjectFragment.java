package com.example.timokrapf.sic_smartindexcards;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class AddSubjectFragment extends Fragment {

    OnAddSubjectButtonClicked mCallback;
    EditText text;

    public AddSubjectFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_subject, container, false);
        Button button = (Button) view.findViewById(R.id.final_add_subject_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = (EditText) view.findViewById(R.id.subject_edit_text);
                String newSubject = text.getText().toString();
                mCallback.addSubjectButtonClicked(newSubject);
            }
        });
        return view;
    }

    public void deleteKeyboardEntry() {
        text.getText().clear();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnAddSubjectButtonClicked) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public interface OnAddSubjectButtonClicked {
        void addSubjectButtonClicked(String subjectTitle);
    }

}
