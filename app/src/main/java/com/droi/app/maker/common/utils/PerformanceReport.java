package com.droi.app.maker.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.AbsListView;

import com.droi.app.maker.common.log.LogUtil;
import com.droi.app.maker.common.utils.proto.UiAction;

public class PerformanceReport {
    private static final long INVALID_TIME = -1;
    private static final long ACTIVE_DURATION = TimeUnit.MINUTES.toMillis(5);

    private static final List<UiAction.Type> mActions = new ArrayList<>();
    private static final List<Long> mActionTimestamps = new ArrayList<>();

    private static final RecyclerView.OnScrollListener mRecordOnScrollListener =
            new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                        PerformanceReport.recordClick(UiAction.Type.SCROLL);
                    }
                    super.onScrollStateChanged(recyclerView, newState);
                }
            };

    private static boolean mRecording = false;
    private static long mAppLaunchTimeMillis = INVALID_TIME;
    private static long mFirstClickTimeMillis = INVALID_TIME;
    private static long mLastActionTimeMillis = INVALID_TIME;

    @Nullable
    private static UiAction.Type mIgnoreActionOnce = null;

    private static int mStartingTabIndex = -1; // UNKNOWN
    public static void startRecording() {
        LogUtil.enterBlock("PerformanceReport.startRecording");

        mAppLaunchTimeMillis = SystemClock.elapsedRealtime();
        mLastActionTimeMillis = mAppLaunchTimeMillis;
        if (!mActions.isEmpty()) {
            mActions.clear();
            mActionTimestamps.clear();
        }
        mRecording = true;
    }

    public static void stopRecording() {
        LogUtil.enterBlock("PerformanceReport.stopRecording");
        mRecording = false;
    }

    public static void recordClick(UiAction.Type action) {
        if (!mRecording) {
            return;
        }

        if (action == mIgnoreActionOnce) {
            LogUtil.i("PerformanceReport.recordClick", "%s is ignored", action.toString());
            mIgnoreActionOnce = null;
            return;
        }
        mIgnoreActionOnce = null;

        LogUtil.v("PerformanceReport.recordClick", action.toString());

        // Timeout
        long currentTime = SystemClock.elapsedRealtime();
        if (currentTime - mLastActionTimeMillis > ACTIVE_DURATION) {
            startRecording();
            recordClick(action);
            return;
        }

        mLastActionTimeMillis = currentTime;
        if (mActions.isEmpty()) {
            mFirstClickTimeMillis = currentTime;
        }
        mActions.add(action);
        mActionTimestamps.add(currentTime - mAppLaunchTimeMillis);
    }

    public static void recordScrollStateChange(int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            recordClick(UiAction.Type.SCROLL);
        }
    }

    public static void logOnScrollStateChange(RecyclerView recyclerView) {
        // Remove the listener in case it was added before
        recyclerView.removeOnScrollListener(mRecordOnScrollListener);
        recyclerView.addOnScrollListener(mRecordOnScrollListener);
    }

    public static boolean isRecording() {
        return mRecording;
    }

    public static long getTimeSinceAppLaunch() {
        if (mAppLaunchTimeMillis == INVALID_TIME) {
            return INVALID_TIME;
        }
        return SystemClock.elapsedRealtime() - mAppLaunchTimeMillis;
    }

    public static long getTimeSinceFirstClick() {
        if (mFirstClickTimeMillis == INVALID_TIME) {
            return INVALID_TIME;
        }
        return SystemClock.elapsedRealtime() - mFirstClickTimeMillis;
    }

    public static List<UiAction.Type> getActions() {
        return mActions;
    }

    public static List<Long> getActionTimestamps() {
        return mActionTimestamps;
    }

    public static int getStartingTabIndex() {
        return mStartingTabIndex;
    }

    public static void setStartingTabIndex(int startingTabIndex) {
        PerformanceReport.mStartingTabIndex = startingTabIndex;
    }

    public static void setIgnoreActionOnce(@Nullable UiAction.Type ignoreActionOnce) {
        PerformanceReport.mIgnoreActionOnce = ignoreActionOnce;
        LogUtil.i(
                "PerformanceReport.setmIgnoreActionOnce",
                "next action will be ignored once if it is %s",
                mIgnoreActionOnce.toString());
    }

    private PerformanceReport() {
    }
}
