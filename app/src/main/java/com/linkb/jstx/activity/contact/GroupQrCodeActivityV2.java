package com.linkb.jstx.activity.contact;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.MineInviteCodeResult;
import com.linkb.jstx.util.ClipboardUtils;
import com.linkb.jstx.util.ConvertUtils;
import com.linkb.jstx.util.ZXingUtils;
import com.linkb.video.utils.ToastUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 二维码功能页面
 */
public class GroupQrCodeActivityV2 extends BaseActivity implements HttpRequestListener<MineInviteCodeResult> {

    static class GroupQrCodeParam implements Serializable {
        public String qrcodeType;
        public String account;
        public String name;
        public String downloadUrl;
    }
    @BindView(R.id.viewQrImg)
    ImageView qrcodeImage;
    @BindView(R.id.viewTVName)
    TextView viewTVName;
    @BindView(R.id.viewInvitationCode)
    TextView viewInvitationCode;
    @BindView(R.id.viewCopyInvitationCode)
    TextView viewCopyInvitationCode;
    @BindView(R.id.viewBtnSaveQrBitmap)
    Button viewBtnSaveQrBitmap;
    @BindView(R.id.viewInvitationInfo)
    TextView viewInvitationInfo;
    @BindView(R.id.viewScrollView)
    ScrollView viewScrollView;

    private Bitmap mBitmap;

    GroupQrCodeParam groupQrCodeParam;

    public static void navToActInvitationDownload(Context context,String account,String name){
        GroupQrCodeParam param=new GroupQrCodeParam();
        param.qrcodeType=Constant.QrCodeFormater.Invitation_TO_Download_QR_CODE;
        param.account=account;
        param.name=name;

        Intent intent = new Intent(context, GroupQrCodeActivityV2.class);
        intent.putExtra("GroupQrCodeParam",param);
        context.startActivity(intent);
    }

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);

        Bundle bundle=getIntent().getExtras();
        groupQrCodeParam= (GroupQrCodeParam) bundle.getSerializable("GroupQrCodeParam");

        updateQRCodeBitmap();
        User user = Global.getCurrentUser();
        String text = String.format(getString(R.string.hint_say_hello), user.name);
        viewTVName.setText(text);

        viewInvitationCode.setText("");
        HttpServiceManager.getMineInviteCode(this);

    }

    private void updateQRCodeBitmap(){
        String qrString=
//                groupQrCodeParam.qrcodeType
//                + Constant.QrCodeFormater.QR_CODE_SPLIT+groupQrCodeParam.account
//                + Constant.QrCodeFormater.QR_CODE_SPLIT +groupQrCodeParam.name
//                + Constant.QrCodeFormater.QR_CODE_SPLIT
//                +
                    String.valueOf(groupQrCodeParam.downloadUrl);

        Bitmap log= BitmapFactory.decodeResource(getResources(),R.mipmap.ic_log_kiki,null);

        int size=ConvertUtils.dp2px(150);
        mBitmap = ZXingUtils.createQRCodeBitmap(qrString, size, size,"UTF-8",
                "H", "1", getResources().getColor(R.color.tex_color_gray_191919),
                Color.WHITE, log,0.2F);
//        mBitmap = ZXingUtils.createQRImage(qrcode,
//                ConvertUtils.dp2px(150), ConvertUtils.dp2px(150));
        qrcodeImage.setImageBitmap(mBitmap);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_group_qr_code_v2;
    }

    @OnClick(R.id.back_btn)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.viewCopyInvitationCode)
    public void copyInvite() {
        String inviteCode = viewInvitationCode.getText().toString();
        ClipboardUtils.copy(this, inviteCode);
        showToastView(getResources().getString(R.string.label_copy_success));
    }

    @OnClick(R.id.viewBtnSaveQrBitmap)
    public void downloadBitmapToLocal() {
        Bitmap bitmap = Bitmap.createBitmap(viewScrollView.getWidth(), viewScrollView.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        viewScrollView.draw(canvas);
//        new fileFromBitmap(mBitmap, getApplicationContext()).execute();
        new fileFromBitmap(bitmap, getApplicationContext()).execute();
    }

    @OnClick(R.id.viewInvitationInfo)
    public void gotoInvitationInfo() {
        Intent intent = new Intent(this, MineInvitationActivityV2.class);
        startActivity(intent);
    }

    @Override
    public void onHttpRequestSucceed(MineInviteCodeResult result, OriginalCall call) {
        if (result !=null && result.isSuccess() && result.getData()!=null) {
            String code=result.getData().getInviteCode();
            String downloadUrl=result.getData().getDownloadUrl();
            if(TextUtils.isEmpty(code)){
                viewInvitationCode.setText("");
            }else {
                viewInvitationCode.setText(code);
            }
            groupQrCodeParam.downloadUrl=downloadUrl;
            updateQRCodeBitmap();
        } else {
            ToastUtils.s(this, result.message);
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        ToastUtils.s(this, "获取邀请码失败");
    }


    private class fileFromBitmap extends AsyncTask<Void, Integer, String> {

        Context context;
        Bitmap bitmap;

        public fileFromBitmap(Bitmap bitmap, Context context) {
            this.bitmap = bitmap;
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);//DIRECTORY_PICTURES
            System.out.println("saveQRCodePath:"+path);
            File file = new File(path, "group_id.jpg");
            try {
                FileOutputStream fo = new FileOutputStream(file);
                fo.write(bytes.toByteArray());
                fo.flush();
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return path.getAbsolutePath();
        }


        @Override
        protected void onPostExecute(String pathStr) {
            super.onPostExecute(pathStr);
            showToastView(R.string.copy_group_qrcode_success_tips);
            Uri contentUri = Uri.fromFile(new File(pathStr));
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,contentUri);
            sendBroadcast(mediaScanIntent);
        }
    }
}
