package com.linkb.jstx.activity.contact;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.util.BitmapUtils;
import com.linkb.jstx.util.ClipboardUtils;
import com.linkb.jstx.util.ConvertUtils;
import com.linkb.jstx.util.ZXingUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**  二维码功能页面
* */
public class GroupQrCodeActivity extends BaseActivity {

    static class GroupQrCodeParam implements Serializable {
        public String qrcodeType;
        public String account;
        public String name;
    }

    @BindView(R.id.imageView2)
    ImageView qrcodeImage;

    private Bitmap mBitmap;
    private File file;

    public static void navToActFriend(Context context,String account,String name){
        GroupQrCodeParam param=new GroupQrCodeParam();
        param.qrcodeType= Constant.QrCodeFormater.PERSON_QR_CODE;
        param.account=account;
        param.name=name;
        Intent intent = new Intent(context, GroupQrCodeActivity.class);
        intent.putExtra("GroupQrCodeParam",param);
        context.startActivity(intent);
    }
    public static void navToActGroup(Context context,String gid){
        GroupQrCodeParam param=new GroupQrCodeParam();
        param.qrcodeType= Constant.QrCodeFormater.GROUP_QR_CODE;
        param.account=gid;
        Intent intent = new Intent(context, GroupQrCodeActivity.class);
        intent.putExtra("GroupQrCodeParam",param);
        context.startActivity(intent);
    }
    @Override
    protected void initComponents() {
        ButterKnife.bind(this);

        GroupQrCodeParam groupQrCodeParam = (GroupQrCodeParam) getIntent().getSerializableExtra("GroupQrCodeParam");
        String qrString=groupQrCodeParam.qrcodeType
                + Constant.QrCodeFormater.QR_CODE_SPLIT+groupQrCodeParam.account
                + Constant.QrCodeFormater.QR_CODE_SPLIT +groupQrCodeParam.name;

        mBitmap = ZXingUtils.createQRImage(qrString,
                ConvertUtils.dp2px(150), ConvertUtils.dp2px(150));
        qrcodeImage.setImageBitmap(mBitmap);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_group_qr_code;
    }

    @OnClick(R.id.back_btn)
    public void onBack(){
        finish();
    }

    @OnClick(R.id.button7)
    public void copyAddress(){
        Uri uri = BitmapUtils.savePhotoBitmap2Uri(mBitmap);
        ClipboardUtils.copyUri(uri);
        showToastView(getResources().getString(R.string.label_copy_bitmap_success));
    }

    @OnClick(R.id.button8)
    public void downloadBitmapToLocal(){
        new fileFromBitmap(mBitmap, getApplicationContext()).execute();
    }

    private class fileFromBitmap extends AsyncTask<Void, Integer, String> {

        Context context;
        Bitmap bitmap;

        public fileFromBitmap(Bitmap bitmap, Context context) {
            this.bitmap = bitmap;
            this.context= context;
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
