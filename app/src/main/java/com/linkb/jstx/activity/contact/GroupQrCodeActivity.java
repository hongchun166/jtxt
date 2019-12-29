package com.linkb.jstx.activity.contact;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.util.BitmapUtils;
import com.linkb.jstx.util.ClipboardUtils;
import com.linkb.jstx.util.ConvertUtils;
import com.linkb.jstx.util.ZXingUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**  二维码功能页面
* */
public class GroupQrCodeActivity extends BaseActivity {

    @BindView(R.id.imageView2)
    ImageView qrcodeImage;

    private Bitmap mBitmap;
    private File file;

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);

        String qrcode = getIntent().getStringExtra("qrcode");

        mBitmap = ZXingUtils.createQRImage(qrcode,
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
