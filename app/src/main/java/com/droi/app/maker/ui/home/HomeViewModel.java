package com.droi.app.maker.ui.home;

import java.util.ArrayList;
import java.util.List;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.droi.app.maker.R;
import com.droi.app.maker.data.database.MakerDatabase;
import com.droi.app.maker.data.database.MakerDatabaseHelper;
import com.droi.app.maker.data.database.home.AppOrderEntity;
import com.droi.app.maker.data.database.home.BannerEntity;

public class HomeViewModel extends ViewModel {

    @Override
    protected void onCleared() {
        List<BannerEntity> bannerEntities = mBannerImageUris.getValue();
        if (bannerEntities != null) {
            bannerEntities.clear();
        }

        List<AppOrderEntity> appOrderEntities = mAppOrderEntities.getValue();
        if (appOrderEntities != null) {
            appOrderEntities.clear();
        }

        mDb.close();
        mDb = null;

        mBannerImageUris = null;
        mAppOrderEntities = null;
    }

    private MakerDatabase mDb;
    private synchronized MakerDatabase initDb(Context context) {
        if (mDb == null) {
            mDb = MakerDatabaseHelper.newInstance(context);
        }
        return mDb;
    }

    private MutableLiveData<List<BannerEntity>> mBannerImageUris;
    LiveData<List<BannerEntity>> getBannerImageUris(Context context) {
        if (mBannerImageUris == null) {
            mBannerImageUris = new MutableLiveData<>();
            mBannerImageUris.setValue(initDb(context).bannerDao().getAll());
        }
        return mBannerImageUris;
    }

    private MutableLiveData<List<AppOrderEntity>> mAppOrderEntities;
    LiveData<List<AppOrderEntity>> getAppOrderEntities(Context context) {
        if (mAppOrderEntities == null) {
            mAppOrderEntities = new MutableLiveData<>();
            mAppOrderEntities.setValue(initDb(context).appOrderDao().getHomePanelApp());
        }
        return mAppOrderEntities;
    }

    private MutableLiveData<List<String>> mNotifyTextEntities;

    LiveData<List<String>> getNotifyTextEntities(Context context) {
        if (mNotifyTextEntities == null) {
            mNotifyTextEntities = new MutableLiveData<>();
            loadNotifyTextEntities(context);
        }
        return mNotifyTextEntities;
    }

    private void loadNotifyTextEntities(Context context) {
        List<String> list = new ArrayList<>();
        list.add("恭喜佑佑家成功体现100元");
        list.add("恭喜佑佑家成功体现100元");
        list.add("恭喜佑佑家成功体现100元");
        list.add("恭喜佑佑家成功体现100元");
        mNotifyTextEntities.setValue(list);
    }
}
