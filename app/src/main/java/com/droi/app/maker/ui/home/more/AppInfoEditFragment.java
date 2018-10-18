package com.droi.app.maker.ui.home.more;

import java.util.List;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.droi.app.maker.R;
import com.droi.app.maker.data.database.home.AppOrderEntity;
import com.droi.app.maker.ui.home.AppDataAdapter;

public class AppInfoEditFragment extends Fragment implements AbsListView.OnItemClickListener {

    public static AppInfoEditFragment newInstance() {
        return new AppInfoEditFragment();
    }

    private AppInfoEditViewModel mViewModel;

    private AppDataAdapter mMyAppAdapter;
    private AppDataAdapter mRecentAppAdapter;
    private AppDataAdapter mAllAppAdapter;

    private boolean mIsEditModeActive;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mViewModel == null) {
            mViewModel = ViewModelProviders.of(this).get(AppInfoEditViewModel.class);

            Context context = getContext();

            mViewModel.getMyAppEntities(context).observe(this,
                    new Observer<List<AppOrderEntity>>() {
                        @Override
                        public void onChanged(@Nullable List<AppOrderEntity> entities) {
                            if (mMyAppAdapter != null) {
                                mMyAppAdapter.setDataEntities(entities);
                                mMyAppAdapter.notifyDataSetChanged();
                            }
                        }
                    });
            mViewModel.getRecentAppEntities(context).observe(this,
                    new Observer<List<AppOrderEntity>>() {
                        @Override
                        public void onChanged(@Nullable List<AppOrderEntity> entities) {
                            if (mRecentAppAdapter != null) {
                                mRecentAppAdapter.setDataEntities(entities);
                                mRecentAppAdapter.notifyDataSetChanged();
                            }
                        }
                    });
            mViewModel.getAllAppEntities(context).observe(this,
                    new Observer<List<AppOrderEntity>>() {
                        @Override
                        public void onChanged(@Nullable List<AppOrderEntity> entities) {
                            if (mAllAppAdapter != null) {
                                mAllAppAdapter.setDataEntities(entities);
                                mAllAppAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }

        ActionBar bar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle(R.string.appinfo_edit_more_apps);
        }
    }

    private static final int MENU_ID_EDIT = 1;
    private static final int MENU_ID_COMPLETE = 2;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, MENU_ID_EDIT, 1, R.string.text_edit)
                .setVisible(true)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0, MENU_ID_COMPLETE, 2, R.string.text_complete)
                .setVisible(false)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(MENU_ID_EDIT).setVisible(!mIsEditModeActive);
        menu.findItem(MENU_ID_COMPLETE).setVisible(mIsEditModeActive);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mIsEditModeActive) {
                    showTipsForSave(true);
                } else {
                    getActivity().finish();
                }
                return true;
            case MENU_ID_EDIT:
                if (!mIsEditModeActive) {
                    mIsEditModeActive = true;
                    updateEditMode();
                }
                return true;
            case MENU_ID_COMPLETE:
                if (mIsEditModeActive) {
                    mIsEditModeActive = false;
                    updateEditMode();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_appinfo_edit_fragment, container,
                false);

        GridView myAppGrid = view.findViewById(R.id.my_app_grid);
        mMyAppAdapter = new AppDataAdapter(getContext(), false);
        myAppGrid.setAdapter(mMyAppAdapter);
        myAppGrid.setOnItemClickListener(this);

        GridView recentAppGrid = view.findViewById(R.id.recent_app_grid);
        mRecentAppAdapter = new AppDataAdapter(getContext(), false);
        recentAppGrid.setAdapter(mRecentAppAdapter);
        recentAppGrid.setOnItemClickListener(this);

        GridView allAppGrid = view.findViewById(R.id.all_app_grid);
        mAllAppAdapter = new AppDataAdapter(getContext(), false);
        allAppGrid.setAdapter(mAllAppAdapter);
        allAppGrid.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!mIsEditModeActive){
            return;
        }
        AppOrderEntity entity = null;
        switch (view.getId()) {
            case R.id.my_app_grid: {
                entity = mMyAppAdapter.getItem(position);
                break;
            }
            case R.id.recent_app_grid: {
                entity = mRecentAppAdapter.getItem(position);
                break;
            }
            case R.id.all_app_grid: {
                entity = mAllAppAdapter.getItem(position);
                break;
            }
            default:
                break;
        }
        mViewModel.editEntity(getContext(), entity);
    }

    private void updateEditMode() {
        getActivity().invalidateOptionsMenu();

        if (mMyAppAdapter != null) {
            mMyAppAdapter.setIsEditModeActive(mIsEditModeActive);
        }
        if (mRecentAppAdapter != null) {
            mRecentAppAdapter.setIsEditModeActive(mIsEditModeActive);
        }
        if (mAllAppAdapter != null) {
            mAllAppAdapter.setIsEditModeActive(mIsEditModeActive);
        }
    }

    private void showTipsForSave(final boolean finishActivity) {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.text_tips)
                .setMessage(R.string.tips_msg_save_for_edit)
                .setPositiveButton(R.string.text_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        mViewModel.saveEditEntities(getContext());
                        if (finishActivity) {
                            getActivity().finish();
                        }
                    }
                })
                .setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (finishActivity) {
                            getActivity().finish();
                        }
                    }
                })
                .create().show();
    }
}
