package com.example.timokrapf.sic_smartindexcards;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

/*
https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#8
 */
public class SubjectViewModel extends AndroidViewModel {

    private SubjectRepository myRepository;
    private LiveData<List<Subject>> subjects;
    private LiveData<List<Schedule>> schedules;

    public SubjectViewModel(@NonNull Application application) {
        super(application);
        myRepository = new SubjectRepository(application);
        subjects = myRepository.getSubjects();
        schedules = myRepository.getScheduleList();
    }

    LiveData<List<Schedule>> getSchedulesList() {
        return schedules;
    }

    LiveData<List<Subject>> getSubjectsList(){
        return subjects;
    }


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
}
