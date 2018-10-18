package com.droi.app.maker.file;

import android.content.Context;

import java.io.File;

public class FileUtils {

    private Context mContext;

    public FileUtils(Context context) {
        mContext = context;
    }

    public String getAppIconFolderPath() {
        return mContext.getExternalCacheDir() + "/appIcon/";
    }

    public void createAppIconFolder() {
        mkdirs(getAppIconFolderPath());
    }


    public String getBannerImageFolderPath() {
        return mContext.getExternalCacheDir() + "/banner/";
    }

    public void createBannerImageFolder() {
        mkdirs(getBannerImageFolderPath());
    }

    private boolean mkdirs(String filePath) {
        File file = new File(filePath);
        return !file.exists() && file.mkdirs();
    }
}
