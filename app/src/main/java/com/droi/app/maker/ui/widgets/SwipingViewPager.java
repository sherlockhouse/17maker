package com.droi.app.maker.ui.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class SwipingViewPager extends ViewPager {
    private boolean mEnableSwipingPages;

    public SwipingViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mEnableSwipingPages = true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        try {
            if (mEnableSwipingPages) {
                return super.onInterceptTouchEvent(event);
            }
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            if (mEnableSwipingPages) {
                return super.onTouchEvent(event);
            }
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public void setEnableSwipingPages(boolean enabled) {
        mEnableSwipingPages = enabled;
    }
}
