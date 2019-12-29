
package com.linkb.jstx.activity.setting;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.database.GlideImageRepository;
import com.farsunset.cim.sdk.android.CIMPushManager;
import com.farsunset.cim.sdk.android.model.SentBody;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.activity.util.PhotoAlbumActivity;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.FileResource;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.database.OrganizationRepository;
import com.linkb.jstx.event.UserInfoChangeEvent;
import com.linkb.jstx.event.WechatShareEvent;
import com.linkb.jstx.fragment.SexChangeDialogFragment;
import com.linkb.jstx.listener.OSSFileUploadListener;
import com.linkb.jstx.network.CloudFileUploader;
import com.linkb.jstx.network.CloudImageLoaderFactory;
import com.linkb.R;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.ModifyPersonInfoResult;
import com.linkb.jstx.util.AppTools;
import com.linkb.jstx.util.BackgroundThreadHandler;
import com.linkb.jstx.util.FileURLBuilder;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileEditActivity extends BaseActivity implements OSSFileUploadListener, SexChangeDialogFragment.OnBottomDialogSelectListener, HttpRequestListener<ModifyPersonInfoResult> {

    private WebImageView icon;
    private User user;
    private SexChangeDialogFragment mSexChangeDialogFragment;
    private String mGender ;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.save_btn)
    TextView rightBtn;

    @Override
    public void initComponents() {
        ButterKnife.bind(this);

        user = Global.getCurrentUser();

        titleTv.setText(getString(R.string.label_setting_profile));
        rightBtn.setText(getString(R.string.label_setting_modify_password));

        findViewById(R.id.iconSwicth).setOnClickListener(this);
        findViewById(R.id.modify_motto_item).setOnClickListener(this);

        ((TextView) findViewById(R.id.account)).setText(user.code);
        ((TextView) findViewById(R.id.name)).setText(user.name);
        ((TextView) findViewById(R.id.email)).setText(user.email);
        ((TextView) findViewById(R.id.telephone)).setText(user.telephone);
        ((TextView) findViewById(R.id.org)).setText(OrganizationRepository.queryOrgName(user.code));


        if (User.GENDER_MAN.equals(user.gender)) {
            ((TextView) findViewById(R.id.gender)).setText(R.string.common_man);
        }
        if (User.GENDER_FEMALE.equals(user.gender)) {
            ((TextView) findViewById(R.id.gender)).setText(R.string.common_female);
        }

        icon = findViewById(R.id.icon);
        icon.load(FileURLBuilder.getUserIconUrl(user.account), R.mipmap.lianxiren, 999);
    }

    @Override
    public void onResume() {
        super.onResume();
        user = Global.getCurrentUser();
        ((TextView) findViewById(R.id.motto)).setText(user.motto);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iconSwicth){
            startActivityForResult(new Intent(this, PhotoAlbumActivity.class), 9);
        }
        if (v.getId() == R.id.modify_motto_item){
            startActivity(new Intent(this, ModifyMottoActivity.class));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 9) {
            AppTools.startIconPhotoCrop(this, data.getData());
        }
        if (resultCode == Activity.RESULT_OK && requestCode == Constant.RESULT_ZOOM && data != null) {
            CloudImageLoaderFactory.get().clearMemory();
            File photo = new File(Global.getCropPhotoFilePath());
            CloudFileUploader.asyncUpload(FileURLBuilder.BUCKET_USER_ICON, user.account, photo, this);
            showProgressDialog(getString(R.string.tip_file_uploading, 0));
        }
        if (resultCode == Activity.RESULT_OK && requestCode == 0x11){
            user = Global.getCurrentUser();
            ((TextView) findViewById(R.id.name)).setText(user.name);
        }
        if (resultCode == Activity.RESULT_OK && requestCode == 0x12){
            user = Global.getCurrentUser();
            ((TextView) findViewById(R.id.telephone)).setText(user.telephone);
        }
        if (resultCode == Activity.RESULT_OK && requestCode == 0x13){
            user = Global.getCurrentUser();
            ((TextView) findViewById(R.id.email)).setText(user.email);
        }
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_profile_edit;
    }


    @Override
    public void onUploadCompleted(FileResource resource) {
        hideProgressDialog();
        SentBody sent = new SentBody();
        sent.setKey(Constant.CIMRequestKey.CLIENT_MODIFY_LOGO);
        sent.put("account", user.account);
        CIMPushManager.sendRequest(this, sent);

        BackgroundThreadHandler.postUIThread(new Runnable() {
            @Override
            public void run() {
                showToastView(R.string.tip_logo_updated);
            }
        });

        GlideImageRepository.save(FileURLBuilder.getUserIconUrl(user.account), String.valueOf(System.currentTimeMillis()));
        Log.d("touxiang", FileURLBuilder.getUserIconUrl(user.account));

        BackgroundThreadHandler.postUIThread(new Runnable() {
            @Override
            public void run() {
                icon.load(FileURLBuilder.getUserIconUrl(user.account), R.mipmap.lianxiren, 999);
            }
        });

        EventBus.getDefault().post(new UserInfoChangeEvent("icon"));
        LvxinApplication.sendLocalBroadcast(new Intent(Constant.Action.ACTION_LOGO_CHANGED));
    }

    @Override
    public void onUploadProgress(String key, float progress) {
        showProgressDialog(getString(R.string.tip_file_uploading, (int) progress));
    }

    @Override
    public void onUploadFailured(FileResource resource, Exception e) {
        hideProgressDialog();
        showToastView(R.string.tip_logo_update_error);
    }

    @OnClick(R.id.modify_name_rly)
    public void modifyName() {
        startActivityForResult(new Intent(this, ModifyNameActivity.class), 0x11);
    }

    @OnClick(R.id.modify_gender_rly)
    public void modifyGender() {
        if (mSexChangeDialogFragment == null){
            mSexChangeDialogFragment = new SexChangeDialogFragment();
            mSexChangeDialogFragment.setListener(this);
        }
        mSexChangeDialogFragment.show(getSupportFragmentManager(), "SexChangeDialogFragment");
    }

    @OnClick(R.id.modify_phone_rly)
    public void modifyPhone() {
        startActivityForResult(new Intent(this, ModifyPhoneActivity.class), 0x12);
    }

    @OnClick(R.id.modify_email_rly)
    public void modifyEmail() {
        startActivityForResult(new Intent(this, ModifyEmailActivity.class), 0x13);
    }

    @OnClick(R.id.save_btn)
    public void save() {
        startActivity(new Intent(this, ModifyPasswordActivity.class));
    }

    @OnClick(R.id.back_btn)
    public void onBack(){finish();}


    @Override
    public void selectFirstItem() {
        mGender = "1";
        HttpServiceManager.modifyPersonInfo("gender","1", this);
    }

    @Override
    public void selectSecondItem() {
        mGender = "0";
        HttpServiceManager.modifyPersonInfo("gender","0", this);
    }

    @Override
    public void onHttpRequestSucceed(ModifyPersonInfoResult result, OriginalCall call) {
        if (result.isSuccess()){
            user.gender = mGender;
            Global.modifyAccount(user);
            showToastView(R.string.tip_save_complete);
            setResult(RESULT_OK, getIntent());
            finish();
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {

    }
}
