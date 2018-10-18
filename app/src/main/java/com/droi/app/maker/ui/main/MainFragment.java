package com.droi.app.maker.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.droi.app.maker.R;
import com.droi.app.maker.common.log.LogUtil;
import com.droi.app.maker.common.log.Logger;
import com.droi.app.maker.common.utils.Assert;
import com.droi.app.maker.common.utils.PerformanceReport;
import com.droi.app.maker.common.utils.proto.ScreenEvent;
import com.droi.app.maker.common.utils.proto.UiAction;
import com.droi.app.maker.ui.widgets.SwipingViewPager;

import java.util.ArrayList;

public class MainFragment extends Fragment implements ViewPager.OnPageChangeListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    private MainViewModel mViewModel;
    private SwipingViewPager mViewPager;
    private BottomNavigationView mNavView;
    private MainPagerAdapter mAdapter;

    private UiAction.Type[] mActionTypeList;

    private final ArrayList<ViewPager.OnPageChangeListener> mOnPageChangeListeners = new ArrayList<>();
    private int mTabIndex = MainPagerAdapter.TAB_INDEX_HOME;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mActionTypeList = new UiAction.Type[MainPagerAdapter.TAB_COUNT];
        mActionTypeList[MainPagerAdapter.TAB_INDEX_HOME] = UiAction.Type.CHANGE_TAB_TO_HOME;
        mActionTypeList[MainPagerAdapter.TAB_INDEX_TASK] = UiAction.Type.CHANGE_TAB_TO_TASK;
        mActionTypeList[MainPagerAdapter.TAB_INDEX_PROFIT] = UiAction.Type.CHANGE_TAB_TO_PROFIT;
        mActionTypeList[MainPagerAdapter.TAB_INDEX_PERSON] = UiAction.Type.CHANGE_TAB_TO_PERSON;

        View view = inflater.inflate(R.layout.main_fragment, container, false);

        mViewPager = view.findViewById(R.id.view_pager);
        mAdapter = new MainPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);

        mNavView = view.findViewById(R.id.bottom_navigation);
        mNavView.setLabelVisibilityMode(1);
        mNavView.setOnNavigationItemSelectedListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewPager.removeOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mTabIndex = mAdapter.getRtlPosition(position);

        final int count = mOnPageChangeListeners.size();
        for (int i = 0; i < count; i++) {
            mOnPageChangeListeners.get(i).onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        PerformanceReport.recordClick(mActionTypeList[position]);

        LogUtil.i("ListsFragment.onPageSelected", "position: %d", position);
        mTabIndex = mAdapter.getRtlPosition(position);

        checkTab(mTabIndex);

        final int count = mOnPageChangeListeners.size();
        for (int i = 0; i < count; i++) {
            mOnPageChangeListeners.get(i).onPageSelected(position);
        }
        sendScreenViewForCurrentPosition();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        final int count = mOnPageChangeListeners.size();
        for (int i = 0; i < count; i++) {
            mOnPageChangeListeners.get(i).onPageScrollStateChanged(state);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_home:
                showTab(MainPagerAdapter.TAB_INDEX_HOME);
                return true;
            case R.id.navigation_task:
                showTab(MainPagerAdapter.TAB_INDEX_TASK);
                return true;
            case R.id.navigation_profit:
                showTab(MainPagerAdapter.TAB_INDEX_PROFIT);
                return true;
            case R.id.navigation_person:
                showTab(MainPagerAdapter.TAB_INDEX_PERSON);
                return true;
        }
        return false;
    }

    public void sendScreenViewForCurrentPosition() {
        if (!isResumed()) {
            return;
        }

        ScreenEvent.Type screenType;
        switch (getCurrentTabIndex()) {
            case MainPagerAdapter.TAB_INDEX_HOME:
                screenType = ScreenEvent.Type.HOME;
                break;
            case MainPagerAdapter.TAB_INDEX_TASK:
                screenType = ScreenEvent.Type.TASK;
                break;
            case MainPagerAdapter.TAB_INDEX_PROFIT:
                screenType = ScreenEvent.Type.PROFIT;
                break;
            case MainPagerAdapter.TAB_INDEX_PERSON:
                screenType = ScreenEvent.Type.PERSON;
                break;
            default:
                return;
        }
        Logger.get(getActivity()).logScreenView(screenType, getActivity());
    }

    public void addOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        if (!mOnPageChangeListeners.contains(onPageChangeListener)) {
            mOnPageChangeListeners.add(onPageChangeListener);
        }
    }

    public int getCurrentTabIndex() {
        return mTabIndex;
    }

    public void showTab(int index) {
        int pos = mAdapter.getRtlPosition(index);
        if (pos >= 0 && pos < MainPagerAdapter.TAB_COUNT) {
            mViewPager.setCurrentItem(mAdapter.getRtlPosition(index));
        } else {
            throw Assert.createIllegalStateFailException("No fragment at position " + index);
        }
    }

    private void checkTab(int index) {
        switch (mAdapter.getRtlPosition(index)) {
            case MainPagerAdapter.TAB_INDEX_HOME:
                mNavView.setSelectedItemId(R.id.navigation_home);
                break;
            case MainPagerAdapter.TAB_INDEX_TASK:
                mNavView.setSelectedItemId(R.id.navigation_task);
                break;
            case MainPagerAdapter.TAB_INDEX_PROFIT:
                mNavView.setSelectedItemId(R.id.navigation_profit);
                break;
            case MainPagerAdapter.TAB_INDEX_PERSON:
                mNavView.setSelectedItemId(R.id.navigation_person);
                break;
        }
    }

    public void setEnableSwipingPages(boolean enabled) {
        if (mViewPager != null) {
            mViewPager.setEnableSwipingPages(enabled);
        }
    }
}
