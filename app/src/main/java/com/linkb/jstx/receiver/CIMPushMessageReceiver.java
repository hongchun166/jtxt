
package com.linkb.jstx.receiver;

import android.content.Intent;

import com.farsunset.cim.sdk.android.CIMEventBroadcastReceiver;
import com.farsunset.cim.sdk.android.CIMListenerManager;
import com.farsunset.cim.sdk.android.CIMPushManager;
import com.farsunset.cim.sdk.android.constant.CIMConstant;
import com.farsunset.cim.sdk.android.model.ReplyBody;
import com.linkb.BuildConfig;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.message.handler.CustomMessageHandlerFactory;
import com.linkb.jstx.message.parser.MessageParserFactory;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.MessageSource;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.service.MessageNotifyService;
import com.linkb.jstx.util.MLog;
import com.linkb.jstx.util.MessageUtil;
import com.linkb.video.RoomActivity;

import java.util.HashSet;


/**
 * 消息入口，所有消息都会经过这里
 */
public final class CIMPushMessageReceiver extends CIMEventBroadcastReceiver {

    private static final String TAG = CIMPushMessageReceiver.class.getSimpleName();

    private static final HashSet<String> INCLUDED_MESSAGE_TYPES = new HashSet<>();

    static {
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_102);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_103);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_104);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_105);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_106);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_107);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_112);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_113);
    }

    private final static String[] MESSAGE_FLUTTER_ACTION = new String[]{Constant.MessageAction.ACTION_102, Constant.MessageAction.ACTION_103, Constant.MessageAction.ACTION_104, Constant.MessageAction.ACTION_105,
            Constant.MessageAction.ACTION_106, Constant.MessageAction.ACTION_107, Constant.MessageAction.ACTION_112, Constant.MessageAction.ACTION_113
    };

    /**
     * 当收到消息时调用此方法
     */
    @Override
    public void onMessageReceived(com.farsunset.cim.sdk.android.model.Message message, Intent intent) {


        MLog.i(TAG, message.toString());


        Message msg = MessageUtil.transform(message);
        if (msg.action.equals(Constant.MessageAction.ACTION_4)){
            if (!receivedVoiceConnectMsg(msg)){
                msg.action = "0";
                msg.content = "语音通话已取消";
                message.setAction("0");
                message.setContent("语音通话已取消");
            }else {
                return;
            }
        }
        if (msg.action.equals(Constant.MessageAction.ACTION_5)){
            if (!receivedVideoConnectMsg(msg)){
                msg.action = "0";
                msg.content = "视频通话已取消";
                message.setAction("0");
                message.setContent("视频通话已取消");
            }else {
                return;
            }
        }

        if (msg.action.equals(Constant.MessageAction.ACTION_6)){
            receiveRejectVideoMsg(msg);
            return;
        }

        if (msg.action.equals(Constant.MessageAction.ACTION_7)){
            receiveCancelVideoMsg(msg);
            return;
        }

        MessageSource source = MessageParserFactory.getFactory().parserMessageSource(msg);
        if (source == null && (msg.action.equals(Constant.MessageAction.ACTION_112) || msg.action.equals(Constant.MessageAction.ACTION_113))) return;

        if (intent.getBooleanExtra(Constant.NEED_RECEIPT, true)) {
            //第一件事情做消息状态回执,将消息状态设为已接收状态
            HttpServiceManager.receipt(msg);
        }

        boolean isNeedDispatch = CustomMessageHandlerFactory.getFactory().handle(super.context, message);
        if (INCLUDED_MESSAGE_TYPES.contains(msg.action)){
            if (MessageRepository.queryMessage(msg, INCLUDED_MESSAGE_TYPES.toArray()).size() > 0) return;
        }
        if (!isNeedDispatch) {
            return;
        }

        MessageRepository.add(msg);
        CIMListenerManager.notifyOnMessageReceived(message);

        if (BuildConfig.DEBUG) {
            CIMListenerManager.logListenersName();
        }

        //显示通知栏消息
        if (Global.getAppInBackground()) {
            Intent notifyIntent = new Intent(context, MessageNotifyService.class);
            notifyIntent.putExtra(Message.NAME, msg);
            context.startService(notifyIntent);
        }
    }

    /** 收到实时音频连接消息
    * */
    private boolean receivedVoiceConnectMsg(Message msg){
        // 判断消息有没有失效(60s时效)
        long currentTime = System.currentTimeMillis();
        if ((currentTime - msg.timestamp) > RoomActivity.CONNECT_TIME_OUT * 1000) {
            return false;
        }

        Intent intent = new Intent(context, RoomActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        intent.putExtra(RoomActivity.EXTRA_ROOM_ID, msg.extra);
        intent.putExtra(RoomActivity.EXTRA_ROOM_TOKEN, msg.content);
        intent.putExtra(RoomActivity.EXTRA_USER_ID, msg.receiver);
        intent.putExtra(RoomActivity.EXTRA_ISSENDER, false);
        intent.putExtra(RoomActivity.EXTRA_OPPOSITE_USER_ID, msg.sender);
        intent.putExtra(RoomActivity.EXTRA_IS_VIDEO, false);
        context.startActivity(intent);
        return true;
    }



    /** 收到实时视频连接消息
     * */
    private boolean receivedVideoConnectMsg(Message msg){
        // 判断消息有没有失效(60s时效)
        long currentTime = System.currentTimeMillis();
        if ((currentTime - msg.timestamp) > RoomActivity.CONNECT_TIME_OUT * 1000) {
            return false;
        }

        Intent intent = new Intent(context, RoomActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        intent.putExtra(RoomActivity.EXTRA_ROOM_ID, msg.extra);
        intent.putExtra(RoomActivity.EXTRA_ROOM_TOKEN, msg.content);
        intent.putExtra(RoomActivity.EXTRA_USER_ID, msg.receiver);
        intent.putExtra(RoomActivity.EXTRA_ISSENDER, false);
        intent.putExtra(RoomActivity.EXTRA_OPPOSITE_USER_ID, msg.sender);
        intent.putExtra(RoomActivity.EXTRA_IS_VIDEO, true);
        context.startActivity(intent);
        return true;
    }

    /** 收到被拒绝实时音视频的消息
    * */
    private void receiveRejectVideoMsg(Message msg){
        // 判断消息有没有失效(60s时效)

        Intent intent = new Intent(Constant.Action.ACTION_OPPOSITE_REJECT_VIDEO_CONNECT);
        intent.putExtra(Message.class.getName(), msg);
        LvxinApplication.sendLocalBroadcast(intent);
    }

    /** 收到对方取消音视频的消息
    * */
    private void receiveCancelVideoMsg(Message msg){
        Intent intent = new Intent(Constant.Action.ACTION_OPPOSITE_CANCEL_VIDEO_CONNECT);
        intent.putExtra(Message.class.getName(), msg);
        LvxinApplication.sendLocalBroadcast(intent);
    }

    @Override
    public void onConnectionSuccessed(boolean hasAutoBind) {
        super.onConnectionSuccessed(hasAutoBind);
        if (!hasAutoBind) {
            // 绑定账号到服务端
            CIMPushManager.bindAccount(LvxinApplication.getInstance(), Global.getCurrentAccount());
        }
    }

    @Override
    public void onReplyReceived(ReplyBody reply) {
        super.onReplyReceived(reply);
        // 当账号绑定到服务端成功，拉取离线消息
        if (reply.getKey().equals(CIMConstant.RequestKey.CLIENT_BIND) && reply.getCode().equals(CIMConstant.ReturnCode.CODE_200)) {
            getOfflineMessage();
        }
        if (reply.getKey().equals(CIMConstant.RequestKey.CLIENT_BIND) && reply.getCode().equals(CIMConstant.ReturnCode.CODE_500)) {
            MLog.e(TAG, reply.getMessage());
        }
    }

    /**
     * 登录成功后，拉取离线消息
     */
    private void getOfflineMessage() {
        /**
         * 第一次，通讯录数据还没有获取到的时候，等待通讯录获取成功再获取离线消息
         */
//        if (FriendRepository.count() == 0)
//        {
//            return;
//        }
        HttpServiceManager.queryOfflineMessage();
    }
}
