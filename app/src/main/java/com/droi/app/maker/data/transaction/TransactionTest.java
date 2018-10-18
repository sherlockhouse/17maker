package com.droi.app.maker.data.transaction;

import android.content.Context;
import android.net.Uri;

import com.droi.app.maker.data.database.home.AppOrderEntity;
import com.droi.app.maker.data.database.home.BannerEntity;
import com.droi.app.maker.data.transaction.home.AppOrderTransaction;
import com.droi.app.maker.data.transaction.home.BannerTransaction;
import com.droi.app.maker.file.FileUtils;

import java.io.File;

public class TransactionTest {
    private Context mContext;

    public TransactionTest(Context context) {
        mContext = context;
    }

    public void addTestData() {
        addAppOrderData();
        addBannerData();
    }

    private void addAppOrderData() {
        AppOrderEntity[] entities = new AppOrderEntity[]{
                createAppOrderEntity("com.android.dialer",
                        "com.android.dialer.Dialer", "Dialer",
                        "1", 1),
                createAppOrderEntity("com.android.contacts",
                        "com.android.contacts.Contacts", "Contacts",
                        "2", 2),
                createAppOrderEntity("com.android.mms",
                        "com.android.mms.Mms", "Mms",
                        "3", 3),
                createAppOrderEntity("com.android.telephony",
                        "com.android.telephony.Telephony", "Telephony",
                        "4", 4),
                createAppOrderEntity("com.android.telecomm",
                        "com.android.telecomm.Telecomm", "Telecomm",
                        "5", 5),
                createAppOrderEntity("com.android.phone",
                        "com.android.phone.Phone", "Phone",
                        "6", 6),
                createAppOrderEntity("com.android.framework",
                        "com.android.framework.Framework", "Framework",
                        "7", 7),
                createAppOrderEntity("com.android.voice",
                        "com.android.voice.Voice", "Voice",
                        "8", 8),
                createAppOrderEntity("com.android.weixin",
                        "com.android.weixin.WeiXin", "WeiXin",
                        "9", 9),
                createAppOrderEntity("com.android.qq",
                        "com.android.qq.QQ", "QQ",
                        "10", 10),
        };
        new AppOrderTransaction(mContext).add(entities);
    }

    private AppOrderEntity createAppOrderEntity(String packageName, String className,
                                                String appName, String appId, int defOrder) {
        AppOrderEntity entity = new AppOrderEntity();
        entity.packageName = packageName;
        entity.className = className;
        entity.appName = appName;
        entity.appId = appId;
        entity.def_order = defOrder;
        return entity;
    }


    private void addBannerData() {
        FileUtils utils = new FileUtils(mContext);
        String bannerFolder = utils.getBannerImageFolderPath();
        BannerEntity[] entities = new BannerEntity[]{
                createBannerEntity("img1", "img1", 0,
                        bannerFolder + "IMG_20180104_202052.jpg"),
                createBannerEntity("img1", "img1", 0,
                        bannerFolder + "IMG_20181017_212307.jpg"),
                createBannerEntity("img1", "img1", 0,
                        bannerFolder + "IMG_20181017_212310.jpg"),
                createBannerEntity("img1", "img1", 0,
                        bannerFolder + "IMG_20181017_212313.jpg"),
                createBannerEntity("img1", "img1", 0,
                        bannerFolder + "IMG_20181017_212315.jpg"),
                createBannerEntity("img1", "img1", 0,
                        bannerFolder + "IMG_20181017_212317.jpg")
        };
        new BannerTransaction(mContext).add(entities);
    }

    private BannerEntity createBannerEntity(String name, String description,
                                            int type, String filePath) {
        BannerEntity entity = new BannerEntity();
        entity.name = name;
        entity.description = description;
        entity.type = type;
        entity.imageUri = Uri.fromFile(new File(filePath)).toString();
        return entity;
    }
}
