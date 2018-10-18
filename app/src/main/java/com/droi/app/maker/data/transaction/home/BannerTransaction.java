package com.droi.app.maker.data.transaction.home;

import android.content.Context;

import com.droi.app.maker.data.database.MakerDatabase;
import com.droi.app.maker.data.database.MakerDatabaseHelper;
import com.droi.app.maker.data.database.home.BannerEntity;

public class BannerTransaction {
    private MakerDatabase mDb;

    public BannerTransaction(Context context) {
        mDb = MakerDatabaseHelper.newInstance(context);
    }

    public void add(BannerEntity... entities) {
        mDb.bannerDao().insert(entities);
    }
}
