
package com.linkb.jstx.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.linkb.jstx.app.ClientConfig;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.Location;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.util.MLog;
import com.farsunset.cim.sdk.android.CIMPushManager;
import com.farsunset.cim.sdk.android.model.SentBody;

public class CycleLocationReceiver extends BroadcastReceiver implements BDLocationListener {
    private final static String TAG = CycleLocationReceiver.class.getSimpleName();
    private LocationClient mLocationClient;

    @Override
    public void onReceive(Context context, Intent intent) {
        mLocationClient = new LocationClient(LvxinApplication.getInstance());
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度，默认值gcj02
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(this);
        mLocationClient.start();
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {

        mLocationClient.stop();
        mLocationClient.unRegisterLocationListener(this);
        mLocationClient = null;

        User self = Global.getCurrentUser();
        if (bdLocation != null && self != null) {
            double latitude = bdLocation.getLatitude();
            double longitude = bdLocation.getLongitude();

            SentBody sent = new SentBody();
            sent.setKey("client_cycle_location");
            sent.put("account", self.account);
            sent.put("latitude", String.valueOf(latitude));
            sent.put("longitude", String.valueOf(longitude));
            sent.put("location", bdLocation.getAddrStr());

            if (CIMPushManager.isConnected(LvxinApplication.getInstance())) {
                CIMPushManager.sendRequest(LvxinApplication.getInstance(), sent);
            }

            Location location = new Location();
            location.latitude = latitude;
            location.longitude = longitude;
            location.location = bdLocation.getAddrStr();
            Global.saveLocation(location);

            ClientConfig.setCurrentRegion(bdLocation.getCity());
            MLog.i(TAG, "*******************定位成功:" + bdLocation.getAddrStr());
        }
    }

}
