
package com.linkb.jstx.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.linkb.jstx.listener.OnTouchMoveCharListener;
import com.linkb.R;

public class CharSelectorBar extends View {
    public final static char STAR = '☆';
    private final static char[] ARRAYS = {STAR, 'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','#'};
    private OnTouchMoveCharListener onTouchMoveCharListener;
    private int current = -1;
    private Paint paint = new Paint();

    private TextView mTextDialog;

    public CharSelectorBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        int singleHeight = height / ARRAYS.length;

        for (int i = 0; i < ARRAYS.length; i++) {
            paint.setColor(Color.parseColor("#989898"));
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            int size = getResources().getDimensionPixelOffset(R.dimen.sort_char_size);
            paint.setTextSize(size);

            if (i == current) {

                paint.setColor(Color.parseColor("#2e76e5"));
                paint.setFakeBoldText(true);
                int size2 = getResources().getDimensionPixelOffset(R.dimen.sort_char_sizeCur);
                paint.setTextSize(size2);
            }

            String charStr = String.valueOf(ARRAYS[i]);
            float xPos = width / 2 - paint.measureText(charStr) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(charStr, xPos, yPos, paint);
            paint.reset();
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final float y = event.getY();
        final int c = (int) (y / getHeight() * ARRAYS.length);

        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            setBackgroundResource(android.R.color.transparent);
            current = -1;
            mTextDialog.setVisibility(View.INVISIBLE);
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            setBackgroundResource(R.color.gray_pressed);
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE && current != c && c >= 0 && c < ARRAYS.length) {

            String charStr = String.valueOf(ARRAYS[c]);
            onTouchMoveCharListener.onCharChanged(ARRAYS[c]);
            mTextDialog.setText(charStr);
            mTextDialog.setVisibility(View.VISIBLE);
            current = c;
            invalidate();
        }

        return true;
    }

    public void setOnTouchMoveCharListener(OnTouchMoveCharListener onTouchMoveCharListener) {
        this.onTouchMoveCharListener = onTouchMoveCharListener;
    }
}
