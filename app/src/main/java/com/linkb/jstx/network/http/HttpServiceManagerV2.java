
package com.linkb.jstx.network.http;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.URLConstant;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.model.Moment;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.CommonResult;
import com.linkb.jstx.network.result.FriendApplyBeResult;
import com.linkb.jstx.network.result.FriendListResultV2;
import com.linkb.jstx.network.result.MomentListResult;
import com.linkb.jstx.network.result.NewsDataResult;
import com.linkb.jstx.network.result.ReceivedRedPacketResult;
import com.linkb.jstx.network.result.v2.AccountBalanceResult;
import com.linkb.jstx.network.result.v2.CheckInGroupResult;
import com.linkb.jstx.network.result.v2.CurrencyInfoResult;
import com.linkb.jstx.network.result.v2.FindGroupsResult;
import com.linkb.jstx.network.result.v2.FindPersonsResult;
import com.linkb.jstx.network.result.v2.GetActiveResult;
import com.linkb.jstx.network.result.v2.GetEditorInfoResult;
import com.linkb.jstx.network.result.v2.GetMessageDestroySwithResult;
import com.linkb.jstx.network.result.v2.GetReceiverDetailResultV2;
import com.linkb.jstx.network.result.v2.GetRedBagResult;
import com.linkb.jstx.network.result.v2.GetTradePasswordStateResult;
import com.linkb.jstx.network.result.v2.ListIndustryResult;
import com.linkb.jstx.network.result.v2.ListMyBalanceFlowResult;
import com.linkb.jstx.network.result.v2.ListMyCurrencyResult;
import com.linkb.jstx.network.result.v2.ListTagsResult;
import com.linkb.jstx.network.result.v2.QueryUserInfoResult;
import com.linkb.jstx.network.result.v2.RedpackgeGetInfoResult;
import com.linkb.jstx.network.result.v2.RedpackgeListCurrenCyResult;
import com.linkb.jstx.network.result.v2.RedpackgeListRcvHistroyResult;
import com.linkb.jstx.network.result.v2.RedpackgeListSndHistoryResult;
import com.linkb.jstx.network.result.v2.SendRedPacketResultV2;
import com.linkb.jstx.network.result.v2.UpdateMessageDestroyTimeResult;
import com.linkb.jstx.util.MD5;

public class HttpServiceManagerV2 {
    /**
     * 注册账号
     *
     * @param locale 0是中国，1是外国
     */
    public static void registerAccount(String account, String sex, String name
            , String password, String vertcode, String locale,String inviteCode
            , HttpRequestListener listener) {

        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.REGISTER_ACCOUNT, BaseResult.class);
        requestBody.addParameter("account", account);
        requestBody.addParameter("name", name);
        requestBody.addParameter("gender", sex);
        requestBody.addParameter("password", password);
        requestBody.addParameter("vertcode", vertcode);
        requestBody.addParameter("locale", locale);
        if(!TextUtils.isEmpty(inviteCode)){
            requestBody.addParameter("inviteCode", inviteCode);
        }
        String tradePassword=null;
        if(!TextUtils.isEmpty(tradePassword)) {
            requestBody.addParameter("tradePassword", "");
        }

        HttpRequestLauncher.execute(requestBody, listener);
    }
    /**
     * 获取好友申请列表
     * /api/userFriend/listFriendApply
     */
    public static void getListFriendApplyV2(String account,HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.LIST_FRIEND_APPLY_V2, FriendApplyBeResult.class);
        requestBody.setContentType(HttpRequestBody.JSON_MEDIATYPE);
        requestBody.addParameter("account",account);
