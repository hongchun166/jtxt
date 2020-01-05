package com.linkb.jstx.activity.setting;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.farsunset.cim.sdk.android.CIMPushManager;
import com.farsunset.cim.sdk.android.model.SentBody;
import com.google.gson.Gson;
import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.activity.util.PhotoAlbumActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.FileResource;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.dialog.MarriageChangeDialogV2;
import com.linkb.jstx.dialog.SexChangeDialogV2;
import com.linkb.jstx.listener.OSSFileUploadListener;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.ModifyPersonInfoResult;
import com.linkb.jstx.network.result.RegionResult;
import com.linkb.jstx.util.FileURLBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileEditActivityV2 extends BaseActivity implements OSSFileUploadListener, HttpRequestListener<ModifyPersonInfoResult> {
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.save_btn)
    TextView rightBtn;
    @BindView(R.id.icon)
    WebImageView imgHeader;
    @BindView(R.id.account)
    TextView tvAccount;
    @BindView(R.id.tv_password)
    TextView tvPassword;
    @BindView(R.id.tv_name)
    TextView tvNAme;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.tv_marriage)
    TextView tvMarriage;
    @BindView(R.id.tv_telephone)
    TextView tvTelephone;
    @BindView(R.id.tv_industry)
    TextView tvIndustry;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    @BindView(R.id.tv_region)
    TextView tvRegion;
    @BindView(R.id.tv_label)
    TextView tvLabel;
    @BindView(R.id.tv_sign)
    TextView tvSign;
    @BindView(R.id.tv_email)
    TextView tvEmail;

    private SexChangeDialogV2 mSexChangeDialog;
    private MarriageChangeDialogV2 mMarriageChangeDialog;
    protected OptionsPickerView pvCustomOptions;
    private List<String> provinces = new ArrayList<>();
    private List<List<String>> citys = new ArrayList<>();
    private WebImageView icon;
    private User user;
    private String mGender;

    @Override
    protected int getToolbarTitle() {
        return super.getToolbarTitle();
    }

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);
        user = Global.getCurrentUser();
        titleTv.setText(getString(R.string.label_setting_profile));
        initUserData();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_profile_edit_v2;
    }


    /**
     * 初始化用户信息
     */
    private void initUserData() {
        if (user == null) return;
        tvGender.setText(User.GENDER_MAN.equals(user.gender) ? R.string.common_man : R.string.common_female);
        imgHeader.load(FileURLBuilder.getUserIconUrl(user.account), R.mipmap.lianxiren, 999);
        tvMarriage.setText(TextUtils.isEmpty(user.marrriage) ? R.string.unmarried : "0".equals(user.marrriage) ? R.string.unmarried : R.string.marriage);
        tvTelephone.setText(TextUtils.isEmpty(user.telephone) ? "" : user.telephone);
        tvAccount.setText(TextUtils.isEmpty(user.code) ? "" : user.code);
        tvNAme.setText(TextUtils.isEmpty(user.name) ? "佚名" : user.name);
        tvEmail.setText(TextUtils.isEmpty(user.email) ? "" : user.email);
        tvSign.setText(TextUtils.isEmpty(user.motto) ? "" : user.motto);
        tvRegion.setText(TextUtils.isEmpty(user.region) ? "" : user.region);
    }


    /**
     * 返回键
     */
    @OnClick(R.id.back_btn)
    public void onBack() {
        finish();
    }


    /**
     * 选择头像
     */
    @OnClick(R.id.iconSwicth)
    public void updateHeader() {
        startActivityForResult(new Intent(this, PhotoAlbumActivity.class), 9);
    }


    /**
     * 修改名称
     */
    @OnClick(R.id.modify_name_rly)
    public void modifyName() {
        startActivityForResult(new Intent(this, ModifyNameActivityV2.class), 0x11);
    }


    /**
     * 修改性别
     */
    @OnClick(R.id.modify_gender_rly)
    public void modifyGender() {
        if (mSexChangeDialog == null) mSexChangeDialog = new SexChangeDialogV2(this);
        mSexChangeDialog.updateStatus((user == null || User.GENDER_MAN.equals(user.gender)) ? 0 : 1);
        mSexChangeDialog.setOnSexCheckListener(new SexChangeDialogV2.OnSexCheckListener() {
            @Override
            public void checkSex(int type) {
                user.gender = type == 0 ? getString(R.string.common_man) : getString(R.string.common_female);
                tvGender.setText(User.GENDER_MAN.equals(user.gender) ? R.string.common_man : R.string.common_female);
                Global.modifyAccount(user);
                mSexChangeDialog.dismiss();
            }
        });
        mSexChangeDialog.show();
    }


    /**
     * 婚姻
     */
    @OnClick(R.id.ll_modify_marriage)
    public void marriage() {
        if (mMarriageChangeDialog == null) mMarriageChangeDialog = new MarriageChangeDialogV2(this);
        mMarriageChangeDialog.updateStatus(user == null ? 0 : TextUtils.isEmpty(user.marrriage) ? 0 : Integer.parseInt(user.marrriage));
        mMarriageChangeDialog.setOnMarriageCheckListener(new MarriageChangeDialogV2.OnMarriageCheckListener() {
            @Override
            public void marriageStatus(int type) {
                user.marrriage = String.valueOf(type);
                tvMarriage.setText(TextUtils.isEmpty(user.marrriage) ? R.string.unmarried : "0".equals(user.marrriage) ? R.string.unmarried : R.string.marriage);

                Global.modifyAccount(user);
                mMarriageChangeDialog.dismiss();
            }
        });
        mMarriageChangeDialog.show();
    }


    /**
     * 修改电话号码
     */
    @OnClick(R.id.modify_phone_rly)
    public void modifyPhone() {
        startActivityForResult(new Intent(this, ModifyPhoneActivityV2.class), 0x12);
    }


    /**
     * 行业
     */
    @OnClick(R.id.modify_industry_rly)
    public void industry() {
    }


    /**
     * 职务
     */
    @OnClick(R.id.modify_position_rly)
    public void position() {
    }


    /**
     * 地址
     */
    @OnClick(R.id.modify_region_rly)
    public void region() {
        if (pvCustomOptions == null) {
            pvCustomOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    String region = "" + provinces.get(options1) + citys.get(options1).get(options2);
                    tvRegion.setText(region);
                    user.region = region;
                    Global.modifyAccount(user);
                }
            })
                    .isDialog(false)
                    .setSubmitText(getString(R.string.finish))
                    .setSubmitColor(ContextCompat.getColor(ProfileEditActivityV2.this, R.color.color_2e76e5))
                    .setCancelText(getString(R.string.common_cancel))
                    .setCancelColor(ContextCompat.getColor(ProfileEditActivityV2.this, R.color.divider_color_gray_999999))
                    .setOutSideCancelable(false)
                    .build();
            pvCustomOptions.setSelectOptions(0, 1, 1);
            Dialog mDialog = pvCustomOptions.getDialog();
            if (mDialog != null) {
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        Gravity.BOTTOM);
                params.leftMargin = 0;
                params.rightMargin = 0;
                pvCustomOptions.getDialogContainerLayout().setLayoutParams(params);
                Window dialogWindow = mDialog.getWindow();
                if (dialogWindow != null) {
                    dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                    dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                    dialogWindow.setDimAmount(0.1f);
                }
            }
        }
        if (provinces == null || provinces.size() == 0 || citys == null || citys.size() == 0)
            getJson("citycode.json");
        pvCustomOptions.setPicker(provinces, citys);
        pvCustomOptions.show();
    }


    /**
     * 标签
     */
    @OnClick(R.id.modify_label_rly)
    public void label() {
    }


    /**
     * 签名
     */
    @OnClick(R.id.modify_sign_rly)
    public void sign() {
        startActivity(new Intent(this, ModifyMottoActivityV2.class));
    }


    /**
     * 修改邮箱
     */
    @OnClick(R.id.modify_email_rly)
    public void modifyEmail() {
        startActivityForResult(new Intent(this, ModifyEmailActivityV2.class), 0x13);
    }

    /**
     * 读取assets本地json
     *
     * @param fileName 文件名称
     */
    public void getJson(final String fileName) {

        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = ProfileEditActivityV2.this.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
            Gson gson = new Gson();
            RegionResult regionResult = gson.fromJson(stringBuilder.toString(), RegionResult.class);
            provinces = new ArrayList<>();
            citys = new ArrayList<>();
            for (int i = 0; i < regionResult.province.size(); i++) {
                provinces.add(regionResult.province.get(i).a);
                List<String> marr = new ArrayList<>();
                for (int j = 0; j < regionResult.province.get(i).city.size(); j++) {
                    marr.add(regionResult.province.get(i).city.get(j).a);
                }
                citys.add(marr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUploadCompleted(FileResource resource) {

    }

    @Override
    public void onUploadProgress(String key, float progress) {

    }

    @Override
    public void onUploadFailured(FileResource resource, Exception e) {

    }

    @Override
    public void onHttpRequestSucceed(ModifyPersonInfoResult result, OriginalCall call) {

    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSexChangeDialog != null) {
            mSexChangeDialog.dismiss();
            mSexChangeDialog = null;
        }
    }
}
