package com.droi.app.maker.ui.start;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.droi.app.maker.R;
import com.droi.app.maker.data.transaction.TransactionTest;
import com.droi.app.maker.file.FileUtils;
import com.droi.app.maker.ui.main.MainActivity;

public class StartActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.start_activity);

        doPreJob();

        startMainActivity();

        initTestData();
    }

    private void doPreJob() {
        FileUtils utils = new FileUtils(this);
        utils.createAppIconFolder();
        utils.createBannerImageFolder();
    }

    private void startMainActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                finish();
            }
        }, 3000);
    }

    private void initTestData() {
//         new TransactionTest(this).addTestData();
    }
}
