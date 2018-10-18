package com.droi.app.maker.data.database.home;

import java.util.List;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao
public interface AppOrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AppOrderEntity... entities);

    @Update
    void update(AppOrderEntity... entity);

    @Delete
    void delete(AppOrderEntity... entity);

    @Query("SELECT * FROM app_order WHERE user_order > 0 AND deleted != 1 ORDER BY user_order ASC LIMIT 7")
    List<AppOrderEntity> getHomePanelApp();

    @Query("SELECT * FROM app_order WHERE deleted != 1 ORDER BY def_order ASC")
    List<AppOrderEntity> getAll();

    @Query("SELECT * FROM app_order WHERE deleted != 1 ORDER BY def_order ASC LIMIT :limit")
    List<AppOrderEntity> getAllLimit(int limit);

    @Query("SELECT * FROM app_order WHERE lastUseTime > 0 AND deleted != 1 ORDER BY lastUseTime DESC LIMIT 8")
    List<AppOrderEntity> getRecentUseApp();

    @Query("SELECT * FROM app_order WHERE user_order = 0 AND deleted != 1 ORDER BY user_order ASC LIMIT :limit")
    List<AppOrderEntity> getNeedAddApp(int limit);

    @Query("SELECT * FROM app_order WHERE deleted = 1")
    List<AppOrderEntity> getAllDeleteApp();
}