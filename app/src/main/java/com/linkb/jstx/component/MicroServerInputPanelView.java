
package com.linkb.jstx.component;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linkb.jstx.comparator.MenuAscComparator;
import com.linkb.jstx.dialog.MicroServerMenuWindow;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.model.MicroServerMenu;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.listener.OnInputPanelEventListener;
import com.linkb.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.dreamtobe.kpswitch.util.KPSwitchConflictUtil;
import cn.dreamtobe.kpswitch.util.KeyboardUtil;
import cn.dreamtobe.kpswitch.widget.KPSwitchPanelLinearLayout;

public class MicroServerInputPanelView extends FrameLayout implements OnClickListener, OnItemClickedListener, TextWatcher, MicroServerMenuWindow.OnMenuClickListener, KeyboardUtil.OnKeyboardShowingListener,KPSwitchConflictUtil.SwitchClickListener {
    private List<MicroServerMenu> menuList;
    private View iconEmotion;
    private EditText messageEditText;
    private EmoticonPanelView emoticonPanelView;
    private OnInputPanelEventListener onInputPanelEventListener;
    private InputMethodManager inputMethodManager;
    private LinearLayout menuBarView;
    private MicroServerMenuWindow menuWindow;
    private MicroServerMenuWindow.OnMenuClickListener OnMenuClickListener;
    private View menuView;
    private View inputView;
    private View menuSwitchButton;
    private View keyboardSwitchButton;

    protected KPSwitchPanelLinearLayout rootPanel;

