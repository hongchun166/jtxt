
package com.linkb.jstx.component;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.linkb.jstx.util.AppTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MeteorWallpaperView extends View {
    private static final int METEOR_SIZE = 7;
    private static final double METEOR_ANGEL = 45 * (Math.PI / 180);
    private static final int MOVE_RATE = 10;

    private Paint mColorPaint;
    private DisplayMetrics displayMetrics;
    private List<Meteor> meteors = new ArrayList<>(METEOR_SIZE);
    private int[] speedArray = new int[]{6,8,10};
    private int[] colors = new int[]{0XFFFFFFFF,0XA0FFFFFF,0x00000000};
    private float[] positions = new float[]{0F,0.03F,1F};
    private Random random = new Random();


    public MeteorWallpaperView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mColorPaint = new Paint();
        mColorPaint.setAntiAlias(true);
        mColorPaint.setStyle(Paint.Style.FILL);
        mColorPaint.setColor(0xFFFFFFFF);
        mColorPaint.setStrokeCap( Paint.Cap.ROUND );       // 线帽，即画的线条两端是否带有圆角，ROUND，圆角
        displayMetrics = Resources.getSystem().getDisplayMetrics();
        batchCreateMeteors();
    }

    private void batchCreateMeteors(){
       for (int i = 0 ;i < METEOR_SIZE;i++){
           meteors.add(createOneMeteor());
       }
        mHandler.sendEmptyMessageDelayed(0,MOVE_RATE);
    }

    private void recreateMeteor(Meteor meteor ){
        meteor.alpha = random.nextInt(180) + 75;
        meteor.length = (10 + random.nextInt(10)) * displayMetrics.widthPixels / 100;
        meteor.size = AppTools.dip2px(random.nextInt(2) + 1);
        meteor.speed = speedArray[random.nextInt(speedArray.length)];
        meteor.origin = new Point();

        int deviation = meteor.length * random.nextInt(5);
        meteor.origin.x = displayMetrics.widthPixels + deviation;
        meteor.origin.y = (int) ((displayMetrics.heightPixels  * ( -20 + random.nextInt(80)) / 100F) - deviation);
        meteor.current  = meteor.origin;
    }

    private Meteor createOneMeteor(){
        Meteor meteor = new Meteor();
        recreateMeteor(meteor);
        return meteor;
    }
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            for (Meteor meteor : meteors){
                int endX = (int) (meteor.current.x + Math.cos(METEOR_ANGEL) * meteor.length);
                int endY = (int) (meteor.current.y - Math.sin(METEOR_ANGEL) * meteor.length);
                if (endX <= 0 || endY >= displayMetrics.heightPixels){
                    recreateMeteor(meteor);
                }else {

                    meteor.current.x -= meteor.speed;
                    meteor.current.y += meteor.speed;
                }
            }
            invalidate();
        }
    };


    @Override
    public void onDraw(Canvas canvas) {
        for (Meteor meteor : meteors){
            int endX = (int) (meteor.current.x + Math.cos(METEOR_ANGEL) * meteor.length);
            int endY = (int) (meteor.current.y - Math.sin(METEOR_ANGEL) * meteor.length);
            mColorPaint.setStrokeWidth(meteor.size);
            mColorPaint.setAlpha(meteor.alpha);
            mColorPaint.setShader(new LinearGradient(meteor.current.x,meteor.current.y,endX,endY,colors,positions,Shader.TileMode.MIRROR));
            canvas.drawLine(meteor.current.x,meteor.current.y, endX,endY,mColorPaint);
        }

        mHandler.sendEmptyMessageDelayed(0,MOVE_RATE);

    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacksAndMessages(null);
    }

    private class Meteor{

        public Point origin;
        public Point current;
        public int length;
        public int size;
        public int speed;
        public int alpha;
    }

}
