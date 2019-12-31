
package com.linkb.jstx.network.http;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.ArrayMap;

import com.farsunset.cim.sdk.android.constant.CIMConstant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.linkb.BuildConfig;
import com.linkb.jstx.app.ClientConfig;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.app.URLConstant;
import com.linkb.jstx.bean.ChatItem;
import com.linkb.jstx.bean.FileResource;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.database.MicroServerRepository;
import com.linkb.jstx.database.MomentRuleRepository;
import com.linkb.jstx.database.OrganizationRepository;
import com.linkb.jstx.database.TagRepository;
import com.linkb.jstx.listener.OSSFileUploadListener;
import com.linkb.jstx.listener.SimpleCloudImageLoadListener;
import com.linkb.jstx.listener.SimpleFileDownloadListener;
import com.linkb.jstx.listener.SimpleFileUploadListener;
import com.linkb.jstx.message.parser.MessageParserFactory;
import com.linkb.jstx.model.Comment;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.Group;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.MicroServer;
import com.linkb.jstx.model.Moment;
import com.linkb.jstx.model.Organization;
import com.linkb.jstx.model.Tag;
import com.linkb.jstx.network.CloudFileDownloader;
import com.linkb.jstx.network.CloudFileUploader;
import com.linkb.jstx.network.CloudImageLoaderFactory;
import com.linkb.jstx.network.model.ChatFile;
import com.linkb.jstx.network.model.ChatMap;
import com.linkb.jstx.network.model.ChatVoice;
import com.linkb.jstx.network.model.SNSChatImage;
import com.linkb.jstx.network.model.SNSVideo;
import com.linkb.jstx.network.result.AppVersionResult;
import com.linkb.jstx.network.result.BaseDataResult;
import com.linkb.jstx.network.result.BasePersonInfoResult;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.CoinReceiveResult;
import com.linkb.jstx.network.result.CoinSearchResult;
import com.linkb.jstx.network.result.CoinTransferResult;
import com.linkb.jstx.network.result.CommentResult;
import com.linkb.jstx.network.result.CommonResult;
import com.linkb.jstx.network.result.ContactsResult;
import com.linkb.jstx.network.result.CurrencyListResult;
import com.linkb.jstx.network.result.FriendListResult;
import com.linkb.jstx.network.result.FriendQueryResult;
import com.linkb.jstx.network.result.GetBannerResult;
import com.linkb.jstx.network.result.GroupMemberListResult;
import com.linkb.jstx.network.result.GroupQueryResult;
import com.linkb.jstx.network.result.GroupResult;
import com.linkb.jstx.network.result.InformationListResult;
import com.linkb.jstx.network.result.LoginResult;
import com.linkb.jstx.network.result.MessageForwardResult;
import com.linkb.jstx.network.result.MessageListResult;
import com.linkb.jstx.network.result.MicroAppListResult;
import com.linkb.jstx.network.result.MicroServerListResult;
import com.linkb.jstx.network.result.MicroServerMenuListResult;
import com.linkb.jstx.network.result.MicroServerResult;
import com.linkb.jstx.network.result.MineInviteCodeResult;
import com.linkb.jstx.network.result.MineInviteInfoResult;
import com.linkb.jstx.network.result.ModifyPersonInfoResult;
import com.linkb.jstx.network.result.MomentListResult;
import com.linkb.jstx.network.result.MomentResult;
import com.linkb.jstx.network.result.MomentRuleResult;
import com.linkb.jstx.network.result.NearlyPeopleResult;
import com.linkb.jstx.network.result.NewsDataResult;
import com.linkb.jstx.network.result.QueryAssetsResult;
import com.linkb.jstx.network.result.QueryCoinTransferStatusResult;
import com.linkb.jstx.network.result.QueryExchangeRateResult;
import com.linkb.jstx.network.result.QueryGroupInfoResult;
import com.linkb.jstx.network.result.QueryMineGroupResult;
import com.linkb.jstx.network.result.QueryRedPacketStatusResult;
import com.linkb.jstx.network.result.QueryWithdrawAmountResult;
import com.linkb.jstx.network.result.ReceivedRedPacketListResult;
import com.linkb.jstx.network.result.ReceivedRedPacketResult;
import com.linkb.jstx.network.result.RedPacketReceivedMemberResult;
import com.linkb.jstx.network.result.SendMessageResult;
import com.linkb.jstx.network.result.SendRedPacketResult;
import com.linkb.jstx.network.result.SendedRedPacketListResult;
import com.linkb.jstx.network.result.WithDrawResult;
import com.linkb.jstx.network.result.WithdrawBillResult;
import com.linkb.jstx.util.AppTools;
import com.linkb.jstx.util.BitmapUtils;
import com.linkb.jstx.util.FileURLBuilder;
import com.linkb.jstx.util.MD5;
import com.linkb.jstx.util.MessageUtil;
import com.linkb.jstx.util.TimeUtils;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.linkb.jstx.app.URLConstant.GET_ALL_GROUP;
import static com.linkb.jstx.app.URLConstant.MESSAGE_REVOKE_URL;

public class HttpServiceManager {


    /**
     * 用户登录
     *
     * @param account
     * @param password
     * @param listener
     */
    public static void login(String account, String password, String device, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.USER_LOGIN_URL, LoginResult.class);
        if (BuildConfig.LOCAL) {
            requestBody.addParameter("password", MD5.digest(password + "blink"));
        } else {
            requestBody.addParameter("password", MD5.digest(password + "blink"));
        }

