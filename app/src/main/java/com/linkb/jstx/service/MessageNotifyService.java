
package com.linkb.jstx.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.linkb.jstx.activity.HomeActivity;
import com.linkb.jstx.app.ClientConfig;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.listener.CloudImageLoadListener;
import com.linkb.jstx.message.parser.MessageParserFactory;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.MessageSource;
import com.linkb.jstx.network.CloudImageLoaderFactory;
import com.linkb.jstx.model.Group;
import com.linkb.R;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BasePersonInfoResult;

public class MessageNotifyService extends Service {

    private NotificationManager notificationMgr;
    private Message message;

    private HttpRequestListener<BasePersonInfoResult> listener = new HttpRequestListener<BasePersonInfoResult>() {
        @Override
        public void onHttpRequestSucceed(BasePersonInfoResult result, OriginalCall call) {
            if (result.isSuccess()){
                Friend friend = User.UserToFriend(result.getData());
                FriendRepository.save(friend);
                buildNotification(message);
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {

        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ClientConfig.getMessageNotifyEnable()) {
            message = (Message) intent.getSerializableExtra(Message.NAME);

            final MessageSource source = MessageParserFactory.getFactory().parserMessageSource(message);

            if (source instanceof Friend){
                Friend friend = (Friend) source;
                HttpServiceManager.queryPersonInfo(friend.account, listener);
            }else {
                buildNotification(message);
            }

        }
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.notificationMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private void buildNotification(Message message) {

        final MessageSource source = MessageParserFactory.getFactory().parserMessageSource(message);
        if (source == null) {
            return;
        }
        /*
           屏蔽了群消息，如果是AT我的再进行提示
         */
        boolean isIgnoreGroupMessage = source instanceof Group && ClientConfig.isIgnoredGroupMessage(message.sender);
        if ( isIgnoreGroupMessage && !ClientConfig.isAtMeGroupMessage(message.id)){
            return;
        }

        String channelId = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channelId = source.getIdentityId();
            NotificationChannel channel = new NotificationChannel(channelId,source.getName(),NotificationManager.IMPORTANCE_LOW);
            channel.setSound(null,null);
            channel.enableLights(true);
            channel.setLightColor(Color.GREEN);
            channel.setShowBadge(true);
            channel.enableVibration(false);
            notificationMgr.createNotificationChannel(channel);
        }

        final int notificationId = source.getIdentityId().hashCode();

        String content = MessageParserFactory.getFactory().parserMessageText(message);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 1, new Intent(this, HomeActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this,channelId);
        builder.setAutoCancel(true);
        builder.setWhen(message.timestamp);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker(content);
        builder.setContentTitle(source.getTitle());
        builder.setContentText(content);
        builder.setContentIntent(contentIntent);
        builder.setDefaults(NotificationCompat.DEFAULT_LIGHTS);

        if (ClientConfig.getMessageSoundEnable()){
            builder.setDefaults(NotificationCompat.DEFAULT_LIGHTS | NotificationCompat.DEFAULT_SOUND);
        }else {
            builder.setDefaults(NotificationCompat.DEFAULT_LIGHTS);
        }
        if (source.getNotificationPriority()  == NotificationCompat.PRIORITY_HIGH){
            builder.setFullScreenIntent(contentIntent,false);
        }

        if (source.getWebIcon() == null){
            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), source.getNotifyIcon()));
            showNotification(notificationId,builder.build());
            return;
        }

        CloudImageLoaderFactory.get().downloadOnly(source.getWebIcon(), new CloudImageLoadListener() {
            @Override
            public void onImageLoadFailure(Object key) {
                builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), source.getNotifyIcon()));
                showNotification(notificationId,builder.build());
            }

            @Override
            public void onImageLoadSucceed(Object key, Bitmap resource) {
                builder.setLargeIcon(resource);
                showNotification(notificationId,builder.build());
            }
        });
    }

    private void showNotification(int id,Notification notification) {
        notificationMgr.notify(id, notification);
        if (ClientConfig.getMessageSoundEnable() && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            RingtoneManager.getRingtone(getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)).play();
        }
    }

    @Override
    public void onDestroy() {
        if (notificationMgr != null) {
            notificationMgr.cancelAll();
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
