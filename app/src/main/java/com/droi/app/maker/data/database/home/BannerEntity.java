package com.droi.app.maker.data.database.home;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "banner")
public class BannerEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo
    public String name;

    @ColumnInfo
    public int type;

    @ColumnInfo
    public String description;

    @ColumnInfo
    public String imageUri;
}
