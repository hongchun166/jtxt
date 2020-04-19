
package com.linkb.jstx.activity.setting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkb.BuildConfig;
import com.linkb.R;
import com.linkb.jstx.activity.AppNewVersionActivity;
import com.linkb.jstx.activity.base.BaseFragment;
import com.linkb.jstx.activity.contact.GroupQrCodeActivity;
import com.linkb.jstx.activity.contact.GroupQrCodeActivityV2;
import com.linkb.jstx.activity.contact.PhoneContactsActivity;
import com.linkb.jstx.activity.trend.MineMomentActivity;
import com.linkb.jstx.activity.wallet.WalletActivityV2;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.AppVersion;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.ActiveStarsView;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.dialog.QuitAppDialog;
import com.linkb.jstx.event.UserInfoChangeEvent;
import com.linkb.jstx.event.WechatShareEvent;
import com.linkb.jstx.listener.CloudImageLoadListener;
import com.linkb.jstx.network.CloudImageLoaderFactory;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.HttpServiceManagerV2;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.AppVersionResult;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.v2.GetActiveResult;
import com.linkb.jstx.util.FileURLBuilder;
import com.linkb.jstx.util.Util;
import com.linkb.jstx.util.ZXingUtils;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class UserCenterFragmentV2 extends BaseFragment implements CloudImageLoadListener {

    @BindView(R.id.imageView29)
    WebImageView icon;
    @BindView(R.id.textView117)
    TextView versionNumberTv;
    @BindView(R.id.viewUserName)
    TextView viewUserName;
    @BindView(R.id.viewUserSing)
    TextView viewUserSing;

    @BindView(R.id.viewActiveStarsView)
    ActiveStarsView viewActiveStarsView;



    private User user;

    private QuitAppDialog quitAppDialog;
    private LogoChangedReceiver logoChangedReceiver;

    /**
     * 微信分享
     */
    private IWXAPI iwxapi;
    private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        registerToWeixin();
        user = Global.getCurrentUser();
        quitAppDialog = new QuitAppDialog(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine_infomation_v2, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        viewUserName.setText(user.name);
        viewUserSing.setText(user.code);
        CloudImageLoaderFactory.get().downloadOnly(FileURLBuilder.getUserIconUrl(user.account), this);

        icon.load(FileURLBuilder.getUserIconUrl(user.account), R.mipmap.lianxiren, 999);

        versionNumberTv.setText(BuildConfig.VERSION_NAME);
        httpUpdateActive();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        logoChangedReceiver = new LogoChangedReceiver();
        LvxinApplication.registerLocalReceiver(logoChangedReceiver, logoChangedReceiver.getIntentFilter());


    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
        LvxinApplication.unregisterLocalReceiver(logoChangedReceiver);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0x11) {
            user = Global.getCurrentUser();
            viewUserName.setText(user.name);
            viewUserSing.setText(user.code);
            icon.load(FileURLBuilder.getUserIconUrl(user.account), R.mipmap.lianxiren, 999);
        }
    }

    /**
     * 将本APP注册到微信
     */
    private void registerToWeixin() {
        iwxapi = WXAPIFactory.createWXAPI(getActivity(), BuildConfig.WX_APP_ID, true);
        iwxapi.registerApp(BuildConfig.WX_APP_ID);
    }

    private void getWechatToken() {
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_linkb";
        iwxapi.sendReq(req);
    }

    /**
     * 判断是否安装微信
     */
    public boolean isWeiXinAppInstall() {
        if (iwxapi == null)
            iwxapi = WXAPIFactory.createWXAPI(getActivity(), BuildConfig.WX_APP_ID);
        if (iwxapi.isWXAppInstalled()) {
            return true;
        } else {
            showToastView(R.string.no_install_wx);
            return false;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserInfoChanged(UserInfoChangeEvent event) {
        user = Global.getCurrentUser();
        viewUserName.setText(user.name);
        viewUserSing.setText(user.code);
        icon.load(FileURLBuilder.getUserIconUrl(user.account), R.mipmap.lianxiren, 999);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WechatShareEvent event) {
        if (event.isSuccess()) {
        } else {
            showToastView("分享失败");
        }
    }

    @OnClick(R.id.imageView29)
    public void gotoModifyInfoAvatar() {
//        startActivityForResult(new Intent(this.getActivity(), ProfileEditActivity.class), 0x11);
        gotoEditInfoAvatar();
    }


    public void gotoEditInfoAvatar() {
        startActivityForResult(new Intent(this.getActivity(), ProfileEditActivityV2.class), 0x11);
    }

    @OnClick(R.id.xiangce_cly)
    public void onGallery() {
        startActivity(new Intent(this.getActivity(), MineMomentActivity.class));
    }
    @OnClick(R.id.mine_wallet_cly)
    public void onMineWallet(){
        startActivity(new Intent(getActivity(), WalletActivityV2.class));
    }


    @OnClick(R.id.message_set_cly)
    public void onMessageSet() {
        startActivity(new Intent(this.getActivity(), MessageSettingActivity.class));
    }

    @OnClick(R.id.version_update_cly)
    public void onVersionUpdate() {
        showProgressDialog("");
        HttpServiceManager.queryNewAppVersion(checkVersionListener);
    }

    @OnClick(R.id.invite_cly)
    public void goInvite() {
//        startActivity(new Intent(this.getActivity(), PhoneContactsActivity.class));
        GroupQrCodeActivityV2.navToActInvitationDownload(getContext(),user.account,user.name);
    }

    @OnClick(R.id.modify_password_cly)
    public void modifyPassword() {
        startActivity(new Intent(getActivity(), ModifyPasswordActivity.class));
    }

    @OnClick(R.id.viewIVQRCode)
    public void onQrCode() {
        GroupQrCodeActivity.navToActFriend(getContext(),user.account ,user.name);
    }


    @OnClick(R.id.exit_login_card_view)
    public void goExitLogin() {
        quitAppDialog.show();
    }

    private class ShareAsyncTask extends AsyncTask<String, Void, byte[]> {

        @Override
        protected byte[] doInBackground(String... strings) {

            Bitmap bitmapBackground = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher, null);

//            Bitmap shareOriginQrCode = ZXingUtils.createQRImage(strings[0], bitmapBackground.getWidth(), bitmapBackground.getHeight());

            Bitmap bitmap = ZXingUtils.createQRCodeBitmap(strings[0], 800, 800, "UTF-8", "H", "1", getResources().getColor(R.color.app_splash_bg), Color.WHITE, bitmapBackground, 0.2F);

//            Bitmap bitmap = ZXingUtils.createQRImage(strings[0], 800, 800);
//            PointF pointF = new PointF(bitmapBackground.getWidth() / 3, bitmapBackground.getHeight()  / 3);
//
//            Bitmap resultBitmap = ZXingUtils.mixtureBitmap(bitmapBackground, shareOriginQrCode, pointF );
            return Util.bmpToByteArray(bitmap, true);
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            hideProgressDialog();
            WXImageObject imgObj = new WXImageObject(bytes);
            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = imgObj;
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("img");
            req.message = msg;
            req.scene = SendMessageToWX.Req.WXSceneSession;
            iwxapi.sendReq(req);
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @Override
    public void onImageLoadFailure(Object key) {

    }

    @Override
    public void onImageLoadSucceed(Object key, Bitmap resource) {

    }

    private void httpUpdateActive(){
        HttpServiceManagerV2.getActive(user.account, new HttpRequestListener<GetActiveResult>() {
            @Override
            public void onHttpRequestSucceed(GetActiveResult result, OriginalCall call) {
                if(result.isSuccess() && result.getData()!=null){
                    viewActiveStarsView.setStartValue(result.getData().getAvtive());
                }
            }
            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {
            }
        });
    }

    private HttpRequestListener<AppVersionResult> checkVersionListener = new HttpRequestListener<AppVersionResult>() {
        @Override
        public void onHttpRequestSucceed(AppVersionResult result, OriginalCall call) {
            hideProgressDialog();
            if (result.code.equals(Constant.ReturnCode.CODE_200)) {
                Intent intent = new Intent(getActivity(), AppNewVersionActivity.class);
                intent.putExtra(AppVersion.class.getName(), result.data);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToastView(getResources().getString(R.string.already_new_version_tips));
                    }
                });
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {
            hideProgressDialog();
        }
    };

    public class LogoChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
        }

        IntentFilter getIntentFilter() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.Action.ACTION_LOGO_CHANGED);
            return filter;
        }

    }
}