    public MicroServerInputPanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        menuWindow = new MicroServerMenuWindow(context, this);
    }


    public void setOnMenuClickListener(MicroServerMenuWindow.OnMenuClickListener onMenuClickListener) {
        OnMenuClickListener = onMenuClickListener;
    }


    public void buildMenus(List<MicroServerMenu> list) {
        this.menuList = list;
        int i = 0;
        List<MicroServerMenu> rootList = getRootMenuList();
        for (MicroServerMenu menu : rootList) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.layout_microserver_root_menu, null);
            ((TextView) itemView.findViewById(R.id.menu_name)).setText(menu.name);
            itemView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
            if (!menu.hasSubMenu()) {
                itemView.findViewById(R.id.root_menu_mark).setVisibility(View.INVISIBLE);
            }
            itemView.setTag(menu);
            itemView.setTag(R.drawable.icon, i);
            itemView.setOnClickListener(this);
            menuBarView.addView(itemView);

            i++;

            if (i < rootList.size()) {
                View divider = new View(getContext());
                divider.setLayoutParams(new LinearLayout.LayoutParams(1, LayoutParams.MATCH_PARENT));
                divider.setBackgroundResource(R.color.list_border);
                menuBarView.addView(divider);
            }
        }
    }

    private List<MicroServerMenu> getSubMenuList(String pid) {

        List<MicroServerMenu> list = new ArrayList<>();
        for (MicroServerMenu menu : menuList) {
            if (pid.equals(menu.fid)) {
                list.add(menu);
            }
        }
        Collections.sort(list, new MenuAscComparator());
        return list;
    }


    private List<MicroServerMenu> getRootMenuList() {

        List<MicroServerMenu> list = new ArrayList<>();
        for (MicroServerMenu menu : menuList) {
            if (menu.isRootMenu()) {
                list.add(menu);
            }
        }

        Collections.sort(list, new MenuAscComparator());
        return list;
    }


    public void show() {
        messageEditText.requestFocus();
        this.setVisibility(View.VISIBLE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        menuBarView = findViewById(R.id.menuBarView);
        messageEditText = this.findViewById(R.id.messageEditText);
        emoticonPanelView = this.findViewById(R.id.emoticonPanelView);
        emoticonPanelView.setOnEmotionSelectedListener(this);
        messageEditText.addTextChangedListener(this);
        inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        iconEmotion = findViewById(R.id.chat_emotion);
        iconEmotion.setOnClickListener(this);



        int cellWidth = Resources.getSystem().getDisplayMetrics().widthPixels / 6;

        menuSwitchButton = findViewById(R.id.menuSwitchButton);
        menuSwitchButton.setOnClickListener(this);
        menuSwitchButton.getLayoutParams().width = cellWidth;

        keyboardSwitchButton = findViewById(R.id.keyboardSwitchButton);
        keyboardSwitchButton.setOnClickListener(this);
        keyboardSwitchButton.getLayoutParams().width = cellWidth;

        findViewById(R.id.sendMessageButton).setOnClickListener(this);

        menuView = findViewById(R.id.menuView);
        inputView = findViewById(R.id.keyboardView);
        rootPanel =findViewById(R.id.rootPanel);


    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        KeyboardUtil.attach((Activity)getContext(), rootPanel,this);
        KPSwitchConflictUtil.attach(rootPanel, messageEditText, this,
                new KPSwitchConflictUtil.SubPanelAndTrigger(emoticonPanelView, iconEmotion)
        );
    }

    private void onEmoticonClicked() {
        if (iconEmotion.isSelected()) {
            iconEmotion.setSelected(false);
            KeyboardUtil.showKeyboard(messageEditText);
        } else {
            iconEmotion.setSelected(true);
            KPSwitchConflictUtil.hidePanelAndKeyboard(rootPanel);
        }
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.chat_emotion:
                onEmoticonClicked();
                break;
            case R.id.sendMessageButton:
                if (!TextUtils.isEmpty(messageEditText.getText())) {
                    onInputPanelEventListener.onSendButtonClicked(messageEditText.getText().toString());
                    ((EditText) findViewById(R.id.messageEditText)).getText().clear();
                }
                break;

            case R.id.root_menu:
                if (((MicroServerMenu) view.getTag()).hasSubMenu()) {
                    menuWindow.showAtLocation(view, getSubMenuList(((MicroServerMenu) view.getTag()).id));
                } else {
                    OnMenuClickListener.onMenuClicked(((MicroServerMenu) view.getTag()));
                }
                break;
            case R.id.keyboardSwitchButton:
                toggleKeyboradMode();
                break;

            case R.id.menuSwitchButton:
                toggleMenuMode();
                break;
        }

    }

    private void toggleKeyboradMode() {
        Animation apperAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.menu_slide_in_from_bottom);
        apperAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                MicroServerInputPanelView.this.clearAnimation();
            }
        });
        inputView.setVisibility(View.VISIBLE);
        this.startAnimation(apperAnimation);
        menuView.setVisibility(View.GONE);
        messageEditText.requestFocus();
    }

    private void toggleMenuMode() {
        Animation menuAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.menu_slide_in_from_bottom);
        menuAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                MicroServerInputPanelView.this.clearAnimation();
            }
        });
        iconEmotion.setSelected(false);
        KPSwitchConflictUtil.hidePanelAndKeyboard(rootPanel);
        menuView.setVisibility(View.VISIBLE);
        this.startAnimation(menuAnimation);
        inputView.setVisibility(View.GONE);
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
        SpannableString ss = new SpannableString(key);

        int id = LvxinApplication.EMOTION_MAP.get(key);

        Drawable drawable = ContextCompat.getDrawable(getContext(), id);
        if (drawable != null) {
            int size = (int) (0.5F + this.getResources().getDisplayMetrics().density * 20);
            drawable.setBounds(0, 0, size, size);
            ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
            ss.setSpan(span, 0, key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //追加到editText
            messageEditText.getEditableText().insert(messageEditText.getSelectionStart(), ss);
        }
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
    }


    public void resetInputPanel() {
        KPSwitchConflictUtil.hidePanelAndKeyboard(rootPanel);
        iconEmotion.setSelected(false);
    }


    @Override
    public void onMenuClicked(MicroServerMenu menu) {
        OnMenuClickListener.onMenuClicked(menu);
    }

    public void setOnInputPanelEventListener(OnInputPanelEventListener onInputPanelEventListener) {
        this.onInputPanelEventListener = onInputPanelEventListener;
    }

    @Override
    public void onKeyboardShowing(boolean isShowing) {
        if (isShowing) {
            iconEmotion.setSelected(false);
        }
    }

    @Override
    public void onClickSwitch(View v, boolean switchToPanel) {
        iconEmotion.setSelected(switchToPanel);
    }
}
