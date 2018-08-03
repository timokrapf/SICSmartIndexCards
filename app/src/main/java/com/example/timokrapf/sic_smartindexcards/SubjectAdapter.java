package com.example.timokrapf.sic_smartindexcards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class SubjectAdapter extends ArrayAdapter<Subject>{

    private Context context;
    private List<Subject> subjects;

    public SubjectAdapter(Context context, List<Subject> subjects) {
        super(context, R.layout.subject_item, subjects);
        this.context = context;
        this.subjects = subjects;
    }

    @Override
    public View getView(int positon, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.subject_item, null);
        }
        Subject subject = subjects.get(positon);
        if(subject != null) {
            TextView subjectTitle = v.findViewById(R.id.subject);
            subjectTitle.setText(subject.getSubjectTitle());
        }
        return v;
    }
}
