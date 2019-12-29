
package com.linkb.jstx.component;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class RecordingColorView extends View {
    private Paint mColorPaint;
    private Path mPath;
    private int[] greenColors = new int[]{0xFF1aad19, 0xe1199018};
    private int[] redColors = new int[]{0xffFF4C23, 0xe1E21674};
    private boolean touchOutSide = false;
    private int mWidth;
    private int mHeight;
    private int mArcHeight;
    private int ox;
    private int oy;

    public RecordingColorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        mHeight = mWidth * 9 / 20;
        mArcHeight = mHeight - (int) (Resources.getSystem().getDisplayMetrics().density * 10);
        ox = mWidth / 2;
        oy = (int) (-mWidth / 2.2);
        mPath = new Path();
        mColorPaint = new Paint();
        mColorPaint.setAntiAlias(true);
        mColorPaint.setStyle(Paint.Style.FILL);
    }

    public int getRealHeight() {
        return mHeight;
    }

    public void setTouchOutSide(boolean outSide) {
        this.touchOutSide = outSide;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!touchOutSide) {
            mColorPaint.setShader(new LinearGradient(ox, oy, mWidth, mHeight, greenColors[0], greenColors[1], Shader.TileMode.MIRROR));
        } else {
            mColorPaint.setShader(new LinearGradient(ox, oy, mWidth, mHeight, redColors[0], redColors[1], Shader.TileMode.MIRROR));
        }

        canvas.drawRect(0, 0, mWidth, mArcHeight, mColorPaint);
        mPath.reset();
        mPath.moveTo(0, mArcHeight);
        mPath.quadTo(0.5f * mWidth, mArcHeight + (mHeight - mArcHeight) * 2, mWidth, mArcHeight);

        mColorPaint.setShadowLayer(15, 0, 0, mColorPaint.getColor());
        canvas.drawPath(mPath, mColorPaint);
    }


}
