package com.example.timokrapf.sic_smartindexcards;

import android.app.Application;
import android.app.PendingIntent;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.util.List;

/*
https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#7
https://www.techotopia.com/index.php/An_Android_Room_Database_and_Repository_Tutorial
 */
public class SubjectRepository  {

    private SubjectDao mySubjectDao;
    private ScheduleDao myScheduleDao;
    private LiveData<List<Subject>> mySubjectList;
    private LiveData<List<Schedule>> myScheduleList;
    private static boolean isNewSubject = true;
    private static boolean isNewSchedule = true;
    private MutableLiveData<Subject> fetchedSubject = new MutableLiveData<>();
    private Context context;

    SubjectRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        mySubjectDao = database.subjectDao();
        mySubjectList = mySubjectDao.getSubjects();
        myScheduleDao = database.scheduleDao();
        myScheduleList = myScheduleDao.getSchedule();
        context = application.getBaseContext();
    }

    LiveData<List<Schedule>> getScheduleList() {
        return myScheduleList;
    }

    LiveData<List<Subject>> getSubjects() {
        return mySubjectList;
    }

    /*
    MutableLiveData<Subject> getFetchedSubject() {
        return fetchedSubject;
    }
    */

    public Subject getFetchedSubject(String subjectTitle) {
        return mySubjectDao.findSubjectByName(subjectTitle);
    }

    public void insertSubject(Subject subject) {
        isNewSubject = true;
        new SubjectUpdateTask(mySubjectDao, myScheduleDao).execute(subject);
    }

    public void deleteSubject(final Subject subject) {
        isNewSubject = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Schedule> scheduleList = myScheduleDao.getScheduleByAttributes(subject.getSubjectTitle());
                for(int i = 0; i < scheduleList.size(); i++) {
                    Schedule schedule = scheduleList.get(i);
                    Intent intent = new Intent(context, ServiceReceiver.class);
                    intent.putExtra(Constants.CHOSEN_SCHEDULE, schedule);
                    intent.putExtra(Constants.RECEIVER_STATUS, Constants.STOP_ALARM_VALUE);
                    context.sendBroadcast(intent);
                }
            }
        }).start();
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


  /*
    public void findSubjectByTitle(String subjectTitle) {
        FindSubjectTask task = new FindSubjectTask(mySubjectDao);
        task.delegate = this;
        task.execute(subjectTitle);
    }


    @Override
    public void asyncFinished(Subject subject) {
        fetchedSubject.setValue(subject);
    }
*/
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
    /*
    private static class FindSubjectTask extends AsyncTask<String, Void, Subject> {

        private SubjectDao subjectDao;
        private SubjectRepository delegate = null;

        FindSubjectTask(SubjectDao dao) {
            subjectDao = dao;
        }

        @Override
        protected Subject doInBackground(String... strings) {
            return subjectDao.findSubjectByName(strings[0]);
        }

        @Override
        protected void onPostExecute(Subject subject) {
            delegate.asyncFinished(subject);
        }
    }
    */

}
