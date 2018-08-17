package com.example.timokrapf.sic_smartindexcards;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "schedule_table")
public class Schedule {

    @ColumnInfo(name = "schedule_title")
    private String subjectTitle;
    @ColumnInfo(name = "schedule_date")
    private String date;
    @ColumnInfo(name = "schedule_time")
    private String time;
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "schedule_id")
    private int scheduleId;

    public Schedule(){

    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }

    @NonNull
    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(@NonNull int scheduleId) {
        this.scheduleId = scheduleId;
    }
}
