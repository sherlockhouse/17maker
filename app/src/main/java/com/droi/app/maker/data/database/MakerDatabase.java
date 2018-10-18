package com.droi.app.maker.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.droi.app.maker.data.database.home.AppOrderDao;
import com.droi.app.maker.data.database.home.AppOrderEntity;
import com.droi.app.maker.data.database.home.BannerDao;
import com.droi.app.maker.data.database.home.BannerEntity;
import com.droi.app.maker.data.database.task.TaskDao;
import com.droi.app.maker.data.database.task.TaskEntity;

@Database(entities = {AppOrderEntity.class, BannerEntity.class, TaskEntity.class}, version = 1)
public abstract class MakerDatabase extends RoomDatabase {

    public abstract AppOrderDao appOrderDao();

    public abstract BannerDao bannerDao();

    public abstract TaskDao taskDao();

}
