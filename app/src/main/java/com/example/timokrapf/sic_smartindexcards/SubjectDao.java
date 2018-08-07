package com.example.timokrapf.sic_smartindexcards;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;


@Dao
public interface SubjectDao {

    @Insert
    void insertSubject(Subject subject);

    @Delete
    void deleteSubject(Subject subject);

   /*
    https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#5
     */
    @Query("DELETE FROM subject_table")
    void deleteAllSubjects();

    @Query("SELECT * FROM subject_table ORDER BY subject_title ASC")
    LiveData<List<Subject>> getSubjects();

    @Query("SELECT * FROM subject_table WHERE subject_title LIKE :subjectTitle LIMIT 1")
    LiveData<List<Subject>> findSubjectByName(String subjectTitle);

    @Query("SELECT * FROM subject_table WHERE subject_id LIKE :subjectId LIMIT 1")
    LiveData<List<Subject>> findSubjectById(int subjectId);

    /*
    https://www.lukegjpotter.com/2017/12/android-room-database-tutorial-with.html
     */
    @Query("SELECT COUNT(*) FROM subject_table")
    int getNumber();

}
