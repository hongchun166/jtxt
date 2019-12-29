
package com.linkb.jstx.message.request;

import android.content.Intent;
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
import com.linkb.jstx.message.builder.Action105Builder;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.MessageSource;
import com.linkb.jstx.model.SystemMessage;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.GroupMemberListResult;
import com.linkb.jstx.database.GroupMemberRepository;
import com.linkb.jstx.database.GroupRepository;
import com.linkb.jstx.model.Group;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.R;
import com.google.gson.Gson;


public class Action105RequestHandler extends RequestHandler implements HttpRequestListener {

    private Action105Builder builder;

    @Override
    public void initialized(BaseActivity context, Message msg) {
        super.initialized(context, msg);
        builder = new Gson().fromJson(message.content, Action105Builder.class);
    }

    @Override
    public CharSequence getMessage() {

        SpannableString text = new SpannableString(context.getString(R.string.tip_request_invitegroup, builder.name, builder.groupName));
        text.setSpan(new ForegroundColorSpan(Color.parseColor("#3C568B")), 0, builder.name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new StyleSpan(Typeface.BOLD), 0, builder.name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return text;
    }

    @Override
    public String getTitle() {
        return context.getString(R.string.tip_title_groupmessage);
    }

    @Override
    public MessageSource decodeMessageSource() {
        return decodeMessageBody();
    }

    @Override
    public void handleRefuse() {

        Message msg = new Message();
        msg.receiver = builder.account;
        msg.content = context.getString(R.string.tip_refuse_invitegroup, self.name, builder.groupName);
        msg.action = Constant.MessageAction.ACTION_2;
        msg.format = Constant.MessageFormat.FORMAT_TEXT;

        HttpServiceManager.sendOnly(msg);

        MessageRepository.updateHandleState(this.message.id, SystemMessage.RESULT_REFUSE);

        context.showToastView(context.getString(R.string.tip_handle_succeed));
        context.finish();
    }


    private void performAgreeJoinRequest() {
        context.showProgressDialog(context.getString(R.string.tip_loading, context.getString(R.string.common_handle)));
        HttpServiceManager.addGroupMember( builder.id,self.account,this);

    }

    @Override
    public void handleAgree() {
        performAgreeJoinRequest();
    }

    @Override
    public CharSequence getDescription() {
        return builder.summary;
    }

    private void onHttpRequestSucceed(GroupMemberListResult result) {
        if (result.isSuccess()) {
            //同意进群申请，刷新群组列表
            Intent joinIntent = new Intent(Constant.Action.ACTION_GROUP_REFRESH);
            LvxinApplication.sendLocalBroadcast(joinIntent);

            GroupMemberRepository.saveAll(result.dataList);
            context.hideProgressDialog();
            context.finish();
        }
    }


    @Override
    public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {

        if (result instanceof GroupMemberListResult){
            onHttpRequestSucceed((GroupMemberListResult) result);
            return;
        }
        if (result.isSuccess()) {
            MessageRepository.batchModifyAgree(builder.account, message.action);
            context.showToastView(context.getString(R.string.tip_handle_succeed));
            GroupRepository.add(decodeMessageBody());
            HttpServiceManager.queryGroupMember(builder.id,this);

            sendAgreeMessageQuitly();
        }
        if (result.code.equals(Constant.ReturnCode.CODE_404)) {
            context.hideProgressDialog();
            context.showToastView(R.string.tip_group_has_dissolved);
            handleIgnore();
        }
    }

    public void handleIgnore() {
        MessageRepository.updateHandleState(this.message.id,SystemMessage.RESULT_IGNORE);
        context.finish();
    }


    /**
     * 给邀请者发送同意通知
     */
    private void sendAgreeMessageQuitly() {
        Message message = new Message();
        message.receiver = builder.account;
        message.sender = Constant.SYSTEM;
        message.action = Constant.MessageAction.ACTION_106;
        message.extra = String.valueOf(builder.id);
        message.content = self.account;
        message.format = Constant.MessageFormat.FORMAT_TEXT;
        HttpServiceManager.sendOnly(message);
    }



    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        context.hideProgressDialog();
    }

    private Group decodeMessageBody() {
        Group group = new Group();
        group.id = builder.id;
        group.name = builder.groupName;
        group.founder = builder.founder;
        group.summary = builder.summary;
        group.category = builder.category;
        return group;
    }
}
