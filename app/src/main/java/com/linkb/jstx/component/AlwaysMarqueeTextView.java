
package com.linkb.jstx.component;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

public class AlwaysMarqueeTextView extends EmoticonTextView {


    public AlwaysMarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSingleLine();
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setMarqueeRepeatLimit(-1);
    }


    @Override
    public boolean isFocused() {
        return true;
    }
}
