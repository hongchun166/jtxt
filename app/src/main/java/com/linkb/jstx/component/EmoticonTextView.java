
package com.linkb.jstx.component;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.linkb.jstx.activity.chat.MMWebViewActivity;
import com.linkb.jstx.app.LvxinApplication;

import java.util.regex.Matcher;


public class EmoticonTextView extends AppCompatTextView {
    private int faceSize = 16;
    private DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
    private int autoLinkMask;

    public EmoticonTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        autoLinkMask =  getAutoLinkMask();
    }

    public void setFaceSize(int faceSize) {

        this.faceSize = faceSize;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {

        if (text == null || TextUtils.isEmpty(text.toString().trim()))
        {
            super.setText(text,type);
            return;
        }
//        if (getAutoLinkMask() != autoLinkMask)
//        {
//            super.setAutoLinkMask(autoLinkMask);
//        }
        super.setText(getEmotionSpaned(text), type);
        if (getText() instanceof Spannable) {
            Spannable spannable = (Spannable) getText();
            URLSpan[] spans = spannable.getSpans(0, text.length(), URLSpan.class);
            for (URLSpan span : spans) {
                int start = spannable.getSpanStart(span);
                int end = spannable.getSpanEnd(span);
                spannable.removeSpan(span);
                span =  new URLClickableSpan(span.getURL());
                spannable.setSpan(span, start,end,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            super.setAutoLinkMask(0);
            super.setText(spannable,type);
        }
    }



    private Spannable getEmotionSpaned(CharSequence text) {

        Spannable  spannable;
        if (text instanceof  Spannable)
        {
            spannable = (Spannable) text;
        }else {
            spannable = new SpannableString(text);
        }
        Matcher matcher = LvxinApplication.EMOTION_PATTERN.matcher(text);
        while (matcher.find()) {
            Integer id = LvxinApplication.EMOTION_MAP.get(matcher.group());
            if (id != null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), id);
                int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,faceSize,metrics);
                drawable.setBounds(0, 0, size, size);
                CenterImageSpan span = new CenterImageSpan(drawable);
                spannable.setSpan(span, matcher.start(), matcher.end(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannable;
    }

    class CenterImageSpan extends ImageSpan {
        CenterImageSpan(Drawable drawable) {
            super(drawable);
        }
        @Override
        public int getSize(Paint paint, CharSequence text, int start, int end,Paint.FontMetricsInt fm) {
            Drawable d = getDrawable();
            Rect rect = d.getBounds();
            if (fm != null) {
                Paint.FontMetricsInt fmPaint=paint.getFontMetricsInt();
                int fontHeight = fmPaint.bottom - fmPaint.top;
                int drHeight=rect.bottom-rect.top;
                int top= drHeight/2 - fontHeight/4;
                int bottom=drHeight/2 + fontHeight/4;
                fm.ascent=-bottom;
                fm.top=-bottom;
                fm.bottom=top;
                fm.descent=top;
            }
            return rect.right;
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
            canvas.save();
            int transY = ((bottom-top) - getDrawable().getBounds().bottom)/2+top;
            canvas.translate(x, transY);
            getDrawable().draw(canvas);
            canvas.restore();
        }
    }

    public static class URLClickableSpan extends URLSpan {

        URLClickableSpan(String url) {
            super(url);
        }

        @Override
        public void onClick(View widget) {
            Uri uri = Uri.parse(getURL());
            if (uri.toString().startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                LvxinApplication.getInstance().startActivity(intent);
            }
            if (uri.toString().startsWith("mailto:")) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                LvxinApplication.getInstance().startActivity(intent);
            }
            if (uri.toString().startsWith("https://") || uri.toString().startsWith("http://")) {
                Intent intent = new Intent(LvxinApplication.getInstance(), MMWebViewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(uri);
                LvxinApplication.getInstance().startActivity(intent);
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }

}
