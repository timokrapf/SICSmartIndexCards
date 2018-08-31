package com.example.timokrapf.sic_smartindexcards;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Subject.class, Schedule.class}, version = 3)

public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract SubjectDao subjectDao();
    public abstract ScheduleDao scheduleDao();

    /*
     https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#6
      */
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "AppDatabase").fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
    /*
    https://android.jlelse.eu/room-persistence-library-typeconverters-and-database-migration-3a7d68837d6c
     Wir momentan nicht gebraucht

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE subject_table "
                    + " ADD COLUMN subject_plan TEXT");
        }
    };
    */
}