
package com.linkb.jstx.activity.setting;

import android.os.Build;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.network.http.HttpRequestLauncher;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.app.URLConstant;
import com.linkb.jstx.network.http.HttpRequestBody;
import com.linkb.BuildConfig;
import com.linkb.R;

public class FeedbackActivity extends BaseActivity {


    @Override
    public void initComponents() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String text = ((EditText) findViewById(R.id.text)).getText().toString();
        if (item.getItemId() == R.id.menu_send && !TextUtils.isEmpty(text)) {
            performFeedbackRequest(text);
            showToastView(R.string.tip_feedback_completed);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void performFeedbackRequest(String text) {

        showToastView(R.string.tip_feedback_completed);
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.FEEDBACK_PUBLISH_URL, BaseResult.class);
        requestBody.addParameter("appVersion", BuildConfig.VERSION_NAME);
        requestBody.addParameter("sdkVersion", Build.VERSION.RELEASE);
        requestBody.addParameter("deviceModel", Build.MODEL);
        requestBody.addParameter("content", text);
        HttpRequestLauncher.executeQuietly(requestBody);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_feedback;
    }


    @Override
    public int getToolbarTitle() {
        return R.string.common_feedback;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
