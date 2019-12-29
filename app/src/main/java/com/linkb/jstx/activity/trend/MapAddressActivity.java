
package com.linkb.jstx.activity.trend;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.adapter.MapAddressListAdapter;
import com.linkb.jstx.network.model.MapAddress;
import com.linkb.R;

public class MapAddressActivity extends BaseActivity implements OnGetPoiSearchResultListener, OnItemClickedListener, BDLocationListener, OnGetGeoCoderResultListener {
    public static final int REQUEST_CODE = 7452;
    private MapAddressListAdapter adapter;
    private LocationClient mLocClient;
    private LatLng mLocation;
    private GeoCoder mGeoCoder;
    @Override
    public void initComponents() {

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter = new MapAddressListAdapter());
        adapter.setOnItemClickedListener(this);
        initLocation();
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGeoCoder!=null){
            mGeoCoder.destroy();
        }
        mLocClient.unRegisterLocationListener(this);
        mLocClient.stop();
    }


    @Override
    public int getContentLayout() {

        return R.layout.activity_common_recyclerview;
    }

    @Override
    public int getToolbarTitle() {

        return R.string.title_select_address;
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult.getAllPoi() != null) {
            adapter.addAll(poiResult.getAllPoi(), mLocation);
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @Override
    public void onItemClicked(Object obj, View view) {
        if (obj == null) {
            setResult(RESULT_CANCELED);
        } else {
            Intent intent = new Intent();
            PoiInfo poiInfo = (PoiInfo) obj;
            MapAddress mapAddress = new MapAddress();
            mapAddress.name = poiInfo.name;
            mapAddress.latitude = poiInfo.location.latitude;
            mapAddress.longitude = poiInfo.location.longitude;
            intent.putExtra(MapAddress.class.getName(), mapAddress);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        mLocClient.unRegisterLocationListener(this);
        mLocClient.stop();
        if (location != null) {
            mLocation = new LatLng(location.getLatitude(), location.getLongitude());

            mGeoCoder = GeoCoder.newInstance();
            mGeoCoder.setOnGetGeoCodeResultListener(this);

            mGeoCoder.reverseGeoCode((new ReverseGeoCodeOption()).location(mLocation));
        }
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result != null && result.error == SearchResult.ERRORNO.NO_ERROR) {
            adapter.addAll(result.getPoiList(), mLocation);
        }
    }
}
