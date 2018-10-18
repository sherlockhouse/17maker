package com.droi.app.maker.ui.home.more;

import java.util.ArrayList;
import java.util.List;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.droi.app.maker.data.database.MakerDatabase;
import com.droi.app.maker.data.database.MakerDatabaseHelper;
import com.droi.app.maker.data.database.home.AppOrderEntity;

public class AppInfoEditViewModel extends ViewModel {
    private MakerDatabase mDb;
    private synchronized MakerDatabase initDb(Context context) {
        if (mDb == null) {
            mDb = MakerDatabaseHelper.newInstance(context);
        }
        return mDb;
    }

    private MutableLiveData<List<AppOrderEntity>> mMyAppEntities;
    LiveData<List<AppOrderEntity>> getMyAppEntities(Context context) {
        if (mMyAppEntities == null) {
            mMyAppEntities = new MutableLiveData<>();
            mMyAppEntities.setValue(initDb(context).appOrderDao().getHomePanelApp());
        }
        return mMyAppEntities;
    }

    private MutableLiveData<List<AppOrderEntity>> mRecentAppEntities;
    LiveData<List<AppOrderEntity>> getRecentAppEntities(Context context) {
        if (mRecentAppEntities == null) {
            mRecentAppEntities = new MutableLiveData<>();
            mRecentAppEntities.setValue(initDb(context).appOrderDao().getRecentUseApp());
        }
        return mRecentAppEntities;
    }

    private MutableLiveData<List<AppOrderEntity>> mAllAppEntities;
    LiveData<List<AppOrderEntity>> getAllAppEntities(Context context) {
        if (mAllAppEntities == null) {
            mAllAppEntities = new MutableLiveData<>();
            mAllAppEntities.setValue(initDb(context).appOrderDao().getAll());
        }
        return mAllAppEntities;
    }

    private MutableLiveData<List<AppOrderEntity>> mEditEntities;
    private LiveData<List<AppOrderEntity>> getEditEntities() {
        if (mEditEntities == null) {
            mEditEntities = new MutableLiveData<>();
            mEditEntities.setValue(new ArrayList<AppOrderEntity>());
        }
        return mEditEntities;
    }

    void editEntity(Context context, AppOrderEntity editEntity) {
        if (editEntity == null) {
            return;
        }

        boolean addToMyApp = editEntity.user_order == 0;

        List<AppOrderEntity> myAppList = getMyAppEntities(context).getValue();
        if (addToMyApp) {
            int order = 1;
            for (AppOrderEntity entity : myAppList) {
                if (order < entity.user_order) {
                    order = entity.user_order;
                }
            }
            editEntity.user_order = order;
            myAppList.add(editEntity);
        } else {
            editEntity.user_order = 0;
            for (AppOrderEntity entity : myAppList) {
                if (editEntity.equals(entity)) {
                    myAppList.remove(entity);
                    break;
                }
            }
        }

        for (AppOrderEntity entity : getRecentAppEntities(context).getValue()) {
            if (editEntity.equals(entity)) {
                entity.user_order = editEntity.user_order;
                break;
            }
        }


        for (AppOrderEntity entity : getAllAppEntities(context).getValue()) {
            if (editEntity.equals(entity)) {
                entity.user_order = editEntity.user_order;
                break;
            }
        }

        List<AppOrderEntity> editList = getEditEntities().getValue();
        boolean hasEditItem = false;
        for (AppOrderEntity entity : editList) {
            if (editEntity.equals(entity)) {
                entity.user_order = editEntity.user_order;
                hasEditItem = true;
                break;
            }
        }
        if (!hasEditItem) {
            editList.add(editEntity);
        }
    }

    void saveEditEntities(Context context) {
        List<AppOrderEntity> list = getEditEntities().getValue();
        if (list != null && !list.isEmpty()) {
            initDb(context).appOrderDao().update(list.toArray(new AppOrderEntity[0]));
        }
    }
}
