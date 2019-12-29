
package com.linkb.jstx.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.util.AppTools;
import com.linkb.jstx.bean.AppVersion;
import com.linkb.R;
import com.linkb.jstx.util.TimeUtils;

import java.io.File;

public class AppNewVersionActivity extends AppCompatActivity implements OnClickListener {

    private AppVersion appVersion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_new_version);

        appVersion = (AppVersion) getIntent().getSerializableExtra(AppVersion.class.getName());

        findViewById(R.id.leftButton).setOnClickListener(this);
        findViewById(R.id.rightButton).setOnClickListener(this);

        if (appVersion.force) {
            findViewById(R.id.leftButton).setVisibility(View.GONE);
        }

        String subtitle = getString(R.string.label_new_version_format, TextUtils.htmlEncode(appVersion.versionName));
        ((TextView) findViewById(R.id.subtitle)).setText(Html.fromHtml(subtitle));

        ((TextView) findViewById(R.id.message)).setText(TextUtils.htmlEncode(appVersion.description));
    }



    @Override
    public void onBackPressed() {
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.leftButton) {
            finish();
            return;
        }
        if (v.getId() == R.id.rightButton) {
            AppTools.showToastView(this,R.string.title_apk_begin_download_toast);
            File file = new File(LvxinApplication.DOWNLOAD_DIR, LvxinApplication.getInstance().getPackageName() + "-" + appVersion.versionName + TimeUtils.getCurrentTimeStr() + "-" + ".apk");
            LvxinApplication.getInstance().startDownloadService(appVersion.url, file);

            if (!appVersion.force){
                finish();
            }
        }
    }





    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && AppTools.isOutOfBounds(this, event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }


}
