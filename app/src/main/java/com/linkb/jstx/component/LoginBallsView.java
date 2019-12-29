
package com.linkb.jstx.component;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

public class LoginBallsView extends FrameLayout {
    private int mWidth;
    private int deviationRange;
    private ColorBallView yellowBall;
    private ColorBallView redBall;
    private ColorBallView blueBall;
    private ColorBallView greenBall;
    private ColorBallView pinkBall;
    private ColorBallView orangeBall;

    public LoginBallsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        deviationRange = (int) (Resources.getSystem().getDisplayMetrics().density * 25);
        mWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    }


    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        ColorBallView grayBall = new ColorBallView(getContext(), mWidth / 2, (int) (-mWidth / 2.2), mWidth, 0xFF262A3B, false);
        addView(grayBall, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        blueBall = new ColorBallView(getContext(), 0, (mWidth / 3), (mWidth / 2), 0xFF6BCFF4, true);
        addView(blueBall, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        blueBall.setMargin(-deviationRange - deviationRange / 5, -deviationRange - deviationRange / 5);
        blueBall.setColor(0xc86BCFF4, 0xFF6BCFF4);

        redBall = new ColorBallView(getContext(), deviationRange / 2, (mWidth / 6), (mWidth / 2), 0xFFFE3E47, true);
        redBall.setColor(0xc8FF4C23, 0xffE21674);
        addView(redBall, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        redBall.setMargin(-deviationRange , -deviationRange);


        yellowBall = new ColorBallView(getContext(), deviationRange, 0, (mWidth / 2), 0xFFF4C900, true);
        addView(yellowBall, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        yellowBall.setColor(0xc8F4C900, 0xFFF4C900);
        yellowBall.setMargin(-deviationRange + deviationRange / 5, -deviationRange);

        orangeBall = new ColorBallView(getContext(), mWidth, (mWidth / 3), (mWidth / 2), 0xFfFC7512, true);
        orangeBall.setColor(0xC8CC6699, 0xFfFC7512);
        addView(orangeBall, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        orangeBall.setMargin(deviationRange + deviationRange / 5, -deviationRange - deviationRange / 5);


        greenBall = new ColorBallView(getContext(), (mWidth - deviationRange / 2), (mWidth / 6), (mWidth / 2), 0xFfEF189B, true);
        greenBall.setColor(0xC899CC66, 0xFf99CC66);
        addView(greenBall, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        greenBall.setMargin(deviationRange, -deviationRange);

        pinkBall = new ColorBallView(getContext(), mWidth - deviationRange, 0, (mWidth / 2), 0xFf99CC66, true);
        pinkBall.setColor(0xC8E370E4, 0xFfE370E4);
        addView(pinkBall, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        pinkBall.setMargin(deviationRange - deviationRange / 5, -deviationRange);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mHeight = (mWidth / 3) + (mWidth / 2) - deviationRange - deviationRange / 5;
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    public void runaway() {


        TranslateAnimation animation = new TranslateAnimation(0, -(mWidth / 2), 0, 0);
        animation.setRepeatCount(0);
        animation.setDuration(300);
        animation.setFillAfter(true);

        TranslateAnimation animation1 = new TranslateAnimation(0, -(mWidth / 2), 0, 0);
        animation1.setRepeatCount(0);
        animation1.setDuration(400);
        animation1.setFillAfter(true);

        TranslateAnimation animation2 = new TranslateAnimation(0, -(mWidth / 2), 0, 0);
        animation2.setRepeatCount(0);
        animation2.setDuration(500);
        animation2.setFillAfter(true);

        TranslateAnimation animation3 = new TranslateAnimation(0, mWidth - (mWidth / 2), 0, 0);
        animation3.setRepeatCount(0);
        animation3.setDuration(300);
        animation3.setFillAfter(true);

        TranslateAnimation animation4 = new TranslateAnimation(0, mWidth - (mWidth / 2), 0, 0);
        animation4.setRepeatCount(0);
        animation4.setDuration(400);
        animation4.setFillAfter(true);

        TranslateAnimation animation5 = new TranslateAnimation(0, mWidth - (mWidth / 2), 0, 0);
        animation5.setRepeatCount(0);
        animation5.setDuration(500);
        animation5.setFillAfter(true);


        blueBall.setAnimation(animation);
        redBall.setAnimation(animation1);
        yellowBall.setAnimation(animation2);

        orangeBall.setAnimation(animation3);
        pinkBall.setAnimation(animation4);
        greenBall.setAnimation(animation5);

        animation.start();
        animation1.start();
        animation2.start();
        animation3.start();
        animation4.start();
        animation5.start();
    }


}
