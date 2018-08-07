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
    private LiveData<List<Subject>> subjectsList;

    public SubjectViewModel(@NonNull Application application) {
        super(application);
        myRepository = new SubjectRepository(application);
        subjectsList = myRepository.getSubjects();
    }

    String getSubjectName() {
        return "";
    }

    LiveData<List<Subject>> getSubjectsList(){
        return subjectsList;
    }

    public void insert(Subject subject) {
        myRepository.insert(subject);
    }
     public void delete(Subject subject) {
        myRepository.delete(subject);
     }
}
