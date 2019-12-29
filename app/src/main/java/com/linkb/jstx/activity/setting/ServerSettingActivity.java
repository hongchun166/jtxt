
package com.linkb.jstx.activity.setting;


import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.ClientConfig;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.database.GlobalDatabaseHelper;
import com.linkb.jstx.dialog.CustomDialog;
import com.linkb.jstx.listener.OnDialogButtonClickListener;
import com.linkb.jstx.util.StringUtils;
import com.farsunset.cim.sdk.android.CIMPushManager;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.URLConstant;
import com.linkb.R;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * 消息配置
 */
public class ServerSettingActivity extends BaseActivity implements OnDialogButtonClickListener {

    private EditText port;
    private EditText path;
    private CustomDialog customDialog;

    @Override
    public void initComponents() {

        path = findViewById(R.id.path);
        path.setText(ClientConfig.getServerPath());
        path.requestFocus();
        port = findViewById(R.id.port);
        port.setText(String.valueOf(ClientConfig.getServerCIMPort()));
        customDialog = new CustomDialog(this);
        customDialog.setOnDialogButtonClickListener(this);
        customDialog.setIcon((R.drawable.icon_dialog_hint));
        customDialog.setMessage((R.string.tip_save_server_config));

    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_server_setting;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.label_setting_server;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_save) {
            do {

                if (TextUtils.isEmpty(path.getText().toString().trim())) {
                    showToastView(R.string.tip_required_server_path);
                    break;
                }
                if (!StringUtils.isWebUrl(path.getText().toString().trim())) {
                    showToastView(R.string.tip_invalid_server_path);
                    break;
                }

                if (TextUtils.isEmpty(port.getText().toString().trim())) {
                    showToastView(R.string.tip_required_server_cimport);
                    break;
                }
                customDialog.show();
            } while (false);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLeftButtonClicked() {
        customDialog.dismiss();
    }

    private void restartApp() {
        URLConstant.initialize();
        Global.removeAccount(new AccountManagerCallback() {
            @Override
            public void run(AccountManagerFuture accountManagerFuture) {
                CIMPushManager.destroy(LvxinApplication.getInstance());
                GlobalDatabaseHelper.onServerChanged();
                LvxinApplication.getInstance().restartSelf();
            }
        });
    }

    @Override
    public void onRightButtonClicked() {
        customDialog.dismiss();

        try {
            URL url = new URL(path.getText().toString().trim());
            ClientConfig.setServerHost(url.getHost());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ClientConfig.setServerPath(path.getText().toString().trim());
        ClientConfig.setServerCIMPort(Integer.parseInt(port.getText().toString().trim()));
        restartApp();
    }

}
