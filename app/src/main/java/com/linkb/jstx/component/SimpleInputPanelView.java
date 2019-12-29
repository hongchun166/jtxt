
package com.linkb.jstx.component;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.linkb.R;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.listener.OnInputPanelEventListener;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.util.AppTools;

import java.util.Objects;
import java.util.regex.Matcher;

import cn.dreamtobe.kpswitch.util.KPSwitchConflictUtil;
import cn.dreamtobe.kpswitch.util.KeyboardUtil;
import cn.dreamtobe.kpswitch.widget.KPSwitchPanelLinearLayout;

public class SimpleInputPanelView extends LinearLayout implements OnClickListener, OnItemClickedListener, TextWatcher,InputFilter,KPSwitchConflictUtil.SwitchClickListener, cn.dreamtobe.kpswitch.util.KeyboardUtil.OnKeyboardShowingListener {
    private static final int EMOTION_SIZE = 22;
    protected View iconEmotion;
    protected View sendButton;
    protected EditText messageEditText;
    protected EmoticonPanelView emotionPanel;
    protected OnInputPanelEventListener onInputPanelEventListener;
    private int inputHeight;
    protected KPSwitchPanelLinearLayout rootPanel;



    /** 红包类型
    * */
    protected int mRedPacketType ;

    /** 群成员数量
    * */
    protected long menberId = 0L;

    public SimpleInputPanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public int getPanelHeight() {
        return inputHeight + KeyboardUtil.getKeyboardHeight(getContext());
    }

    public void show() {
        messageEditText.requestFocus();
        this.setVisibility(View.VISIBLE);
        KeyboardUtil.showKeyboard(messageEditText);
    }

    public void dismiss() {
        messageEditText.clearFocus();
        messageEditText.setHint(null);
        messageEditText.setText(null);
        iconEmotion.setSelected(false);
        KeyboardUtil.hideKeyboard(messageEditText);
        setVisibility(View.GONE);
    }



    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        inputHeight = AppTools.dip2px(50);
        sendButton = findViewById(R.id.sendMessageButton);
        sendButton.setOnClickListener(this);
        messageEditText = this.findViewById(R.id.messageEditText);
        emotionPanel = this.findViewById(R.id.emoticonPanelView);
        emotionPanel.setOnEmotionSelectedListener(this);
        messageEditText.addTextChangedListener(this);
        messageEditText.setFilters(new InputFilter[]{this});
        iconEmotion = findViewById(R.id.chat_emotion);
        rootPanel =findViewById(R.id.rootPanel);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initKPSwitchPanel();
    }

    protected void initKPSwitchPanel(){
        KeyboardUtil.attach((Activity)getContext(), rootPanel,this);

        KPSwitchConflictUtil.attach(rootPanel, messageEditText, this,
                new KPSwitchConflictUtil.SubPanelAndTrigger(emotionPanel, iconEmotion)
        );
    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.sendMessageButton && !TextUtils.isEmpty(messageEditText.getText())){
            //将全角中文转换为半角
            String originStr = messageEditText.getText().toString();
            if (onInputPanelEventListener != null) {
                onInputPanelEventListener.onSendButtonClicked(originStr);
            }
        }
    }

    private void appendImageSpan(String key) {

        SpannableString ss = new SpannableString(key);
        int id = LvxinApplication.EMOTION_MAP.get(key);

        Drawable drawable = ContextCompat.getDrawable(getContext(), id);
        if (drawable != null) {
            int size = (int) (0.5F + this.getResources().getDisplayMetrics().density * EMOTION_SIZE);
            drawable.setBounds(0, 0, size, size);
            ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
            ss.setSpan(span, 0, key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //追加到editText
            messageEditText.getEditableText().insert(messageEditText.getSelectionStart(), ss);
        }

    }

    public void clearText() {
        messageEditText.getText().clear();
    }

    @Override
    public void onItemClicked(Object obj, View view) {
        String key = obj.toString();
        messageEditText.setCursorVisible(true);
        if ("DELETE".equals(key)) {
            KeyEvent keyEventDown = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL);
            messageEditText.onKeyDown(KeyEvent.KEYCODE_DEL, keyEventDown);
            return;
        }

        appendImageSpan(key);
    }


    public void setHint(String hint) {
        messageEditText.setHint(hint);
    }

    public void setContent(String text) {
        messageEditText.setText(text);
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    @Override
    public void beforeTextChanged(CharSequence charsequence, int i, int j, int k) {
    }

    @Override
    public void onTextChanged(CharSequence charsequence, int i, int j, int k) {
        if (onInputPanelEventListener != null){
            onInputPanelEventListener.onMessageTextDelete(charsequence.toString());
        }
    }

    @Override
    public void onKeyboardShowing(boolean isShowing) {
        if (onInputPanelEventListener != null){
            onInputPanelEventListener.onKeyboardStateChanged(isShowing);
        }

        if (isShowing) {
            iconEmotion.setSelected(false);
        }
    }

    public void reset() {
        iconEmotion.setSelected(false);
        KPSwitchConflictUtil.hidePanelAndKeyboard(rootPanel);
        messageEditText.setText(null);
    }

    public void hidePanelAndKeyboard() {
        iconEmotion.setSelected(false);
        KPSwitchConflictUtil.hidePanelAndKeyboard(rootPanel);
    }

    public String getInputTextTrim() {
        return messageEditText.getText().toString().trim();
    }

    public String getInputText() {
        return messageEditText.getText().toString();
    }

    public void setInputText(String text) {
        if (!TextUtils.isEmpty(text)) {
            messageEditText.setText(matchEmoticon(text));
        }
    }

    private SpannableStringBuilder matchEmoticon(CharSequence text) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        Matcher matcher = LvxinApplication.EMOTION_PATTERN.matcher(text);
        while (matcher.find()) {
            Integer id = LvxinApplication.EMOTION_MAP.get(matcher.group());
            if (id != null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), id);
                int size = (int) (0.5F + this.getResources().getDisplayMetrics().density * 20);
                drawable.setBounds(0, 0, size, size);
                ImageSpan span = new ImageSpan(drawable);
                builder.setSpan(span, matcher.start(), matcher.end(),  ImageSpan.ALIGN_BASELINE);
            }
        }
        return builder;
    }

    public void setOnInputPanelEventListener(OnInputPanelEventListener onInputPanelEventListener) {
        this.onInputPanelEventListener = onInputPanelEventListener;
    }


    public void applyMessageText(CharSequence charSequence){
        messageEditText.getText().clear();
        messageEditText.getText().append(charSequence);
        messageEditText.requestFocus();
    }

    public int getRedPacketType() {
        return mRedPacketType;
    }

    public void setRedPacketType(int mRedPacketType) {
        this.mRedPacketType = mRedPacketType;
    }

    public void setMenberId(long groupId){
        this.menberId = groupId;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if (Objects.equals("@",source.toString())){
            if (onInputPanelEventListener != null){
                onInputPanelEventListener.onMessageInsertAt();
            }
        }
        return source;
    }
    @Override
    public void onClickSwitch(View target, boolean switchToPanel) {
        if (onInputPanelEventListener != null) {
            onInputPanelEventListener.onPanelStateChanged(switchToPanel);
        }

        if (target == iconEmotion){
            iconEmotion.setSelected(switchToPanel);
        }
    }
}
