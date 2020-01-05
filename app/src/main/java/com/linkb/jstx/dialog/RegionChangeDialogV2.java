package com.linkb.jstx.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.google.gson.Gson;
import com.linkb.R;
import com.linkb.jstx.adapter.wallet.ReginCityAdapterV2;
import com.linkb.jstx.adapter.wallet.RegionProvinceAdapterV2;
import com.linkb.jstx.dialog.base.BaseDialog;
import com.linkb.jstx.network.result.RegionResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RegionChangeDialogV2 extends BaseDialog implements View.OnClickListener {
    public OnRegionChangeListener mListener;
    public RegionResult regionResult;
    private Context mContext;
    private int mType;
    public WheelView pWheelView;
    public WheelView cWheelView;
    public ReginCityAdapterV2 cityAdapterV2;
    public RegionProvinceAdapterV2 provinceAdapterV2;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                updateData();
            }
        }
    };

    public RegionChangeDialogV2(Context context) {
        super(context);
        this.mContext = context;
        initUI();

    }

    private void initUI() {
        setContentView(R.layout.dialog_region_change_v2);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;
        getJson("citycode.json");
        pWheelView = findViewById(R.id.lv_province);
        cWheelView = findViewById(R.id.lv_city);
        initWheelVIew();
    }

    public void updateData() {
        System.out.println("双方都是顺丰到付吧" + regionResult.province.size());
        provinceAdapterV2 = new RegionProvinceAdapterV2(regionResult.province);
        cityAdapterV2 = new ReginCityAdapterV2(regionResult.province.get(0).city);
        pWheelView.setAdapter(provinceAdapterV2);
        cWheelView.setAdapter(cityAdapterV2);

//        notifyAll();
    }

    private void initWheelVIew() {
        pWheelView.setLineSpacingMultiplier(1.0f);
        cWheelView.setLineSpacingMultiplier(1.0f);
        cWheelView.setCyclic(false);
        pWheelView.setCyclic(false);
        pWheelView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int b = pWheelView.getCurrentItem();
                cityAdapterV2 = new ReginCityAdapterV2(regionResult.province.get(b).city);
                cWheelView.setAdapter(cityAdapterV2);
                System.out.println("大师傅撒旦艰苦" + b);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_no:
                updateStatus(0);
                break;
            case R.id.ll_yes:
                updateStatus(1);
                break;
            case R.id.tv_ok:
                if (mListener != null) mListener.marriageStatus(mType);
                break;
        }
    }

    /**
     * 修改所有选中状态
     *
     * @param type 状态 0为未婚1为已婚
     */
    public void updateStatus(int type) {
        this.mType = type;

    }

    /**
     * 读取assets本地json
     *
     * @param fileName 文件名称
     */
    public void getJson(final String fileName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //将json数据变成字符串
                StringBuilder stringBuilder = new StringBuilder();
                try {
                    //获取assets资源管理器
                    AssetManager assetManager = RegionChangeDialogV2.this.mContext.getAssets();
                    //通过管理器打开文件并读取
                    BufferedReader bf = new BufferedReader(new InputStreamReader(
                            assetManager.open(fileName)));
                    String line;
                    while ((line = bf.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    Gson gson = new Gson();
                    regionResult = gson.fromJson(stringBuilder.toString(), RegionResult.class);
                    updateData();
//                    Message message = new Message();
//                    message.what = 1;
//                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 设置婚姻状态选择的回调监听
     *
     * @param checkListener 对象
     */
    public void setOnMarriageCheckListener(OnRegionChangeListener checkListener) {
        this.mListener = checkListener;
    }

    public interface OnRegionChangeListener {
        /**
         * 性别选择的回调监听
         *
         * @param type 0为未婚，1为已婚
         */
        void marriageStatus(int type);
    }

}
