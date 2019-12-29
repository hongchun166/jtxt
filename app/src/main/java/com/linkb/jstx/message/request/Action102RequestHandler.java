
package com.linkb.jstx.message.request;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.message.builder.Action103Builder;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.app.URLConstant;
import com.linkb.jstx.database.GroupRepository;
import com.linkb.jstx.message.builder.Action102Builder;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.Group;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.MessageSource;
import com.linkb.jstx.model.SystemMessage;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.R;
import com.google.gson.Gson;

public class Action102RequestHandler extends RequestHandler implements HttpRequestListener {

    private Action102Builder builder;

    @Override
    public void initialized(BaseActivity context, Message msg) {
        super.initialized(context, msg);
        builder = new Gson().fromJson(message.content, Action102Builder.class);
    }

    @Override
    public CharSequence getMessage() {
        StringBuilder buffer = new StringBuilder();
        Group target = GroupRepository.queryById(builder.id);
        if (target ==  null){
            buffer.append(context.getString(R.string.tip_request_joingroup_default, builder.name ));
        }else {
            buffer.append(context.getString(R.string.tip_request_joingroup, builder.name, target.name));
        }

        SpannableString text = new SpannableString(buffer.toString());
        text.setSpan(new ForegroundColorSpan(Color.parseColor("#3C568B")), 0, builder.name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new StyleSpan(Typeface.BOLD), 0, builder.name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return text;
    }

    @Override
    public String getTitle() {
        return LvxinApplication.getInstance().getString(R.string.title_joingroup_request);
    }

    @Override
    public MessageSource decodeMessageSource() {
        Friend friend = new Friend();
        friend.account = builder.account;
        friend.name = builder.name;
        return friend;
    }

    @Override
    public void handleRefuse() {

        Message message = new Message();
        message.receiver = builder.account;
        message.content = context.getString(R.string.tip_refuse_joingroup, self.name, builder.groupName);
        message.action = Constant.MessageAction.ACTION_2;
        message.format = Constant.MessageFormat.FORMAT_TEXT;

        HttpServiceManager.sendOnly(message);

        MessageRepository.updateHandleState(this.message.id,SystemMessage.RESULT_REFUSE);
        context.showToastView(context.getString(R.string.tip_handle_succeed));
        context.finish();
    }


    @Override
    public void handleAgree() {
        performAgreeJoinRequest();
    }

    private void performAgreeJoinRequest() {
        context.showProgressDialog(context.getString(R.string.tip_loading, context.getString(R.string.common_handle)));

//        HttpServiceManager.addGroupMember(builder.id,builder.account,this);
        HttpServiceManager.agreeJoinGroup(builder.id,builder.account,this);
    }


    @Override
    public CharSequence getDescription() {
        return context.getString(R.string.tip_request_verify, builder.message);
    }

    @Override
    public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
        context.hideProgressDialog();
        if (result.isSuccess() && call.equals(URLConstant.GROUP_MEMBER_URL)) {
            MessageRepository.batchModifyAgree(builder.account, message.action);
            context.showToastView(context.getString(R.string.tip_handle_succeed));

            sendAgreeMessageQuitly();
            context.finish();
        }
        if (result.isSuccess() && call.equals(URLConstant.AGREE_JOIN_GROUP)) {
            MessageRepository.batchModifyAgree(builder.account, message.action);
            context.showToastView(context.getString(R.string.tip_handle_succeed));

            sendAgreeMessageQuitly();
            context.finish();
        }
    }


    private void sendAgreeMessageQuitly() {
        Message message = new Message();
        message.receiver = builder.account;
        message.sender = Constant.SYSTEM;
        message.action = Constant.MessageAction.ACTION_103;
        message.content = new Action103Builder().buildJsonString(self, GroupRepository.queryById(builder.id));
        message.format = Constant.MessageFormat.FORMAT_TEXT;

        HttpServiceManager.sendOnly(message);
    }


    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        context.hideProgressDialog();
    }


}
