package com.droi.app.maker.data.database.task;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "task")
public class TaskEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo
    public String taskId;

    @ColumnInfo
    public String taskName;

    @ColumnInfo
    public String taskDescription;
}
