package com.example.timokrapf.sic_smartindexcards;

import android.app.Application;
import android.app.PendingIntent;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

/*
Idea from https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#7 and
https://www.techotopia.com/index.php/An_Android_Room_Database_and_Repository_Tutorial
Multiple changes were made.
This repository serves as bridge between Room Database and its Data Access Objects
 */
public class SubjectRepository implements AsyncResult {

    //Data Access Objects

    private SubjectDao mySubjectDao;
    private ScheduleDao myScheduleDao;
    private SmartIndexCardsDao myCardsDao;

    //Observeable LiveData containing different lists of database items

    private LiveData<List<Subject>> mySubjectList;
    private LiveData<List<Schedule>> myScheduleList;
    private MutableLiveData<List<SmartIndexCards>> myCardsList = new MutableLiveData<>();

    //Booleans to check if object should be deleted or add

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

    //getter for SubjectViewModel

    LiveData<List<Schedule>> getScheduleList() {
        return myScheduleList;
    }

    LiveData<List<Subject>> getSubjects() {
        return mySubjectList;
    }

    MutableLiveData<List<SmartIndexCards>> getMyCardsList() {
        return myCardsList;
    }

    //defines how quizactivity starts subjectactivity after quiz is over

    public void handleNumberOfCards(final int number, final String subjectTitle) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String forToast;
                if(number == myCardsDao.getNumber(subjectTitle)) {
                    forToast = context.getString(R.string.answered_all_questions);

                } else {
                    forToast = context.getString(R.string.almost_everything_right);
                }
                Intent intent = new Intent(context, SubjectActivity.class);
                intent.putExtra(Constants.SUBJECT_TITLE_KEY, subjectTitle);
                intent.putExtra(Constants.TOAST_FOR_QUIZ_IS_OVER, forToast);
                context.startActivity(intent);
            }
        }).start();
    }

    //method to update specific card

    public void updateCard(SmartIndexCards card) {
        new UpdateOldCardTask(myCardsDao).execute(card);
    }

    //method to delete one schedule item by its Requestcode

    public void removeScheduleByRequestCode(int requestcode) {
        new RemoveScheduleByRequestcodeTask(myScheduleDao).execute(requestcode);
    }

    //method to get one specific subject by its title

    public Subject getFetchedSubject(String subjectTitle) {
        return mySubjectDao.findSubjectByName(subjectTitle);
    }

    //methods to delete and add specific item

    public void insertSubject(Subject subject) {
        isNewSubject = true;
        new SubjectUpdateTask(mySubjectDao, myScheduleDao, myCardsDao).execute(subject);
    }

    //also stops app from sending notification
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
        new SubjectUpdateTask(mySubjectDao, myScheduleDao, myCardsDao).execute(subject);
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


    /*
    From https://www.techotopia.com/index.php/An_Android_Room_Database_and_Repository_Tutorial
    Code was adapted to fit into context
     */

    public void findCardsForSubject(String subjectTitle) {
        CardsTask task = new CardsTask(myCardsDao);
        task.delegate = this;
        task.execute(subjectTitle);
    }


    public void findSubjectByName(String subjectTitle, int status) {
        FindSubjectTask task = new FindSubjectTask(mySubjectDao, myCardsDao, status);
        task.delegate = this;
        task.execute(subjectTitle);
    }

    @Override
    public void cardsTaskFinished(List<SmartIndexCards> cards) {
        myCardsList.setValue(cards);
    }

    @Override
    public void findSubjectTaskFinished(Subject subject, int status) {
        if(status == 0) {
            if(subject.getNumberOfCards() == 0) {
                Toast.makeText(context, context.getString(R.string.no_card_created), Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(context, QuizActivity.class);
                intent.putExtra(Constants.SUBJECT_TITLE_KEY, subject.getSubjectTitle());
                context.startActivity(intent);
            }
        }
    }

    //AsyncTasks to handle specific database tasks like adding or updating

    private static class SubjectUpdateTask extends AsyncTask<Subject, Void, String> {

        private SubjectDao subjectTaskDao;
        private ScheduleDao scheduleDao;
        private SmartIndexCardsDao smartIndexCardsDao;

        SubjectUpdateTask(SubjectDao dao, ScheduleDao dataAccessO, SmartIndexCardsDao sicDao) {
            subjectTaskDao = dao;
            scheduleDao = dataAccessO;
            smartIndexCardsDao = sicDao;

        }

        @Override
        protected String doInBackground(Subject... subjects) {
            Subject currentSubject = subjects[0];
            if(isNewSubject) {
                subjectTaskDao.insertSubject(currentSubject);
            } else {
                subjectTaskDao.deleteSubject(currentSubject);
                scheduleDao.removeScheduleByName(currentSubject.getSubjectTitle());
                smartIndexCardsDao.deleteCardsByTitle(currentSubject.getSubjectTitle());
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

    /*
       From https://www.techotopia.com/index.php/An_Android_Room_Database_and_Repository_Tutorial
       Code was adapted to fit into context
        */
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
            delegate.cardsTaskFinished(cards);
        }
    }

    private static class UpdateOldCardTask extends AsyncTask<SmartIndexCards, Void, Void> {

        private SmartIndexCardsDao cardsDao;

        UpdateOldCardTask(SmartIndexCardsDao dao) {
            cardsDao = dao;
        }

        @Override
        protected Void doInBackground(SmartIndexCards... smartIndexCards) {
            cardsDao.updateCard(smartIndexCards[0]);
            return null;
        }
    }
    //Update subject if necessary or send toast if no card was created
    private static class FindSubjectTask extends AsyncTask<String, Void, Subject> {

        private SubjectRepository delegate = null;
        private SubjectDao dao;
        private SmartIndexCardsDao cardsDao;
        private int status;

        FindSubjectTask(SubjectDao dao,SmartIndexCardsDao cardsDao, int status) {
            this.dao = dao;
            this.status = status;
            this.cardsDao = cardsDao;
        }
        @Override
        protected Subject doInBackground(String... strings) {
            Subject subject =dao.findSubjectByName(strings[0]);
            if(status == Constants.UPDATE_SUBJECT_NUMBER) {
                subject.setNumberOfCards(cardsDao.getNumber(strings[0]));
                dao.updateSubject(subject);
            }
            return subject;
        }

        @Override
        protected void onPostExecute(Subject subject) {
            delegate.findSubjectTaskFinished(subject, status);
        }
    }
}