package com.example.timokrapf.sic_smartindexcards;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
/*
generate parcelable
 */
@Entity(tableName = "subject_table")
public class Subject  {

    @NonNull
    @ColumnInfo(name = "subject_id")
    @PrimaryKey(autoGenerate = true)
    private int subjectId;
    @ColumnInfo(name = "subject_title")
    private String subjectTitle;
    @ColumnInfo(name = "number_of_cards")
    private int numberOfCards;
    @ColumnInfo
    private boolean isChosen = false;

    public Subject() {
    }

    public void setSubjectId(@NonNull int subjectId) {
        this.subjectId = subjectId;
    }

    @NonNull
    public int getSubjectId() {
        return subjectId;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }

    public int getNumberOfCards() {
        return numberOfCards;
    }

    public void setNumberOfCards(int numberOfCards) {
        this.numberOfCards = numberOfCards;
    }

    public void setChosen(boolean chosen) {
        isChosen = chosen;
    }

    public boolean isChosen() {
        return isChosen;
    }
}
