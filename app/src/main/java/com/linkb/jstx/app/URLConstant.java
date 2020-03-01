
package com.linkb.jstx.app;

import com.linkb.BuildConfig;

public class URLConstant {

    public static String API_URL;

    //用户登录
    public static String USER_LOGIN_URL;
    //用户退出
    public static String USER_LOGOUT_URL;

    //公众号详情
    public static String MICROAPP_LIST_URL;

    public static String MICROSERVER_SEARCH_URL;

    //查看所有公众号列表
    public static String MICROSERVER_LIST_URL;

    //关注公众号
    public static String SUBSCRIBE_OPERATION_URL;
    //获取公众号菜单
    public static String MICROSERVER_MENU_URL;
    //修改密码
    public static String USER_PASSWORD_URL;
    //修改支付密码
    public static String MODIFY_APPLY_PASSWORD_URL;
    //群组详情
    public static String GROUP_OPERATION_URL;

    public static String GROUP_SEARCH_URL;

    //加入用户到群组
    public static String GROUP_QUIT_URL;

    //加入用户到群组
    public static String GROUP_MEMBER_URL;
    //将用户移除群组
    public static String GROUP_MEMBER_BATCH_URL;
    //更新群组
    //解散群组
    public static String GROUP_DISBAND_URL;

    public static String GROUP_SET_LOGO_URL;
    public static String GROUP_SET_NAME_URL;
    /**
     * 修改群简介
     */
    public static String GROUP_SET_PROFILE_URL;
    /**
     * 修改群公告
     */
    public static String GROUP_SET_SUMMARY_URL;


    /**
     * 获取所有群组列表
     */
    public static String GET_ALL_GROUP;

    //群组成员列表
    public static String GROUP_MEMBER_LIST_URL;
    //邀请用户加入群组
    public static String GROUP_MEMBER_INVITE_URL;
    //发送消息
    public static String MESSAGE_SEND_URL;
    //批量消息接收回执
    public static String MESSAGE_BATCH_RECEIVE_URL;
    //单个消息接收回执
    public static String MESSAGE_RECEIVE_URL;
    //阅读消息接收回执
    public static String MESSAGE_READ_URL;
    //转发消息
    public static String MESSAGE_FORWARD_URL;
    //撤回消息
    public static String MESSAGE_REVOKE_URL;
    //获取批量离线消息
    public static String MESSAGE_OFFLINELIST_URL;
    //阅读消息接收回执
    public static String MOMENT_PUBLISH_URL;

    public static String MOMENT_ME_LIST_URL;

    //获取空间内容列表
    public static String MOMENT_TIMELINE_URL;
    //获取别人的空间内容列表
    public static String MOMENT_OTHER_LIST_URL;
    //获取文章内容详情
    public static String MOMENT_OPERATION_URL;
    //发表评论
    public static String COMMENT_OPERATION_URL;
    //发表点赞
    public static String COMMENT_PRAISE_URL;
    //删除评论，点赞
    public static String COMMENT_DELETE_URL;
    //获取系统配置
    public static String CHECK_NEW_VERSION_URL;
    //用户反馈
    public static String FEEDBACK_PUBLISH_URL;

    public static String HOST_DISPENSE_URL;

    //同步后台基础数据
    public static String GET_BASE_DATA_URL;
    //文件路径
    //上传文件
    public static String FILE_UPLOAD_URL;

    public static String FILE_DOWNLOAD_URL;

    public static String FILE_OSS_DOWNLOAD_URL;

    //db文件路径
    public static String USER_DATABASE_FILE_URL;
    //db文件路径
    public static String ORG_DATABASE_FILE_URL;

    //获取我的空间权限设置
    public static String MOMENT_RULE_LIST_URL;
    //保持空间权限设置
    public static String MOMENT_RULE_URL;

    public static String MESSAGE_READ_NOTIFY_URL;

    public static String TAG_MEMBER_URL;

    public static String TAG_URL;

    public static String TAG_DELETE_URL;

    /**
     * 获取资讯列表
     */
    public static String GET_INFORMATION;
    /**
     * 获取新闻列表
     */
    public static String GET_NEWS_LIST;
    /**
     * 评论
     */
    public static String INFORMATIN_COMMENT;

    /**
     * 注册账号
     */
    public static String REGISTER_ACCOUNT;

    /**
     * 查询好友
     */
    public static String GET_FRIENDS_LIST;


