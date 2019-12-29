
package com.linkb.jstx.component;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class TopBottomDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public TopBottomDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int index = parent.getChildAdapterPosition(view);
        if (index == 0) {
            outRect.top = space;
        }
        if (index == parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = space;
        }
    }
}

