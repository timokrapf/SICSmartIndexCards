package com.example.timokrapf.sic_smartindexcards;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Subject.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract SubjectDao subjectDao();
}
