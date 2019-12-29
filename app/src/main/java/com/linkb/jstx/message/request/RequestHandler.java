
package com.linkb.jstx.message.request;

import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.MessageSource;
import com.linkb.jstx.model.SystemMessage;
import com.linkb.R;

public abstract class RequestHandler {

    Message message;
    BaseActivity context;
    User self;

    public void initialized(BaseActivity context, Message msg) {
        this.message = msg;
        this.context = context;
        self = Global.getCurrentUser();
    }


    public abstract CharSequence getMessage();

    public abstract CharSequence getDescription();

    public abstract String getTitle();

    /**
     * 解析消息的来源对象
     *
     * @return
     */
    public abstract MessageSource decodeMessageSource();

    public abstract void handleRefuse();


    public void handleIgnore() {
        MessageRepository.updateHandleState(message.id,SystemMessage.RESULT_IGNORE);
        context.showToastView(context.getString(R.string.tip_handle_succeed));
        context.finish();
    }

    public abstract void handleAgree();

}