    /**
     * 添加好友
     */
    public static String ADD_FRIEND;
    /**
     * 申请好友关系
     */
    public static String APPLY_FRIEND;
    /**
     * 申请好友关系
     */
    public static String ADD_FRIEND_APPLY_V2;
    /**
     * 同意、拒绝好友申请
     */
    public static String APPLY_FRIENDV_ANSWER_V2;
    /**
     * 删除好友
     */
    public static String APPLY_FRIENDV_DELETE_V2;
    /**
     * 好友申请列表
     */
    public static String LIST_FRIEND_APPLY_V2;
    /**
     * 我的好友列表
     */
    public static String LIST_MY_FRIENDS_V2;
    /**
     * 获取用户信息
     */
    public static String QUERY_USER_INFO_V2;
    /**
     * 删除好友
     */
    public static String DELETE_FRIEND;
    /**
     * 查询好友
     */
    public static String QUERY_FRIEND;

    /**
     * 查询账户资产余额
     */
    public static String QUERY_ASSETS_BALANCE;
     /**
     * 查询账户资产余额V2
     */
    public static String QUERY_ASSETS_BALANCEV2;
    /**
     * 查询当前货币的汇率
     */
    public static String QUERY_COIN_EXCHANGE_RATE;

    /**
     * 查询币种列表
     */
    public static String QUERY_CURRENCY_LIST;

    /**
     * 验证支付密码接口
     */
    public static String VERIFY_APPLY_PASSWORD;

    /**
     * 发送红包
     */
    public static String SEND_RED_PACKET;

    /**
     * 领取红包
     */
    public static String RECEIVED_RED_PACKET;

    /**
     * 领取红包成员查询
     */
    public static String RECEIVED_RED_PACKET_MENBER;

    /**
     * 充币获取钱包地址
     */
    public static String CHARGE_COIN_GET_ADDRESS;

    /**
     * 提取币
     */
    public static String WITHDRAW_COIN;

    /**
     * 支付短信验证码
     */
    public static String APPLY_MESSAGE_VERIFY_CODE;

    /**
     * 注册短信验证码
     */
    public static String REGISTER_MESSAGE_VERIFY_CODE;
    /**
     * 获取邮箱验证码
     */
    public static String GET_EMAIL_VERIFY_CODE;

    /**
     * 提币账单获取
     */
    public static String GET_WITHDRAW_BILL;

    /**
     * 查询红包是否已经领取
     */
    public static String QUERY_RED_PACKET_RECEIVEDED;

    /**
     * 查询红包是否可以领取
     */
    public static String QUERY_RED_PACKET_ENABLED;

    /**
     * 收到红包列表
     */
    public static String RECEIVED_RED_PACKET_LIST;

    /**
     * 发出的红包列表
     */
    public static String SENDED_RED_PACKET_LIST;
    /**
     * 查询提币的额度
     */
    public static String QUERY_WITHDRAW_AVAILABLE_AMOUNT;
    /**
     * 搜索群组（模糊查找）
     */
    public static String QUERY_GROUP;
    /**
     * 申请加入群组
     */
    public static String APPLY_JOIN_GROUP;
    /**
     * 同意加入群组
     */
    public static String AGREE_JOIN_GROUP;
    /**
     * 修改个人资料
     */
    public static String MODIFY_PERSON_INFO;

    /**
     * 查询是否是好友关系
     */
    public static String QUERY_IS_FRIEND;
    /**
     * 设置备注
     */
    public static String SET_REMARK;
    /**
     * 查询个人资料
     */
    public static String QUERY_PERSON_INFO;
    /**
     * 查询本人的群组列表
     */
    public static String QUERY_PERSON_GROUP;
    /**
     * 查询群组详情资料
     */
    public static String QUERY_GROUP_INFO;
    /**
     * 群禁言
     */
    public static String GROUP_BANNED;
    /**
     * 群设置不可互加好友
     */
    public static String SET_NOT_CHECK_INFO;
    /**
     * 设置群管理员
     */
    public static String SET_GROUP_MANAGER;
    /**
     * 收集接口， 进入程序的时候调用
     */
    public static String LOGIN_LOG_COLLECT;
    /**
     * 找回密码
     */
    public static String FIND_PASSWORD_REQUEST;
    /**
     * 获取发现模块的Banner图片
     */
    public static String GET_BANNER;
    /**
     * 获取音视频roomToken
     */
    public static String GET_VIDEO_ROOM_TOKEN;
    /**
     * 发送音视频消息
     */
    public static String SEND_VIDEO_CONNECT_MESSAGE;
    /**
     * 拒绝音视频消息
     */
    public static String REJECT_VIDEO_CONNECT_MESSAGE;
    /**
     * 上传崩溃日志
     */
    public static String UPLOAD_CRASH_LOG;
    /**
     * 附近的人
     */
    public static String GET_NEARLY_PEOPLE_LIST;

