package com.example.timokrapf.sic_smartindexcards;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SicAdapter extends ArrayAdapter<sic> {

    Context context;
    int layoutResourceId;
    sic[] data = null;

    public SicAdapter(Context context, int layoutResourceId, sic[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        sicHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new sicHolder();
            holder.imgIcon = (TextView) row.findViewById(R.id.gridView_item_id);
            row.setTag(holder);
        } else {
            holder = (sicHolder) row.getTag();
        }

        sic sic = data[position];
        holder.imgIcon.setText(sic.icon);
        return row;
    }


    static class sicHolder {
        TextView imgIcon;
    }
}
