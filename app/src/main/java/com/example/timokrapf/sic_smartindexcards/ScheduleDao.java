package com.example.timokrapf.sic_smartindexcards;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ScheduleDao {

    @Insert
    void insertSchedule(Schedule schedule);

    @Delete
    void deleteSchedule(Schedule schedule);

    @Query("DELETE FROM schedule_table WHERE requestcode LIKE :requestcode")
    void removeScheduleByRequestcode(int requestcode);

    @Query("SELECT * FROM schedule_table ORDER BY schedule_date")
    LiveData<List<Schedule>> getSchedule();

    @Query("SELECT * FROM schedule_table WHERE schedule_title LIKE :scheduleTitle ")
    List <Schedule> getScheduleByAttributes(String scheduleTitle);

    @Query("DELETE FROM schedule_table WHERE schedule_title LIKE :scheduleTitle")
    void removeScheduleByName(String scheduleTitle);

}
