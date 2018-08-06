package com.example.timokrapf.sic_smartindexcards;

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

    @Query("SELECT * FROM Subject")
    List<Subject> getSubjects();

    @Query("SELECT * FROM Subject WHERE subject_title LIKE :subjectTitle LIMIT 1")
    Subject findSubjectByName(String subjectTitle);
}
