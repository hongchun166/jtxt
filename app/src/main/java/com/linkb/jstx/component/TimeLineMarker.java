package com.linkb.jstx.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.linkb.R;


/**
 * 实现思路，把时间线分为三个部分，上竖线，下竖线，和中间圆
 *
 * 先在onMeasure测量圆的高度 resolveSize是一个很好的方法
 * 先在onSizeChange中设置圆的位置，通过圆的位置的中心点对准上边界画出上竖线，下竖线也是一样
 */
public class TimeLineMarker extends View {
    private int mMarkerSize = 24;
    private int mLineSize = 12;
    private Drawable mBeginLine;
    private Drawable mEndLine;
    private Drawable mMarkerDrawable;

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBeginLine != null) {
            mBeginLine.draw(canvas);
        }

        if (mEndLine != null) {
            mEndLine.draw(canvas);
        }

        if (mMarkerDrawable != null) {
            mMarkerDrawable.draw(canvas);
        }

        super.onDraw(canvas);
    }

    public TimeLineMarker(Context context) {
        this(context, null);
    }

    public TimeLineMarker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeLineMarker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TimeLineMarker);

        mMarkerSize = a.getDimensionPixelSize(R.styleable.TimeLineMarker_markerSize, mMarkerSize);

        mLineSize = a.getDimensionPixelSize(R.styleable.TimeLineMarker_lineSize, mLineSize);

        mBeginLine = a.getDrawable(R.styleable.TimeLineMarker_beginLine);

        mEndLine = a.getDrawable(R.styleable.TimeLineMarker_endLine);

        mMarkerDrawable = a.getDrawable(R.styleable.TimeLineMarker_marker);

        a.recycle();

        if (mBeginLine != null)
            mBeginLine.setCallback(this);

        if (mEndLine != null)
            mEndLine.setCallback(this);

        if (mMarkerDrawable != null)
            mMarkerDrawable.setCallback(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec,heightMeasureSpec);

        int w = getPaddingLeft() + getPaddingRight();
        int h = getPaddingTop() + getPaddingBottom();

        if (mMarkerDrawable != null) {
            w += mMarkerSize;
            h += mMarkerSize;
        }

        w = Math.max(w, getMeasuredWidth());
        h = Math.max(h, getMeasuredHeight());

        /**
         * 测量，父容器允许的宽高，还有就是子控件
         *
         * resolveSizeAndState ，很棒的一个方法
         */
        int widthSize = resolveSizeAndState(w, widthMeasureSpec, 0);
        int heightSize = resolveSizeAndState(h, heightMeasureSpec, 0);

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initDrawableSize();
    }

    private void initDrawableSize() {
        int pLeft = getPaddingLeft();
        int pRight = getPaddingRight();
        int pTop = getPaddingTop();
        int pBottom = getPaddingBottom();

        /**
         * 根据父类测量的宽高
         */
        int width = getWidth();
        int height = getHeight();

        /**
         * 圆的宽高大小
         */
        int cWidth = width - pLeft - pRight;
        int cHeight = height - pTop - pBottom;

        Rect bounds;

        if (mMarkerDrawable != null) {
            // 取最小值
            int markerSize = Math.min(mMarkerSize, Math.min(cWidth, cHeight));
            mMarkerDrawable.setBounds(pLeft, pTop, pLeft + markerSize, pTop + markerSize);

            bounds = mMarkerDrawable.getBounds();
        } else {
            //没有数据的时候
            bounds = new Rect(pLeft, pTop, pLeft + cWidth, pTop + cHeight);
        }

        int halfLineSize = mLineSize >> 1;
        int lineLeft = bounds.centerX() - halfLineSize;

        /**
         *   设置开始线位置
         */
        if (mBeginLine != null) {
            mBeginLine.setBounds(lineLeft, 0, lineLeft + mLineSize, bounds.top);
        }

        /**
         *   设置结束线位置
         */
        if (mEndLine != null) {
            mEndLine.setBounds(lineLeft, bounds.bottom, lineLeft + mLineSize, height);
        }
    }


    public void setLineSize(int lineSize) {
        if (mLineSize != lineSize) {
            this.mLineSize = lineSize;
            initDrawableSize();
            invalidate();
        }
    }

    public void setMarkerSize(int markerSize) {
        if (this.mMarkerSize != markerSize) {
            mMarkerSize = markerSize;
            initDrawableSize();
            invalidate();
        }
    }

    public void setBeginLine(Drawable beginLine) {
        if (this.mBeginLine != beginLine) {
            this.mBeginLine = beginLine;
            if (mBeginLine != null) {
                mBeginLine.setCallback(this);
            }
            initDrawableSize();
            invalidate();
        }
    }

    public void setEndLine(Drawable endLine) {
        if (this.mEndLine != endLine) {
            this.mEndLine = endLine;
            if (mEndLine != null) {
                mEndLine.setCallback(this);
            }
            initDrawableSize();
            invalidate();
        }
    }

    public void setMarkerDrawable(Drawable markerDrawable) {
        if (this.mMarkerDrawable != markerDrawable) {
            this.mMarkerDrawable = markerDrawable;
            if (mMarkerDrawable != null) {
                mMarkerDrawable.setCallback(this);
            }
            initDrawableSize();
            invalidate();
        }
    }
}
