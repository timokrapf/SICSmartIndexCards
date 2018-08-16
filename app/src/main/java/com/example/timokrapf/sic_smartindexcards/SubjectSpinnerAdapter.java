package com.example.timokrapf.sic_smartindexcards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class SubjectSpinnerAdapter extends BaseAdapter {

    private List<Subject> list;
    private Context context;
    private LayoutInflater inflater;
    private TextView view;


    public SubjectSpinnerAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    void setSubjectList(List<Subject> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    String getSubjectTitle() {
        if(view != null) {
            return view.getText().toString();
        }
        return context.getString(R.string.no_subject_was_chosen);
    }
    @Override
    public int getCount() {
        if(list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Subject getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.recyclerview_subject_item, null);
        view = (TextView) convertView.findViewById(R.id.recyclerview_textview);
        if(list != null) {
            view.setText(list.get(position).getSubjectTitle());
        }
        return convertView;
    }
}
