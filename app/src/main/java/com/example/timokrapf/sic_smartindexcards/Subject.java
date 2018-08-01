package com.example.timokrapf.sic_smartindexcards;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Subject {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int subjectId;
    private String subjectTitle;

    public Subject(int subjectId, String subjectTitle) {
        this.subjectId = subjectId;
        this.subjectTitle = subjectTitle;
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
}
