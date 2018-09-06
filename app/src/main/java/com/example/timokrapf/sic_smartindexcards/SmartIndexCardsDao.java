package com.example.timokrapf.sic_smartindexcards;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface SmartIndexCardsDao {

    @Insert
    void insertCard(SmartIndexCards card);

    @Delete
    void deleteCard(SmartIndexCards card);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCard(SmartIndexCards card);

    @Query("SELECT * FROM sic WHERE subject LIKE :subjectTitle")
    List<SmartIndexCards> getCardsForSubject(String subjectTitle);

    @Query("SELECT * FROM sic WHERE boolean = :hasBeenAnsweredRight")
    List<SmartIndexCards> getRightAnsweredCards(boolean hasBeenAnsweredRight);

    @Query("DELETE FROM sic WHERE subject LIKE :subjectTitle")
    void deleteCardsByTitle(String subjectTitle);


}
