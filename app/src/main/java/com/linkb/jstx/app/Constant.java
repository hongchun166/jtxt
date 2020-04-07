
package com.linkb.jstx.app;


import android.os.Environment;

public interface Constant {

    int CIM_SERVER_PORT = 23456;

    //String SERVER_URL = "http://192.168.1.106:9090";
    String SERVER_URL = "http://";
    int DEF_PAGE_INDEX = 0;

    long MOMENT_PAGE_SIZE = 10;

    long MESSAGE_PAGE_SIZE = 20;

    String SYSTEM = "system";

    int EMOTION_FACE_SIZE = 24;

    String CHAT_OTHRES_ID = "othersId";

    String GROUP_ID = "group_id";

    String CHAT_OTHRES_NAME = "othersName";

    String NEED_RECEIPT = "NEED_RECEIPT";

    int RESULT_ZOOM = 11;
    int RESULT_CAMERA = 1920;


    //对话页面消息时间显示间隔
    int CHATTING_TIME_SPACE =  2 * 60 * 1000;

    String SYSTEM_LVXIN_DIR = Environment.getExternalStorageDirectory() + "/DCIM/lvxin";

    int MAX_FILE_LIMIT = (int) (20 * org.apache.commons.io.FileUtils.ONE_MB);


    int MAX_GRID_MEMBER = 45;

    int MAX_MOMENT_PHOTO_SIZE = 9;

    interface ReturnCode {
        /*
         * api访问成功
         */
        String CODE_200 = "200";

        /*
         * 请求的参数内容不正确或者参数缺失
         */
        String CODE_400 = "400";


        /*
         * 访问接口 token无效
         */
        String CODE_401 = "401";

        /* 内容被禁止使用
         */
        String CODE_402 = "402";
        /*
         * 获得没有权限。或者密码错误
         */
        String CODE_403 = "403";

        /*
         * 获得文件或者数据不存在
         */
        String CODE_404 = "404";


        /*
         * 上传文件过大
         */
        String CODE_413 = "413";

        /*
         * 资源状态被锁定，不能再做修改
         */
        String CODE_423 = "423";



    }

    interface MessageAction {
    //0.用户之间的普通消息  1.用户发送的群组消息 2.系统向用户发送的普通消息 3.群里用户发送的 消息，4阅读即焚消息
    // 消息格式 ：0:文字1：图片，2：语音,3 文件  4:地图  8:视频

        //用户之间的普通消息
        String ACTION_0 = "0";

        /** 群里面用户发送的普通消息
        * */
        String ACTION_1 = "1";

        //系统向用户发送的普通消息
        String ACTION_2 = "2";

        //群里用户发送的  消息
        String ACTION_3 = "3";



        /** 用户之间的音频发送的消息
         * */
        String ACTION_4 = "4";

        /** 用户之间的视频发送的消息
         * */
        String ACTION_5 = "5";

        /** 用户拒绝对方的视频的消息
         * */
        String ACTION_6 = "6";
        /** 用户取消的音视频的消息
         * */
        String ACTION_7 = "7";
        /**
         * 阅读即焚消息
         */
        String ACTION_ReadDelete = "10";
        /**
         * 好友申请，20-04-08 00:17
         */
        String ACTION_FrienApply= "11";
        /**
         * ********************************************1开头统一为聊天消息**********************************************************
         */

        //系统定制消息---进群请求
        String ACTION_102 = "102";

        //系统定制消息---同意进群请求
        String ACTION_103 = "103";

        //系统定制消息---群解散消息
        String ACTION_104 = "104";

        //系统定制消息---邀请入群请求
        String ACTION_105 = "105";

        //系统定制消息---同意邀请入群请求
        String ACTION_106 = "106";

        //系统定制消息---被剔除群
        String ACTION_107 = "107";

        //系统定制消息---消息被阅读
        String ACTION_108 = "108";

        //系统定制消息---好友替换了头像
        String ACTION_110 = "110";

        //系统定制消息---好友修改了名称或者签名
        String ACTION_111 = "111";

        //系统定制消息---用户退出了群
        String ACTION_112 = "112";

        //系统定制消息---用户加入了群
        String ACTION_113 = "113";

        // 系统定制消息---群名称被修改
        String ACTION_114 = "114";

        // 系统定制消息---群公告被修改
        String ACTION_115 = "115";

