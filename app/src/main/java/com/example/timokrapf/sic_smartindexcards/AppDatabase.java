package com.example.timokrapf.sic_smartindexcards;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Subject.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract SubjectDao subjectDao();

    /*
     https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#6
      */
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "AppDatabase").build();
                }
            }
        }
        return INSTANCE;
    }
}