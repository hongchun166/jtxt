package com.linkb.jstx.activity.contact;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.MineInviteCodeResult;
import com.linkb.jstx.util.BitmapUtils;
import com.linkb.jstx.util.ClipboardUtils;
import com.linkb.jstx.util.ConvertUtils;
import com.linkb.jstx.util.ZXingUtils;
import com.linkb.video.utils.ToastUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 二维码功能页面
 */
public class GroupQrCodeActivityV2 extends BaseActivity implements HttpRequestListener<MineInviteCodeResult> {

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

    private Bitmap mBitmap;
    private File file;

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);

        String qrcode = getIntent().getStringExtra("qrcode");

        mBitmap = ZXingUtils.createQRImage(qrcode,
                ConvertUtils.dp2px(150), ConvertUtils.dp2px(150));
        qrcodeImage.setImageBitmap(mBitmap);

        User user = Global.getCurrentUser();
        String text = String.format(getString(R.string.hint_say_hello), user.name);
        viewTVName.setText(text);

        viewInvitationCode.setText("NHJHHGG");
        HttpServiceManager.getMineInviteCode(this);

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
        showToastView(getResources().getString(R.string.label_copy_bitmap_success));
    }

    @OnClick(R.id.viewBtnSaveQrBitmap)
    public void downloadBitmapToLocal() {
        new fileFromBitmap(mBitmap, getApplicationContext()).execute();
    }

    @OnClick(R.id.viewInvitationInfo)
    public void gotoInvitationInfo() {
        Intent intent = new Intent(this, MineInvitationActivityV2.class);
        startActivity(intent);
    }

    @Override
    public void onHttpRequestSucceed(MineInviteCodeResult result, OriginalCall call) {
        if (result.isSuccess()) {
            viewInvitationCode.setText("NHJHHGG");
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
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File file = new File(path, "group_id.jpg");
            try {
                FileOutputStream fo = new FileOutputStream(file);
                fo.write(bytes.toByteArray());
                fo.flush();
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            showToastView(R.string.copy_group_qrcode_success_tips);
        }
    }
}
