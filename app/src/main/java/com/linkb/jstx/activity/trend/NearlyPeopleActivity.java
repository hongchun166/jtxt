package com.linkb.jstx.activity.trend;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.activity.contact.PersonInfoActivity;
import com.linkb.jstx.adapter.trend.NearlyPeopleAdapter;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.NearlyPeopleFilterPopupWindow;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.NearlyPeopleResult;
import com.linkb.jstx.util.ConvertUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**  附近的人
* */
public class NearlyPeopleActivity extends BaseActivity implements BDLocationListener, HttpRequestListener<NearlyPeopleResult>, OnItemClickedListener<NearlyPeopleResult.DataBean>, NearlyPeopleFilterPopupWindow.NearlyPeopleFilterListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.emptyView)
    View emptyView;
    @BindView(R.id.more_btn)
    View moreBtn;

    private LocationClient mLocClient;
    private LatLng mLocation;
    private GeoCoder mGeoCoder;
    private User mSelf;
    /**
     * 查看性别, 男是1，女是0,2是全部
     */
    private int mGender = 2;

    private NearlyPeopleAdapter mAdapter;

    private List<NearlyPeopleResult.DataBean> mNearlyPeopleList = new ArrayList<>();

    private NearlyPeopleFilterPopupWindow mNearlyPeopleFilterPopupWindow;

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);

        mSelf = Global.getCurrentUser();
        initLocation();

        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                loadDate();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter = new NearlyPeopleAdapter(mNearlyPeopleList, this));
        mAdapter.setOnItemClickedListener(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_nearly_people;
    }


    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        mLocClient.unRegisterLocationListener(this);
        mLocClient.stop();
        if (bdLocation != null) {
            mLocation = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            loadDate();
        }else {
            showToastView(getString(R.string.get_location_failure));
        }

    }

    private void loadDate(){
        if (mLocation != null){

            HttpServiceManager.checkNearlyPeople(mSelf.account,mGender, ConvertUtils.doubleToString(mLocation.longitude),
                    ConvertUtils.doubleToString(mLocation.latitude), NearlyPeopleActivity.this  );
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGeoCoder!=null){
            mGeoCoder.destroy();
        }
        mLocClient.unRegisterLocationListener(this);
        mLocClient.stop();
    }

    private void initLocation() {
        mLocClient = new LocationClient(this);

        mLocClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setIsNeedAddress(true);
        mLocClient.setLocOption(option);
        mLocClient.start();
        mLocClient.requestLocation();
    }

    @OnClick(R.id.back_btn)
    public void onBack(){ finish();}

    @OnClick(R.id.more_btn)
    public void onMore() {
        if (mNearlyPeopleFilterPopupWindow == null){
            mNearlyPeopleFilterPopupWindow = new NearlyPeopleFilterPopupWindow(this, this);
        }
        //显示窗口
        mNearlyPeopleFilterPopupWindow.showAsDropDown(moreBtn, -ConvertUtils.dp2px(105), ConvertUtils.dp2px(0), Gravity.LEFT); //设置layout在PopupWindow中显示的位置
    }

    @Override
    public void onHttpRequestSucceed(NearlyPeopleResult result, OriginalCall call) {
        refreshLayout.finishRefresh();
        if (result.isSuccess()){
            mAdapter.addAll(result.getData());
            emptyView.setVisibility(result.getData().size() > 0 ? View.INVISIBLE: View.VISIBLE);
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        refreshLayout.finishRefresh();
    }

    @Override
    public void onItemClicked(NearlyPeopleResult.DataBean obj, View view) {
        Friend friend = new Friend();
        friend.name = obj.getUsername();
        friend.account = obj.getAccount();
        friend.gender = String.valueOf(obj.getGender());

        Intent intent = new Intent(NearlyPeopleActivity.this, PersonInfoActivity.class);
        intent.putExtra(Friend.class.getName(), friend);
        startActivity(intent);
    }

    @Override
    public void selected(int index) {
        mGender = index;
        loadDate();
    }
}
