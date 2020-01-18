package com.linkb.jstx.activity.contact;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.activity.chat.FriendChatActivity;
import com.linkb.jstx.activity.setting.ModifyRemarkActivity;
import com.linkb.jstx.activity.trend.FriendMomentActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.database.StarMarkRepository;
import com.linkb.jstx.dialog.CustomDialog;
import com.linkb.jstx.listener.OnDialogButtonClickListener;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseDataResult;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.util.FileURLBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/*个人详细信息页面*/
public class PersonInfoActivity extends BaseActivity implements OnDialogButtonClickListener {

    private static final int APPLY_FRIEND_REQEUST_CODE = 0x21;

    /**个人信息
    * */
    private Friend friend;
    @BindView(R.id.imageView12) WebImageView avatarImageView;
    @BindView(R.id.textView51) TextView nameTv;
    @BindView(R.id.textView55) TextView signatureTv;
    @BindView(R.id.textView53) TextView blinkAccounterName;


    @BindView(R.id.add_friend_btn)
    Button addFriendBtn;
    @BindView(R.id.send_message_btn)
    Button sendMessageBtn;

    private User mUser;
    private String reMarkName;

    private boolean isDeleteFriend = false;
    /** 删除好友确认框
    * */
    private CustomDialog customDialog;

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);
        ImmersionBar.with(this)
                .keyboardEnable(true)
                .statusBarDarkFont(true)
                .init();

        setToolbarTitle(getResources().getString(R.string.person_info));

        mUser = Global.getCurrentUser();
        friend = (Friend) getIntent().getSerializableExtra(Friend.class.getName());
        reMarkName = getIntent().getStringExtra("reMarkName");

        avatarImageView.load(FileURLBuilder.getUserIconUrl(friend.account), R.mipmap.lianxiren, 999);
        nameTv.setText(TextUtils.isEmpty(reMarkName) ? friend.name : reMarkName);
        initConfirmDialog(TextUtils.isEmpty(reMarkName) ? friend.name : reMarkName);
        blinkAccounterName.setText(friend.code);

        HttpServiceManager.checkIsFriend(friend.account, checkFiendListener);
    }

    /** 初始化删除好友确认弹框
    * */
    private void initConfirmDialog(String friendName) {
        customDialog = new CustomDialog(this);
        customDialog.setOnDialogButtonClickListener(this);
        customDialog.setIcon(R.drawable.icon_dialog_delete);
        customDialog.setMessage(getString(R.string.confirm_delete_friend, friendName));
        customDialog.setOnDialogButtonClickListener(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_person_info_v2;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.person_info_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_friend:    //删除好友
                customDialog.show();
                break;
            case R.id.report:       //举报
                showToastView(getResources().getString(R.string.accelerate_develop));
                break;
            case R.id.share_card:       //分享名片
                showToastView(getResources().getString(R.string.accelerate_develop));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0x11){
            if (data != null){
                reMarkName = data.getStringExtra("name");
                nameTv.setText(reMarkName);
            }
        }
    }

    @OnClick(R.id.viewDetailedInfoItem)
    public void gotoDetailedInfo(){
        //详细信息
        UserDetailActivityV2.navToAct(this,friend.account);
    }
    @OnClick(R.id.bar_album_cly)
    public void gotoMomentRule(){
        //朋友圈
        Intent uintent = new Intent(this, FriendMomentActivity.class);
        uintent.putExtra(Friend.class.getName(), friend);
        startActivity(uintent);
    }

    @OnClick(R.id.permission_cly)
    public void gotoRemark(){
        //权限设置
        Intent intent = new Intent(this, MomentRuleActivity.class);
        intent.putExtra(Friend.NAME, friend);
        startActivityForResult(intent, MomentRuleActivity.REQUEST_CODE);
    }

    @OnClick(R.id.start_mark_cly)
    public void gotoCommonGroup(){
        //星标标记
        if (StarMarkRepository.isStarMark(friend.account)) {
            StarMarkRepository.delete(friend.account);
            findViewById(R.id.icon_starmark).setSelected(false);

        } else {
            StarMarkRepository.save(friend.account);
            findViewById(R.id.icon_starmark).setSelected(true);
        }
        LvxinApplication.sendLocalBroadcast(new Intent(Constant.Action.ACTION_RELOAD_CONTACTS));
    }

    @OnClick(R.id.send_message_btn)
    public void gotoChart(){
        //去聊天
        Intent intent = new Intent(this, FriendChatActivity.class);
        intent.putExtra(Constant.CHAT_OTHRES_ID, friend.account);
        intent.putExtra(Constant.CHAT_OTHRES_NAME, reMarkName);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.add_friend_btn)
    public void applyFiren() {
        //申请好友
        Intent intent = new Intent(PersonInfoActivity.this, ApplyFriendActivity.class);
        intent.putExtra(Friend.class.getName(), friend);
        startActivityForResult(intent, APPLY_FRIEND_REQEUST_CODE);
//        HttpServiceManager.addFriend(friend.account, friend.getName(),addFriendRequest);
    }

    @OnClick(R.id.set_remark_cly)
    public void setRemark(){
        //设置备注
        Intent intent = new Intent(this, ModifyRemarkActivity.class);
        intent.putExtra(Friend.class.getName(), friend);
        intent.putExtra("reMarkName", reMarkName);
        startActivityForResult(intent, 0x11);

    }

    private HttpRequestListener<BaseResult> deleteListener = new HttpRequestListener<BaseResult>() {
        @Override
        public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
            hideProgressDialog();
            showToastView(result.message);
            if (result.isSuccess()){
                LvxinApplication.sendLocalBroadcast(new Intent(Constant.Action.ACTION_RELOAD_CONTACTS));
                MessageRepository.deleteBySenderOrReceiver(friend.account);
                Intent intent = new Intent(Constant.Action.ACTION_DELETE_FRIEND);
                LvxinApplication.sendLocalBroadcast(intent);

                isDeleteFriend = true;
                finish();
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {
            hideProgressDialog();
        }
    };

    private HttpRequestListener<BaseDataResult> checkFiendListener = new HttpRequestListener<BaseDataResult>() {
        @Override
        public void onHttpRequestSucceed(BaseDataResult result, OriginalCall call) {
            if (result.isSuccess()){
                if (result.getData().equals("false") && !mUser.account.equals(friend.account)){
                    addFriendBtn.setVisibility(View.VISIBLE);
                }else {
                    addFriendBtn.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {

        }
    };

    private HttpRequestListener<BaseResult> addFriendRequest = new HttpRequestListener<BaseResult>() {
        @Override
        public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
            hideProgressDialog();
            if (result.isSuccess()){
                showToastView(result.message);
                LvxinApplication.sendLocalBroadcast(new Intent(Constant.Action.ACTION_RELOAD_CONTACTS));
            }else {
                showToastView(result.message);
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {
            hideProgressDialog();
        }
    };

    @Override
    public void finish() {
        Intent intent = getIntent();
        intent.putExtra("reMarkName", nameTv.getText().toString());
        intent.putExtra("deleteFriend", isDeleteFriend);
        setResult(RESULT_OK, intent);
        super.finish();
    }

    @Override
    public void onLeftButtonClicked() {
        customDialog.dismiss();
    }

    @Override
    public void onRightButtonClicked() {
        customDialog.dismiss();
        showProgressDialog("");
        HttpServiceManager.deleteFriend(friend.account, deleteListener);
    }
}
