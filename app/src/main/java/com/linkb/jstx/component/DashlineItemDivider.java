
package com.linkb.jstx.component;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.linkb.jstx.util.AppTools;

public class DashlineItemDivider extends RecyclerView.ItemDecoration {


    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
         int left = parent.getPaddingLeft();
         int right = parent.getWidth() - parent.getPaddingRight();

         int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
             View child = parent.getChildAt(i);
             RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
             int top = child.getBottom() + params.bottomMargin;

            //绘制虚线
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(ContextCompat.getColor(parent.getContext(), android.R.color.darker_gray));
            Path path = new Path();
            path.moveTo(left, top);
            path.lineTo(right,top);
            PathEffect effects = new DashPathEffect(new float[]{AppTools.dip2px(5),AppTools.dip2px(5),AppTools.dip2px(5),AppTools.dip2px(5)},5);//此处单位是像素不是dp  注意 请自行转化为dp
            paint.setPathEffect(effects);
            c.drawPath(path, paint);
        }
    }

}