package com.example.timokrapf.sic_smartindexcards;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

//Room database to persist data of three Entities
@Database(entities = {Subject.class, Schedule.class, SmartIndexCards.class}, version = 12, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;
    //Data Access Objects
    public abstract SubjectDao subjectDao();
    public abstract ScheduleDao scheduleDao();
    public abstract SmartIndexCardsDao cardsDao();

    /*
     From https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#6
     One little change was made.
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
}