    /**
     * 查询币种地址
     */
    public static String QUERY_COIN_ADDRESS;

    /**
     * 添加币种
     */
    public static String ADD_COIN;

    /**
     * 转账
     */
    public static String COIN_TRANSFER;

    /**
     * 接收转账
     */
    public static String RECEIVE_COIN_TRANSFER;

    /**
     * 查询转账状态
     */
    public static String QUERY_COIN_TRANSFER_STATUS;
    /**
     * 获取个人邀请详情
     */
    public static String GET_INVITE_INFO;
    public static String GET_MINE_INVITE_CODE;


    public static String getAccountBalance;// 获取账户总余额
    public static String getMyCurrencyById;//查询我的钱包某个币种余额信息
    public static String listMyBalanceFlow;//查询钱包账户流水
    public static String listMyCurrency;//查询我的钱包列表

    public static String lisTags;//获取标签列表
    public static String listIndustry;//获取行业列表
    public static String updateUserInfo;//修改个人资料
    public static String findPersons;//找一找（人）
    public static String findGroups;//找一找（群）
    public static String checkInGroup;//检查自己是否加入了该群
    public static String checkFriend;//检查是否为好友

    public static String getMessageDestroySwith;// 获取阅读即焚开关状态
    public static String updateMessageDestroySwith;// 修改阅读即焚开关状态

    public static String updateMessageDestroyTime;// 设置阅读即焚时长
    public static String getMessageDestroyTime;// 获取阅读即焚时长

