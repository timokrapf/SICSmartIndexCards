package com.example.timokrapf.sic_smartindexcards;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

//normal entity with getters and setters

@Entity(tableName = "sic")
public class SmartIndexCards {
    @ColumnInfo(name = "subject")
    private String subject;
    @ColumnInfo(name = "question")
    private String question;
    @ColumnInfo(name = "answer")
    private String answer;
    @NonNull
    @ColumnInfo(name = "sic_id")
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "boolean")
    private boolean wasRightAnswer;

    public SmartIndexCards() {

    }

    public String getAnswer() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isWasRightAnswer() {
        return wasRightAnswer;
    }

    public void setWasRightAnswer(boolean wasRightAnswer) {
        this.wasRightAnswer = wasRightAnswer;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }
}
