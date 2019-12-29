
package com.linkb.jstx.component;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.linkb.R;
import com.linkb.jstx.activity.chat.FriendChatActivity;
import com.linkb.jstx.activity.chat.MapLocationActivity;
import com.linkb.jstx.adapter.wallet.ContentPagerAdapter;
import com.linkb.jstx.fragment.ChatInputToolFirstFragment;
import com.linkb.jstx.fragment.ChatInputToolSecondFragment;
import com.linkb.jstx.util.ClipboardUtils;
import com.linkb.jstx.util.StringUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.circlenavigator.CircleNavigator;

import java.util.ArrayList;
import java.util.List;

import cn.dreamtobe.kpswitch.util.KPSwitchConflictUtil;
import cn.dreamtobe.kpswitch.util.KeyboardUtil;
import cn.dreamtobe.kpswitch.widget.KPSwitchPanelLinearLayout;
import pub.devrel.easypermissions.EasyPermissions;

import static android.media.AudioRecord.MetricsConstants.CHANNELS;


public class ChattingInputPanelView extends SimpleInputPanelView {

    public final static int PERMISSION_RADIO = 100;
    public final static int PERMISSION_RADIO_VIDEO = 101;
    public final static int PERMISSION_LOCATION = 200;
    private View centerInputBox;
    private View iconVoice;
    private View iconTool;
    private View toolPanel;
    private View voiceButton;
    /** 输入框工具栏ViewPager
    * */
    private ViewPager toolViewPager;
    private MagicIndicator magicIndicator;
    private ContentPagerAdapter mContentPagerAdapter;
    private ChatInputToolFirstFragment firstFragment;
    private ChatInputToolSecondFragment secondFragment;
    private List<Fragment> mFragmentList= new ArrayList<>();

    public ChattingInputPanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private ChattingPanelClickListener mChattingPanelClickListener;

    public void setChattingPanelClickListener(ChattingPanelClickListener chattingPanelClickListener) {
        this.mChattingPanelClickListener = chattingPanelClickListener;
        firstFragment.setListener(chattingPanelClickListener);
        secondFragment.setListener(chattingPanelClickListener);
    }


    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        iconTool = findViewById(R.id.chating_select_more);
        iconVoice = findViewById(R.id.leftSwitchButton);
        toolViewPager = findViewById(R.id.toolViewPager);
        magicIndicator = findViewById(R.id.magic_indicator1);
//        findViewById(R.id.tool_camera).setOnClickListener(this);
//        findViewById(R.id.tool_photo).setOnClickListener(this);
//        findViewById(R.id.tool_file).setOnClickListener(this);
//        findViewById(R.id.tool_location).setOnClickListener(this);
//        findViewById(R.id.tool_red_packet).setOnClickListener(this);
//        findViewById(R.id.tool_recommend_contacts).setOnClickListener(this);
//        findViewById(R.id.tool_video_connect).setOnClickListener(this);
//        findViewById(R.id.tool_voice_connect).setOnClickListener(this);

        iconVoice.setOnClickListener(this);
        centerInputBox = findViewById(R.id.centerInputBox);
        toolPanel =findViewById(R.id.toolPanelView);
        voiceButton = findViewById(R.id.voiceButton);

