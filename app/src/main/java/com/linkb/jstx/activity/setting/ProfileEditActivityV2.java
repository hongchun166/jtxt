package com.linkb.jstx.activity.setting;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.farsunset.cim.sdk.android.CIMPushManager;
import com.farsunset.cim.sdk.android.model.SentBody;
import com.google.gson.Gson;
import com.j256.ormlite.stmt.query.In;
import com.linkb.R;
import com.linkb.jstx.activity.SelectCountryActivity;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.activity.util.PhotoAlbumActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.FileResource;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.database.GlideImageRepository;
import com.linkb.jstx.dialog.MarriageChangeDialogV2;
import com.linkb.jstx.dialog.SexChangeDialogV2;
import com.linkb.jstx.event.UserInfoChangeEvent;
import com.linkb.jstx.listener.OSSFileUploadListener;
import com.linkb.jstx.model.world_area.CountryBean;
import com.linkb.jstx.model.world_area.WorlAreaOpt;
import com.linkb.jstx.network.CloudFileUploader;
import com.linkb.jstx.network.CloudImageLoaderFactory;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.HttpServiceManagerV2;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.ModifyPersonInfoResult;
import com.linkb.jstx.network.result.RegionResult;
import com.linkb.jstx.util.AppTools;
import com.linkb.jstx.util.BackgroundThreadHandler;
import com.linkb.jstx.util.FileURLBuilder;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileEditActivityV2 extends BaseActivity implements OSSFileUploadListener, HttpRequestListener<ModifyPersonInfoResult> {
    final int REQUEST_CODE_CountryBean=0x10;
    final int REQUEST_CODE_Name=0x11;
    final int REQUEST_CODE_Telephone=0x12;
    final int REQUEST_CODE_Email=0x13;
    final int REQUEST_CODE_Industry=0x14;
    final int REQUEST_CODE_Motto=0x15;
    final int REQUEST_CODE_Label=0x16;
    final int REQUEST_CODE_Position=0x17;
    final int REQUEST_CODE_Head=0x18;

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.save_btn)
    TextView rightBtn;
    @BindView(R.id.icon)
    WebImageView imgHeader;
    @BindView(R.id.account)
    TextView tvAccount;
    @BindView(R.id.llPassword)
    LinearLayout llPassword;
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
    @BindView(R.id.tv_job)
    TextView tv_job;
    @BindView(R.id.tv_region)
    TextView tvRegion;
    @BindView(R.id.tv_label)
    TextView tvLabel;
    @BindView(R.id.tv_sign)
    TextView tvSign;
    @BindView(R.id.modify_email_rly)
    LinearLayout modify_email_rly;
    @BindView(R.id.tv_email)
    TextView tvEmail;


    private SexChangeDialogV2 mSexChangeDialog;
    private MarriageChangeDialogV2 mMarriageChangeDialog;
    protected OptionsPickerView pvCustomOptions;
    private List<String> provinces = new ArrayList<>();
    private List<List<String>> citys = new ArrayList<>();
    private User user;

    WorlAreaOpt worlAreaOpt;

    @Override
    protected int getToolbarTitle() {
        return super.getToolbarTitle();
    }

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);
        user = Global.getCurrentUser();
        titleTv.setText(getString(R.string.label_setting_profile));
        llPassword.setVisibility(View.GONE);
        modify_email_rly.setVisibility(View.GONE);
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
        tvMarriage.setText(TextUtils.isEmpty(user.marrriage) ? R.string.unmarried2 : "0".equals(user.marrriage) ? R.string.unmarried2 : R.string.marriage2);
        tvTelephone.setText(TextUtils.isEmpty(user.telephone) ? "" : user.telephone);
        tvAccount.setText(TextUtils.isEmpty(user.code) ? "" : user.code);
        tvNAme.setText(TextUtils.isEmpty(user.name) ? "" : user.name);//佚名
        tvEmail.setText(TextUtils.isEmpty(user.email) ? "" : user.email);
        tvSign.setText(TextUtils.isEmpty(user.motto) ? "" : user.motto);
        tvRegion.setText(TextUtils.isEmpty(user.area) ? "" : user.area);
        tvIndustry.setText(TextUtils.isEmpty(user.industry) ? "" : user.industry);
        tvLabel.setText(TextUtils.isEmpty(user.tag) ? "" : user.tag);
        tv_job.setText(TextUtils.isEmpty(user.position) ? "" : user.position);
        worlAreaOpt = new WorlAreaOpt();
        worlAreaOpt.loadWorldAreaData(this);
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
        startActivityForResult(new Intent(this, PhotoAlbumActivity.class), REQUEST_CODE_Head);
    }


    /**
     * 修改名称
     */
    @OnClick(R.id.modify_name_rly)
    public void modifyName() {
        startActivityForResult(new Intent(this, ModifyNameActivityV2.class), REQUEST_CODE_Name);
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
//                HttpServiceManager.modifyPersonInfo("gender", type == 0 ? String.valueOf(1) : String.valueOf(0), ProfileEditActivityV2.this);
                final User userTemp=new User();
                userTemp.gender=type == 0 ? String.valueOf(1) : String.valueOf(0);
                HttpServiceManagerV2.updateUserInfo(userTemp,httpRequestListener);

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

                String unmarried=getString(R.string.unmarried2);
                String marriage=getString(R.string.marriage2);
                final User userTemp=new User();
                userTemp.marrriage=TextUtils.isEmpty(user.marrriage) ? unmarried : "0".equals(user.marrriage) ? unmarried: marriage;
                HttpServiceManagerV2.updateUserInfo(userTemp,httpRequestListener);


                tvMarriage.setText(TextUtils.isEmpty(user.marrriage) ? R.string.unmarried2 : "0".equals(user.marrriage) ? R.string.unmarried2 : R.string.marriage2);

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
        startActivityForResult(new Intent(this, ModifyPhoneActivityV2.class),REQUEST_CODE_Telephone);
    }


    /**
     * 行业
     */
    @OnClick(R.id.modify_industry_rly)
    public void industry() {
        ModifyIndustryActivityV2.navToActByUpdateTag(this, REQUEST_CODE_Industry);
    }


    /**
     * 职务
     */
    @OnClick(R.id.modify_position_rly)
    public void position() {
        ModifyPositionActivityV2.navToAct(this,REQUEST_CODE_Position);

    }

    CountryBean countryBean;
    /**
     * 地址
     */
    @OnClick(R.id.modify_region_rly)
    public void region() {
        changeRegion(null);
        showRegionDialog();
    }

    private void showRegionDialog(){
        if (pvCustomOptions == null) {
            pvCustomOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    String region = "" + provinces.get(options1) + "-" + citys.get(options1).get(options2);

                    final User userTemp=new User();
                    userTemp.area=region;
                    HttpServiceManagerV2.updateUserInfo(userTemp,httpRequestListener);

                    tvRegion.setText(region);
                    user.area = region;
                    Global.modifyAccount(user);
                }
            }).setLayoutRes(R.layout.view_region_options_layout, new CustomListener() {
                @Override
                public void customLayout(View v) {
                    final TextView viewCancel = (TextView) v.findViewById(R.id.viewCancel);
                    final TextView viewFinish = (TextView) v.findViewById(R.id.viewFinish);
                    TextView viewCountry = (TextView) v.findViewById(R.id.viewCountry);
                    viewCountry.setText(countryBean.getCname());
                    viewFinish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pvCustomOptions.returnData();
                            pvCustomOptions.dismiss();
                        }
                    });
                    viewCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pvCustomOptions.dismiss();
                        }
                    });
                    viewCountry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivityForResult(
                                    new Intent(ProfileEditActivityV2.this, SelectCountryActivity.class)
                                    ,REQUEST_CODE_CountryBean);
                        }
                    });
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
        pvCustomOptions.setPicker(provinces, citys);
        pvCustomOptions.show();
    }
    private void changeRegion(CountryBean countryBean){
        this.countryBean=countryBean;
        if(pvCustomOptions!=null){
            pvCustomOptions.dismiss();
            pvCustomOptions=null;
        }
        if(provinces!=null)provinces.clear();
        if(citys!=null)citys.clear();
        loadRegionData();
        if(provinces.size()==0 ){
            tvRegion.setText(countryBean.getCname());
            if(pvCustomOptions!=null){
                pvCustomOptions.dismiss();
                pvCustomOptions=null;
            }
        }else if(citys.size()==0){
            tvRegion.setText(countryBean.getCname());
            if(pvCustomOptions!=null){
                pvCustomOptions.dismiss();
                pvCustomOptions=null;
            }
        }else {
            showRegionDialog();
        }
    }
    private void loadRegionData(){
        if(countryBean==null){
            countryBean=new CountryBean();
            countryBean.setId("44");//中国
            countryBean.setCname("中国");
            countryBean.setName("China");
        }
        if(provinces == null || provinces.size() == 0 || citys == null || citys.size() == 0){
            if(countryBean.getId().equals("44")){
                getJson("citycode.json");
            }else {
                List<List<String>> provincesDouList = worlAreaOpt.qureyProvinceList(countryBean.getId());
                provinces = provincesDouList.get(1);
                citys = worlAreaOpt.qureyCityList(provincesDouList.get(0));
                if(provinces.size()==1 && provinces.get(0).equals("")){
                    provinces.clear();
                    provinces.add(countryBean.getCname());
                }
            }
        }
    }
    /**
     * 标签
     */
    @OnClick(R.id.modify_label_rly)
    public void label() {
        ModifyLabelActivityV2.navToActByUpdateTag(this,REQUEST_CODE_Label, tvLabel.getText().toString().trim());

    }


    /**
     * 签名
     */
    @OnClick(R.id.modify_sign_rly)
    public void sign() {
        startActivityForResult(new Intent(this, ModifyMottoActivityV2.class), REQUEST_CODE_Motto);
    }


    /**
     * 修改邮箱
     */
    @OnClick(R.id.modify_email_rly)
    public void modifyEmail() {
        startActivityForResult(new Intent(this, ModifyEmailActivityV2.class), REQUEST_CODE_Email);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQUEST_CODE_CountryBean && Activity.RESULT_OK==resultCode){
            CountryBean countryBean= (CountryBean) data.getSerializableExtra("CountryBean");
            changeRegion(countryBean);
            return;
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_Head) {
            AppTools.startIconPhotoCrop(this, data.getData());
        }
        if (resultCode == Activity.RESULT_OK && requestCode == Constant.RESULT_ZOOM && data != null) {
            CloudImageLoaderFactory.get().clearMemory();
            File photo = new File(Global.getCropPhotoFilePath());
            CloudFileUploader.asyncUpload(FileURLBuilder.BUCKET_USER_ICON, user.account, photo, this);
            showProgressDialog(getString(R.string.tip_file_uploading, 0));
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_Name) {
            user = Global.getCurrentUser();
            tvNAme.setText(user.name);
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_Telephone) {
            user = Global.getCurrentUser();
            tvTelephone.setText(user.telephone);
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_Email) {
            user = Global.getCurrentUser();
            tvEmail.setText(user.email);
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_Industry) {
            String curIndustrySt=data.getStringExtra("curIndustrySt");
            user.industry = curIndustrySt;
            Global.modifyAccount(user);
            user = Global.getCurrentUser();
            tvIndustry.setText(user.industry);
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_Motto) {
            user = Global.getCurrentUser();
            tvSign.setText(user.motto);
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_Position) {
            user = Global.getCurrentUser();
            tv_job.setText(user.position);
        }
        if(requestCode==REQUEST_CODE_Label){
            String labelName = data.getStringExtra("labelItem");
            tvLabel.setText(labelName);
            user.tag = labelName;
            Global.modifyAccount(user);
        }
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
                imgHeader.load(FileURLBuilder.getUserIconUrl(user.account), R.mipmap.lianxiren, 999);
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

    @Override
    public void onHttpRequestSucceed(ModifyPersonInfoResult result, OriginalCall call) {
        if (result.isSuccess()) {
            Global.modifyAccount(user);
            showToastView(R.string.tip_save_complete);
//            setResult(RESULT_OK, getIntent());
//            finish();
        }
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
    HttpRequestListener httpRequestListener=new HttpRequestListener() {
        @Override
        public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {

        }
        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {

        }
    };
}
