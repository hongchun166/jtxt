package com.linkb.jstx.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkb.BuildConfig;
import com.linkb.R;
import com.linkb.jstx.adapter.trend.ExpressAdapter;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.dialog.EditorRedBagDig;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.HttpServiceManagerV2;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseDataResult;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.NewsDataResult;
import com.linkb.jstx.network.result.v2.GetEditorInfoResult;
import com.linkb.jstx.network.result.v2.GetRedBagResult;
import com.linkb.jstx.util.Util;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
* 快讯页面
* */
public class ExpressFragment extends LazyLoadFragment implements HttpRequestListener<NewsDataResult> , ExpressAdapter.ExpressCommentListener {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    private ExpressAdapter mAdapter;
    /*
     * 数据的页数
     * */
    private int mPage = 0;

    private List<NewsDataResult.DataListBean> mNewsList = new ArrayList<>();

    /** 微信分享
     * */
    private IWXAPI iwxapi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerToWeixin();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_express, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPage = 0 ;
                loadExpressData(mPage);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter( mAdapter = new ExpressAdapter(mNewsList, getContext(), this));


    }

    /** 将本APP注册到微信
     * */
    private void registerToWeixin() {
        iwxapi = WXAPIFactory.createWXAPI(getActivity(), BuildConfig.WX_APP_ID, true);
        iwxapi.registerApp(BuildConfig.WX_APP_ID);
    }

    /**
     * 判断是否安装微信
     */
    private boolean isWeiXinAppInstall() {
        if (iwxapi == null)
            iwxapi = WXAPIFactory.createWXAPI(getActivity(), BuildConfig.WX_APP_ID);
        if (iwxapi.isWXAppInstalled()) {
            return true;
        } else {
            showToastView(R.string.no_install_wx);
            return false;
        }
    }

    @Override
    public void requestData() {
        loadExpressData(mPage);
    }

    private void loadExpressData(int page){
        HttpServiceManager.getNewsOrExpressList(page, 2, this);
    }

    @Override
    public void onHttpRequestSucceed(NewsDataResult result, OriginalCall call) {
        refreshLayout.finishRefresh();
        hideProgressDialog();
        if (result.isSuccess()){
            mAdapter.addAll(result.getDataList());
        }else {
            showToastView(result.message);
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        hideProgressDialog();
    }

    @Override
    public void onGoodNews(NewsDataResult.DataListBean dataListBean) {
        showProgressDialog("");
        HttpServiceManager.saveInformationComment(dataListBean.getId(), "3", "", "", onGoodNewsListener);
    }

    @Override
    public void onBadNews(NewsDataResult.DataListBean dataListBean) {
        showProgressDialog("");
        HttpServiceManager.saveInformationComment(dataListBean.getId(), "4", "", "", onBadNewsListener);
    }

    @Override
    public void onGetRedBag(NewsDataResult.DataListBean dataListBean) {
        if(dataListBean.getLottery_amount()!=null){
            getOrShowRed(true,dataListBean.getLottery_amount().doubleValue());
        }else {
            httpGetEditorInfo(dataListBean);
        }
    }

    @Override
    public void onShareNews(NewsDataResult.DataListBean dataListBean) {
        //分享消息链接
        if (!isWeiXinAppInstall()) return;
        showProgressDialog("");

        //初始化一个WXWebpageObject，填写url
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl ="网页url";

        //用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title ="网页标题 ";
        msg.description ="网页描述";
        Bitmap thumbBmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("url");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;

        //调用api接口，发送数据到微信
        iwxapi.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }


    private void getOrShowRed(boolean hasSuc,double moneyNum){
        EditorRedBagDig.RedBagBParam param=new EditorRedBagDig.RedBagBParam();
        param.number=moneyNum;
        param.state=hasSuc?EditorRedBagDig.RedBagBParam.STATE_SUC:EditorRedBagDig.RedBagBParam.STATE_FAIL;
        EditorRedBagDig.build().buildDialog(getContext()).setRedBagBParam(param).showDialog();
    }
    private void httpGetEditorInfo(NewsDataResult.DataListBean dataListBean){
        showProgressDialog("");
        HttpServiceManagerV2.getEditorInfo(String.valueOf(dataListBean.getId()), new HttpRequestListener<GetEditorInfoResult>() {
            @Override
            public void onHttpRequestSucceed(GetEditorInfoResult result, OriginalCall call) {
                hideProgressDialog();
                if(result.isSuccess() ){
                    if(result.getData().getLottery_amount()!=null){
                        double amount=result.getData().getLottery_amount().doubleValue();
                        getOrShowRed(true,amount);
                        mAdapter.updateRedStateSuc(dataListBean.getId(),amount);
                    }else {
                        httpGetRedBag(dataListBean);
                    }
                }else {
                    showToastView(String.valueOf(result.message));
                }
            }
            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {
                hideProgressDialog();
                showToastView(R.string.commit_apply_friend_failure);
            }
        });
    }
    private void httpGetRedBag(NewsDataResult.DataListBean dataListBean){
        String account= Global.getCurrentUser().getAccount();
        HttpServiceManagerV2.getRedBag(account, String.valueOf(dataListBean.getId()), new HttpRequestListener<GetRedBagResult>() {
            @Override
            public void onHttpRequestSucceed(GetRedBagResult result, OriginalCall call) {
                hideProgressDialog();
                if(result.isSuccess()){
                    if(result.getData()!=null){
                        getOrShowRed(true,result.getData().doubleValue());
                        mAdapter.updateRedStateSuc(dataListBean.getId(),result.getData().doubleValue());
                    }else {
                        getOrShowRed(false,result.getData().doubleValue());
                    }
                }else {
                    showToastView(String.valueOf(result.message));
                }
            }
            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {
                hideProgressDialog();
                showToastView(R.string.commit_apply_friend_failure);
            }
        });
    }

    private HttpRequestListener<BaseDataResult> onGoodNewsListener = new HttpRequestListener<BaseDataResult>() {
        @Override
        public void onHttpRequestSucceed(BaseDataResult result, OriginalCall call) {

            if (!result.isSuccess()){
                hideProgressDialog();
                showToastView(result.message);
            }else {
                loadExpressData(mPage);
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {
            hideProgressDialog();
        }
    };

    private HttpRequestListener<BaseDataResult> onBadNewsListener = new HttpRequestListener<BaseDataResult>() {
        @Override
        public void onHttpRequestSucceed(BaseDataResult result, OriginalCall call) {
            if (!result.isSuccess()){
                hideProgressDialog();
                showToastView(result.message);
            }else {
                loadExpressData(mPage);
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {
            hideProgressDialog();
        }
    };
}
