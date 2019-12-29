
package com.linkb.jstx.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkb.jstx.util.AppTools;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.R;

public class ForceOfflineAlertActivity extends AppCompatActivity implements OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_dialog);
        findViewById(R.id.button).setOnClickListener(this);
        ((ImageView) findViewById(R.id.icon)).setImageResource(R.drawable.icon_dialog_waring);
        ((TextView) findViewById(R.id.message)).setText(getIntent().getStringExtra("message"));
        cancelNotification();
    }
    @Override
    public void onBackPressed() {
    }

    private void cancelNotification() {
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
    }

    @Override
    public void onClick(View v) {
        this.finish();
        LvxinApplication.getInstance().restartSelf();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && AppTools.isOutOfBounds(this, event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }


}
