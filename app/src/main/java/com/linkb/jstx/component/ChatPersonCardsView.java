package com.linkb.jstx.component;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.contact.PersonInfoActivity;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseDataResult;
import com.linkb.jstx.network.result.BasePersonInfoResult;
import com.linkb.jstx.network.result.SendCardsResult;
import com.linkb.jstx.util.FileURLBuilder;

/** 个人名片基本布局
* */
public class ChatPersonCardsView extends CardView implements View.OnClickListener{

    private WebImageView avatarImage;
    private TextView nameTv;
    private Friend mFriend;
    private SendCardsResult.DataBean mDataBean;
//    private boolean isFriend = false;

    public ChatPersonCardsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        avatarImage = findViewById(R.id.person_card_avatar);
        nameTv = findViewById(R.id.person_card_name);

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getContext(), PersonInfoActivity.class);
        intent.putExtra(Friend.class.getName(), mFriend);
        view.getContext().startActivity(intent);
    }


    public void initSendPersonCards(SendCardsResult.DataBean dataBean) {
        mDataBean = dataBean;
        avatarImage.load(FileURLBuilder.getUserIconUrl(dataBean.getCardsUserAccount()), R.mipmap.lianxiren, 999);
        nameTv.setText(dataBean.getCardsUserName());
        //查询名片上的个人信息和发送者的信息
        mFriend = FriendRepository.queryFriendExit(dataBean.getCardsUserAccount());
        if (mFriend == null){
            HttpServiceManager.queryPersonInfo(dataBean.getCardsUserAccount(), queryPersonInfoListener);
        }
        if (FriendRepository.queryFriendExit(dataBean.getSendCardAccount()) == null){
            HttpServiceManager.queryPersonInfo(dataBean.getSendCardAccount(), queryPersonInfoListener);
        }

    }

//    private HttpRequestListener<BaseDataResult> checkIsFiendListener = new HttpRequestListener<BaseDataResult>() {
//        @Override
//        public void onHttpRequestSucceed(BaseDataResult result, OriginalCall call) {
//            if (result.isSuccess()){
//                isFriend = !result.getData().equals("false");
//            }
//        }
//
//        @Override
//        public void onHttpRequestFailure(Exception e, OriginalCall call) {
//
//        }
//    };

    private HttpRequestListener<BasePersonInfoResult> queryPersonInfoListener = new HttpRequestListener<BasePersonInfoResult>() {
        @Override
        public void onHttpRequestSucceed(BasePersonInfoResult result, OriginalCall call) {
            if (result.isSuccess()){
                mFriend = User.UserToFriend(result.getData());
                FriendRepository.save(mFriend);
                //查询是否是好友
//                HttpServiceManager.checkIsFriend(mDataBean.getCardsUserAccount(), checkIsFiendListener);
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {

        }
    };


}
