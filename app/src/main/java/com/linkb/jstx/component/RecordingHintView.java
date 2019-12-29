
package com.linkb.jstx.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.util.AppTools;


public class RecordingHintView extends RelativeLayout {

    private TextView recordingTime;
    private TextView recordingHint;
    private RecordingColorView colorView;
    private boolean recording = true;

    public RecordingHintView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void setHintText(int resId) {
        recordingHint.setText(resId);
    }

    public void setTimeText(String txt) {
        recordingTime.setText(txt);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        recordingHint = findViewById(R.id.recordingHint);
        recordingTime = findViewById(R.id.recordingTime);
        AppTools.measureView(recordingHint);
        AppTools.measureView(recordingTime);
        colorView = findViewById(R.id.recordingColorView);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) recordingTime.getLayoutParams();
        params.setMargins(0, (int) ((colorView.getRealHeight() - recordingTime.getMeasuredHeight()) / 2.5), 0, 0);
        recordingTime.setLayoutParams(params);

        params = (RelativeLayout.LayoutParams) recordingHint.getLayoutParams();
        params.setMargins(0, colorView.getRealHeight() - recordingHint.getMeasuredHeight() * 2, 0, 0);
        recordingHint.setLayoutParams(params);

    }

    public boolean getRecording() {
        return recording;

    }

    public void setRecording(boolean recording) {
        this.recording = recording;
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == GONE) {
            colorView.setTouchOutSide(false);
        }
    }

    public void setTouchOutSide(boolean outSide) {
        if (outSide) {
            setHintText(R.string.label_chat_unlashcancle);
        } else {
            setHintText(R.string.label_chat_soundcancle);
        }
        colorView.setTouchOutSide(outSide);
    }
}
