package com.example.timokrapf.sic_smartindexcards;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

/*
https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#7
 */
public class SubjectRepository {

    private SubjectDao mySubjectDao;
    private LiveData<List<Subject>> mySubjectList;
    private static boolean isNewSubject;

    SubjectRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        mySubjectDao = database.subjectDao();
        mySubjectList = mySubjectDao.getSubjects();
    }

    LiveData<List<Subject>> getSubjects() {
        return mySubjectList;
    }

    public void insert(Subject subject) {
        isNewSubject = true;
        new SubjectTask(mySubjectDao).execute(subject);
    }
    public void delete(Subject subject) {
        isNewSubject = false;
        new SubjectTask(mySubjectDao).execute(subject);
    }

    private static class SubjectTask extends AsyncTask<Subject, Void, Void> {

        private SubjectDao subjectTaskDao;

        SubjectTask(SubjectDao dao) {
            subjectTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Subject... subjects) {
            if(isNewSubject) {
                subjectTaskDao.insertSubject(subjects[0]);
            } else {
                subjectTaskDao.deleteSubject(subjects[0]);
            }
            return null;
        }
    }

}
