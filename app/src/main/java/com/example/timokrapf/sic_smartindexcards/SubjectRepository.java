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
public class SubjectRepository implements AsyncResult {

    private SubjectDao mySubjectDao;
    private ScheduleDao myScheduleDao;
    private SmartIndexCardsDao myCardsDao;
    private LiveData<List<Subject>> mySubjectList;
    private LiveData<List<Schedule>> myScheduleList;
    private MutableLiveData<List<SmartIndexCards>> myCardsList = new MutableLiveData<>() ;
    private static boolean isNewSubject = true;
    private static boolean isNewSchedule = true;
    private static boolean isNewCard = true;
    private Context context;

    SubjectRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        mySubjectDao = database.subjectDao();
        mySubjectList = mySubjectDao.getSubjects();
        myScheduleDao = database.scheduleDao();
        myScheduleList = myScheduleDao.getSchedule();
        myCardsDao = database.cardsDao();
        context = application.getBaseContext();
    }

    LiveData<List<Schedule>> getScheduleList() {
        return myScheduleList;
    }

    LiveData<List<Subject>> getSubjects() {
        return mySubjectList;
    }

    MutableLiveData<List<SmartIndexCards>> getMyCardsList() {
        return myCardsList;
    }

    /*
    MutableLiveData<Subject> getFetchedSubject() {
        return fetchedSubject;
    }
    */

    public void removeScheduleByRequestCode(int requestcode) {
        new RemoveScheduleByRequestcodeTask(myScheduleDao).execute(requestcode);
    }

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

    public void insertCard(SmartIndexCards card) {
        isNewCard = true;
        new SicUpdateTask(myCardsDao).execute(card);
    }

    public void deleteCard(SmartIndexCards card) {
        isNewCard = false;
        new SicUpdateTask(myCardsDao).execute(card);
    }

    public void insertSchedule(Schedule schedule) {
        isNewSchedule = true;
        new ScheduleUpdateTask(myScheduleDao).execute(schedule);
    }

    public void deleteSchedule(Schedule schedule) {
        isNewSchedule = false;
        new ScheduleUpdateTask(myScheduleDao).execute(schedule);
    }


    public void findCardsForSubject(String subjectTitle) {
        CardsTask task = new CardsTask(myCardsDao);
        task.delegate = this;
        task.execute(subjectTitle);
    }


    //try: highlight subject
    public void highlightSubject(Subject subject){
        //select item
    }


    @Override
    public void asyncFinished(List<SmartIndexCards> cards) {
        myCardsList.setValue(cards);
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

    private static class SicUpdateTask extends AsyncTask<SmartIndexCards, Void, Void> {

        private SmartIndexCardsDao cardsDao;

        SicUpdateTask(SmartIndexCardsDao dao) {
            cardsDao = dao;
        }

        @Override
        protected Void doInBackground(SmartIndexCards... smartIndexCards) {
            if(isNewCard) {
                cardsDao.insertCard(smartIndexCards[0]);
            } else {
                cardsDao.deleteCard(smartIndexCards[0]);
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

    private static class RemoveScheduleByRequestcodeTask extends AsyncTask<Integer, Void, Void> {

        private ScheduleDao scheduleDao;

        RemoveScheduleByRequestcodeTask(ScheduleDao dao) {
           scheduleDao = dao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            scheduleDao.removeScheduleByRequestcode(integers[0]);
            return null;
        }
    }


    private static class CardsTask extends AsyncTask<String, Void, List<SmartIndexCards>> {

        private SmartIndexCardsDao cardsDao;
        private SubjectRepository delegate = null;

        CardsTask(SmartIndexCardsDao dao) {
            cardsDao = dao;
        }

        @Override
        protected List<SmartIndexCards> doInBackground(String... strings) {
            return cardsDao.getCardsForSubject(strings[0]);
        }

        @Override
        protected void onPostExecute(List<SmartIndexCards> cards) {
            delegate.asyncFinished(cards);
        }
    }


}
