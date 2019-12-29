package com.linkb.jstx.activity.contact;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.activity.util.PhotoAlbumActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.FileResource;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.database.GroupRepository;
import com.linkb.jstx.dialog.CustomProgressDialog;
import com.linkb.jstx.listener.OSSFileUploadListener;
import com.linkb.jstx.model.Group;
import com.linkb.jstx.network.CloudFileUploader;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.util.AppTools;
import com.linkb.jstx.util.FileURLBuilder;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** 编辑群资料功能
* */
public class GroupInfoEditActivity extends BaseActivity implements OSSFileUploadListener {

    @BindView(R.id.imageView40)
    WebImageView groupAvatarImg;

    @BindView(R.id.textView141)
    EditText mGroupNameEdit;

    @BindView(R.id.textView143)
    EditText mGroupProfileEdit;

    @BindView(R.id.button10)
    Button confirmBtn;

    private CustomProgressDialog progressDialog;

    /** 头像File
    * */
    private File logoFile;
    private Group mGroup ;
    /** 是否修改过头像
    * */
    private boolean enableModifyAvatar = false;
    /** 是否修改过群名称
     * */
    private boolean enableModifyGroupName = false;
    /** 是否修改过群简介
     * */
    private boolean enableModifyGroupProfile = false;

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);

        mGroup = (Group) this.getIntent().getSerializableExtra("group");
        mGroupNameEdit.setText(mGroup.name);
        mGroupProfileEdit.setText(mGroup.category);
        groupAvatarImg.load(FileURLBuilder.getGroupIconUrl(mGroup.id), R.drawable.logo_group_normal, 999);

        progressDialog = new CustomProgressDialog(this, R.style.CustomDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.tip_loading, getString(R.string.label_group_profile_upload_avatar)));

        mGroupNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!enableModifyAvatar && !enableModifyGroupProfile && charSequence.toString().equals(mGroup.name)){
                    confirmBtn.setEnabled(false);
                    enableModifyGroupProfile = false;
                }else if (charSequence.equals(mGroup.name)){
                    enableModifyGroupName = false;

                }else if(!charSequence.equals(mGroup.name)){
                    enableModifyGroupName = true;
                    confirmBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mGroupProfileEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!enableModifyAvatar && !enableModifyGroupName && charSequence.toString().equals(mGroup.category)){
                    confirmBtn.setEnabled(false);
                    enableModifyGroupProfile = false;
                }else if (charSequence.equals(mGroup.name)){
                    enableModifyGroupProfile = false;
                }else if(!charSequence.equals(mGroup.name)){
                    enableModifyGroupProfile = true;
                    confirmBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_edit_group_info;
    }

    @OnClick(R.id.button10)
    public void onConfirmBtn(){
        if (enableModifyAvatar){
            performUploadLogo();
        }
        if (enableModifyGroupName){
            mGroup.name = mGroupNameEdit.getText().toString().trim();
            GroupRepository.update(mGroup);
            HttpServiceManager.setGroupName(mGroup.id,mGroup.name);
        }

        if (enableModifyGroupProfile){
            mGroup.category = mGroupProfileEdit.getText().toString().trim();
            GroupRepository.update(mGroup);
            HttpServiceManager.setGroupProfile(mGroup.id,mGroup.category);
        }

        finish();
    }

    @OnClick(R.id.imageView40)
    public void onUploadAvatar(){
        Intent intentFromGallery = new Intent();
        intentFromGallery.setClass(this, PhotoAlbumActivity.class);
        startActivityForResult(intentFromGallery, 9);
    }

    @OnClick(R.id.back_btn)
    public void onBackBtn(){
        finish();
    }

    /** 上传头像
    * */
    private void performUploadLogo() {
        if (logoFile != null) {
            progressDialog.setMessage(getString(R.string.tip_file_uploading, 0));
            CloudFileUploader.asyncUpload(FileURLBuilder.BUCKET_GROUP_ICON, String.valueOf(mGroup.id), logoFile,this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 9) {

            AppTools.startIconPhotoCrop(this, data.getData());
        }
        if (resultCode == Activity.RESULT_OK && requestCode == Constant.RESULT_ZOOM && data != null) {
            logoFile = new File(Global.getCropPhotoFilePath());
            groupAvatarImg.load(logoFile,0,999);

            enableModifyAvatar = true;
            confirmBtn.setEnabled(true);
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
    public void finish() {
        if (enableModifyAvatar || enableModifyGroupName || enableModifyGroupProfile){
            Intent intent = new Intent();
            intent.putExtra(Group.class.getName(),mGroup);
            setResult(RESULT_OK,intent);
        }

        super.finish();
    }
}
