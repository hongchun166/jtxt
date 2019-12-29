
package com.linkb.jstx.activity.chat;


import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.network.model.MapAddress;
import com.linkb.R;

public class MapViewActivity extends BaseActivity implements OnMapLoadedCallback {
    private MapView mapView;
    private BaiduMap mBaiduMap;
    private double latitude;
    private double longitude;
    private String address;

    @Override
    public void initComponents() {

        mapView = findViewById(R.id.mapView);
        mBaiduMap = mapView.getMap();
        mBaiduMap.setOnMapLoadedCallback(this);
        MapAddress mapAddress = (MapAddress) getIntent().getSerializableExtra(MapAddress.class.getName());

        address = mapAddress.name;
        ((TextView) findViewById(R.id.text)).setText(address);
        findViewById(R.id.text).setVisibility(View.VISIBLE);

        latitude = mapAddress.latitude;
        longitude = mapAddress.longitude;

        LatLng llA = new LatLng(latitude, longitude);
        OverlayOptions ooA = new MarkerOptions().position(llA).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_location))
                .zIndex(9).draggable(true);
        mBaiduMap.addOverlay(ooA);

        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(llA);
        mBaiduMap.animateMapStatus(u);
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

    }


    @Override
    public int getContentLayout() {

        return R.layout.activity_map_view;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.common_map;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_icon, menu);
        menu.findItem(R.id.menu_icon).setIcon(R.drawable.icon_map_view);
        menu.findItem(R.id.menu_icon).setTitle(R.string.common_map);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_icon) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + latitude + "," + longitude + "?q=" + address)));
            } catch (Exception e) {
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMapLoaded() {
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(13));
    }
}
