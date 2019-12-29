
package com.linkb.jstx.message.handler;

import android.content.Context;
import android.content.Intent;

import com.farsunset.cim.sdk.android.model.Message;
import com.linkb.jstx.activity.ForceOfflineAlertActivity;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.R;
import com.linkb.jstx.util.AppTools;

public class Action999MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {

        Global.removePassword();

        String deviceModel = message.getContent();
        String loginTime = AppTools.getDateTimeString(message.getTimestamp());
        Intent intent = new Intent(context, ForceOfflineAlertActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.putExtra("title", context.getString(R.string.title_login_remind));
        intent.putExtra("message", context.getString(R.string.tip_force_offline, loginTime, deviceModel));
        context.startActivity(intent);
        MessageRepository.deleteById(message.getId());
        return false;
    }

}
