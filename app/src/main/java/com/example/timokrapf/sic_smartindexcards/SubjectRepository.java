package com.example.timokrapf.sic_smartindexcards;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

/*
https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#7
 */
public class SubjectRepository {

    private SubjectDao mySubjectDao;
    private ScheduleDao myScheduleDao;
    private LiveData<List<Subject>> mySubjectList;
    private LiveData<List<Schedule>> myScheduleList;
    private static boolean isNewSubject = true;
    private static boolean isNewSchedule = true;
    private LiveData<Subject> fetchedSubject;
    private Schedule fetchedSchedule;

    SubjectRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        mySubjectDao = database.subjectDao();
        mySubjectList = mySubjectDao.getSubjects();
        myScheduleDao = database.scheduleDao();
        myScheduleList = myScheduleDao.getSchedule();
    }

    LiveData<List<Schedule>> getScheduleList() {
        return myScheduleList;
    }

    LiveData<List<Subject>> getSubjects() {
        return mySubjectList;
    }

    public void insertSubject(Subject subject) {
        isNewSubject = true;
        new SubjectUpdateTask(mySubjectDao, myScheduleDao).execute(subject);
    }

    public void deleteSubject(Subject subject) {
        isNewSubject = false;
        new SubjectUpdateTask(mySubjectDao, myScheduleDao).execute(subject);
    }

    public void insertSchedule(Schedule schedule) {
        isNewSchedule = true;
        new ScheduleUpdateTask(myScheduleDao).execute(schedule);
    }

    public void deleteSchedule(Schedule schedule) {
        isNewSchedule = false;
        new ScheduleUpdateTask(myScheduleDao).execute(schedule);
    }

    public LiveData<Subject> fetchOneSubjectByTitle(final String subjectTitle) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                fetchedSubject = mySubjectDao.findSubjectByName(subjectTitle);
            }
        }).start();
        return fetchedSubject;
    }

    public Schedule getScheduleByAttributes(final String subject, final String date, final String time) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                fetchedSchedule = myScheduleDao.getScheduleByAttributes(subject, date, time);
            }
        }).start();
        return fetchedSchedule;
    }

    private static class SubjectUpdateTask extends AsyncTask<Subject, Void, String> {

        private SubjectDao subjectTaskDao;
        private ScheduleDao scheduleDao;


        SubjectUpdateTask(SubjectDao dao, ScheduleDao dataAccessO) {
            subjectTaskDao = dao;
            scheduleDao = dataAccessO;
        }

        @Override
        protected String doInBackground(Subject... subjects) {
            Subject currentSubject = subjects[0];
            if(isNewSubject) {
                subjectTaskDao.insertSubject(currentSubject);
            } else {
                subjectTaskDao.deleteSubject(currentSubject);
                scheduleDao.removeScheduleByName(currentSubject.getSubjectTitle());
            }
            return null;
        }
    }

    private static class ScheduleUpdateTask extends AsyncTask<Schedule, Void, String> {

        private ScheduleDao scheduleDao;

        ScheduleUpdateTask(ScheduleDao dao) {
            scheduleDao = dao;
        }

        @Override
        protected String doInBackground(Schedule... schedules) {
            Schedule currentSchedule = schedules[0];
            if(isNewSchedule) {
                scheduleDao.insertSchedule(currentSchedule);
            } else {
                scheduleDao.deleteSchedule(currentSchedule);
            }
            return null;
        }
    }
}
