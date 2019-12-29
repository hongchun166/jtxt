
package com.linkb.jstx.component;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.linkb.jstx.listener.OnListViewRefreshListener;
import com.linkb.R;

public class CustomSwipeRefreshLayout extends SwipeRefreshLayout implements SwipeRefreshLayout.OnRefreshListener {

    private Handler handler = new Handler();
    private OnListViewRefreshListener listener;
    private long refreshTime;
    private boolean limited = false;

    public CustomSwipeRefreshLayout(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        setColorSchemeResources(R.color.theme_green, R.color.theme_blue, R.color.theme_red, R.color.theme_orange);
        super.setOnRefreshListener(this);
    }

    public void setLimited(boolean limited) {
        this.limited = limited;
    }

    public void setOnRefreshListener(OnListViewRefreshListener listener) {
        this.listener = listener;
    }

    public void startRefreshing() {
        setProgressViewOffset(false, 0, (int) (Resources.getSystem().getDisplayMetrics().density * 30));
        super.setRefreshing(true);
        listener.onGetFirstPage();
    }

    public void onRefreshCompleted() {
        super.setRefreshing(false);
        refreshTime = System.currentTimeMillis();
    }

    public void onRefreshFailed() {
        super.setRefreshing(false);
    }

    private void cancelRefreshing() {
        super.setRefreshing(false);
    }

    @Override
    public void onRefresh() {

        if (listener != null) {
            if (!limited) {
                listener.onGetFirstPage();
                return;
            }
            long t = System.currentTimeMillis();
            //如果在5分钟之内刷新过 就不再刷新
            if (t - refreshTime >= 5 * 60 * 1000) {
                listener.onGetFirstPage();
            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cancelRefreshing();
                    }
                }, 1000);
            }
        }
    }
}
