package com.droi.app.maker.data.transaction.home;

import android.content.Context;

import com.droi.app.maker.R;
import com.droi.app.maker.data.database.MakerDatabaseHelper;
import com.droi.app.maker.data.database.home.AppOrderDao;
import com.droi.app.maker.data.database.home.AppOrderEntity;

import java.util.ArrayList;
import java.util.List;

public class AppOrderTransaction {

    private AppOrderDao mDao;
    private final int mHomePanelAppCount;

    public AppOrderTransaction(Context context) {
        mDao = MakerDatabaseHelper.newInstance(context).appOrderDao();

        mHomePanelAppCount = context.getResources().getInteger(
                R.integer.appinfo_count_in_home_panel_def_value);
    }

    public void add(AppOrderEntity... entities) {
        if (entities == null || entities.length <= 0) {
            mDao.delete(mDao.getAll().toArray(new AppOrderEntity[0]));
            return;
        }

        List<AppOrderEntity> homeList = mDao.getHomePanelApp();

        mDao.delete(mDao.getAll().toArray(new AppOrderEntity[0]));

        int homeCount = 0;
        int maxUserOrder = 0;
        if (homeList != null) {
            for (AppOrderEntity homeEntity : homeList) {
                if (homeEntity != null) {
                    for (AppOrderEntity entity : entities) {
                        if (homeEntity.equals(entity)) {
                            entity.user_order = homeEntity.user_order;
                            if (maxUserOrder < entity.user_order) {
                                maxUserOrder = entity.user_order;
                            }
                            homeCount++;
                        }
                    }
                }
            }
        }
        mDao.insert(entities);

        List<AppOrderEntity> needAutoAddToHomePanelList = mDao.getNeedAddApp(
                mHomePanelAppCount - homeCount);
        for (AppOrderEntity entity : needAutoAddToHomePanelList) {
            maxUserOrder++;
            entity.user_order = maxUserOrder;
        }
        mDao.update(needAutoAddToHomePanelList.toArray(new AppOrderEntity[0]));
    }
}
