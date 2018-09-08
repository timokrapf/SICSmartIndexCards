package com.example.timokrapf.sic_smartindexcards;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

//Data Access Object with methods
@Dao
public interface SubjectDao {

    @Insert
    void insertSubject(Subject subject);

    @Delete
    void deleteSubject(Subject subject);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateSubject(Subject subject);

    @Query("SELECT * FROM subject_table WHERE subject_title LIKE :subjectTitle LIMIT 1")
    Subject findSubjectByName(String subjectTitle);

    /*
    FROM https://www.sqlite.org/datatype3.html#collation
    Minor changes were made.
     */

    @Query("SELECT * FROM subject_table ORDER BY subject_title COLLATE NOCASE")
    LiveData<List<Subject>> getSubjects();
}
