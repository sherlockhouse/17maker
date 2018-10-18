package com.droi.app.maker.ui.home;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.droi.app.maker.R;
import com.droi.app.maker.data.database.home.AppOrderEntity;

import java.util.ArrayList;
import java.util.List;

public class AppDataAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<AppOrderEntity> mEntities = new ArrayList<>();

    private AppOrderEntity mMore;
    private boolean mHasMoreEntity;
    private boolean mIsEditModeActive;

    public AppDataAdapter(Context context, boolean hasMoreEntity) {
        mContext = context;
        mHasMoreEntity = hasMoreEntity;

        mInflater = LayoutInflater.from(mContext);

        if (mHasMoreEntity) {
            mMore = new AppOrderEntity();
            mMore.appIcon = String.valueOf(R.drawable.ic_launcher_foreground);
            mMore.appName = "更多";
            mMore.id = -1;
        }
    }

    public void setDataEntities(List<AppOrderEntity> entities) {
        mEntities.clear();
        if (entities != null) {
            mEntities.addAll(entities);
        }
        if (mHasMoreEntity) {
            mEntities.add(mMore);
        }
    }

    public void setIsEditModeActive(boolean mIsEditModeActive) {
        this.mIsEditModeActive = mIsEditModeActive;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mEntities.size();
    }

    @Override
    public AppOrderEntity getItem(int position) {
        return mEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();

            convertView = mInflater.inflate(R.layout.home_app_grid_item, parent, false);

            holder.mAppIconView = convertView.findViewById(R.id.app_icon);
            holder.mAppEditStub = convertView.findViewById(R.id.app_icon_edit_stub);
            holder.mAppNameView = convertView.findViewById(R.id.app_name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AppOrderEntity entity = getItem(position);
        if (entity.id > 0) {
            if (entity.appIcon != null) {
                holder.mAppIconView.setImageURI(Uri.parse(entity.appIcon));
            } else {
                // set default icon
                holder.mAppIconView.setImageResource(R.drawable.ic_launcher_foreground);
            }
        } else {
            holder.mAppIconView.setImageResource(Integer.valueOf(entity.appIcon));
        }
        holder.mAppNameView.setText(entity.appName);

        if (mIsEditModeActive) {
            if (holder.mAppEditView == null) {
                holder.mAppEditView = (ImageView) holder.mAppEditStub.inflate();
            }
            holder.mAppEditView.setVisibility(View.VISIBLE);
            if (entity.user_order > 0) {
                holder.mAppEditView.setImageResource(R.drawable.ic_appinfo_edit_remove);
            } else {
                holder.mAppEditView.setImageResource(R.drawable.ic_appinfo_edit_add);
            }
        } else if (holder.mAppEditView != null) {
            holder.mAppEditView.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView mAppIconView;
        ViewStub mAppEditStub;
        ImageView mAppEditView;
        TextView mAppNameView;
    }
}
