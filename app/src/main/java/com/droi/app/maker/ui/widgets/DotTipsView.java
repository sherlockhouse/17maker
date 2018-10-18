package com.droi.app.maker.ui.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.droi.app.maker.R;

public class DotTipsView extends View {

    private final static int DOT_SIZE = 15;
    private final static int DOT_INTERVAL = 15;
    private final static int STROKE_WIDTH = 1;

    private int mCurrentIndex;
    private Paint mPaint;
    private int mCount;
    private int mSize;
    private int mInterval;
    private int mCheckColor;
    private int mUnCheckColor;
    private int mUnCheckStrokeColor;
    private int mUnCheckStrokeWidth;

    public DotTipsView(Context context) {
        this(context, null);
    }

    public DotTipsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        final Resources res = getResources();
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DotTips);
        mSize = a.getDimensionPixelSize(R.styleable.DotTips_size, DOT_SIZE);
        mInterval = a.getDimensionPixelSize(R.styleable.DotTips_interval, DOT_INTERVAL);
        mCheckColor = a.getColor(R.styleable.DotTips_checkColor, res.getColor(R.color.colorAccent));
        mUnCheckColor = a.getColor(R.styleable.DotTips_uncheckColor, res.getColor(R.color.colorPrimary));
        mUnCheckStrokeColor = a.getColor(R.styleable.DotTips_uncheckStrokeColor, Color.GRAY);
        mUnCheckStrokeWidth = a.getDimensionPixelSize(R.styleable.DotTips_uncheckStrokeWidth, STROKE_WIDTH);
        a.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mCount <= 0) {
            return;
        }
        final int height = getHeight();
        final int width = getWidth();
        final int dotWidth = mCount * mSize + (mCount - 1) * mInterval;

        final int left = (width - dotWidth) / 2;
        int top = (height - mSize) / 2;
        int bottom = top + mSize;

        if (top < 0) {
            top = 0;
            bottom = height;
        }

        for (int i = 0; i < mCount; i++) {
            int l = left + (i * (mSize + mInterval));
            if (i == mCurrentIndex) {
                mPaint.setColor(mCheckColor);
                canvas.drawOval(l, top, l + mSize, bottom, mPaint);
            } else {
                mPaint.setColor(mUnCheckStrokeColor);
                canvas.drawOval(l, top, l + mSize, bottom, mPaint);
                mPaint.setColor(mUnCheckColor);
                canvas.drawOval(l + mUnCheckStrokeWidth,
                        top + mUnCheckStrokeWidth,
                        l + mSize - mUnCheckStrokeWidth,
                        bottom - mUnCheckStrokeWidth,
                        mPaint);
            }
        }
        super.onDraw(canvas);
    }

    public void setDotCount(int count) {
        this.mCount = count;
        invalidate();
    }

    public void setCheckIndex(int index) {
        this.mCurrentIndex = index;
        invalidate();
    }
}
