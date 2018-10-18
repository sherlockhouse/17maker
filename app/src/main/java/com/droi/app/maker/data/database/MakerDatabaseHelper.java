package com.droi.app.maker.data.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

public class MakerDatabaseHelper {

    public static MakerDatabase newInstance(Context context) {
        MakerDatabase database = Room.databaseBuilder(context.getApplicationContext(),
                MakerDatabase.class, "maker.db")
                .addCallback(new RoomDatabase.Callback() {
                    /**
                     * Called when the database is created for the first time. This is called after all the
                     * tables are created.
                     *
                     * @param db The database.
                     */
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {

                    }

                    /**
                     * Called when the database has been opened.
                     * @param db The database.
                     */
                    @Override
                    public void onOpen(@NonNull SupportSQLiteDatabase db) {

                    }
                })
                .allowMainThreadQueries()
//                .addMigrations(
//                        new Migration(1, 2) {
//                            @Override
//                            public void migrate(@NonNull SupportSQLiteDatabase database) {
//
//                            }
//                        }, new Migration(2, 3) {
//                            @Override
//                            public void migrate(@NonNull SupportSQLiteDatabase database) {
//
//                            }
//                        })
                .fallbackToDestructiveMigration()
                .build();

        return database;
    }
}
