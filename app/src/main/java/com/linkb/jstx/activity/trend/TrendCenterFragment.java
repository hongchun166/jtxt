
package com.linkb.jstx.activity.trend;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.linkb.jstx.activity.contact.GroupListActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.component.GlideImageLoader;
import com.linkb.jstx.activity.base.CIMMonitorFragment;
import com.linkb.R;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.GetBannerResult;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

public class TrendCenterFragment extends CIMMonitorFragment implements OnClickListener, HttpRequestListener<GetBannerResult> {

    private View hotGroupRly, informationRly, discussRly, nearlyPeopleRly, gameRly;

    /** banner 图片地址list
    * */
    private List<String> bannerUrlList = new ArrayList<>();
    private Banner mBanner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_trend, container, false);
//        toolBarTitle = view.findViewById(R.id.tool_bar_title);
//        imageBtn = view.findViewById(R.id.right_img_btn);
//        imageBtn.setVisibility(View.INVISIBLE);
//        toolBarTitle.setText(R.string.common_trend);


        initView(view);
        return view;
    }

    private void initView(View view) {
        hotGroupRly = view.findViewById(R.id.popular_group_rly);
        informationRly = view.findViewById(R.id.information_rly);
        nearlyPeopleRly = view.findViewById(R.id.nearly_people_Item);
        gameRly = view.findViewById(R.id.game_Item);
        view.findViewById(R.id.circleItem).setOnClickListener(this);
        hotGroupRly.setOnClickListener(this);
        informationRly.setOnClickListener(this);
        nearlyPeopleRly.setOnClickListener(this);
        gameRly.setOnClickListener(this);
        mBanner = view.findViewById(R.id.banner);
//        discussRly.setOnClickListener(this);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mBanner.setImageLoader(new GlideImageLoader());



    }

    @Override
    public void onResume() {
        super.onResume();
        refreshNewMessageHintView();
    }


    private void refreshNewMessageHintView() {
//        long momentCount = MessageRepository.countNewMessage(new String[]{Constant.MessageAction.ACTION_501, Constant.MessageAction.ACTION_502});
//        mMomentBadge.setVisibility(momentCount > 0 ? View.VISIBLE : View.GONE);
//        mMomentBadge.setText(momentCount > 0 ? String.valueOf(momentCount) : null);
//
//        Message momentMsg = MessageRepository.queryNewMomentMessage();
//        mTabRedDot.setVisibility(momentMsg != null ? View.VISIBLE : View.GONE);
//        mMomentHintView.setVisibility(momentMsg != null ? View.VISIBLE : View.GONE);
//        if (momentMsg != null) {
//            mMomentIcon.load(FileURLBuilder.getUserIconUrl(momentMsg.sender), R.mipmap.lianxiren);
//        }
//
//
//        mTabBadge.setVisibility(momentCount > 0 ? View.VISIBLE : View.GONE);
//        mTabBadge.setText(momentCount > 0 ? String.valueOf(momentCount) : null);



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.game_Item:
                this.startActivity(new Intent(this.getActivity(), GameListActivity.class));
                break;
            case R.id.nearly_people_Item:
                this.startActivity(new Intent(this.getActivity(), NearlyPeopleActivity.class));
                break;

            case R.id.popular_group_rly:
                this.startActivity(new Intent(this.getActivity(), GroupListActivity.class));
                break;
            case R.id.information_rly:
                this.startActivity(new Intent(this.getActivity(), InformationActivity.class));


//                Intent intent = new Intent(getContext(), MMWebViewActivity.class);
//                intent.setData(Uri.parse("https://www.qcaijing.com/money"));
//                getContext().startActivity(intent);

                break;
            case R.id.circleItem:
                this.startActivity(new Intent(this.getActivity(), TimelineMomentActivity.class));
//                mMomentHintView.setVisibility(View.GONE);
//                mTabRedDot.setVisibility(View.GONE);
//                mTabBadge.setVisibility(View.GONE);
//                mTabBadge.setText(null);
//                mMomentBadge.setVisibility(View.GONE);
//                mMomentBadge.setText(null);
                break;


//            case R.id.menu_microapp:
//                this.startActivity(new Intent(this.getActivity(), MicroAppListActivity.class));
//                break;
//            case R.id.circleItem:
//                this.startActivity(new Intent(this.getActivity(), TimelineMomentActivity.class));
//                mMomentHintView.setVisibility(View.GONE);
//                mTabRedDot.setVisibility(View.GONE);
//                mTabBadge.setVisibility(View.GONE);
//                mTabBadge.setText(null);
//                mMomentBadge.setVisibility(View.GONE);
//                mMomentBadge.setText(null);
//                break;
        }
    }

    @Override
    public void onMessageReceived(com.farsunset.cim.sdk.android.model.Message msg) {

        if (Constant.MessageAction.ACTION_500.equals(msg.getAction())
                || Constant.MessageAction.ACTION_501.equals(msg.getAction())
                || Constant.MessageAction.ACTION_502.equals(msg.getAction())
                || Constant.MessageAction.ACTION_503.equals(msg.getAction())
                || Constant.MessageAction.ACTION_504.equals(msg.getAction())) {
            refreshNewMessageHintView();
        }

    }

    @Override
    public void requestData() {
        HttpServiceManager.getTrendBannerList(this);
    }

    @Override
    public void onHttpRequestSucceed(GetBannerResult result, OriginalCall call) {
        if (result != null && result.isSuccess()){
            for (GetBannerResult.DataListBean dataBean : result.getDataList()) {
                bannerUrlList.add(dataBean.getUrl());
            }
            mBanner.setImages(bannerUrlList);
            mBanner.start();
        }else {
            showToastView(result.message);
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {

    }
}