        requestBody.addParameter("account", account);
        requestBody.addParameter("device", device);
        requestBody.addParameter("loginType", 1);   // android 是1 iOS是2
        requestBody.addParameter("device", device);
        requestBody.addParameter("curTime", TimeUtils.getCurrentTime());
        requestBody.addParameter("sign", MD5.digest(device + "blink" + String.valueOf(TimeUtils.getCurrentTime() / 600L))); //防止脚本自动登录
        HttpRequestLauncher.execute(requestBody, listener);
    }

    public static void logout() {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.GET, URLConstant.USER_LOGOUT_URL, LoginResult.class);
        HttpRequestLauncher.executeQuietly(requestBody);
    }

    public static void updatePassword(String oldPassword, String newPassword, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.PATCH, URLConstant.USER_PASSWORD_URL, BaseResult.class);
        requestBody.addParameter("oldPassword", MD5.digest(oldPassword + "blink"));
        requestBody.addParameter("newPassword", MD5.digest(newPassword + "blink"));
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 修改支付密码
     */
    public static void updateApplyPassword(String oldPassword, String newPassword, String identifyingCode, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.PATCH, URLConstant.MODIFY_APPLY_PASSWORD_URL, BaseResult.class);
        requestBody.addParameter("oldPassword", MD5.digest(oldPassword + "blink"));
        requestBody.addParameter("newPassword", MD5.digest(newPassword + "blink"));
        requestBody.addParameter("identifyingCode", identifyingCode);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    public static void loadAllBaseData() {
//        loadUserDatabase();
        loadOrgDatabase();
        queryMomentRule();
        queryBaseDataList();
    }

    private static void queryBaseDataList() {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.GET_BASE_DATA_URL, ContactsResult.class);
        requestBody.get();
        requestBody.setRunWithOtherThread();
        HttpRequestLauncher.execute(requestBody, new SimpleHttpRequestListener<ContactsResult>() {
            @Override
            public void onHttpRequestSucceed(ContactsResult result, OriginalCall call) {
                if (result.isNotEmpty(Tag.class)) {
                    TagRepository.saveAll(result.tagList);
                }
                if (result.isNotEmpty(Group.class)) {
//                    GroupRepository.saveAll(result.groupList);
                }
                if (result.isNotEmpty(MicroServer.class)) {
                    MicroServerRepository.saveAll(result.microServerList);
                }
            }
        });
    }

    private static void queryMomentRule() {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.MOMENT_RULE_LIST_URL, MomentRuleResult.class);
        requestBody.get();
        requestBody.setRunWithOtherThread();
        HttpRequestLauncher.execute(requestBody, new SimpleHttpRequestListener<MomentRuleResult>() {
            @Override
            public void onHttpRequestSucceed(MomentRuleResult result, OriginalCall call) {
                if (result.isSuccess() && result.isNotEmpty()) {
                    MomentRuleRepository.saveAll(result.dataList);
                }
            }
        });
    }


    public static void loadUserDatabase() {
        CloudFileDownloader.asyncDownloadUserDatabase(new SimpleFileDownloadListener() {
            @Override
            public void onDownloadCompleted(final File file, String currentKey) {
                final String QUERY_USER_SQL = "select * from t_jstx_user ";
//                final String QUERY_USER_SQL = "select * from " + Friend.TABLE_NAME;
                SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(file, null);
                Cursor cursor = database.rawQuery(QUERY_USER_SQL, null);
                if (cursor.moveToFirst()) {
                    List<Friend> list = new ArrayList<>();
                    do {
                        Friend friend = new Friend();
                        friend.account = cursor.getString(cursor.getColumnIndex("account"));
                        friend.name = cursor.getString(cursor.getColumnIndex("name"));
                        friend.code = cursor.getString(cursor.getColumnIndex("code"));
                        friend.telephone = cursor.getString(cursor.getColumnIndex("telephone"));
                        friend.email = cursor.getString(cursor.getColumnIndex("email"));
                        friend.gender = cursor.getString(cursor.getColumnIndex("gender"));
                        friend.motto = cursor.getString(cursor.getColumnIndex("motto"));
                        list.add(friend);
                    } while (cursor.moveToNext());

                    FriendRepository.saveAll(list);
                }

                AppTools.closeQuietly(cursor);
                IOUtils.closeQuietly(database);

//                LvxinApplication.sendLocalBroadcast(new Intent(Constant.Action.ACTION_RELOAD_CONTACTS));
            }
        });
    }

    public static void loadOrgDatabase() {
        CloudFileDownloader.asyncDownloadOrgDatabase(new SimpleFileDownloadListener() {
            @Override
            public void onDownloadCompleted(final File file, String currentKey) {
                final String QUERY_ORG_SQL = "select * from t_jstx_organization";
//                final String QUERY_ORG_SQL = "select * from " + Organization.TABLE_NAME;
                SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(file, null);
                Cursor cursor = database.rawQuery(QUERY_ORG_SQL, null);

                if (cursor.moveToFirst()) {
                    List<Organization> list = new ArrayList<>();
                    do {
                        Organization org = new Organization();
                        org.code = cursor.getString(cursor.getColumnIndex("code"));
                        org.name = cursor.getString(cursor.getColumnIndex("name"));
                        org.parentCode = cursor.getString(cursor.getColumnIndex("parentCode"));
                        org.parentCode = TextUtils.isEmpty(org.parentCode) ? null : org.parentCode.trim();
                        org.sort = cursor.getInt(cursor.getColumnIndex("sort"));
                        list.add(org);

                    } while (cursor.moveToNext());
                    OrganizationRepository.saveAll(list);
                }

                AppTools.closeQuietly(cursor);
                IOUtils.closeQuietly(database);
            }
        });
    }


    public static void forwardImage(final Message message, Uri imageUri, final HttpRequestListener listener) {
        CloudImageLoaderFactory.get().downloadOnly(imageUri.toString(), new SimpleCloudImageLoadListener() {
            @Override
            public void onImageLoadSucceed(Object key, Bitmap resource) {
                SNSChatImage snsImage = BitmapUtils.compressSNSImage(resource);
                message.content = new Gson().toJson(snsImage);
                if (!snsImage.thumb.equals(snsImage.image)) {
                    CloudFileUploader.asyncUpload(snsImage.thumb, new File(LvxinApplication.CACHE_DIR_IMAGE, snsImage.thumb), null);
                }
                CloudFileUploader.asyncUpload(snsImage.image, new File(LvxinApplication.CACHE_DIR_IMAGE, snsImage.image), new SimpleFileUploadListener() {
                    @Override
                    public void onUploadCompleted(FileResource resource) {
                        forwardText(message, listener);
                    }

                    @Override
                    public void onUploadFailured(FileResource resource, Exception e) {
                        listener.onHttpRequestFailure(e, new OriginalCall(URLConstant.MESSAGE_FORWARD_URL, HttpMethod.POST));
                    }
                });
            }
        });
    }

    public static void forwardText(Message message, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.MESSAGE_FORWARD_URL, MessageForwardResult.class);
        requestBody.addParameter("content", message.content);
        requestBody.addParameter("sender", message.sender);
        requestBody.addParameter("receiver", message.receiver);
        requestBody.addParameter("format", message.format);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    public static void forwardFile(final Message message, final HttpRequestListener listener) {
        final ChatFile chatFile = new Gson().fromJson(message.content, ChatFile.class);
        final File file = chatFile.getLocalFile();
        CloudFileUploader.asyncUpload(chatFile.file, file, new SimpleFileUploadListener() {
            @Override
            public void onUploadCompleted(FileResource resource) {
                chatFile.path = null;
                message.content = new Gson().toJson(chatFile);
                forwardText(message, listener);
            }

            @Override
            public void onUploadFailured(FileResource resource, Exception e) {
                listener.onHttpRequestFailure(e, null);
            }
        });
    }


    public static void read(Message message) {
        if (message.isNeedShowReadStatus()) {
            if (ClientConfig.getMessageReceiptEnable()) {
                HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.MESSAGE_READ_NOTIFY_URL, BaseResult.class);
                requestBody.addParameter("receiver", message.sender);
                requestBody.addParameter("id", message.id);
//                requestBody.addPathVariable("id", message.id);
//                requestBody.addPathVariable("receiver", message.receiver);
                HttpRequestLauncher.executeQuietly(requestBody);
            } else {
                HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.MESSAGE_READ_URL, BaseResult.class);
                requestBody.addParameter("id-token", message.id);
//                requestBody.addPathVariable("id", message.id);
                HttpRequestLauncher.executeQuietly(requestBody);
            }
        }
    }

    public static void receipt(Message message) {
        //9开头的为瞬时动作消息，
        if (message.isActionMessage()) {
            return;
        }
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.GET, URLConstant.MESSAGE_RECEIVE_URL, BaseResult.class);
        requestBody.addPathVariable("id", message.id);
        HttpRequestLauncher.executeQuietly(requestBody);
    }

    public static void queryOfflineMessage() {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.GET, URLConstant.MESSAGE_OFFLINELIST_URL, MessageListResult.class);
        requestBody.setRunWithOtherThread();
        HttpRequestLauncher.execute(requestBody, new SimpleHttpRequestListener<MessageListResult>() {
            @Override
            public void onHttpRequestSucceed(MessageListResult result, OriginalCall call) {
                if (result.isSuccess() && result.isNotEmpty()) {
                    for (Message message : result.dataList) {
                        Intent intent = new Intent(CIMConstant.IntentAction.ACTION_MESSAGE_RECEIVED);
                        intent.putExtra(com.farsunset.cim.sdk.android.model.Message.class.getName(), MessageUtil.transform(message));
                        intent.putExtra(Constant.NEED_RECEIPT, false);
                        LvxinApplication.sendGlobalBroadcast(intent);
                    }
                    HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.GET, URLConstant.MESSAGE_BATCH_RECEIVE_URL, BaseResult.class);
                    HttpRequestLauncher.executeQuietly(requestBody);
                }
            }
        });
    }


    public static void queryOtherMomentList(String account, int page, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.GET, URLConstant.MOMENT_OTHER_LIST_URL, MomentListResult.class);
        requestBody.addPathVariable("page", page);
        requestBody.addPathVariable("account", account);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    public static void queryMeMomentList(int page, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.GET, URLConstant.MOMENT_ME_LIST_URL, MomentListResult.class);
        requestBody.addPathVariable("page", page);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    public static void queryMomentTimeline(int page, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.GET, URLConstant.MOMENT_TIMELINE_URL, MomentListResult.class);
        requestBody.addPathVariable("page", page);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    public static void queryMoment(long id, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.GET, URLConstant.MOMENT_OPERATION_URL, MomentResult.class);
        requestBody.addPathVariable("id", id);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    public static void deleteMoment(long id, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.DELETE, URLConstant.MOMENT_OPERATION_URL, BaseResult.class);
        requestBody.addPathVariable("id", id);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    public static void publish(Comment comment, String author, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.COMMENT_OPERATION_URL, CommentResult.class);
        requestBody.addParameter("sourceId", comment.id);
        requestBody.addParameter("content", comment.content);
        requestBody.addParameter("targetId", comment.targetId);
        requestBody.addParameter("author", author);
        requestBody.addParameter("reply", comment.reply);
        requestBody.addParameter("type", comment.type);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    public static void publish(Moment moment, HttpRequestListener listener) {

        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.MOMENT_PUBLISH_URL, CommonResult.class);
        requestBody.addParameter("type", moment.type);
        requestBody.addParameter("content", moment.content);
        requestBody.addParameter("extra", moment.extra);
        requestBody.addParameter("text", moment.text);
        HttpRequestLauncher.execute(requestBody, listener);

        OSSFileUploadListener UPLOAD_LISTENER = new SimpleFileUploadListener() {
            private final int TRY_COUNT = 3;
            private final ArrayMap<String, Integer> COUNTER = new ArrayMap<>();

            @Override
            public void onUploadCompleted(FileResource resource) {
                COUNTER.remove(resource.key);
            }

            @Override
            public void onUploadFailured(FileResource resource, Exception e) {
                Integer count = COUNTER.get(resource.key);
                count = (count == null ? 0 : count.intValue());
                if (count < TRY_COUNT && AppTools.isNetworkConnected()) {
                    CloudFileUploader.asyncUpload(resource, this);
                    COUNTER.put(resource.key, ++count);
                }
            }
        };

        if (moment.type.equals(Moment.FORMAT_MULTI_IMAGE)) {
            List<SNSChatImage> snsImageList = new Gson().fromJson(moment.content, new TypeToken<List<SNSChatImage>>() {
            }.getType());
            for (SNSChatImage image : snsImageList) {
                CloudFileUploader.asyncUpload(FileURLBuilder.BUCKET_MOMENT, image.thumb, new File(LvxinApplication.CACHE_DIR_IMAGE, image.thumb), UPLOAD_LISTENER);
                CloudFileUploader.asyncUpload(FileURLBuilder.BUCKET_MOMENT, image.image, new File(LvxinApplication.CACHE_DIR_IMAGE, image.image), UPLOAD_LISTENER);
            }
        }

        if (moment.type.equals(Moment.FORMAT_IMAGE)) {
            SNSChatImage image = new Gson().fromJson(moment.content, SNSChatImage.class);
            CloudFileUploader.asyncUpload(FileURLBuilder.BUCKET_MOMENT, image.thumb, new File(LvxinApplication.CACHE_DIR_IMAGE, image.thumb), UPLOAD_LISTENER);
            CloudFileUploader.asyncUpload(FileURLBuilder.BUCKET_MOMENT, image.image, new File(LvxinApplication.CACHE_DIR_IMAGE, image.image), UPLOAD_LISTENER);
        }

        if (moment.type.equals(Moment.FORMAT_VIDEO)) {
            SNSVideo video = new Gson().fromJson(moment.content, SNSVideo.class);
            CloudFileUploader.asyncUpload(FileURLBuilder.BUCKET_MOMENT, video.video, new File(LvxinApplication.CACHE_DIR_VIDEO, video.video), UPLOAD_LISTENER);
            CloudFileUploader.asyncUpload(FileURLBuilder.BUCKET_MOMENT, video.image, new File(LvxinApplication.CACHE_DIR_VIDEO, video.image), UPLOAD_LISTENER);
        }
    }


    /**
     * 发送音视频连接的消息
     */
    public static void sendVideoConnectMessage(String roomName, String permission, String userId, String action, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.SEND_VIDEO_CONNECT_MESSAGE, SendMessageResult.class);
        requestBody.addParameter("action", action);
        requestBody.addParameter("appId", BuildConfig.QINIU_APP_ID);
        requestBody.addParameter("roomName", roomName);
        requestBody.addParameter("userId", userId);
        requestBody.addParameter("expireAt", TimeUtils.getCurrentTime() + 60 * 60 * 24);
        requestBody.addParameter("permission", permission);
        HttpRequestLauncher.execute(requestBody, listener);
    }


    /**
     * 发送音视频连接的消息
     */
    public static void rejectVideoConnectMessage(Message message) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.REJECT_VIDEO_CONNECT_MESSAGE, SendMessageResult.class);
        requestBody.addParameter("extra", message.extra);
        requestBody.addParameter("content", message.content);
        requestBody.addParameter("sender", message.sender);
        requestBody.addParameter("receiver", message.receiver);
        requestBody.addParameter("action", message.action);
        requestBody.addParameter("format", message.format);
        HttpRequestLauncher.executeQuietly(requestBody);
    }

    public static void sendOnly(Message message) {
        sendOnly(message, null);
    }

    public static void sendOnly(Message message, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.MESSAGE_SEND_URL, SendMessageResult.class);
        requestBody.addParameter("extra", message.extra);
        requestBody.addParameter("content", message.content);
        requestBody.addParameter("sender", message.sender);
        requestBody.addParameter("receiver", message.receiver);
        requestBody.addParameter("action", message.action);
        requestBody.addParameter("format", message.format);
        if (listener == null) {
            HttpRequestLauncher.executeQuietly(requestBody);
        } else {
            HttpRequestLauncher.execute(requestBody, listener);
        }
    }

    public static void send(final Message message) {

        final HttpRequestListener httpListener = new HttpRequestListener<SendMessageResult>() {
            @Override
            public void onHttpRequestSucceed(SendMessageResult result, OriginalCall call) {
                ChatItem chat = new ChatItem(message, MessageParserFactory.getFactory().parserMessageSource(message));
                if (result.isSuccess()) {
                    MessageRepository.deleteById(message.id);
                    message.id = result.id;
                    message.timestamp = result.timestamp;
                    message.state = Constant.MessageStatus.STATUS_SEND;
                    MessageRepository.add(message);
                } else if (result.code.equals("402")) {
                    message.state = Constant.MessageStatus.STATUS_SEND_FAILURE;
                    //群被禁言
                    Intent bannedIntent = new Intent(Constant.Action.ACTION_GROUP_BANNED);
                    bannedIntent.putExtra(ChatItem.NAME, chat);
                    LvxinApplication.sendLocalBroadcast(bannedIntent);
                } else {
                    message.state = Constant.MessageStatus.STATUS_SEND_FAILURE;
                }


                //通知主页对话列表刷新
                Intent rencentIntent = new Intent(Constant.Action.ACTION_RECENT_REFRESH_CHAT);
                rencentIntent.putExtra(ChatItem.NAME, chat);
                LvxinApplication.sendLocalBroadcast(rencentIntent);

                //通知聊天窗口页面列表刷新
                Intent windowIntent = new Intent(Constant.Action.ACTION_WINDOW_REFRESH_MESSAGE);
                windowIntent.putExtra(ChatItem.NAME, chat);
                LvxinApplication.sendLocalBroadcast(windowIntent);


            }

            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {
                message.state = Constant.MessageStatus.STATUS_SEND_FAILURE;
                MessageRepository.updateStatus(message.id, message.state);

                ChatItem chat = new ChatItem(message, MessageParserFactory.getFactory().parserMessageSource(message));


                //通知主页对话列表刷新
                Intent rencentIntent = new Intent(Constant.Action.ACTION_RECENT_REFRESH_CHAT);
                rencentIntent.putExtra(ChatItem.NAME, chat);
                LvxinApplication.sendLocalBroadcast(rencentIntent);

                //通知聊天窗口页面列表刷新
                Intent windowIntent = new Intent(Constant.Action.ACTION_WINDOW_REFRESH_MESSAGE);
                windowIntent.putExtra(ChatItem.NAME, chat);
                LvxinApplication.sendLocalBroadcast(windowIntent);
            }

        };

        OSSFileUploadListener uploadListener = new SimpleFileUploadListener() {
            @Override
            public void onUploadCompleted(FileResource resource) {
                sendOnly(message, httpListener);
            }

            @Override
            public void onUploadProgress(String key, float progress) {
                Intent intent = new Intent(Constant.Action.ACTION_UPLOAD_PROGRESS);
                intent.putExtra("objectKey", key);
                intent.putExtra("progress", progress);
                LvxinApplication.sendLocalBroadcast(intent);
            }

            @Override
            public void onUploadFailured(FileResource resource, Exception e) {
                httpListener.onHttpRequestFailure(e, new OriginalCall(URLConstant.MESSAGE_SEND_URL, HttpMethod.POST));
            }
        };

        if (message.format.equals(Constant.MessageFormat.FORMAT_TEXT)) {
            sendOnly(message, httpListener);
            return;
        }
        if (message.format.equals(Constant.MessageFormat.FORMAT_SEND_CARDS)) {
            sendOnly(message, httpListener);
        }
        if (message.format.equals(Constant.MessageFormat.FORMAT_COIN_TRANSFER)) {
            sendOnly(message, httpListener);
        }
        if (message.format.equals(Constant.MessageFormat.FORMAT_RED_PACKET)) {
            sendOnly(message, httpListener);
        }
        if (message.format.equals(Constant.MessageFormat.FORMAT_FILE)) {
            ChatFile chatFile = new Gson().fromJson(message.content, ChatFile.class);
            File file = chatFile.getLocalFile();
            CloudFileUploader.asyncUpload(chatFile.file, file, uploadListener);
            chatFile.path = null;
            message.content = new Gson().toJson(chatFile);
            return;
        }
        if (message.format.equals(Constant.MessageFormat.FORMAT_IMAGE)) {

            SNSChatImage chatImage = new Gson().fromJson(message.content, SNSChatImage.class);
            CloudFileUploader.asyncUpload(chatImage.image, new File(LvxinApplication.CACHE_DIR_IMAGE, chatImage.image), uploadListener);
            if (!chatImage.thumb.equals(chatImage.image)) {
                CloudFileUploader.asyncUpload(chatImage.thumb, new File(LvxinApplication.CACHE_DIR_IMAGE, chatImage.thumb), null);
            }

            return;
        }
        if (message.format.equals(Constant.MessageFormat.FORMAT_MAP)) {
            ChatMap chatMap = new Gson().fromJson(message.content, ChatMap.class);
            CloudFileUploader.asyncUpload(chatMap.image, new File(LvxinApplication.CACHE_DIR_IMAGE, chatMap.image), uploadListener);
            return;
        }
        if (message.format.equals(Constant.MessageFormat.FORMAT_VOICE)) {
            ChatVoice chatVoice = new Gson().fromJson(message.content, ChatVoice.class);
            CloudFileUploader.asyncUpload(chatVoice.file, new File(LvxinApplication.CACHE_DIR_VOICE, chatVoice.file), uploadListener);
        }
        if (message.format.equals(Constant.MessageFormat.FORMAT_VIDEO)) {
            SNSVideo chatVideo = new Gson().fromJson(message.content, SNSVideo.class);
            CloudFileUploader.asyncUpload(chatVideo.video, new File(LvxinApplication.CACHE_DIR_VIDEO, chatVideo.video), uploadListener);
            CloudFileUploader.asyncUpload(chatVideo.image, new File(LvxinApplication.CACHE_DIR_VIDEO, chatVideo.image), null);
        }
    }


    public static void revokeMessage(long id, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.DELETE, MESSAGE_REVOKE_URL, BaseResult.class);
        requestBody.addPathVariable("id", id);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 获取所有群组列表
     */
    public static void getAllGroup(HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.GET, GET_ALL_GROUP, GroupResult.class);
        HttpRequestLauncher.execute(requestBody, listener);
    }


    public static void create(Group group, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.GROUP_OPERATION_URL, GroupResult.class);
        requestBody.addParameter("name", group.name);
        requestBody.addParameter("founder", group.founder);
        requestBody.addParameter("summary", group.summary);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    public static void setGroupLogo(long id) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.PATCH, URLConstant.GROUP_SET_LOGO_URL, BaseResult.class);
        requestBody.addParameter("id", id);
        HttpRequestLauncher.executeQuietly(requestBody);
    }

    public static void setGroupName(long id, String name) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.PATCH, URLConstant.GROUP_SET_NAME_URL, BaseResult.class);
        requestBody.addParameter("id", id);
        requestBody.addParameter("name", name);

        HttpRequestLauncher.executeQuietly(requestBody);
    }

    public static void setGroupProfile(long id, String catogory) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.PATCH, URLConstant.GROUP_SET_PROFILE_URL, BaseResult.class);
        requestBody.addParameter("id", id);
        requestBody.addParameter("category", catogory);

        HttpRequestLauncher.executeQuietly(requestBody);
    }


    public static void setGroupSummary(long id, String summary) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.PATCH, URLConstant.GROUP_SET_SUMMARY_URL, BaseResult.class);
        requestBody.addParameter("id", id);
        requestBody.addParameter("summary", summary);
        HttpRequestLauncher.executeQuietly(requestBody);
    }

    public static void disbandGroup(long id, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.GROUP_DISBAND_URL, BaseResult.class);
        requestBody.delete();
        requestBody.addPathVariable("id", id);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 查询群资料详情
     */
    public static void queryGroupInfo(long id, HttpRequestListener<QueryGroupInfoResult> listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.GET, URLConstant.QUERY_GROUP_INFO, QueryGroupInfoResult.class);
        requestBody.addPathVariable("id", id);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    public static void quitGroup(long id, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.DELETE, URLConstant.GROUP_QUIT_URL, BaseResult.class);
        requestBody.addPathVariable("groupId", id);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    public static void searchGroup(String keyword, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.GET, URLConstant.GROUP_SEARCH_URL, GroupResult.class);
        requestBody.addPathVariable("keyword", keyword);
        HttpRequestLauncher.execute(requestBody, listener);
    }


    public static void queryGroupMember(long id, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.GROUP_MEMBER_LIST_URL, GroupMemberListResult.class);
        requestBody.addPathVariable("id", id);
        requestBody.get();
        HttpRequestLauncher.execute(requestBody, listener);
    }

    public static void removeGroupMember(long id, String account, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.DELETE, URLConstant.GROUP_MEMBER_BATCH_URL, BaseResult.class);
        requestBody.addPathVariable("account", account);
        requestBody.addPathVariable("groupId", id);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    public static void addGroupMember(long id, String account, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.GROUP_MEMBER_URL, BaseResult.class);
        requestBody.addParameter("groupId", id);
        requestBody.addParameter("account", account);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    public static void inviteGroupMember(String account, String conntent, long groupId, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.GROUP_MEMBER_INVITE_URL, BaseResult.class);
        requestBody.addParameter("account", account);
        requestBody.addParameter("content", conntent);
        requestBody.addParameter("groupId", groupId);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    public static void queryAllMicroServer(HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.MICROSERVER_LIST_URL, MicroServerListResult.class);
        requestBody.get();
        HttpRequestLauncher.execute(requestBody, listener);
    }

    public static void unsubscriber(String target, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.DELETE, URLConstant.SUBSCRIBE_OPERATION_URL, BaseResult.class);
        requestBody.addPathVariable("target", target);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    public static void subscriber(String target, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.GET, URLConstant.SUBSCRIBE_OPERATION_URL, BaseResult.class);
        requestBody.addPathVariable("target", target);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    public static void searchMicroServer(String keyword, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.GET, URLConstant.MICROSERVER_SEARCH_URL, MicroServerResult.class);
        requestBody.addPathVariable("keyword", keyword);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    public static void queryMicroServerMenuList(String target, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.MICROSERVER_MENU_URL, MicroServerMenuListResult.class);
        requestBody.get();
        requestBody.addPathVariable("account", target);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    public static void settingMomentRule(String target, String type, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.MOMENT_RULE_URL, BaseResult.class);
        requestBody.addPathVariable("target", target);
        requestBody.addPathVariable("type", type);
        requestBody.addParameter("r", 0);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    public static void removeMomentRule(String target, String type, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.DELETE, URLConstant.MOMENT_RULE_URL, BaseResult.class);
        requestBody.addPathVariable("target", target);
        requestBody.addPathVariable("type", type);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    public static void deleteComment(long id, String author, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.DELETE, URLConstant.COMMENT_DELETE_URL, BaseResult.class);
        requestBody.addPathVariable("author", author);
        requestBody.addPathVariable("id", id);
        if (listener == null) {
            HttpRequestLauncher.executeQuietly(requestBody);
        } else {
            HttpRequestLauncher.execute(requestBody, listener);
        }
    }

    public static void addPraise(long targetId, String author, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.COMMENT_PRAISE_URL, CommentResult.class);
        requestBody.addParameter("author", author);
        requestBody.addParameter("targetId", targetId);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    public static void queryMicroAppList(HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.GET, URLConstant.MICROAPP_LIST_URL, MicroAppListResult.class);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    public static void queryNewAppVersion(HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.GET, URLConstant.CHECK_NEW_VERSION_URL, AppVersionResult.class);
        requestBody.addPathVariable("versionCode", BuildConfig.VERSION_CODE);
        requestBody.addPathVariable("domain", "lvxin_android");
        requestBody.setRunWithOtherThread();
        HttpRequestLauncher.execute(requestBody, listener);
    }

    public static void createTag(String name, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.TAG_URL, CommonResult.class);
        requestBody.addParameter("name", name);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    public static void deleteTag(long id) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.DELETE, URLConstant.TAG_DELETE_URL, BaseResult.class);
        requestBody.addPathVariable("id", id);
        HttpRequestLauncher.executeQuietly(requestBody);
    }

    public static void updateTag(long id, String name) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.PATCH, URLConstant.TAG_URL, BaseResult.class);
        requestBody.addParameter("id", id);
        requestBody.addParameter("name", name);
        HttpRequestLauncher.executeQuietly(requestBody);
    }

    public static void addTagMember(long id, List<String> accountList) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.TAG_MEMBER_URL, BaseResult.class);
        requestBody.addParameter("id", id);
        requestBody.addParameter("account", TextUtils.join(",", accountList));
        HttpRequestLauncher.executeQuietly(requestBody);
    }

    public static void removeTagMember(long id, List<String> accountList) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.DELETE, URLConstant.TAG_MEMBER_URL, BaseResult.class);
        requestBody.addParameter("id", id);
        requestBody.addParameter("account", TextUtils.join(",", accountList));
        HttpRequestLauncher.executeQuietly(requestBody);
    }

    /**
     * 获取资讯列表
     */
    public static void getInformationList(HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.GET, URLConstant.GET_INFORMATION, InformationListResult.class);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 获取新闻 / 快讯 列表
     *
     * @param page 页数
     * @param type 1表示新闻 2 表示资讯
     */
    public static void getNewsOrExpressList(int page, int type, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.GET, URLConstant.GET_NEWS_LIST, NewsDataResult.class);
        requestBody.addPathVariable("currentPage", page);
        requestBody.addPathVariable("type", type);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 资讯评论
     *
     * @param type    评论类型(0 回复,1赞, 2踩, 3利好,4利空)
     * @param replyId type = 0时,需要传content和replyId ,replyId为回复ID.
     */
    public static void saveInformationComment(long editorId, String type, String content, String replyId, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.INFORMATIN_COMMENT, BaseDataResult.class);
        requestBody.addParameter("editorId", editorId);
        requestBody.addParameter("type", type);
        requestBody.addParameter("content", content);
        requestBody.addParameter("replyId", replyId);
        HttpRequestLauncher.execute(requestBody, listener);
    }


    /**
     * 获取资讯列表
     */
    public static void getTrendBannerList(HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.GET, URLConstant.GET_BANNER, GetBannerResult.class);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 注册账号
     *
     * @param locale 0是中国，1是外国
     */
    public static void registerAccount(String account, String gender, String name, String password, String vertcode, String locale, HttpRequestListener listener) {

        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.REGISTER_ACCOUNT, BaseResult.class);
        requestBody.addParameter("account", account);
        requestBody.addParameter("name", name);
        requestBody.addParameter("gender", gender);
        requestBody.addParameter("password", password);
        requestBody.addParameter("vertcode", vertcode);
        requestBody.addParameter("locale", locale);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 获取好友列表
     */
    public static void getFriendsList(HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.GET_FRIENDS_LIST, FriendListResult.class);
        requestBody.addParameter("access-token", Global.getAccessToken());
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 添加好友
     */
    public static void addFriend(String friendAccount, String name, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.ADD_FRIEND, BaseResult.class);
        requestBody.addParameter("friendAccount", friendAccount);
        requestBody.addParameter("name", name);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 添加好友2
     */
    public static void addFriend(String friendAccount, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.ADD_FRIEND, BaseResult.class);
        requestBody.addParameter("friendAccount", friendAccount);
        requestBody.addParameter("name", "");
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 申请好友关系
     */
    public static void applyFriend(String account, String friendAccount, String applyContent, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.APPLY_FRIEND, BaseResult.class);
        requestBody.addParameter("account", account);
        requestBody.addParameter("friendAccount", friendAccount);
        requestBody.addParameter("applyContent", applyContent);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 删除好友
     */
    public static void deleteFriend(String friendAccount, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.DELETE_FRIEND, BaseResult.class);
        requestBody.addParameter("friendAccount", friendAccount);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 查询好友
     */
    public static void queryFriend(String friendAccount, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.QUERY_FRIEND, FriendQueryResult.class);
        requestBody.addParameter("friendAccount", friendAccount);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 查询账号资产余额
     */
    public static void queryAssetsBalance(HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.QUERY_ASSETS_BALANCE, QueryAssetsResult.class);
        requestBody.addParameter("access-token", Global.getAccessToken());
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 查询当前各个币种的实时汇率
     */
    public static void queryCoinCurrentExchangeRate(HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.QUERY_COIN_EXCHANGE_RATE, QueryExchangeRateResult.class);
        requestBody.addParameter("access-token", Global.getAccessToken());
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 查询币种列表
     */
    public static void queryCurrencyList(HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.QUERY_CURRENCY_LIST, CurrencyListResult.class);
        requestBody.addParameter("", "");
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 验证支付密码
     */
    public static void verifyApplyPassword(String password, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.VERIFY_APPLY_PASSWORD, BaseResult.class);
        if (BuildConfig.LOCAL) {
            requestBody.addParameter("fundPassword", MD5.digest(password + "blink"));
        } else {
            requestBody.addParameter("fundPassword", MD5.digest(password + "blink"));
        }
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 发送红包
     *
     * @param sendMoney     当redPacketType == 1 和 3时，为红包总额; 当当redPacketType == 2时，为单个红包金额
     * @param redPacketType 红包类型    1 表示普通红包, 2表示普通群发红包   , 3表示拼手气群发红包
     */
    public static void sendRedPacket(int currencyId, String sendMoney, String remark, int redCount, int redPacketType, String tradePassword, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.SEND_RED_PACKET, SendRedPacketResult.class);
        requestBody.addParameter("currencyId", currencyId);
        requestBody.addParameter("sendMoney", sendMoney);
        requestBody.addParameter("redCount", redCount);
        requestBody.addParameter("remark", remark);
        requestBody.addParameter("redType", redPacketType);
        requestBody.addParameter("tradePassword", MD5.digest(tradePassword + "blink"));
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 接收红包
     */
    public static void receiveRedPacket(int currencyId, String fromAccount, String redFlag, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.RECEIVED_RED_PACKET, ReceivedRedPacketResult.class);
        requestBody.addParameter("currencyId", currencyId);
        requestBody.addParameter("redFlag", redFlag);
        requestBody.addParameter("fromAccount", fromAccount);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 查询红包接收详情
     */
    public static void queryRedPacketReceivedMenber(String fromAccount, String redFlag, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.RECEIVED_RED_PACKET_MENBER, RedPacketReceivedMemberResult.class);
        requestBody.addParameter("fromAccount", fromAccount);
        requestBody.addParameter("redFlag", redFlag);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 获取充币地址
     */
    public static void getChargeCoinAddress(int currencyId, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.CHARGE_COIN_GET_ADDRESS, BaseDataResult.class);
        requestBody.addParameter("currencyId", currencyId);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 提币
     */
    public static void withdrawCoin(String userAccount, int currencyId, String remark, String money, String address, String verifyCode, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.WITHDRAW_COIN, WithDrawResult.class);
        requestBody.addParameter("userAccount", userAccount);
        requestBody.addParameter("currencyId", currencyId);
        requestBody.addParameter("remark", remark);
        requestBody.addParameter("money", money);
        requestBody.addParameter("arriveAddress", address);
        requestBody.addParameter("arriveAddress", address);
        requestBody.addParameter("identifyingCode", verifyCode);

        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 请求获取支付短信验证码
     */
    public static void getApplyVerifyCode(HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.APPLY_MESSAGE_VERIFY_CODE, BaseDataResult.class);
        requestBody.addParameter("access-token", Global.getAccessToken());
        HttpRequestLauncher.execute(requestBody, listener);
    }


    /**
     * 请求获取支付短信验证码
     */
    public static void getRegisterVerifyCode(String account, String type, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.REGISTER_MESSAGE_VERIFY_CODE, BaseDataResult.class);
        requestBody.addParameter("account", account);
        requestBody.addParameter("type", type);
        requestBody.addParameter("curTime", TimeUtils.getCurrentTime());
        requestBody.addParameter("sign", MD5.digest(account + "blink" + String.valueOf(TimeUtils.getCurrentTime() / 600L))); //防止脚本自动登录
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 请求获取支付短信验证码
     */
    public static void getRegisterVerifyCode(String account, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.REGISTER_MESSAGE_VERIFY_CODE, BaseDataResult.class);
        requestBody.addParameter("account", account);
        requestBody.addParameter("curTime", TimeUtils.getCurrentTime());
        requestBody.addParameter("sign", MD5.digest(account + "blink" + String.valueOf(TimeUtils.getCurrentTime() / 600L))); //防止脚本自动登录
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 获取邮箱验证码接口
     */
    public static void getEmailVerifyCode(String account, String type, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.GET_EMAIL_VERIFY_CODE, BaseDataResult.class);
        requestBody.addParameter("account", account);
        requestBody.addParameter("type", type);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 获取邮箱验证码接口
     */
    public static void getEmailVerifyCode(String account, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.GET_EMAIL_VERIFY_CODE, BaseDataResult.class);
        requestBody.addParameter("account", account);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 获取提币账单
     *
     * @param beginTime 2018-02-14
     */
    public static void getWithdrawBill(String beginTime, String endTime, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.GET_WITHDRAW_BILL, WithdrawBillResult.class);
        requestBody.addParameter("beginTime", beginTime);
        requestBody.addParameter("endTime", endTime);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 查询红包是否已经被领取
     * 本接口以及合并到下面的查询红包状态接口
     */
    public static void queryRedPacketReceiveded(String toAccount, String redFlag, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.QUERY_RED_PACKET_RECEIVEDED, BaseDataResult.class);
        requestBody.addParameter("toAccount", toAccount);
        requestBody.addParameter("redFlag", redFlag);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 查询红包状态
     */
    public static void queryRedPacketEnabled(String fromAccount, String redFlag, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.QUERY_RED_PACKET_ENABLED, QueryRedPacketStatusResult.class);
        requestBody.addParameter("fromAccount", fromAccount);
        requestBody.addParameter("redFlag", redFlag);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 查询收到红包列表
     */
    public static void queryRedPacketReceivedList(HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.RECEIVED_RED_PACKET_LIST, ReceivedRedPacketListResult.class);
        requestBody.addParameter("access-token", Global.getAccessToken());
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 查询发出红包列表
     */
    public static void querySendedPacketList(HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.SENDED_RED_PACKET_LIST, SendedRedPacketListResult.class);
        requestBody.addParameter("access-token", Global.getAccessToken());
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 查询提币额度
     */
    public static void queryWithdrawAmount(int currencyId, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.QUERY_WITHDRAW_AVAILABLE_AMOUNT, QueryWithdrawAmountResult.class);
        requestBody.addParameter("currencyId", currencyId);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 查询提币额度
     */
    public static void queryGroup(String keyWord, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.QUERY_GROUP, GroupQueryResult.class);
        requestBody.addParameter("keyword", keyWord);
        HttpRequestLauncher.execute(requestBody, listener);
    }


    /**
     * 申请加入群组
     */
    public static void applyJoinGroup(long groupId, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.APPLY_JOIN_GROUP, BaseDataResult.class);
        requestBody.addParameter("groupId", groupId);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 同意加入群组
     */
    public static void agreeJoinGroup(long groupId, String applyAccount, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.AGREE_JOIN_GROUP, BaseDataResult.class);
        requestBody.addParameter("groupId", groupId);
        requestBody.addParameter("applyAccount", applyAccount);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 修改个人资料
     */
    public static void modifyPersonInfo(String modifyType, String modifyContent, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.MODIFY_PERSON_INFO, ModifyPersonInfoResult.class);
        requestBody.addParameter("modifyType", modifyType);
        requestBody.addParameter("modifyContent", modifyContent);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 查询是否是朋友关系
     */
    public static void checkIsFriend(String friendAccount, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.QUERY_IS_FRIEND, BaseDataResult.class);
        requestBody.addParameter("friendAccount", friendAccount);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 设置备注
     */
    public static void setRemark(String friendAccount, String name, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.SET_REMARK, BaseDataResult.class);
        requestBody.addParameter("friendAccount", friendAccount);
        requestBody.addParameter("name", name);
        HttpRequestLauncher.execute(requestBody, listener);
    }


    /**
     * 查询个人资料
     */
    public static void queryPersonInfo(String findAccount, HttpRequestListener<BasePersonInfoResult> listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.QUERY_PERSON_INFO, BasePersonInfoResult.class);
        requestBody.addParameter("findAccount", findAccount);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 查询我的群组资料
     */
    public static void queryPersonGroup(HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.GET, URLConstant.QUERY_PERSON_GROUP, QueryMineGroupResult.class);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 群禁言
     *
     * @param bannedType 0 不禁言,1禁言
     * @param groupId    群id
     */
    public static void setGroupBanned(long groupId, int bannedType, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.GROUP_BANNED, BaseDataResult.class);
        requestBody.addParameter("id", groupId);
        requestBody.addParameter("banned", bannedType);
        HttpRequestLauncher.execute(requestBody, listener);
    }


    /**
     * 群设置不可查看资料，不可互加好友
     *
     * @param memberAble 0是可以加  1是不可以加
     * @param groupId    群id
     */
    public static void setGroupCheckMenberInfo(long groupId, int memberAble, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.SET_NOT_CHECK_INFO, BaseDataResult.class);
        requestBody.addParameter("id", groupId);
        requestBody.addParameter("memberAble", memberAble);
        HttpRequestLauncher.execute(requestBody, listener);
    }


    /**
     * 群设置不可查看资料，不可互加好友
     *
     * @param memberAccount
     * @param groupId       群id
     * @param host          0是普通，1是群主，2是管理员。
     */
    public static void setGroupManager(long groupId, String memberAccount, String host, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.SET_GROUP_MANAGER, BaseDataResult.class);
        requestBody.addParameter("groupId", groupId);
        requestBody.addParameter("memberAccount", memberAccount);
        requestBody.addParameter("host", host);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 登录日志收集功能
     */
    public static void setGroupCheckMenberInfo(String device, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.LOGIN_LOG_COLLECT, BaseDataResult.class);
        requestBody.addParameter("device", device);
        requestBody.addParameter("loginType", 1);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 找回密码
     */
    public static void findPassword(String account, String newPassword, String vertcode, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.FIND_PASSWORD_REQUEST, BaseDataResult.class);
        requestBody.addParameter("newPassword", MD5.digest(newPassword + "blink"));
        requestBody.addParameter("account", account);
        requestBody.addParameter("vertcode", vertcode);
        HttpRequestLauncher.execute(requestBody, listener);
    }


    /**
     * 获取音视频通话token
     */
    public static void requestRoomToken(String roomName, String userId, String permission, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.GET_VIDEO_ROOM_TOKEN, BaseDataResult.class);
        requestBody.addParameter("appId", BuildConfig.QINIU_APP_ID);
        requestBody.addParameter("roomName", roomName);
        requestBody.addParameter("userId", userId);
        requestBody.addParameter("expireAt", TimeUtils.getCurrentTime() + 60 * 60 * 24);
        requestBody.addParameter("permission", permission);

        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 查询附近的人
     *
     * @param gender 2是全部，1是男，0是女
     */
    public static void checkNearlyPeople(String account, int gender, String lng, String lat, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.GET_NEARLY_PEOPLE_LIST, NearlyPeopleResult.class);
        requestBody.addParameter("account", account);
        requestBody.addParameter("gender", gender);
        requestBody.addParameter("lng", lng);
        requestBody.addParameter("lat", lat);
        HttpRequestLauncher.execute(requestBody, listener);
    }


    /**
     * 查询币种
     */
    public static void queryCoin(String account, String address, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.QUERY_COIN_ADDRESS, CoinSearchResult.class);
        requestBody.addParameter("account", account);
        requestBody.addParameter("address", address);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 增加币种
     */
    public static void addCoin(String account, String address, String tokenName, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.ADD_COIN, BaseResult.class);
        requestBody.addParameter("account", account);
        requestBody.addParameter("address", address);
        requestBody.addParameter("tokenName", tokenName);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 转账
     * account/transferOfAccount?userAccount=15802674030&currencyId=22&sendMoney=10&remark=恭喜发财&redType=5&tradePassword=4579a0a67759cd28a5a8176691604757
     */
    public static void transferCoin(String userAccount, String currencyId, String sendMoney, String remark, String redType, String tradePassword, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.COIN_TRANSFER, CoinTransferResult.class);
        requestBody.addParameter("userAccount", userAccount);
        requestBody.addParameter("currencyId", currencyId);
        requestBody.addParameter("sendMoney", sendMoney);
        requestBody.addParameter("remark", remark);
        requestBody.addParameter("redType", redType);
        requestBody.addParameter("tradePassword", MD5.digest(tradePassword + "blink"));
        HttpRequestLauncher.execute(requestBody, listener);
    }


    /**
     * 接收转账
     * userAccount=15874986188&currencyId=22&redFlag=blink1559290871758199&fromAccount=15802674030
     */
    public static void receiveTransfer(String userAccount, String currencyId, String redFlag, String fromAccount, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.RECEIVE_COIN_TRANSFER, CoinReceiveResult.class);
        requestBody.addParameter("userAccount", userAccount);
        requestBody.addParameter("currencyId", currencyId);
        requestBody.addParameter("redFlag", redFlag);
        requestBody.addParameter("fromAccount", fromAccount);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 查询转账状态
     *
     * @param fromAccount 转账的发起者
     * @param userAccount 当前查询的用户
     */
    public static void queryCoinTransferEnabled(String userAccount, String fromAccount, String redFlag, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.QUERY_COIN_TRANSFER_STATUS, QueryCoinTransferStatusResult.class);
        requestBody.addParameter("userAccount", userAccount);
        requestBody.addParameter("fromAccount", fromAccount);
        requestBody.addParameter("redFlag", redFlag);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 获取个人邀请详情
     */
    public static void getInviteInfo(HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.GET_INVITE_INFO, MineInviteInfoResult.class);
        requestBody.addParameter("redFlag", 1);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 获取我的邀请码
     */
    public static void getMineInviteCode(HttpRequestListener<MineInviteCodeResult> listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.GET_MINE_INVITE_CODE, MineInviteCodeResult.class);
        requestBody.addParameter("redFlag", 1);
        HttpRequestLauncher.execute(requestBody, listener);
    }
}
