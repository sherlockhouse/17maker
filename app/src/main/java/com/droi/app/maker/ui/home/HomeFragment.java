package com.droi.app.maker.ui.home;

import java.util.List;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.droi.app.maker.R;
import com.droi.app.maker.data.database.home.AppOrderEntity;
import com.droi.app.maker.data.database.home.BannerEntity;
import com.droi.app.maker.ui.home.more.AppInfoEditActivity;
import com.droi.app.maker.ui.main.MainFragment;
import com.droi.app.maker.ui.widgets.DotTipsView;
import com.droi.app.maker.ui.widgets.GlideImageSwitcher;

public class HomeFragment extends Fragment implements View.OnTouchListener,
        AbsListView.OnItemClickListener, View.OnClickListener {

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    private static final int BANNER_ANIM_LEFT_IN = 0;
    private static final int BANNER_ANIM_LEFT_OUT = 1;
    private static final int BANNER_ANIM_RIGHT_IN = 2;
    private static final int BANNER_ANIM_RIGHT_OUT = 3;

    private static final int EVENT_BANNER_AUTO_SWITCH = 1;
    private static final int EVENT_NOTIFY_TEXT_AUTO_SWITCH = 2;
    private static final int BANNER_AUTO_SWITCH_DALEY = 5000;
    private static final int NOTIFY_TEXT_AUTO_SWITCH_DALEY = 3000;

    private HomeViewModel mViewModel;
    private SwitcherHandler mHandler;
    private AppDataAdapter mAppAdapter;

    private TextSwitcher mNotifyTextSwitcher;
    private GlideImageSwitcher mImageSwitcher;
    private DotTipsView mDotTipsView;

    private Animation[] mBannerAnimations;

    private float mTouchDownX;
    private int mImgIndex;
    private int mTextIndex;

    private class SwitcherHandler extends Handler {
        SwitcherHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Context context = getContext();
            switch (msg.what) {
                case EVENT_BANNER_AUTO_SWITCH: {
                    mHandler.removeMessages(EVENT_BANNER_AUTO_SWITCH);
                    List<BannerEntity> entities = mViewModel.getBannerImageUris(context).getValue();
                    if (entities == null || entities.isEmpty()) {
                        mImageSwitcher.setImageURI(null);
                        break;
                    }
                    preMoveBannerRight(entities);
                    switcherImage(entities, mImgIndex);
                    mHandler.sendEmptyMessageDelayed(EVENT_BANNER_AUTO_SWITCH,
                            BANNER_AUTO_SWITCH_DALEY);
                    break;
                }
                case EVENT_NOTIFY_TEXT_AUTO_SWITCH: {
                    mHandler.removeMessages(EVENT_NOTIFY_TEXT_AUTO_SWITCH);
                    List<String> strings = mViewModel.getNotifyTextEntities(context).getValue();
                    if (strings == null || strings.isEmpty()) {
                        mNotifyTextSwitcher.setText(null);
                        break;
                    }
                    if (mTextIndex < strings.size() - 1) {
                        mTextIndex++;
                    } else {
                        mTextIndex = 0;
                    }
                    switcherText(strings, mTextIndex);
                    break;
                }
                default:
                    break;
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mViewModel == null) {
            mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

            Context context = getContext();

            mViewModel.getAppOrderEntities(context).observe(this,
                    new Observer<List<AppOrderEntity>>() {
                        @Override
                        public void onChanged(@Nullable List<AppOrderEntity> entities) {
                            if (mAppAdapter != null) {
                                mAppAdapter.setDataEntities(entities);
                                mAppAdapter.notifyDataSetChanged();
                            }
                        }
                    });

            mViewModel.getBannerImageUris(context).observe(this,
                    new Observer<List<BannerEntity>>() {
                        @Override
                        public void onChanged(@Nullable List<BannerEntity> entities) {
                            mHandler.removeMessages(EVENT_BANNER_AUTO_SWITCH);
                            int size = 0;
                            if (entities != null && !entities.isEmpty()) {
                                size = entities.size();
                                int lastIndex = size - 1;
                                if (mImgIndex > lastIndex) {
                                    mImgIndex = lastIndex;
                                }
                                mImageSwitcher.setOnTouchListener(HomeFragment.this);
                                mImageSwitcher.setOnClickListener(HomeFragment.this);
                                mImageSwitcher.setImageURI(Uri.parse(entities.get(mImgIndex).imageUri));
                                mHandler.sendEmptyMessageDelayed(EVENT_BANNER_AUTO_SWITCH,
                                        BANNER_AUTO_SWITCH_DALEY);
                            } else {
                                mImageSwitcher.setImageURI(null);
                                mImageSwitcher.setOnTouchListener(null);
                                mImageSwitcher.setOnClickListener(null);
                            }
                            mDotTipsView.setDotCount(size);
                        }
                    });

            mViewModel.getNotifyTextEntities(context).observe(this,
                    new Observer<List<String>>() {
                        @Override
                        public void onChanged(@Nullable List<String> strings) {
                            if (strings == null || strings.isEmpty()) {
                                mNotifyTextSwitcher.setText(null);
                                return;
                            }
                            if (mTextIndex > strings.size() - 1) {
                                mTextIndex++;
                            } else {
                                mTextIndex = 0;
                            }
                            switcherText(strings, mTextIndex);
                        }
                    });
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mHandler = new SwitcherHandler(Looper.myLooper());

        Context context = getContext();
        mBannerAnimations = new Animation[]{
                AnimationUtils.loadAnimation(context, R.anim.banner_left_in),
                AnimationUtils.loadAnimation(context, R.anim.banner_left_out),
                AnimationUtils.loadAnimation(context, R.anim.banner_right_in),
                AnimationUtils.loadAnimation(context, R.anim.banner_right_out)};

        View view = inflater.inflate(R.layout.home_fragment, container, false);

        mImageSwitcher = view.findViewById(R.id.banner);
        mImageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView img = new ImageView(getContext());
                img.setBackgroundColor(Color.TRANSPARENT);
                img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                img.setLayoutParams(new ImageSwitcher.LayoutParams(
                        ImageSwitcher.LayoutParams.MATCH_PARENT,
                        ImageSwitcher.LayoutParams.MATCH_PARENT));
                return img;
            }
        });
        mImageSwitcher.setOnTouchListener(this);
        mImageSwitcher.setOnClickListener(this);

        mDotTipsView = view.findViewById(R.id.banner_dots);

        GridView mAppGrid = view.findViewById(R.id.app_grid);
        mAppAdapter = new AppDataAdapter(getContext(), true);
        mAppGrid.setAdapter(mAppAdapter);
        mAppGrid.setOnItemClickListener(this);

        mNotifyTextSwitcher = view.findViewById(R.id.notify_text);
        mNotifyTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView tv = new TextView(getContext());
                tv.setGravity(Gravity.CENTER_VERTICAL);
                tv.setLayoutParams(new TextSwitcher.LayoutParams(
                        TextSwitcher.LayoutParams.MATCH_PARENT,
                        TextSwitcher.LayoutParams.MATCH_PARENT));
                return tv;
            }
        });
        mNotifyTextSwitcher.setInAnimation(
                AnimationUtils.loadAnimation(context, R.anim.text_bottom_in));
        mNotifyTextSwitcher.setOutAnimation(
                AnimationUtils.loadAnimation(context, R.anim.text_top_out));

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        mHandler.removeMessages(EVENT_BANNER_AUTO_SWITCH);
        mHandler.removeMessages(EVENT_NOTIFY_TEXT_AUTO_SWITCH);
    }

    @Override
    public void onResume() {
        super.onResume();
        mImgIndex = 0;
        mHandler.removeMessages(EVENT_BANNER_AUTO_SWITCH);
        mHandler.sendEmptyMessageDelayed(EVENT_BANNER_AUTO_SWITCH, BANNER_AUTO_SWITCH_DALEY);

        mTextIndex = 0;
        mHandler.removeMessages(EVENT_NOTIFY_TEXT_AUTO_SWITCH);
        mHandler.sendEmptyMessageDelayed(EVENT_NOTIFY_TEXT_AUTO_SWITCH,
                NOTIFY_TEXT_AUTO_SWITCH_DALEY);

        if (mViewModel != null && mAppAdapter != null) {
            mAppAdapter.setDataEntities(mViewModel.getAppOrderEntities(getContext()).getValue());
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mTouchDownX = event.getX();
                setEnableSwipingPages(false);
                mHandler.removeMessages(EVENT_BANNER_AUTO_SWITCH);
                break;
            }
            case MotionEvent.ACTION_UP: {
                float touchUpX = event.getX();
                float absMove = Math.abs(touchUpX - mTouchDownX);
                if (absMove < 50) {
                    setEnableSwipingPages(true);
                    mHandler.removeMessages(EVENT_BANNER_AUTO_SWITCH);
                    mHandler.sendEmptyMessageDelayed(EVENT_BANNER_AUTO_SWITCH,
                            BANNER_AUTO_SWITCH_DALEY);
                    if (absMove < 10) {
                        v.performClick();
                    }
                    return true;
                }

                List<BannerEntity> entities = mViewModel.getBannerImageUris(getContext()).getValue();
                if (entities == null || entities.isEmpty()) {
                    mImageSwitcher.setImageURI(null);
                    return true;
                }
                if (touchUpX > mTouchDownX) {
                    preMoveBannerLeft(entities);
                } else {
                    preMoveBannerRight(entities);
                }

                switcherImage(entities, mImgIndex);

                setEnableSwipingPages(true);
                mHandler.removeMessages(EVENT_BANNER_AUTO_SWITCH);
                mHandler.sendEmptyMessageDelayed(EVENT_BANNER_AUTO_SWITCH,
                        BANNER_AUTO_SWITCH_DALEY);
            }
            break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.banner: {
                List<BannerEntity> entities = mViewModel.getBannerImageUris(getContext()).getValue();
                if (entities != null && !entities.isEmpty()) {
                    if (mImgIndex >= 0 && mImgIndex < entities.size() - 1) {
                        BannerEntity entity = entities.get(mImgIndex);
                        Toast.makeText(getContext(), "click banner: " + entity.name,
                                Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AppOrderEntity entity = mAppAdapter.getItem(position);
        if (entity.id > 0) {
            Toast.makeText(getContext(), "click app: " + entity.appName,
                    Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(getContext(), AppInfoEditActivity.class));
        }
    }

    private void preMoveBannerLeft(@NonNull List<BannerEntity> entities) {
        if (mImgIndex > 0) {
            mImgIndex--;
        } else {
            mImgIndex = entities.size() - 1;
        }
        mImageSwitcher.setInAnimation(mBannerAnimations[BANNER_ANIM_LEFT_IN]);
        mImageSwitcher.setOutAnimation(mBannerAnimations[BANNER_ANIM_RIGHT_OUT]);
    }

    private void preMoveBannerRight(@NonNull List<BannerEntity> entities) {
        if (mImgIndex < entities.size() - 1) {
            mImgIndex++;
        } else {
            mImgIndex = 0;
        }
        mImageSwitcher.setInAnimation(mBannerAnimations[BANNER_ANIM_RIGHT_IN]);
        mImageSwitcher.setOutAnimation(mBannerAnimations[BANNER_ANIM_LEFT_OUT]);
    }

    private void switcherImage(@NonNull List<BannerEntity> entities, int index) {
        int lastIndex = entities.size() - 1;
        if (index > lastIndex) {
            index = lastIndex;
        }
        mImgIndex = index;
        mImageSwitcher.setImageURI(Uri.parse(entities.get(mImgIndex).imageUri));
        mDotTipsView.setCheckIndex(mImgIndex);
    }

    private void setEnableSwipingPages(boolean enable) {
        if (getParentFragment() instanceof MainFragment) {
            ((MainFragment) getParentFragment()).setEnableSwipingPages(enable);
        }
    }

    private void switcherText(@NonNull List<String> strings, int index) {
        mHandler.removeMessages(EVENT_NOTIFY_TEXT_AUTO_SWITCH);
        int lastIndex = strings.size() - 1;
        if (index > lastIndex) {
            index = lastIndex;
        }
        mTextIndex = index;
        mNotifyTextSwitcher.setText(strings.get(mTextIndex));
        mHandler.sendMessageDelayed(
                mHandler.obtainMessage(EVENT_NOTIFY_TEXT_AUTO_SWITCH, mTextIndex, 0),
                NOTIFY_TEXT_AUTO_SWITCH_DALEY);
    }
}
