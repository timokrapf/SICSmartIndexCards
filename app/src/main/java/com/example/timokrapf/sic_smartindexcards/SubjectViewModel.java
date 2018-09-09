package com.example.timokrapf.sic_smartindexcards;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;

/*
From https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#8
Minor changes were made
 */
public class SubjectViewModel extends AndroidViewModel {

    //repository to save Data

    private SubjectRepository myRepository;
    private LiveData<List<Subject>> subjects;
    private LiveData<List<Schedule>> schedules;
    private MutableLiveData<List<SmartIndexCards>> cards;

    public SubjectViewModel(@NonNull Application application) {
        super(application);
        myRepository = new SubjectRepository(application);
        subjects = myRepository.getSubjects();
        schedules = myRepository.getScheduleList();
        cards = myRepository.getMyCardsList();
    }

    //getters for observable livedata

    LiveData<List<Schedule>> getSchedulesList() {
        return schedules;
    }

    LiveData<List<Subject>> getSubjectsList(){
        return subjects;
    }

    MutableLiveData<List<SmartIndexCards>> getCards() {
        return cards;
    }



    //methods to invoke subjectrepository methods

    public void insertSubject(Subject subject) {
        myRepository.insertSubject(subject);
    }

    public void deleteSubject(Subject subject) {
        myRepository.deleteSubject(subject);
    }

    public void insertSchedule(Schedule schedule) {
        myRepository.insertSchedule(schedule);
    }

    public void deleteSchedule(Schedule schedule) {
        myRepository.deleteSchedule(schedule);
    }

    public void insertCard(SmartIndexCards card) {
        myRepository.insertCard(card);
    }

    public void deleteCard(SmartIndexCards card) {
        myRepository.deleteCard(card);
    }

    public void updateCard(SmartIndexCards card) {
        myRepository.updateCard(card);
    }

    //sets Cards list which can be observed

    public void findCardsForSubject(String subjectTitle) {
        myRepository.findCardsForSubject(subjectTitle);
    }
    //update subject if necessary. otherwise show toast if no card was created

    public void updateSubjectByName(String subjectTitle, int status) {
        myRepository.findSubjectByName(subjectTitle, status);
    }

    //defines how quizactivity starts subjectactivity after quiz is over

    public void handleNumberOfCards(int number, String subjectTitle) {
        myRepository.handleNumberOfCards(number,subjectTitle);
    }

}
