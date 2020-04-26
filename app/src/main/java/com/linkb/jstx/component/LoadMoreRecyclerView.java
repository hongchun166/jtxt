
package com.linkb.jstx.component;


import android.content.Context;
import com.linkb.jstx.listener.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.linkb.jstx.listener.OnLoadRecyclerViewListener;
import com.linkb.jstx.network.model.Page;

public class LoadMoreRecyclerView extends RecyclerView {
    private OnLoadRecyclerViewListener onLoadEventListener;
    private Page mPage;
    private ListFooterView footerView;

    public LoadMoreRecyclerView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        setLayoutManager(new LinearLayoutManager(paramContext));
        setItemAnimator(new DefaultItemAnimator());
        addOnScrollListener(new RecyclerOnScrollListener());
    }

    public void setOnLoadEventListener(OnLoadRecyclerViewListener onLoadEventListener) {
        this.onLoadEventListener = onLoadEventListener;
    }

    public void hideHintView() {
        footerView.hideHintView();
    }

    public void showProgressBar() {
        footerView.showProgressBar();
    }

    private boolean isSlideToBottom() {
        int scrollExtent = computeVerticalScrollExtent();
        int scrollOffset = computeVerticalScrollOffset();
        int scrollRange = computeVerticalScrollRange();

        return scrollExtent + scrollOffset >= scrollRange;

    }

    public void setFooterView(ListFooterView footerView) {
        this.footerView = footerView;
    }

    public void showMoreComplete(Page page) {
        mPage = page;
        footerView.hideProgressBar();
        if (mPage == null) {
            footerView.hideHintView();
            return;
        }
        if (!mPage.hasMore()) {
            footerView.showHintView();
        }
    }


    class RecyclerOnScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                onLoadEventListener.onListViewStartScroll();
            }
            if (newState == RecyclerView.SCROLL_STATE_IDLE && isSlideToBottom() && computeVerticalScrollOffset() > 0) {
                if (mPage == null || mPage.hasMore()) {
                    footerView.showProgressBar();
                    footerView.hideHintView();
                    onLoadEventListener.onGetNextPage();
                } else {
                    footerView.hideProgressBar();
                    footerView.showHintView();
                }
            }
        }
    }
}
