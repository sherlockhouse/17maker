package com.droi.app.maker.ui.widgets;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.droi.app.maker.R;

public class GlideImageSwitcher extends ImageSwitcher {

    public GlideImageSwitcher(Context context) {
        super(context);
    }

    public GlideImageSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setImageURI(Uri uri) {
        ImageView image = (ImageView) getNextView();
        if (uri != null) {
            Glide.with(getContext())
                    .load(uri)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(image);
        }
        showNext();
    }
}
