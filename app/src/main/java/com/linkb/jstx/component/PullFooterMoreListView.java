
package com.linkb.jstx.component;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.linkb.jstx.listener.OnListViewRefreshListener;
import com.linkb.R;

public class PullFooterMoreListView extends ListView implements OnScrollListener {

    private final static int LOADING_MORE = 5;
    private final static int LOADING_MORE_DONE = 6;
    private boolean hasMore = true;
    private View footer;
    private int footerState;
    private OnListViewRefreshListener refreshListener;


    public PullFooterMoreListView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        setOnScrollListener(this);
        init(paramContext);
    }


    private void init(final Context context) {
        footer = LayoutInflater.from(context).inflate(R.layout.layout_list_footer, null);
        addFooterView(footer, null, false);
        footer.setVisibility(View.GONE);
        footer.setPadding(0, 0, 0, -1 * footer.getMeasuredHeight());
    }


    @Override
    public void onScroll(AbsListView arg0, int firstVisiableItem, int visibleItemCount, int count) {

    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            //判断是否滚动到底部
            int lastIndex = view.getLastVisiblePosition();
            int count = view.getCount();
            boolean isBottom = lastIndex == count - 1;
            if (isBottom) {
                footer.setVisibility(View.VISIBLE);
                if (hasMore) {
                    footer.findViewById(R.id.footer_progressBar).setVisibility(View.VISIBLE);
                    footer.findViewById(R.id.footer_hint).setVisibility(View.GONE);
                    if (footerState != LOADING_MORE) {
                        refreshListener.onGetNextPage();
                        footerState = LOADING_MORE;
                    }
                }
            } else {
                footer.findViewById(R.id.footer_progressBar).setVisibility(View.GONE);
                footer.findViewById(R.id.footer_hint).setVisibility(View.VISIBLE);
            }
        }
    }


    public void setOnRefreshListener(OnListViewRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }


    public void showMoreComplete(boolean hasMore) {

        this.hasMore = hasMore;
        footerState = LOADING_MORE_DONE;
        footer.findViewById(R.id.footer_progressBar).setVisibility(View.GONE);
    }


}