    public static String getActive;// 查询账号活跃度查询
    public static String listInvitePrizeDetail;//邀请奖励明细
    public static String  getRedBag;//抽红包(资讯)
    static {
        initialize();
        initializeV2();
    }
    public static void initializeV2(){
        ADD_FRIEND_APPLY_V2= API_URL + "userFriend/addFriendApply";
        APPLY_FRIENDV_ANSWER_V2=API_URL +"userFriend/agreeFriendApply";
        APPLY_FRIENDV_DELETE_V2=API_URL +"userFriend/delete";
        LIST_FRIEND_APPLY_V2=API_URL +"userFriend/listFriendApply";
        LIST_MY_FRIENDS_V2=API_URL +"userFriend/listMyFriends";

        QUERY_USER_INFO_V2=API_URL +"userFriend/query";

        getAccountBalance=API_URL +"currency/getAccountBalance";
        getMyCurrencyById=API_URL +"currency/getMyCurrencyById";
        listMyBalanceFlow=API_URL +"currency/listMyBalanceFlow";
        listMyCurrency=API_URL +"currency/listMyCurrency";
        QUERY_ASSETS_BALANCEV2 = API_URL + "currency/listMyCurrencyBalance";
        lisTags = API_URL + "/public/lisTags";
        listIndustry = API_URL + "/public/listIndustry";
        updateUserInfo=API_URL + "/user/updateUserInfo";
        findPersons=API_URL + "/public/findPersons";
        findGroups=API_URL + "/public/findGroups";
        checkInGroup=API_URL + "/group/checkInGroup";
        checkFriend=API_URL + "/userFriend/checkFriend";
        getMessageDestroySwith=API_URL + "message/getMessageDestroySwith";
        updateMessageDestroySwith=API_URL + "message/updateMessageDestroySwith";

        updateMessageDestroyTime=API_URL + "message/updateMessageDestroyTime";
        getMessageDestroyTime=API_URL + "message/getMessageDestroyTime";

        getActive=API_URL + "user/getActive";
        listInvitePrizeDetail=API_URL + "user/listInvitePrizeDetail";
        getRedBag=API_URL + "editor/getRedBag";
    }
    public static void initialize() {
//         API_URL = ClientConfig.getServerPath() + "/api/";

        API_URL = BuildConfig.API_HOST + "/api/";

        USER_LOGIN_URL = API_URL + "user/login";

        USER_LOGOUT_URL = API_URL + "user/logout";

        MICROSERVER_SEARCH_URL = API_URL + "microserver/search/{keyword}";

        SUBSCRIBE_OPERATION_URL = API_URL + "subscriber/{target}";

        MICROSERVER_MENU_URL = API_URL + "microserver/menu/list/{account}";

        MICROSERVER_LIST_URL = API_URL + "microserver/list";

        USER_PASSWORD_URL = API_URL + "user/password";

        MICROAPP_LIST_URL = API_URL + "microapp/list";

        GROUP_OPERATION_URL = API_URL + "group";

        GROUP_SET_LOGO_URL = API_URL + "group/logo";
        GROUP_SET_NAME_URL = API_URL + "group/name";
        GROUP_SET_SUMMARY_URL = API_URL + "group/summary";
        GROUP_SET_PROFILE_URL = API_URL + "group/category";

        GROUP_QUIT_URL = API_URL + "group/member/{groupId}";

        GROUP_MEMBER_URL = API_URL + "group/member";

        GROUP_MEMBER_BATCH_URL = API_URL + "group/member/batch/{groupId}/{account}";

        GROUP_DISBAND_URL = API_URL + "group/{id}";

        QUERY_GROUP_INFO = API_URL + "group/{id}";

        GROUP_SEARCH_URL = API_URL + "group/search/{keyword}";

        GROUP_MEMBER_LIST_URL = API_URL + "group/member/list/{id}";

        GROUP_MEMBER_INVITE_URL = API_URL + "group/member/invite";

        MESSAGE_SEND_URL = API_URL + "message/send";

        MESSAGE_RECEIVE_URL = API_URL + "message/received/{id}";

        MESSAGE_READ_URL = API_URL + "message/read";

        MESSAGE_READ_NOTIFY_URL = API_URL + "message/readNotify";

        MESSAGE_FORWARD_URL = API_URL + "message/forward";

        MESSAGE_BATCH_RECEIVE_URL = API_URL + "message/received/batch";
        MESSAGE_REVOKE_URL = API_URL + "message/revoke/{id}";
        MESSAGE_OFFLINELIST_URL = API_URL + "message/list/offline";

        MOMENT_PUBLISH_URL = API_URL + "moment";

        MOMENT_TIMELINE_URL = API_URL + "moment/timeline/{page}";

        MOMENT_OTHER_LIST_URL = API_URL + "moment/list/other/{account}/{page}";

        MOMENT_ME_LIST_URL = API_URL + "moment/list/me/{page}";

        MOMENT_OPERATION_URL = API_URL + "moment/{id}";

        COMMENT_OPERATION_URL = API_URL + "comment";
        COMMENT_PRAISE_URL = API_URL + "comment/praise";
        COMMENT_DELETE_URL = API_URL + "comment/{id}/{author}";

        HOST_DISPENSE_URL = API_URL + "host/dispense";


        GET_BASE_DATA_URL = API_URL + "base";

        USER_DATABASE_FILE_URL = API_URL + "base/user/db";
        ORG_DATABASE_FILE_URL = API_URL + "base/org/db";

        FILE_UPLOAD_URL = API_URL + "file/{bucket}/{filename}";

//        FILE_DOWNLOAD_URL      =  ClientConfig.getServerPath() + "/app/file/%1$s/%2$s";
        FILE_DOWNLOAD_URL = BuildConfig.IMAGE_HOST + "/%1$s/%2$s?aa=%3$d";
        FILE_OSS_DOWNLOAD_URL = BuildConfig.IMAGE_HOST + "/%1$s?aa=%2$d";

        MOMENT_RULE_LIST_URL = API_URL + "moment/rule/list";
        MOMENT_RULE_URL = API_URL + "moment/rule/{target}/{type}";

        TAG_MEMBER_URL = API_URL + "tag/member";
        TAG_URL = API_URL + "tag";
        TAG_DELETE_URL = API_URL + "tag/{id}";

        CHECK_NEW_VERSION_URL = API_URL + "config/version/{domain}/{versionCode}";

        GET_ALL_GROUP = API_URL + "group/list";

        GET_INFORMATION = API_URL + "editor/list/0";

        REGISTER_ACCOUNT = API_URL + "user/register";

        GET_FRIENDS_LIST = API_URL + "userFriend/list";

        ADD_FRIEND = API_URL + "userFriend/add";

        DELETE_FRIEND = API_URL + "userFriend/delete";

        QUERY_FRIEND = API_URL + "userFriend/query";

        QUERY_ASSETS_BALANCE = API_URL + "account/query";

        QUERY_COIN_EXCHANGE_RATE = API_URL + "account/queryCoinPrice";

        QUERY_CURRENCY_LIST = API_URL + "account/queryCurrency";

        VERIFY_APPLY_PASSWORD = API_URL + "account/validateTradePassword";

        SEND_RED_PACKET = API_URL + "account/sendRedEnvelope";

        RECEIVED_RED_PACKET = API_URL + "account/receiveRedEnvelope";

        RECEIVED_RED_PACKET_MENBER = API_URL + "account/queryReceiveRedEnvelope";

        CHARGE_COIN_GET_ADDRESS = API_URL + "account/coinCharging";

        WITHDRAW_COIN = API_URL + "account/withdrawMoney";

        APPLY_MESSAGE_VERIFY_CODE = API_URL + "account/sendWeiquVCode";

        REGISTER_MESSAGE_VERIFY_CODE = BuildConfig.API_HOST + "/web/sendWeiquVCode.action";

        GET_WITHDRAW_BILL = API_URL + "account/queryBill";

        QUERY_RED_PACKET_RECEIVEDED = API_URL + "account/redIsReceive";

        QUERY_RED_PACKET_ENABLED = API_URL + "account/checkRed";

        RECEIVED_RED_PACKET_LIST = API_URL + "account/queryReceiveRedEnvelopeList";

        SENDED_RED_PACKET_LIST = API_URL + "account/querySendRedEnvelopeList";

        QUERY_WITHDRAW_AVAILABLE_AMOUNT = API_URL + "account/queryWithdrawMoney";

        MODIFY_APPLY_PASSWORD_URL = API_URL + "user/updateTradePass";

        QUERY_GROUP = API_URL + "group/searchGroup";

        APPLY_JOIN_GROUP = API_URL + "group/member/apply";

        AGREE_JOIN_GROUP = API_URL + "group/member/applyAgree";

        MODIFY_PERSON_INFO = API_URL + "user/modify";

        QUERY_IS_FRIEND = API_URL + "userFriend/checkMyFriend";

        SET_REMARK = API_URL + "userFriend/modifyNote";

        QUERY_PERSON_INFO = API_URL + "userFriend/get";

        QUERY_PERSON_GROUP = API_URL + "group/list/me";

        GROUP_BANNED = API_URL + "group/banned";

        GET_EMAIL_VERIFY_CODE = BuildConfig.API_HOST + "/web/sendEmailVCode.action";

        SET_NOT_CHECK_INFO = API_URL + "group/memberAble ";

        SET_GROUP_MANAGER = API_URL + "group/member/setManage";

        LOGIN_LOG_COLLECT = API_URL + "user/loginLog";

        FIND_PASSWORD_REQUEST = API_URL + "user/findPassword";

        GET_NEWS_LIST = API_URL + "editor/list/{currentPage}/{type}";

        INFORMATIN_COMMENT = API_URL + "editor/saveComment";

        GET_BANNER = API_URL + "config/banner";

        GET_VIDEO_ROOM_TOKEN = API_URL + "qiniu/getToken";

        SEND_VIDEO_CONNECT_MESSAGE = API_URL + "qiniu/sendAV";

        REJECT_VIDEO_CONNECT_MESSAGE = API_URL + "qiniu/send";

        UPLOAD_CRASH_LOG = API_URL + "file/exceptionFile";

        GET_NEARLY_PEOPLE_LIST = API_URL + "user/nearUser";

        ADD_COIN = API_URL + "account/saveEthToken";

        QUERY_COIN_ADDRESS = API_URL + "account/findEthToken";

        APPLY_FRIEND = API_URL + "userFriend/applyFriend";

        COIN_TRANSFER = API_URL + "account/transferOfAccount";

        RECEIVE_COIN_TRANSFER = API_URL + "account/receiveTransferOfAccount";

        QUERY_COIN_TRANSFER_STATUS = API_URL + "account/checkTransferOfAccountState";
        GET_INVITE_INFO = API_URL + "user/getInviteInfo";
        GET_MINE_INVITE_CODE = API_URL + "user/getInviteCode";

    }
}
