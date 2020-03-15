
package com.linkb.jstx.activity.trend;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.linkb.R;
import com.linkb.jstx.activity.base.CIMMonitorFragment;
import com.linkb.jstx.activity.contact.FindFindActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.component.GlideImageLoader;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.GetBannerResult;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

public class TrendCenterFragmentV2 extends CIMMonitorFragment implements OnClickListener, HttpRequestListener<GetBannerResult> {

    private View hotGroupRly, informationRly, discussRly, nearlyPeopleRly, gameRly;

    /** banner 图片地址list
    * */
    private List<String> bannerUrlList = new ArrayList<>();
    private Banner mBanner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_trend_v2, container, false);
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
        nearlyPeopleRly.setVisibility(View.GONE);
        gameRly.setVisibility(View.GONE);
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
//                this.startActivity(new Intent(this.getActivity(), GroupListActivity.class));
                 startActivity(new Intent(getActivity(), FindFindActivity.class));
                break;
            case R.id.information_rly:
                this.startActivity(new Intent(this.getActivity(), InformationActivity.class));

                break;
            case R.id.circleItem:
                this.startActivity(new Intent(this.getActivity(), TimelineMomentActivity.class));
                break;
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
            bannerUrlList.clear();
            mBanner.stopAutoPlay();
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
