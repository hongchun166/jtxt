package com.linkb.jstx.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.activity.contact.ApplyFriendActivityV2;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.HttpServiceManagerV2;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseDataResult;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.v2.CheckInGroupResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;

public class QrcodeScanActivity extends BaseActivity implements QRCodeView.Delegate {
    private String TAG = QrcodeScanActivity.class.getName();

    /** 二维码种类
    * */
    private int qrCode = -1;

    @BindView(R.id.zbarview)
    ZBarView mZBarView;

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);
        mZBarView.setDelegate(this);

        qrCode = getIntent().getIntExtra(Constant.QrCodeType.QR_CODE_TYPE, -1);

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_qrcode_scan;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mZBarView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
//        mZBarView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别

        mZBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别
    }

    @Override
    protected void onStop() {
        mZBarView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mZBarView.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);

    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i(TAG, "result:" + result);

        vibrate();

//        mZBarView.startSpot(); // 开始识别

        if (qrCode == Constant.QrCodeType.WALLET_QRCODE){
            //钱包扫码
            Intent intent = getIntent();
            intent.putExtra(Constant.QrCodeType.QR_CODE_CONTENT, result);
            setResult(RESULT_OK, intent);
            finish();
            return;
        }

        if (!TextUtils.isEmpty(result) && result.contains(Constant.QrCodeFormater.QR_CODE_SPLIT)){
            String[] str = result.split(Constant.QrCodeFormater.QR_CODE_SPLIT);
            if (str.length > 0) {
                if (str[0].equals(Constant.QrCodeFormater.GROUP_QR_CODE)){
                    //申请加入群组
//                    HttpServiceManager.applyJoinGroup(Long.valueOf(str[1]),  applyJoinGroupListener);
                    checkHasJoinGroup(str[1]);
                }else if (str[0].equals(Constant.QrCodeFormater.PERSON_QR_CODE)){
                    //添加好友
//                    HttpServiceManager.addFriend(str[1], str[2],addFriendRequest);
                      checkHasFriend(str[1],str[2]);
                }else if(str[0].equals(Constant.QrCodeFormater.Invitation_TO_Download_QR_CODE)){
                    String downloadUrl=str[3];
                    Uri uri = Uri.parse(downloadUrl);    //设置跳转的网站
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    finish();
                }
            }
        }else {
            showToastView(getString(R.string.read_qr_code_error));
        }
    }

    private void checkHasFriend(String friendAccount,String friendName){
        User user= Global.getCurrentUser();
        HttpServiceManagerV2.checkFriend(user.account, friendAccount, new HttpRequestListener<CheckInGroupResult>() {
            @Override
            public void onHttpRequestSucceed(CheckInGroupResult result, OriginalCall call) {
                hideProgressDialog();
                if(result.isSuccess()){
                    if(result.isData()){
                        showToastView(getResources().getString(R.string.add_friend_success_tips));
                        finish();
                    }else {
                        httpAddFriend(friendAccount,friendName);
                    }
                }
            }
            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {
                hideProgressDialog();
            }
        });
    }

    private void checkHasJoinGroup(String groupId){
        User user= Global.getCurrentUser();
        HttpServiceManagerV2.checkInGroup(user.account, groupId, new HttpRequestListener<CheckInGroupResult>() {
            @Override
            public void onHttpRequestSucceed(CheckInGroupResult result, OriginalCall call) {
                hideProgressDialog();
                if(result.isSuccess()){
                    if(result.isData()){
                        showToastView(getResources().getString(R.string.apply_join_group_error_tips));
                    }else {
                        httpJoinGroup(groupId);
                    }
                }
            }
            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {
                hideProgressDialog();
            }
        });
    }

    private void httpJoinGroup(String groupId){
        HttpServiceManager.applyJoinGroup(Long.valueOf(groupId),  applyJoinGroupListener);
    }
    private void httpAddFriend(String friendAccount,String friendName){
        Friend friend = new Friend();
        friend.name = friendName;
        friend.account =friendAccount;
        Intent intent=new Intent(this, ApplyFriendActivityV2.class);
        intent.putExtra(Friend.class.getName(),friend);
        startActivity(intent);
        finish();
    }

    /** 申请加群
    * */
    private HttpRequestListener<BaseDataResult> applyJoinGroupListener = new HttpRequestListener<BaseDataResult>() {
        @Override
        public void onHttpRequestSucceed(BaseDataResult result, OriginalCall call) {
            hideProgressDialog();
            if (result.isSuccess()){
                showToastView(getResources().getString(R.string.apply_join_group_tips));
                finish();
            }else {
                if (result.code.equals("470")){
                    showToastView(getResources().getString(R.string.apply_join_group_error_tips));
                }else {
                    showToastView(getResources().getString(R.string.read_qr_code_error));
                }

            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {
            hideProgressDialog();
        }
    };

//    /** 申请好友
//    * */
//    private HttpRequestListener<BaseResult> addFriendRequest = new HttpRequestListener<BaseResult>() {
//        @Override
//        public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
//            hideProgressDialog();
//            if (result.isSuccess()){
//                showToastView(getResources().getString(R.string.add_friend_success_tips));
//                LvxinApplication.sendLocalBroadcast(new Intent(Constant.Action.ACTION_RELOAD_CONTACTS));
//                finish();
//            }else {
//                showToastView(result.message);
//            }
//        }
//
//        @Override
//        public void onHttpRequestFailure(Exception e, OriginalCall call) {
//            hideProgressDialog();
//        }
//    };

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {

    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }

    @OnClick(R.id.button9)
    public void startScan(){
        mZBarView.startCamera();
        mZBarView.startSpot();
    }

    @OnClick(R.id.back_btn)
    public void onBack(){
        finish();
    }
}
