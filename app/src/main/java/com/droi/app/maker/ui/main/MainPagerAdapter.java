package com.droi.app.maker.ui.main;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.droi.app.maker.common.utils.Assert;
import com.droi.app.maker.common.utils.ViewUtil;
import com.droi.app.maker.ui.home.HomeFragment;
import com.droi.app.maker.ui.person.PersonFragment;
import com.droi.app.maker.ui.profit.ProfitFragment;
import com.droi.app.maker.ui.task.TaskFragment;

class MainPagerAdapter extends FragmentPagerAdapter {

    static final int TAB_INDEX_HOME = 0;
    static final int TAB_INDEX_TASK = 1;
    static final int TAB_INDEX_PROFIT = 2;
    static final int TAB_INDEX_PERSON = 3;

    static final int TAB_COUNT = 4;

    private HomeFragment mHomeFragment;
    private TaskFragment mTaskFragment;
    private ProfitFragment mProfitFragment;
    private PersonFragment mPersonFragment;

    MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (getRtlPosition(position)) {
            case TAB_INDEX_HOME:
                if (mHomeFragment == null) {
                    mHomeFragment = HomeFragment.newInstance();
                }
                return mHomeFragment;
            case TAB_INDEX_TASK:
                if (mTaskFragment == null) {
                    mTaskFragment = TaskFragment.newInstance();
                }
                return mTaskFragment;
            case TAB_INDEX_PROFIT:
                if (mProfitFragment == null) {
                    mProfitFragment = ProfitFragment.newInstance();
                }
                return mProfitFragment;
            case TAB_INDEX_PERSON:
                if (mPersonFragment == null) {
                    mPersonFragment = PersonFragment.newInstance();
                }
                return mPersonFragment;
            default:
                throw Assert.createIllegalStateFailException("No fragment at position " + position);
        }
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Override
    public long getItemId(int position) {
        return getRtlPosition(position);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final Fragment fragment = (Fragment) super.instantiateItem(container, position);
        if (fragment instanceof HomeFragment) {
            mHomeFragment = (HomeFragment) fragment;
        } else if (fragment instanceof TaskFragment) {
            mTaskFragment = (TaskFragment) fragment;
        } else if (fragment instanceof ProfitFragment) {
            mProfitFragment = (ProfitFragment) fragment;
        } else if (fragment instanceof PersonFragment) {
            mPersonFragment = (PersonFragment) fragment;
        }
        return fragment;
    }

    public int getRtlPosition(int position) {
        if (ViewUtil.isRtl()) {
            return getCount() - 1 - position;
        }
        return position;
    }
}
