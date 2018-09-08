package com.example.timokrapf.sic_smartindexcards;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    //Adapter to set schedules to List

    private LayoutInflater inflater;
    private Context context;
    private List<Schedule> scheduleList;
    private OnItemClickListener listener;
    private boolean chooseModeIsOn;

    ScheduleAdapter(Context context, OnItemClickListener listener) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.schedule_item, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        Schedule schedule = scheduleList.get(position);
        holder.bind(schedule, listener);
    }

    //get number of list

    @Override
    public int getItemCount() {
        if(scheduleList != null) {
            return scheduleList.size();
        }
        return 0;
    }

    void setChooseModeIsOn(boolean chooseModeIsOn) {
        this.chooseModeIsOn = chooseModeIsOn;
    }
    void setScheduleList(List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
        notifyDataSetChanged();
    }


    class ScheduleViewHolder extends RecyclerView.ViewHolder {

        TextView subjectTitleView, dateView, timeView;

        public ScheduleViewHolder(View itemView) {
            super(itemView);
            subjectTitleView = itemView.findViewById(R.id.schedule_name);
            dateView = itemView.findViewById(R.id.schedule_date);
            timeView = itemView.findViewById(R.id.schedule_time);
        }

        private void bind(final Schedule schedule, final OnItemClickListener listener) {
            if (scheduleList == null) {
                subjectTitleView.setText(R.string.no_schedulelist);
            } else {
                subjectTitleView.setText(schedule.getSubjectTitle());
                dateView.setText(schedule.getDate());
                timeView.setText(schedule.getTime());
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                                if (!chooseModeIsOn) {
                                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                                    dialogBuilder.setTitle(R.string.schedules_want_to_select);
                                    dialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            listener.onItemLongClicked(schedule, itemView);
                                            chooseModeIsOn = true;
                                            dialog.cancel();
                                        }
                                    });
                                    dialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    dialogBuilder.create().show();
                                }
                                return true;
                            }
                     });
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClicked(schedule, itemView);
                    }
                });
                }
            }
        }
    public interface OnItemClickListener {
        void onItemLongClicked(Schedule schedule, View itemView);
        void onItemClicked(Schedule schedule, View itemView);
    }
}
