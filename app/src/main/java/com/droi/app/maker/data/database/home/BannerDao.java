package com.droi.app.maker.data.database.home;

import java.util.List;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao
public interface BannerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BannerEntity... entities);

    @Update
    void update(BannerEntity... entities);

    @Insert
    void delete(BannerEntity entity);

    @Query("SELECT * FROM banner")
    List<BannerEntity> getAll();
}