        // 系统定制消息---群logo被修改
        String ACTION_116 = "116";
        // 好友申请
        String ACTION_117 = "117";
        // 已经同意好友
        String ACTION_118 = "118";
        /**
         * ********************************************2开头统一为公众号消息**********************************************************
         */
        //系统定制消息---用户向公众号发消息
        String ACTION_200 = "200";

        //系统定制消息---公众号向用户回复的消息
        String ACTION_201 = "201";

        //系统定制消息---公众号向用户群发消息
        String ACTION_202 = "202";

        //系统定制消息---公众号信息更新
        String ACTION_203 = "203";

        //系统定制消息---公众号菜单信息更新
        String ACTION_204 = "204";

        //系统定制消息---公众号LOGO更新
        String ACTION_205 = "205";


        /**
         * ********************************************2开头统一为小程序消息**********************************************************
         */
        // 系统定制消息---后台设置小程序Logo
        String ACTION_300 = "300";

        // 系统定制消息---后台新增或者修改了小程序
        String ACTION_301 = "301";

        // 系统定制消息---后台删除了小程序
        String ACTION_302 = "302";

        /**
         * ********************************************4开头统一为系统控制消息**********************************************************
         */
        //系统定制消息---强制下线消息
        String ACTION_444 = "444";

        //系统定制消息---聊天背景图片更新
        String ACTION_400 = "400";

        //系统定制消息---我的页面背景图片更新
        String ACTION_401 = "401";

        //系统定制消息---用户详情页背景图片更新
        String ACTION_402 = "402";

        /**
         * ********************************************8开头统一为圈子动态消息**********************************************************
         */
        //系统定制消息---好友新动态消息
        String ACTION_500 = "500";

        //系统定制消息---好友新动态评论消息
        String ACTION_501 = "501";

        //系统定制消息---好友新动态评论回复评论消息
        String ACTION_502 = "502";

        //系统定制消息---好友删除新动态
        String ACTION_503 = "503";

        //系统定制消息---好友删除评论或者取消点赞
        String ACTION_504 = "504";

        /**
         * ********************************************9开头统一为动作消息**********************************************************
         */
        //系统定制消息---好友下线消息
        String ACTION_900 = "900";

        //系统定制消息---好友上线消息
        String ACTION_901 = "901";

        //系统定制消息---更新用户数据
        String ACTION_998 = "998";

        //系统定制消息---强制下线消息
        String ACTION_999 = "999";

    }


    interface CIMRequestKey {

        //用户修改了名称或签名，向服务器发请求，通知其他好友及时更新
        String CLIENT_MODIFY_LOGO = "client_modify_logo";

        //用户修改了头像，向服务器发请求推送其他好友及时更新
        String CLIENT_MODIFY_PROFILE = "client_modify_profile";
    }

    interface MessageFormat {

        //文字
        String FORMAT_TEXT = "0";

        //图片
        String FORMAT_IMAGE = "1";

        //语音
        String FORMAT_VOICE = "2";


        //文件
        String FORMAT_FILE = "3";

        //地图
        String FORMAT_MAP = "4";


        //链接
        String FORMAT_LINK = "5";

        //多条链接
        String FORMAT_LINKLIST = "6";

        //文字面板
        String FORMAT_TEXTPANEL = "7";

        //视频
        String FORMAT_VIDEO = "8";

        //红包
        String FORMAT_RED_PACKET = "9";

        //音频连接
        String FORMAT_VOICE_CONNECT = "10";

        //视频连接
        String FORMAT_VIDEO_CONNECT = "11";

        /** 发送名片
        * */
        String FORMAT_SEND_CARDS = "12";

        /** 转账
         * */
        String FORMAT_COIN_TRANSFER = "13";


    }

    interface MessageStatus {

        //正在发送
        String STATUS_SENDING = "-2";

        //还未发送
        String STATUS_NO_SEND = "-1";

        //发送失败
        String STATUS_SEND_FAILURE = "-3";

        //延迟发送
        String STATUS_DELAY_SEND = "-4";

        String STATUS_OTHERS_READ = "9";//别人已经阅读


        //消息已经发送
        String STATUS_SEND = "1";
    }

    interface Action {
        String ACTION_DELETE_MOMENT = "com.farsunset.lvxin.DELETE_MOMENT";
        String ACTION_REFRESH_MOMENT = "com.farsunset.lvxin.REFRESH_MOMENT";

