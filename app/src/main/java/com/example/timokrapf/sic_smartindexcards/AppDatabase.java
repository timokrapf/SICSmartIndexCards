package com.example.timokrapf.sic_smartindexcards;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Subject.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract SubjectDao subjectDao();

   /*
    https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#6
     */
    public static AppDatabase getDatabase(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "AppDatabase").addCallback(appDatabaseCallback).build();
        }
        return INSTANCE;
    }
/*
https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#11
 */
    private static RoomDatabase.Callback appDatabaseCallback = new RoomDatabase.Callback() {
        @Override
    public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbTask(INSTANCE).execute();
        }
    };

    private static class PopulateDbTask extends AsyncTask<Void, Void, Void> {

        private final SubjectDao dao;

        PopulateDbTask(AppDatabase database) {
            dao = database.subjectDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAllSubjects();
            Subject subject = new Subject();
            subject.setSubjectTitle("Latein");
            dao.insertSubject(subject);
            return null;
        }
    }
}
