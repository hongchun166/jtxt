
package com.linkb.jstx.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.res.ComplexColorCompat;

import com.farsunset.cim.sdk.android.CIMEventBroadcastReceiver;
import com.farsunset.cim.sdk.android.CIMListenerManager;
import com.farsunset.cim.sdk.android.CIMPushManager;
import com.farsunset.cim.sdk.android.constant.CIMConstant;
import com.farsunset.cim.sdk.android.model.ReplyBody;
import com.linkb.BuildConfig;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.event.FriendRelationChnageEB;
import com.linkb.jstx.event.ReceiveFrienApplyEB;
import com.linkb.jstx.message.handler.CustomMessageHandlerFactory;
import com.linkb.jstx.message.parser.MessageParserFactory;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.MessageSource;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.service.MessageNotifyService;
import com.linkb.jstx.util.MLog;
import com.linkb.jstx.util.MessageUtil;
import com.linkb.video.RoomActivity;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


/**
 * 消息入口，所有消息都会经过这里
 */
public final class CIMPushMessageReceiver extends CIMEventBroadcastReceiver {

    private static final String TAG = CIMPushMessageReceiver.class.getSimpleName();

    private static final HashSet<String> INCLUDED_MESSAGE_TYPES = new HashSet<>();

    /**
     *
     * action IntentFilter中的某一个action，因为获取到的是IntentFilter的所有action，所以只要匹配一个就可以
     *
     */
    private static boolean isRegister(LocalBroadcastManager manager, String action) {
        boolean isRegister = false;
        try {
            Field mReceiversField = manager.getClass().getDeclaredField("mReceivers");
            mReceiversField.setAccessible(true);
//            String name = mReceiversField.getName();
            HashMap<BroadcastReceiver, ArrayList<IntentFilter>> mReceivers = (HashMap<BroadcastReceiver, ArrayList<IntentFilter>>) mReceiversField.get(manager);

            for (BroadcastReceiver key : mReceivers.keySet()) {
                ArrayList<IntentFilter> intentFilters = mReceivers.get(key);
//                MyLogUtil.e("Key: " + key + " Value: " + intentFilters);
                for (int i = 0; i < intentFilters.size(); i++) {
                    IntentFilter intentFilter = intentFilters.get(i);
                    Field mActionsField = intentFilter.getClass().getDeclaredField("mActions");
                    mActionsField.setAccessible(true);
                    ArrayList<String> mActions = (ArrayList<String>) mActionsField.get(intentFilter);
                    for (int j = 0; j < mActions.size(); j++) {
                        if (mActions.get(i).equals(action)) {
                            isRegister = true;
                            break;
                        }
                    }
                }
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return isRegister;
    }
    public static void regCimReceiverBySDk(Context context){
        if(true){
            return;
        }
        if(Build.VERSION.SDK_INT >= 26 && !isRegister(LocalBroadcastManager.getInstance(context),"com.farsunset.lvxin.MESSAGE_RECEIVED")){
            CIMPushMessageReceiver receiver=new CIMPushMessageReceiver();
            IntentFilter intentFilter=new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");//网络变化广播
            intentFilter.addAction("com.farsunset.lvxin.MESSAGE_RECEIVED");//消息广播action
            intentFilter.addAction("com.farsunset.lvxin.SENT_FAILED");//发送sendbody失败广播
            intentFilter.addAction("com.farsunset.lvxin.SENT_SUCCESSED");//发送sendbody成功广播
            intentFilter.addAction("com.farsunset.lvxin.CONNECTION_RECOVERY");//重新连接
            intentFilter.addAction("com.farsunset.lvxin.CONNECTION_CLOSED");//链接意外关闭广播
            intentFilter.addAction("com.farsunset.lvxin.CONNECTION_FAILED");//链接失败广播
            intentFilter.addAction("com.farsunset.lvxin.CONNECTION_SUCCESSED");//链接成功广播
            intentFilter.addAction("com.farsunset.lvxin.REPLY_RECEIVED");//发送sendbody成功后获得replaybody回应广播

            //【可选】 一些常用的系统广播，增强pushservice的复活机会
            intentFilter.addAction("android.intent.action.USER_PRESENT");
            intentFilter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
            intentFilter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
//            context.registerReceiver(receiver,intentFilter);
            LocalBroadcastManager.getInstance(context).registerReceiver(receiver,intentFilter);
        }
    }

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
        if (msg.action.equals(Constant.MessageAction.ACTION_FrienApply) ||msg.action.equals(Constant.MessageAction.ACTION_117)){

            FriendRepository.updateFriendRelationNewApply(true);

            ReceiveFrienApplyEB receiveFrienApplyEB=new ReceiveFrienApplyEB();
            EventBus.getDefault().post(receiveFrienApplyEB);
            return;
        }
        if(msg.action.equals(Constant.MessageAction.ACTION_118) ||msg.action.equals(Constant.MessageAction. ACTION_119)){
            FriendRelationChnageEB friendRelationChnageEB=new FriendRelationChnageEB();
            EventBus.getDefault().post(friendRelationChnageEB);
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

    //3.8
    @Override
    public void onConnectFinished(boolean hasAutoBind) {
        super.onConnectFinished(hasAutoBind);
        if (!hasAutoBind) {
            // 绑定账号到服务端
            CIMPushManager.bindAccount(LvxinApplication.getInstance(), Global.getCurrentAccount());
        }
    }
//  3.5
//    @Override
//    public void onConnectionSuccessed(boolean hasAutoBind) {
//        super.onConnectionSuccessed(hasAutoBind);
//        if (!hasAutoBind) {
//            // 绑定账号到服务端
//            CIMPushManager.bindAccount(LvxinApplication.getInstance(), Global.getCurrentAccount());
//        }
//    }

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
