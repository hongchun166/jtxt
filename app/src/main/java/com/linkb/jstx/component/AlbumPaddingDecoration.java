
package com.linkb.jstx.component;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.linkb.jstx.util.AppTools;

public class AlbumPaddingDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public AlbumPaddingDecoration() {
        this.space = AppTools.dip2px(3);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int index = parent.getChildAdapterPosition(view);
        int clounm = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
        outRect.bottom = space;
        if (index % clounm != 0) {
            outRect.left = space;
        }
    }
}