//        HashMap<String,String> bean=new HashMap<>();
//        bean.put("account",account);
//        String beanStr=new Gson().toJson(bean);
//        requestBody.setContent(beanStr);

        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 申请好友关系
     */
    public static void applyFriendV2(String account, String receiverUser, String applyContent, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.ADD_FRIEND_APPLY_V2, BaseResult.class);
        requestBody.addParameter("account", account);
        requestBody.addParameter("receiverUser", receiverUser);
        requestBody.addParameter("applyContent", applyContent);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     *
     * @param account
     * @param id  好友ID  long
     * @param state   处理状态（1同意，2拒绝） integer
     * @param listener
     */
    public static void agreeFriendV2(String account, String id, String state, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.APPLY_FRIENDV_ANSWER_V2, BaseResult.class);
        requestBody.addParameter("account", account);
        requestBody.addParameter("id", id);
        requestBody.addParameter("state", state);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 删除好友
     * @param account
     * @param friendAccount
     * @param listener
     */
    public static void deleteFriendV2(String account, String friendAccount, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.APPLY_FRIENDV_DELETE_V2, BaseResult.class);
        requestBody.addParameter("account", account);
        requestBody.addParameter("friendAccount", friendAccount);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 我的好友列表
     * @param account
     * @param listener
     */
    public static void listMyFriendV2(String account,  HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.LIST_MY_FRIENDS_V2, FriendListResultV2.class);
        requestBody.addParameter("account", account);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 获取用户信息
     * @param account
     * @param friendAccount
     * @param listener
     */
    public static void queryUserInfo(String account,String friendAccount,  HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.QUERY_USER_INFO_V2, QueryUserInfoResult.class);
        requestBody.addParameter("account", account);
        requestBody.addParameter("friendAccount", friendAccount);
        HttpRequestLauncher.execute(requestBody, listener);
    }


    /**
     * 获取账户总余额
     * @param account
     * @param listener
     */
    public static void getAccountBalance(String account,  HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.getAccountBalance, AccountBalanceResult.class);
        requestBody.addParameter("account", account);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 查询我的钱包某个币种余额信息
     * @param account
     * @param currencyId  钱包ID
     * @param listener
     */
    public static void getMyCurrencyById(String account,String currencyId,  HttpRequestListener<CurrencyInfoResult> listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.getMyCurrencyById, CurrencyInfoResult.class);
        requestBody.addParameter("account", account);
        requestBody.addParameter("currencyId", currencyId);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 查询钱包账户流水
     * @param account
     * @param type
     * 流水类型(1收入2支出3奖励)(不填则查询所有流水类型)
     * @param currencyId
     * 钱包I(不填则查询所有币种余额)（
     * @param listener
     */
    public static void listMyBalanceFlow(String account,String type,String currencyId,  HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.listMyBalanceFlow, ListMyBalanceFlowResult.class);
        requestBody.addParameter("account", account);//
        if(!TextUtils.isEmpty(type)) requestBody.addParameter("type", type);
        if(!TextUtils.isEmpty(currencyId)) requestBody.addParameter("currencyId", currencyId);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 查询我的钱包列表
     * @param account
     * @param currencyId
     * 钱包ID（不填则查询所有币种）
     * @param listener
     */
    public static void listMyCurrency(String account,String currencyId,  HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.listMyCurrency, ListMyCurrencyResult.class);
        requestBody.addParameter("account", account);//
        if(!TextUtils.isEmpty(currencyId)) requestBody.addParameter("currencyId", currencyId);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 获取标签列表
     * @param listener
     */
    public static void getLisTags(String account,HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.lisTags, ListTagsResult.class);
        requestBody.addParameter("account", account);//
        HttpRequestLauncher.execute(requestBody, listener);
    }
    /**
     * 获取行业列表
     * @param listener
     */
    public static void getListIndustry(String account,HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.listIndustry, ListIndustryResult.class);
        requestBody.addParameter("account", account);//
        HttpRequestLauncher.execute(requestBody, listener);
    }
    /**
     * 修改个人资料
     * @param listener
     */
    public static void updateUserInfo(User user, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.updateUserInfo, BaseResult.class);
        if(!TextUtils.isEmpty(user.headUrl)) requestBody.addParameter("headUrl", user.headUrl);
        if(!TextUtils.isEmpty(user.name)) requestBody.addParameter("name", user.name);
        if(!TextUtils.isEmpty(user.gender)) requestBody.addParameter("gender", user.gender);
        if(!TextUtils.isEmpty(user.getMarrriage())) requestBody.addParameter("marrriage", user.getMarrriage());
        if(!TextUtils.isEmpty(user.telephone)) requestBody.addParameter("telephone", user.telephone);
        if(!TextUtils.isEmpty(user.industry)) requestBody.addParameter("industry", user.industry);
        if(!TextUtils.isEmpty(user.position)) requestBody.addParameter("position", user.position);
        if(!TextUtils.isEmpty(user.area)) requestBody.addParameter("area", user.area);
        if(!TextUtils.isEmpty(user.tag)) requestBody.addParameter("tag", user.tag);
        if(!TextUtils.isEmpty(user.motto)) requestBody.addParameter("motto", user.motto);
        if(!TextUtils.isEmpty(user.backgroudUrl)) requestBody.addParameter("backgroudUrl", user.backgroudUrl);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 找一找（群）
     * @param listener
     */
    public static void findGroups(String content,HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.findGroups, FindGroupsResult.class);
       requestBody.addParameter("content", content);//
        requestBody.addParameter("currentPage", 0);//
        HttpRequestLauncher.execute(requestBody, listener);
    }
    /**
     * 检查自己是否加入了该群
     * @param listener
     */
    public static void checkInGroup(String account,String groupId,HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.checkInGroup, CheckInGroupResult.class);
        requestBody.addParameter("account", account);//
        requestBody.addParameter("groupId", groupId);//
        HttpRequestLauncher.execute(requestBody, listener);
    }
    /**
     * 检查是否为好友
     * @param listener
     */
    public static void checkFriend(String account,String friendAccount,HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.checkFriend, CheckInGroupResult.class);
        requestBody.addParameter("account", account);//
        requestBody.addParameter("friendAccount", friendAccount);//
        HttpRequestLauncher.execute(requestBody, listener);
    }
    /**
     * 找一找（人）
     * @param content
     * @param area  区域
     * @param industry  行业
     * @param tag      标签
     * @param gender      性别(0女1男)
     * @param listener
     */
    public static void findPersons(String content,String area,String industry,String tag,String gender,String marrriage,HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.findPersons, FindPersonsResult.class);
        requestBody.addParameter("userAccount", Global.getCurrentUser().account);//
        requestBody.addParameter("currentPage", 0);//
        requestBody.addParameter("content",content);//
        if(!TextUtils.isEmpty(area)) requestBody.addParameter("area", area);//
        if(!TextUtils.isEmpty(industry)) requestBody.addParameter("industry", industry);//
        if(!TextUtils.isEmpty(tag))requestBody.addParameter("tag", tag);//
        if(!TextUtils.isEmpty(gender))requestBody.addParameter("gender", gender);//
        if(!TextUtils.isEmpty(marrriage))requestBody.addParameter("marrriage", marrriage);//
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 获取阅读即焚开关状态
     * @param listener
     */
    public static void getMessageDestroySwith(String account,String friendAccount,HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.getMessageDestroySwith, GetMessageDestroySwithResult.class);
        requestBody.addParameter("account", account);//
        requestBody.addParameter("friendAccount", friendAccount);//
        HttpRequestLauncher.execute(requestBody, listener);
    }
    /**
     * 修改阅读即焚开关状态
     * @param account
     * @param state  阅读即焚状态（0关闭，1开启）
     * @param listener
     */
    public static void updateMessageDestroySwith(String account,String friendAccount,int state,HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.updateMessageDestroySwith, GetMessageDestroySwithResult.class);
        requestBody.addParameter("account", account);//
        requestBody.addParameter("friendAccount", friendAccount);//
        requestBody.addParameter("state", String.valueOf(state));//
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 设置阅读即焚时长
     * @param listener
     */
    public static void updateMessageDestroyTime(String account,String receiverMessageAccount,int time,HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.updateMessageDestroyTime, UpdateMessageDestroyTimeResult.class);
        requestBody.addParameter("receiverMessageAccount", receiverMessageAccount);//
        requestBody.addParameter("time", time);//
        requestBody.addParameter("account", account);//
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 获取阅读即焚时长
     * @param listener
     */
    public static void getMessageDestroyTime(String account,String receiverMessageAccount,HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.getMessageDestroyTime, UpdateMessageDestroyTimeResult.class);
        requestBody.addParameter("sendMessageAccount", receiverMessageAccount);//
        requestBody.addParameter("account", account);//
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * H获取活跃度
     * @param listener
     */
    public static void getActive(String account,HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.getActive, GetActiveResult.class);
        requestBody.addParameter("account", account);//
        HttpRequestLauncher.execute(requestBody, listener);
    }
    /**
     * 邀请奖励明细
     * @param listener
     */
    public static void listInvitePrizeDetail(String account,HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.listInvitePrizeDetail, BaseResult.class);
        requestBody.addParameter("account", account);//
        HttpRequestLauncher.execute(requestBody, listener);
    }
    /**
     * 抽红包(资讯)
     * @param listener
     * @param id 资讯或新闻ID
     *
     */
    public static void getRedBag(String account,String id,HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.getRedBag, GetRedBagResult.class);
        requestBody.addParameter("account", account);//
        requestBody.addParameter("id", id);//
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 资讯列表
     * @param currentPage 当前页
     * @param type       分类1 新闻 2 资讯
     * @param listener
     */
    public static void getEditorList(int currentPage,int type,HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.GET, URLConstant.editorList, NewsDataResult.class);
        requestBody.addPathVariable("currentPage", currentPage);
        requestBody.addPathVariable("type", type);
        HttpRequestLauncher.execute(requestBody, listener);
    }
    /**
     *   资讯闲情
     * @param id 资讯或新闻ID
     * @param listener
     */
    public static void getEditorInfo(String id,HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.GET, URLConstant.editorInfo, GetEditorInfoResult.class);
        requestBody.addPathVariable("id", id);//
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 获取朋友圈内容列表
     * @param page
     * @param listener
     */
    public static void queryMomentTimeline(String account,int page, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.GET, URLConstant.momentTimeline, MomentListResult.class);
        requestBody.addPathVariable("account", account);
        requestBody.addPathVariable("currentPage", page);
        HttpRequestLauncher.execute(requestBody, listener);
    }
    /**
     * 获取朋友圈内容列表
     * @param id
     * @param listener
     */
    public static void deleteMomentById(long id,HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.momentDelete, MomentListResult.class);
        requestBody.addParameter("id", id);
        HttpRequestLauncher.execute(requestBody, listener);
    }
    /**
     * 获取朋友圈内容列表
     * @param account
     * @param listener
     */
    public static void momentSave(String account, Moment moment, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.momentSave, CommonResult.class);
        requestBody.addParameter("article",new Gson().toJson(moment));
