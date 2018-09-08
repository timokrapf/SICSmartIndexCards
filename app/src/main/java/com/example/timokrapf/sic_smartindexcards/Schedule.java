package com.example.timokrapf.sic_smartindexcards;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
/*
made parcelable through generate function of android studio
except for that it´s a normal entity class with getter and setters
 */
@Entity(tableName = "schedule_table")
public class Schedule implements Parcelable {

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
    @ColumnInfo(name = "alarm_time")
    private long alarmTime;
    @ColumnInfo(name = "requestcode")
    private int requestCode;

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

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public void setAlarmTime(long alarmTime) {
        this.alarmTime = alarmTime;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public long getAlarmTime() {
        return alarmTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.subjectTitle);
        dest.writeString(this.date);
        dest.writeString(this.time);
        dest.writeInt(this.scheduleId);
        dest.writeLong(this.alarmTime);
        dest.writeInt(this.requestCode);
    }

    protected Schedule(Parcel in) {
        this.subjectTitle = in.readString();
        this.date = in.readString();
        this.time = in.readString();
        this.scheduleId = in.readInt();
        this.alarmTime = in.readLong();
        this.requestCode = in.readInt();
    }

    public static final Parcelable.Creator<Schedule> CREATOR = new Parcelable.Creator<Schedule>() {
        @Override
        public Schedule createFromParcel(Parcel source) {
            return new Schedule(source);
        }

        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    };
}
