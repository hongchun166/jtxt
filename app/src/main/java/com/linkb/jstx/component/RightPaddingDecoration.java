
package com.linkb.jstx.component;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RightPaddingDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public RightPaddingDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.right = space;
    }
}