//        requestBody.addParameter("type", moment.type);
//        requestBody.addParameter("content", moment.content);
//        requestBody.addParameter("extra", moment.extra);
//        requestBody.addParameter("text", moment.text);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 查看红包基本信息
     * @param redPackgeId 红包ID
     */
    public static void redpackgeGetInfo(String redPackgeId,HttpRequestListener listener){
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST,URLConstant.REDPACKGE_GetInfo, RedpackgeGetInfoResult.class);
        requestBody.addParameter("redPackgeId", redPackgeId);
        HttpRequestLauncher.execute(requestBody, listener);
    }
    /**
     * 获取红包领取详情(包括红包信息，和已领取的记录信息
     * @param redPackgeId 红包ID
     */
    public static void redpackgeGetReceiverDetail(String redPackgeId,HttpRequestListener listener){
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST,URLConstant.REDPACKGE_getReceiverDetail, GetReceiverDetailResultV2.class);
//        requestBody.addParameter("userAccount", userAccount);
        requestBody.addParameter("redPackgeId", redPackgeId);
        HttpRequestLauncher.execute(requestBody, listener);
    }
    /**
     * 领红包
     * @param redPackgeId 红包ID
     */
    public static void redpackgeReceiver(String redPackgeId,HttpRequestListener listener){
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST,URLConstant.REDPACKGE_receiverPackge, ReceivedRedPacketResult.class);
//        requestBody.addParameter("userAccount", userAccount);
        requestBody.addParameter("redPackgeId", redPackgeId);
        HttpRequestLauncher.execute(requestBody, listener);
    }
    /**
     * 发送红包
     * @param currencyId   币钟ID
     * @param sendMoney     当redPacketType == 1 和 3时，为红包总额; 当当redPacketType == 2时，为单个红包金额
     * @param remark  备足备注
     * @param redCount  红包数量
     * @param redType  红包类型   1 普通私人红包   2 群发红包（普通红包） 3 群发红包（拼手气红包）
     *                 红包类型    1 表示普通红包, 2表示普通群发红包     3表示拼手气群发红包
     * @param  receiver  红包接收人|接收群
     * @param  tradePassword 支付密码
     */
    public static void redpackgeSend(long currencyId, String sendMoney, String remark, int redCount, int redType, String tradePassword,String receiver, HttpRequestListener listener){
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST,URLConstant.REDPACKGE_send, SendRedPacketResultV2.class);
//        requestBody.addParameter("userAccount", userAccount);
        requestBody.addParameter("currencyId", currencyId);
        requestBody.addParameter("sendMoney", sendMoney);
        requestBody.addParameter("redCount", redCount);
        requestBody.addParameter("remark", remark);
        requestBody.addParameter("redType", redType);