        String ACTION_WINDOW_REFRESH_MESSAGE = "com.farsunset.lvxin.WINDOW_REFRESH_MESSAGE";
        String ACTION_RECENT_APPEND_CHAT = "com.farsunset.lvxin.RECENT_APPEND_CHAT";
        String ACTION_RECENT_DELETE_CHAT = "com.farsunset.lvxin.RECENT_DELETE_CHAT";
        String ACTION_RECENT_REFRESH_CHAT = "com.farsunset.lvxin.RECENT_REFRESH_CHAT";
        String ACTION_RECENT_REFRESH_LOGO = "com.farsunset.lvxin.RECENT_REFRESH_LOGO";
        String ACTION_UPLOAD_PROGRESS = "com.farsunset.lvxin.UPLOAD_PROGRESS";
        String ACTION_RELOAD_CONTACTS = "com.farsunset.lvxin.RELOAD_CONTACTS";
        String ACTION_LOGO_CHANGED = "com.farsunset.lvxin.ACTION_LOGO_CHANGED";

        /** 删除好友删除聊天对话框
        * */
        String ACTION_DELETE_FRIEND = "com.farsunset.lvxin.ACTION_DELETE_FRIEND ";
        /** 好友备注变更了
        * */
        String ACTION_FRIEND_MARK_CHANGE = "com.farsunset.lvxin.ACTION_FRIEND_MARK_CHANGE ";

        String ACTION_FINISH_ACTIVITY = "com.farsunset.lvxin.ACTION_FINISH_ACTIVITY";
        String ACTION_MICROSERVER_MENU_INVOKED = "com.farsunset.lvxin.ACTION_MICROSERVER_MENU_INVOKED";
        String ACTION_THEME_CHANGED = "com.farsunset.lvxin.ACTION_THEME_CHANGED";
        String ACTION_GROUP_DELETE = "com.farsunset.lvxin.ACTION_GROUP_DELETE";
        String ACTION_GROUP_ADD = "com.farsunset.lvxin.ACTION_GROUP_ADD";
        String ACTION_GROUP_UPDATE = "com.farsunset.lvxin.ACTION_GROUP_UPDATE";
        String ACTION_GROUP_REFRESH = "com.farsunset.lvxin.ACTION_GROUP_REFRESH";
        String ACTION_ICON_LONGCLICKED = "com.farsunset.lvxin.ACTION_ICON_LONGCLICKED";

        String ACTION_GROUP_BANNED = "com.farsunset.lvxin.ACTION_GROUP_BANNED";

        String ACTION_GROUP_UNABLE_CHECK_INFO = "com.farsunset.lvxin.ACTION_GROUP_UNABLE_CHECK_INFO";

        String ACTION_MULTIPLE_PHOTO_SELECTOR= "com.farsunset.lvxin.ACTION_MULTIPLE_PHOTO_SELECTOR";

        /** 被对方拒绝了音视频通话
        * */
        String ACTION_OPPOSITE_REJECT_VIDEO_CONNECT = "com.farsunset.lvxin.ACTION_OPPOSITE_REJECT_VIDEO_CONNECT";
        /** 对方取消了音视频通话
         * */
        String ACTION_OPPOSITE_CANCEL_VIDEO_CONNECT = "com.farsunset.lvxin.ACTION_OPPOSITE_CANCEL_VIDEO_CONNECT";
    }

    /**  发红包种类
     *  1 表示普通红包, 2表示普通群发红包   , 3表示拼手气群发红包, 4表示转账
     * */
    interface RedPacketType {
        String RED_PACKET_TYPE = "redPacketType";

        int COMMON_RED_PACKET = 1;
        int COMMON_GROUP_RED_PACKET = 2;
        int COMMON_GROUP_LURKEY_RED_PACKET = 3;
    }

    /** 二维码格式统一为 type#value#value 例如：1#186 ， 2#17602060788#camss
     *  其中 群组type为1， 个人账号为2
     *  个人账号第一个值为账号，第二个值为名称
     * */
    interface QrCodeFormater {
        String QR_CODE_SPLIT = "#";
        String GROUP_QR_CODE = "1";
        String PERSON_QR_CODE = "2";
    }

    /** 二维码扫码种类
     *  0 表示钱包扫码
     *  1 表示群或者添加好友
    * */
    interface QrCodeType {
        String QR_CODE_TYPE = "QR_CODE_TYPE";
        String QR_CODE_CONTENT = "QR_CODE_CONTENT";

        int WALLET_QRCODE = 0;
        int GROUP_OR_PERSON_QRCODE = 1;
//        int PERSON_QRCODE = 2;
    }

}
