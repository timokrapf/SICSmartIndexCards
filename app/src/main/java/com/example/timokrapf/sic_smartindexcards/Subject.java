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
public class Subject implements Parcelable {

    @NonNull
    @ColumnInfo(name = "subject_id")
    @PrimaryKey(autoGenerate = true)
    private int subjectId;
    @ColumnInfo(name = "subject_title")
    private String subjectTitle;
    @TypeConverters(CardsConverter.class)
    private ArrayList<SmartIndexCards> mCards = null;



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

    public ArrayList<SmartIndexCards> getCards() {
        return mCards;
    }

    public void setCards(ArrayList<SmartIndexCards> cards) {
        this.mCards = cards;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.subjectId);
        dest.writeString(this.subjectTitle);
        dest.writeList(this.mCards);
    }

    protected Subject(Parcel in) {
        this.subjectId = in.readInt();
        this.subjectTitle = in.readString();
        this.mCards = new ArrayList<SmartIndexCards>();
        in.readList(this.mCards, SmartIndexCards.class.getClassLoader());
    }

    public static final Parcelable.Creator<Subject> CREATOR = new Parcelable.Creator<Subject>() {
        @Override
        public Subject createFromParcel(Parcel source) {
            return new Subject(source);
        }

        @Override
        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };
}
