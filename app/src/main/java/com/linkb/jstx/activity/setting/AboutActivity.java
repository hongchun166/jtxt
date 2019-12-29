
package com.linkb.jstx.activity.setting;

import android.widget.TextView;

import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.BuildConfig;
import com.linkb.R;

public class AboutActivity extends BaseActivity {


    @Override
    public void initComponents() {

        ((TextView) findViewById(R.id.version)).setText(BuildConfig.VERSION_NAME);
        ((TextView) findViewById(R.id.summary)).setText(R.string.about_text);

        findViewById(R.id.item_name).setOnClickListener(this);
        findViewById(R.id.item_qq).setOnClickListener(this);
        findViewById(R.id.item_weixin).setOnClickListener(this);
    }


    @Override
    public int getContentLayout() {
        return R.layout.activity_about;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.common_about;
    }


}
