
package com.linkb.jstx.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CircleProgressView extends View {
    private Paint mBorderPaint;
    private Paint mProgressPaint;
    private int progress;
    private RectF mRectF = new RectF();

    public CircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mProgressPaint = new Paint();
        mProgressPaint.setColor(0x7fffffff);
        mProgressPaint.setAntiAlias(true);
        mBorderPaint = new Paint();
        mBorderPaint.setStrokeWidth(getResources().getDisplayMetrics().density * 2);
        mBorderPaint.setColor(0xffffffff);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);


    }

    public void setProgress(int progress) {
        this.progress = progress;
        mProgressPaint.setColor(0x7fffffff);
        mBorderPaint.setColor(0xffffffff);
        invalidate();
    }

    public void loadError() {
        invalidate();
        mProgressPaint.setColor(0x7fED445C);
        mBorderPaint.setColor(0xffED445C);
    }

    @Override
    public void onDraw(Canvas canvas) {
        int radius = (int) (getWidth() / 2 * 0.9);
        int ox = getWidth() / 2;
        int oy = getHeight() / 2;
        mRectF.left = ox - radius;
        mRectF.top = oy - radius;
        mRectF.right = ox + radius;
        mRectF.bottom = oy + radius;
        canvas.drawArc(mRectF, -90, 360 * progress / 100, true, mProgressPaint);
        canvas.drawArc(mRectF, 0, 360, false, mBorderPaint);
    }


}