//        requestBody.addParameter("tradePassword", MD5.digest(tradePassword + "blink"));
        requestBody.addParameter("tradePassword",tradePassword);
        requestBody.addParameter("receiver", receiver);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 获取红包发送币种列表
     * @param
     */
    public static void redpackgeListCurrenCy(String userAccount,HttpRequestListener listener){
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST,URLConstant.REDPACKGE_listCurrenCy, RedpackgeListCurrenCyResult.class);
        requestBody.addParameter("userAccount",userAccount);
        HttpRequestLauncher.execute(requestBody, listener);
    }
    /**
     * 获取领取红包历史记录)
     * @param
     */
    public static void redpackgeListReceivHistroy(String userAccount,HttpRequestListener listener){
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST,URLConstant.REDPACKGE_listReceivHistroy, RedpackgeListRcvHistroyResult.class);
        requestBody.addParameter("userAccount",userAccount);
        HttpRequestLauncher.execute(requestBody, listener);
    }
    /**
     * 获取发送红包历史记录)
     * @param
     */
    public static void redpackgeListSendHistroy(String userAccount,HttpRequestListener listener){
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST,URLConstant.REDPACKGE_listSendHistroy, RedpackgeListSndHistoryResult.class);
        requestBody.addParameter("userAccount",userAccount);
        HttpRequestLauncher.execute(requestBody, listener);
    }


    /**
     * 用户是否设置支付密码
     * @param
     */
    public static void getTradePasswordState(String userAccount,HttpRequestListener listener){
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.GET,URLConstant.getTradePasswordState, GetTradePasswordStateResult.class);
//        requestBody.addPathVariable("userAccount",userAccount);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 验证支付密码是否正确
     * @param fundPassword 密码
     * @param listener
     */
    public static void validateTradePassword(String fundPassword,HttpRequestListener listener){
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST,URLConstant.validateTradePassword, BaseResult.class);
        requestBody.addParameter("fundPassword",fundPassword);
        HttpRequestLauncher.execute(requestBody, listener);
    }
    /**
     * 发送短信验证码
     * @param account
     */
    public static void sendWeiquVCode(String account,HttpRequestListener listener){
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST,URLConstant.sendWeiquVCode, BaseResult.class);
        requestBody.addParameter("account",account);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 修改支付密码
     * @param account
     * @param newPassword
     * @param identifyingCode  短信验证码
     * @param listener
     */
    public static void updateTradePass(String account,String newPassword,String identifyingCode,HttpRequestListener listener){
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.PATCH,URLConstant.updateTradePass, BaseResult.class);
        requestBody.addParameter("newPassword",newPassword);
        requestBody.addParameter("identifyingCode",identifyingCode);
        requestBody.addParameter("account",account);
        HttpRequestLauncher.execute(requestBody, listener);
    }
}
