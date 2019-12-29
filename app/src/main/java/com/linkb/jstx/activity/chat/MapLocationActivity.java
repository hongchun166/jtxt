
package com.linkb.jstx.activity.chat;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.location.Geocoder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BaiduMap.SnapshotReadyCallback;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.network.model.ChatMap;
import com.linkb.jstx.util.BitmapUtils;
import com.linkb.jstx.util.StringUtils;
import com.linkb.R;
import com.linkb.jstx.util.BackgroundThreadHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MapLocationActivity extends BaseActivity implements BDLocationListener, SnapshotReadyCallback, OnMapLoadedCallback {
    private static String TAG = MapLocationActivity.class.getName();

    private ChatMap chatMap = new ChatMap();
    private MenuItem button;
    private TextView addressTextView;
    private MapView mapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocClient;

    /** 定位icon
    * */
    private OverlayOptions ooAMarker;
    //实例化一个地理编码查询对象
    private GeoCoder mGeoCoder;
    //设置反地理编码位置坐标
    private ReverseGeoCodeOption mReverseGeoCodeOption;
    /** 附近地理数据
    * */
    private List<PoiInfo> poiInfoList = new ArrayList<>();
//    private PoiListAdapter

    /**
     * 设置一些amap的属性
     */
    @Override
    public void initComponents() {


        addressTextView = findViewById(R.id.text);
        mGeoCoder = GeoCoder.newInstance();
        mReverseGeoCodeOption = new ReverseGeoCodeOption();
        //发起反地理编码请求(经纬度->地址信息)
        mGeoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                Log.d(TAG, "onGetGeoCodeResult" + geoCodeResult.getAddress());
            }

            @Override
            public void onGetReverseGeoCodeResult(final ReverseGeoCodeResult reverseGeoCodeResult) {
                Log.d(TAG, "onGetReverseGeoCodeResult" + reverseGeoCodeResult.getAddress());
                poiInfoList = reverseGeoCodeResult.getPoiList();
                //获取点击的坐标地址
                chatMap.address = reverseGeoCodeResult.getAddress();
                BackgroundThreadHandler.postUIThread(new Runnable() {
                    @Override
                    public void run() {
                        addressTextView.setVisibility(View.VISIBLE);
                        addressTextView.setText(getString(R.string.common_current_location, reverseGeoCodeResult.getAddress()));
                        button.setVisible(true);
                    }
                });
            }
        });
        // 地图初始化
        mapView = findViewById(R.id.mapView);
        mapView.showZoomControls(false);
        mBaiduMap = mapView.getMap();
        mBaiduMap.setOnMapLoadedCallback(this);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //设置拖拽定位功能
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
                Log.d(TAG, "onMapStatusChangeStart" + mapStatus.target.toString());
            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {
                Log.d(TAG, "onMapStatusChangeStart2" + mapStatus.target.toString());
            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
                Log.d(TAG, "onMapStatusChange" + mapStatus.target.toString());
                ooAMarker = new MarkerOptions().position(mapStatus.target).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_location))
                        .zIndex(9).draggable(false);
                mBaiduMap.clear();
                mBaiduMap.addOverlay(ooAMarker);
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                Log.d(TAG, "onMapStatusChangeFinish" + mapStatus.target.toString());
                chatMap.latitude = mapStatus.target.latitude;
                chatMap.longitude = mapStatus.target.longitude;

                mReverseGeoCodeOption.location(mapStatus.target);

                mGeoCoder.reverseGeoCode(mReverseGeoCodeOption);
            }
        });
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setIsNeedAddress(true);
        mLocClient.setLocOption(option);
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onReceiveLocation(final BDLocation location) {
        mLocClient.stop();
        if (location != null) {

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(u);
            ooAMarker = new MarkerOptions().position(ll).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_location))
                    .zIndex(9).draggable(false);
            mBaiduMap.clear();
            mBaiduMap.addOverlay(ooAMarker);


            chatMap.latitude = latitude;
            chatMap.longitude = longitude;
            chatMap.address = location.getAddrStr();


            BackgroundThreadHandler.postUIThread(new Runnable() {
                @Override
                public void run() {
                    addressTextView.setVisibility(View.VISIBLE);
                    addressTextView.setText(getString(R.string.common_current_location, location.getAddrStr()));
                    button.setVisible(true);
                }
            });
        }
    }



    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }


    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

        if (mLocClient != null) {

            mLocClient.unRegisterLocationListener(this);
            mLocClient.stop();
        }
        mLocClient = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_send) {
            int w = Resources.getSystem().getDisplayMetrics().widthPixels; // 得到图片的宽，高
            int h = Resources.getSystem().getDisplayMetrics().heightPixels;
            int cropHeight =   (w * 9 / 16);
            int cropY0 = (h - cropHeight) / 2 - toolbar.getMeasuredHeight();
            int cropY1 = cropY0 + cropHeight;
            final Rect rect = new Rect(0, cropY0, w, cropY1);
            mBaiduMap.snapshotScope(rect, this);
            showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_handle)));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSnapshotReady(Bitmap bitmap) {
        hideProgressDialog();
        File file = new File(LvxinApplication.CACHE_DIR_IMAGE, StringUtils.getUUID());

        if (BitmapUtils.saveMapBitmap2File(bitmap, file)) {
            chatMap.image = file.getName();
            Intent intent = new Intent();
            intent.putExtra(ChatMap.class.getName(), chatMap);
            setResult(Activity.RESULT_OK, intent);
            this.finish();
        }

    }


    @Override
    public int getContentLayout() {

        return R.layout.activity_map_view;
    }


    @Override
    public int getToolbarTitle() {

        return R.string.common_location;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send, menu);
        button = menu.findItem(R.id.menu_send);
        button.setVisible(false);
        mLocClient.start();
        mLocClient.requestLocation();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onMapLoaded() {

        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(14));
    }


}
