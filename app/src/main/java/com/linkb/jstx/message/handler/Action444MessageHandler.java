
package com.linkb.jstx.message.handler;

import android.content.Context;
import android.content.Intent;

import com.linkb.jstx.activity.ForceOfflineAlertActivity;
import com.linkb.jstx.database.MessageRepository;
import com.farsunset.cim.sdk.android.model.Message;
import com.linkb.jstx.app.Global;
import com.linkb.R;

public class Action444MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {

        Global.removePassword();

        Intent intent = new Intent(context, ForceOfflineAlertActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("title", context.getString(R.string.common_notice));
        intent.putExtra("message", context.getString(R.string.tip_user_disabled_offline));
        context.startActivity(intent);
        MessageRepository.deleteById(message.getId());

        return false;
    }

}
