package com.linkb.video.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.linkb.R;

public class UserTrackViewFullScreen extends UserTrackView {

    public UserTrackViewFullScreen(@NonNull Context context) {
        super(context);
    }

    public UserTrackViewFullScreen(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayout() {
        return R.layout.user_tracks_view_full_screen;
    }
}