        firstFragment = ChatInputToolFirstFragment.getInstance();
        secondFragment = ChatInputToolSecondFragment.getInstance();
        mFragmentList.add(firstFragment);
        mFragmentList.add(secondFragment);
        mContentPagerAdapter = new ContentPagerAdapter(((FragmentActivity) getContext()).getSupportFragmentManager(), mFragmentList);
        toolViewPager.setAdapter(mContentPagerAdapter);
        initMagicIndicator1();
    }

    private void initMagicIndicator1() {
        ScaleCircleNavigator scaleCircleNavigator = new ScaleCircleNavigator(getContext());
        scaleCircleNavigator.setCircleCount(mFragmentList.size());
        scaleCircleNavigator.setNormalCircleColor(Color.LTGRAY);
        scaleCircleNavigator.setSelectedCircleColor(Color.DKGRAY);
        scaleCircleNavigator.setCircleClickListener(new ScaleCircleNavigator.OnCircleClickListener() {
            @Override
            public void onClick(int index) {
                toolViewPager.setCurrentItem(index);
            }
        });
        magicIndicator.setNavigator(scaleCircleNavigator);
        ViewPagerHelper.bind(magicIndicator, toolViewPager);
    }


    @Override
    protected void initKPSwitchPanel(){
        KeyboardUtil.attach((Activity)getContext(), rootPanel,this);
        KPSwitchConflictUtil.attach(rootPanel, messageEditText, this,
                new KPSwitchConflictUtil.SubPanelAndTrigger(emotionPanel, iconEmotion),
                new KPSwitchConflictUtil.SubPanelAndTrigger(toolPanel, iconTool));
    }


    public KPSwitchPanelLinearLayout getRootPanel(){
        return  rootPanel;
    }

    private boolean checkAudioPermission() {
        if (EasyPermissions.hasPermissions(getContext(),Manifest.permission.RECORD_AUDIO)){
            return true;
        }
        EasyPermissions.requestPermissions((Activity) getContext(), getContext().getString(R.string.tip_permission_audio_disable), PERMISSION_RADIO, Manifest.permission.RECORD_AUDIO);
        return false;
    }

    private boolean checkAudioVideoPermission() {
        if (EasyPermissions.hasPermissions(getContext(),Manifest.permission.RECORD_AUDIO)){
            return true;
        }
        EasyPermissions.requestPermissions((Activity) getContext(), getContext().getString(R.string.tip_permission_audio_disable), PERMISSION_RADIO_VIDEO, Manifest.permission.RECORD_AUDIO);
        return false;
    }

    private boolean checkLocationPermission() {
        if (EasyPermissions.hasPermissions(getContext(),Manifest.permission.ACCESS_FINE_LOCATION)){
            return true;
        }
        EasyPermissions.requestPermissions((Activity) getContext(), getContext().getString(R.string.tip_permission_location_disable), PERMISSION_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
        return false;
    }

    public void onCameraButtonClicked() {
//        if (!checkAudioVideoPermission()) {
//            return;
//        }

//        Intent intent = new Intent(getContext(), VideoRecorderActivity.class);
//        ((AppCompatActivity) getContext()).startActivityForResult(intent, 1);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
//            case R.id.tool_voice_connect:
//                //语音通话
//                if (mChattingPanelClickListener != null){
//                    mChattingPanelClickListener.onClickVoiceConnect();
//                }
//                break;
//            case R.id.tool_video_connect:
//                //视频通话
//                if (mChattingPanelClickListener != null){
//                    mChattingPanelClickListener.onClickVideoConnect();
//                }
//                break;
            case R.id.leftSwitchButton:
                if (iconVoice.isSelected()){
                    onKeyboardMenuClicked();
                }else{
                    onRadioButtonClicked();
                }

                break;
//            case R.id.tool_camera:
//                onCameraButtonClicked();
//                break;

            case R.id.tool_photo:

//                Intent intentFromGallery = new Intent(getContext(), PhotoAlbumActivity.class);
//                ((Activity) getContext()).startActivityForResult(intentFromGallery, 2);
                break;


            case R.id.tool_file:

//                Intent intentFile = new Intent(getContext(), FileChoiceActivity.class);
//                ((Activity) getContext()).startActivityForResult(intentFile, 3);
                break;
            case R.id.tool_location:
//                onMapButtonClicked();
//                Intent intentTransfer = new Intent(getContext(), CoinTransferActivity.class);
//                ((Activity) getContext()).startActivityForResult(intentTransfer, FriendChatActivity.COIN_TRANSFER_REQUEST_CODE);
                break;

            case R.id.sendMessageButton:
                //将全角中文转换为半角
                String originStr = messageEditText.getText().toString();
                if (!TextUtils.isEmpty(originStr)){
                    if (onInputPanelEventListener != null){
                        this.onInputPanelEventListener.onSendButtonClicked(originStr);
                    }
                }
                break;
            case R.id.tool_red_packet:
//                Intent intentRedPacket = new Intent(getContext(), RedPacketActivity.class);
//                intentRedPacket.putExtra(Constant.RedPacketType.RED_PACKET_TYPE, mRedPacketType);
//                intentRedPacket.putExtra(Constant.GROUP_ID, menberId);
//                ((Activity) getContext()).startActivityForResult(intentRedPacket, SEND_RED_PACKET_REQUEST_CODE);
                break;
            case R.id.tool_recommend_contacts:
//                //由推荐联系人改为发送名片
//                Intent intent = new Intent(getContext(), SendCardsSelectContactActivity.class);
//                ((Activity) getContext()).startActivityForResult(intent , SendCardsSelectContactActivity.SELECT_CONTACTS_CARDS_REQUEST);

//                ((Activity) getContext()).startActivity(new Intent(getContext(), PhoneContactsActivity.class));
                break;
        }

    }

    private void onKeyboardMenuClicked(){
        centerInputBox.setVisibility(VISIBLE);
        toolViewPager.setVisibility(GONE);
        iconVoice.setSelected(false);
        voiceButton.setVisibility(GONE);
        KeyboardUtil.showKeyboard(messageEditText);
        if (!TextUtils.isEmpty(messageEditText.getText().toString())) {
            iconTool.setVisibility(GONE);
            sendButton.setVisibility(VISIBLE);
        }
    }
    public void onRadioButtonClicked() {

        if (!checkAudioPermission()) {
            return;
        }

        KPSwitchConflictUtil.hidePanelAndKeyboard(rootPanel);

        iconTool.setVisibility(VISIBLE);
        sendButton.setVisibility(INVISIBLE);
        iconEmotion.setSelected(false);
        iconVoice.setSelected(true);
        voiceButton.setVisibility(VISIBLE);
        centerInputBox.setVisibility(GONE);
        KeyboardUtil.hideKeyboard(messageEditText);

    }


    public void onMapButtonClicked() {

        if (!checkLocationPermission()) {
            return;
        }
        Intent intentLoc = new Intent(getContext(), MapLocationActivity.class);
        ((Activity) getContext()).startActivityForResult(intentLoc, 4);
    }

    @Override
    public void onKeyboardShowing(boolean visable) {
        super.onKeyboardShowing(visable);
//        centerInputBox.setVisibility(VISIBLE);
//        voiceButton.setVisibility(GONE);
    }

    @Override
    public void onTextChanged(CharSequence content, int i, int j, int k) {

        if (messageEditText.getVisibility() != VISIBLE) {
            return;
        }
        if (!StringUtils.isEmpty(content)) {
            iconTool.setVisibility(GONE);
            sendButton.setVisibility(VISIBLE);
        } else {
            iconTool.setVisibility(VISIBLE);
            sendButton.setVisibility(INVISIBLE);
        }
    }


    @Override
    public void onClickSwitch(View target,boolean switchToPanel) {
        super.onClickSwitch(target,switchToPanel);

        if (target == iconTool){
            iconVoice.setSelected(false);
            iconEmotion.setSelected(false);
            voiceButton.setVisibility(GONE);
            centerInputBox.setVisibility(VISIBLE);

            if (ClipboardUtils.getUri()  != null) {
                ((FriendChatActivity)getContext()).sendCopyUriBitmap(ClipboardUtils.getUri(), target);
            }
        }
    }

    /** 输入框工具栏点击事件监听
    * */
    public interface ChattingPanelClickListener {
        void onChatInputCamera();
        void onChatInputPhoto();
        void onChatInputFile();
        void onChatInputLocation();
        void onChatInputSendCards();
        void onClickVideoConnect();
        void onClickVoiceConnect();
        void onChatInputRedPacket();
        void onCoinTransfer();
        void onRecommendContact();
    }

}
