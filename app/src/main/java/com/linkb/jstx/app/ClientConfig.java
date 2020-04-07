
package com.linkb.jstx.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.linkb.jstx.database.ConfigRepository;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class ClientConfig {

    private static volatile ClientConfig instance;

    private Locale systemCurrentLocal = Locale.ENGLISH;

    public static ClientConfig getInstance(){
        if (instance == null){
            synchronized (ClientConfig.class){
                if (instance == null){
                    instance = new ClientConfig();
                }
            }
        }
        return  instance;
    }

    private static final String MODEL_KEY = "CLIENT_CONFIG";
    private static final String KEY_MESSAGE_NOTIFY_SWITCH = "KEY_MESSAGE_NOTIFY_SWITCH";
    private static final String KEY_MESSAGE_SOUND_SWITCH = "KEY_MESSAGE_SOUND_SWITCH";
    private static final String KEY_MESSAGE_DISTURB_SWITCH = "KEY_MESSAGE_DISTURB_SWITCH";
    private static final String KEY_SHAKE_SOUND_SWITCH = "KEY_MESSAGE_SOUND_SWITCH";
    private static final String KEY_CURRNET_REGION = "KEY_CURRNET_REGION";

    private static final String KEY_MESSAGE_RECEIPT_SWITCH = "KEY_MESSAGE_RECEIPT_SWITCH";
    private static final String KEY_MESSAGE_STATUS_SWITCH = "KEY_MESSAGE_STATUS_SWITCH";

    private static final String KEY_SETTING_SERVER_PATH = "KEY_SETTING_SERVER_PATH";
    private static final String KEY_SETTING_SERVER_PORT = "KEY_SETTING_SERVER_PORT";
    private static final String KEY_SETTING_SERVER_HOST = "KEY_SETTING_SERVER_HOST";

    private static final String ENABLE_FIRST_LOGIN = "ENABLE_FIRST_LOGIN";

    private static final String KEY_SOFTKEYBORD_HEIGHT = "KEY_SOFTKEYBORD_HEIGHT";


    private static final String KEY_IGNORE_GROUP_MESSAGE = "IGNORE_GROUP_MESSAGE_%1$s";

    private static final String KEY_NEW_GROUP_MESSAGE_COUNT = "NEW_GROUP_MESSAGE_COUNT_%1$s";

    private static final String KEY_HAS_AT_GROUP_MESSAGE = "NEW_HAS_AT_GROUP_MESSAGE_%1$s";

    private static final String KEY_IS_AT_GROUP_MESSAGE = "NEW_IS_AT_GROUP_MESSAGE_%1$s";

    /** 一次性Flag， 通讯录获取好友的时候调用
    * */
    private static final String KEY_ONE_TIME_MAPPING_FRIEND = "KEY_ONE_TIME_MAPPING_FRIEND";

    /** 保存语言名称
     * */
    private static final String KEY_LANGUAGE_SELECT_NAME = "KEY_LANGUAGE_SELECT_NAME";

    /** 保存语言种类
     * */
    private static final String KEY_LANGUAGE_SELECT_TYPE = "KEY_LANGUAGE_SELECT_TYPE";

    private static final String FRIEND_RELATION = "friend_Relation";
    private static final String FRIEND_RELATION_HAVE_NEW_APPLY= "FRIEND_RELATION_HAVE_NEW_APPLY";

    public ClientConfig() {
    }

    public static void setOneTimeFlagMapFriend(boolean flag) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_ONE_TIME_MAPPING_FRIEND, flag).apply();
    }

    public static boolean getOneTimeFlagMapFriend() {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_ONE_TIME_MAPPING_FRIEND, false);
    }

    public static void setBooleanConfig(String key ,boolean flag) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, flag).apply();
    }

    public static boolean getBooleanConfig(String key,boolean defValue) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    public static void setMessageDisturbEnable(boolean flag) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_MESSAGE_DISTURB_SWITCH, flag).apply();
    }

    public static boolean getMessageDisturbEnable() {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_MESSAGE_DISTURB_SWITCH, true);
    }

    public static void setEnableFirstLogin(boolean flag) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putBoolean(ENABLE_FIRST_LOGIN, flag).apply();
    }

    public static boolean getEnableFirstLogin() {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getBoolean(ENABLE_FIRST_LOGIN, true);
    }

    public static void setMessageNotifyEnable(boolean flag) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_MESSAGE_NOTIFY_SWITCH, flag).apply();
    }


    public static boolean getMessageNotifyEnable() {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_MESSAGE_NOTIFY_SWITCH, true);
    }

    public static void setMessageSoundEnable(boolean flag) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_MESSAGE_SOUND_SWITCH, flag).apply();
    }


    public static boolean getMessageSoundEnable() {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_MESSAGE_SOUND_SWITCH, true);
    }


    public static void setShakeSoundEnable(boolean flag) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_SHAKE_SOUND_SWITCH, flag).apply();
    }

    public static boolean getShakeSoundEnable() {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_SHAKE_SOUND_SWITCH, true);
    }

    public static String getCurrentRegion() {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getString(KEY_CURRNET_REGION, null);
    }

    public static void setCurrentRegion(String region) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_CURRNET_REGION, region).apply();
    }

    public static void setMessageReceiptEnable(boolean flag) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_MESSAGE_RECEIPT_SWITCH, flag).apply();
    }

    public static boolean getMessageReceiptEnable() {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_MESSAGE_RECEIPT_SWITCH, true);
    }

    public static void setMessageStatusVisable(boolean flag) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_MESSAGE_STATUS_SWITCH, flag).apply();
    }

    public static boolean getMessageStatusVisable() {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_MESSAGE_STATUS_SWITCH, true);
    }

    public static String getServerPath() {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getString(KEY_SETTING_SERVER_PATH, Constant.SERVER_URL);
    }

    public static void setServerPath(String url) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_SETTING_SERVER_PATH, url).apply();
    }

    public static int getServerCIMPort() {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getInt(KEY_SETTING_SERVER_PORT, Constant.CIM_SERVER_PORT);
    }

    public static void setServerCIMPort(int port) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putInt(KEY_SETTING_SERVER_PORT, port).apply();
    }

    public static String getServerHost() {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        String host = sp.getString(KEY_SETTING_SERVER_HOST, null);
        if (host == null) {
            try {
                host = new URL(Constant.SERVER_URL).getHost();
            } catch (MalformedURLException e) {
            }
        }
        return host;
    }

    public static void setServerHost(String url) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_SETTING_SERVER_HOST, url).apply();
    }

    public static int getKeybordHeight(final int defaultHeight) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getInt(KEY_SOFTKEYBORD_HEIGHT, defaultHeight);
    }

    public static void saveKeybordHeight(int height) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putInt(KEY_SOFTKEYBORD_HEIGHT, height).apply();
    }


    /**
     * 是否屏蔽了某个群消息
     *
     * @return
     */
    public static boolean isIgnoredGroupMessage(Object groupId) {
        String value = ConfigRepository.queryValue(String.format(KEY_IGNORE_GROUP_MESSAGE, groupId));
        return value != null && "1".equals(value);
    }

    public static void saveIgnoredGroupMessage(long groupId, boolean isIgnored) {
        if (isIgnored) {
            ConfigRepository.add(String.format(KEY_IGNORE_GROUP_MESSAGE, groupId), "1");
        } else {
            ConfigRepository.delete(String.format(KEY_IGNORE_GROUP_MESSAGE, groupId));
        }
    }

    public static void addNewGroupMessageCount(long groupId) {
        String value = ConfigRepository.queryValue(String.format(KEY_NEW_GROUP_MESSAGE_COUNT, groupId));
        int count  = 0 ;
        if (!TextUtils.isEmpty(value)){
            try{count = Integer.parseInt(value);}catch (Exception e){ }
        }
        ConfigRepository.add(String.format(KEY_NEW_GROUP_MESSAGE_COUNT, groupId),String.valueOf(count+1));
    }
    public static int getNewGroupMessageCount(long groupId) {
        String value = ConfigRepository.queryValue(String.format(KEY_NEW_GROUP_MESSAGE_COUNT, groupId));
        int count  = 0 ;
        if (!TextUtils.isEmpty(value)){
            try{count = Integer.parseInt(value);}catch (Exception e){ }
        }
        return count;
    }
    public static void clearNewGroupMessageCount(long groupId) {
        ConfigRepository.delete(String.format(KEY_NEW_GROUP_MESSAGE_COUNT, groupId));
    }


    public static boolean isAtMeGroupMessage(long mid) {
        String value = ConfigRepository.queryValue(String.format(KEY_IS_AT_GROUP_MESSAGE, mid));
        return value != null;
    }

    public static void saveAtMeGroupMessageId(long mid) {
        ConfigRepository.add(String.format(KEY_IS_AT_GROUP_MESSAGE, mid),String.valueOf(1));
    }

    public static void saveHasAtMeGroupMessage(long groupId) {
        ConfigRepository.add(String.format(KEY_HAS_AT_GROUP_MESSAGE, groupId),String.valueOf(1));
    }
    public static void clearAtMeGroupMessage(long groupId) {
        ConfigRepository.delete(String.format(KEY_HAS_AT_GROUP_MESSAGE, groupId));
    }

    public static boolean hasAtMeGroupMessage(long groupId) {
        String value = ConfigRepository.queryValue(String.format(KEY_HAS_AT_GROUP_MESSAGE, groupId));
        return value != null;
    }

    public static String getLanguageName() {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getString(KEY_LANGUAGE_SELECT_NAME, null);
    }

    public static void setLanguageName(String languageName) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_LANGUAGE_SELECT_NAME, languageName).apply();
    }

    public static int getLanguageType(Context context) {
        SharedPreferences sp = context.getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getInt(KEY_LANGUAGE_SELECT_TYPE, 0);
    }

    public static void setLanguageType(int languageType) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putInt(KEY_LANGUAGE_SELECT_TYPE, languageType).apply();
    }

    public static void setFriendRelation(String loginUid,String account,int state) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putInt(FRIEND_RELATION+"_"+loginUid+"_"+account, state).apply();
    }
    public static int getFriendRelation(String loginUid,String account) {
        SharedPreferences sp =  LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getInt(FRIEND_RELATION+"_"+loginUid+"_"+account, 0);
    }

    public static void setFriendRelationNewApply(String loginUid,boolean hasNewApply) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putBoolean(FRIEND_RELATION_HAVE_NEW_APPLY+"_"+loginUid, hasNewApply).apply();
    }
    public static boolean getFriendRelationNewApply(String loginUid) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getBoolean(FRIEND_RELATION_HAVE_NEW_APPLY+"_"+loginUid, false);
    }
    public Locale getSystemCurrentLocal() {
        return systemCurrentLocal;
    }

    public void setSystemCurrentLocal(Locale local) {
        systemCurrentLocal = local;
    }
}
