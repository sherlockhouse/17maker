package com.droi.app.maker.data.database.home;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.text.TextUtils;

@Entity(tableName = "app_order", indices = @Index(value = {"packageName", "className"}, unique = true))
public class AppOrderEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo
    public String packageName;

    @ColumnInfo
    public String className;

    @ColumnInfo
    public String appName;

    @ColumnInfo
    public String appIcon;

    @ColumnInfo
    public String appId;

    @ColumnInfo
    public int user_order;

    @ColumnInfo
    public int def_order;

    @ColumnInfo
    public long lastUseTime;

    @ColumnInfo
    public int deleted;

    public boolean equals(AppOrderEntity entity) {
        if (entity == null) {
            return false;
        }
        return TextUtils.equals(packageName, entity.packageName)
                && TextUtils.equals(className, entity.className)
                && appId == entity.appId;
    }
}
