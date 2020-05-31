package com.linkb.jstx.activity.contact.contracts;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.linkb.R;
import com.linkb.jstx.activity.SelectCountryActivity;
import com.linkb.jstx.model.world_area.CountryBean;
import com.linkb.jstx.model.world_area.WorlAreaOpt;
import com.linkb.jstx.network.result.RegionResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RegionDigOpt implements View.OnClickListener {
    public interface OnRegionDigOptCallback {
        void onRegionDigCountryClick();

        void onRegionSelectSuc(String region);
    }

    protected OptionsPickerView pvCustomOptions;
    private List<String> provinces = new ArrayList<>();
    private List<List<String>> citys = new ArrayList<>();
    OnRegionDigOptCallback onRegionDigOptCallback;
    WorlAreaOpt worlAreaOpt;

    CountryBean countryBean;
    Context context;

    public RegionDigOpt() {

    }

    public void loadData(Context context) {
        this.context = context;
        worlAreaOpt = new WorlAreaOpt();
        worlAreaOpt.loadWorldAreaData(context);
    }

    public void release() {
        this.onRegionDigOptCallback = null;
        this.context = null;
        worlAreaOpt = null;
        if (pvCustomOptions != null) {
            pvCustomOptions.dismiss();
            pvCustomOptions = null;
        }
        this.provinces.clear();
        this.citys.clear();
    }

    private String getString(int resid) {
        return context.getResources().getString(resid);
    }

    private void sendEvenSelectSuc(String region) {
        if (onRegionDigOptCallback != null) {
            onRegionDigOptCallback.onRegionSelectSuc(region);
        }
    }

    private void sendEvenOnClick(View view) {
        if (view.getId() == R.id.viewFinish) {
            pvCustomOptions.returnData();
            pvCustomOptions.dismiss();
        } else if (view.getId() == R.id.viewCancel) {
            pvCustomOptions.dismiss();
        } else if (view.getId() == R.id.viewCountry) {
            if (onRegionDigOptCallback != null) {
                onRegionDigOptCallback.onRegionDigCountryClick();
            }
        } else if (view.getId() == R.id.viewNo) {
            if (onRegionDigOptCallback != null) {
                onRegionDigOptCallback.onRegionSelectSuc("");
            }
            pvCustomOptions.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        sendEvenOnClick(v);
    }

    private Context getContext() {
        return context;
    }

    public void setOnRegionDigOptCallback(OnRegionDigOptCallback onRegionDigOptCallback) {
        this.onRegionDigOptCallback = onRegionDigOptCallback;
    }

    private void showRegionDialog() {
        showRegionDialog(context);
    }
    boolean hasShowNoView=true;
    public void showRegionDialog(Context contex) {
        showRegionDialog(contex,hasShowNoView);
    }
    public void showRegionDialog(Context context,boolean hasShowNoView) {
        this.hasShowNoView=hasShowNoView;
        this.context = context;
        if (pvCustomOptions == null) {
            pvCustomOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    String region = "" + provinces.get(options1) + "-" + citys.get(options1).get(options2);
                    sendEvenSelectSuc(region);
                }
            }).setLayoutRes(R.layout.view_region_options_layout, new CustomListener() {
                @Override
                public void customLayout(View v) {
                    final TextView viewCancel = (TextView) v.findViewById(R.id.viewCancel);
                    final TextView viewFinish = (TextView) v.findViewById(R.id.viewFinish);
                    TextView viewCountry = (TextView) v.findViewById(R.id.viewCountry);
                    TextView viewNo = v.findViewById(R.id.viewNo);
                    viewCountry.setText(countryBean.getCname());
                    viewFinish.setOnClickListener(RegionDigOpt.this);
                    viewCancel.setOnClickListener(RegionDigOpt.this);
                    viewCountry.setOnClickListener(RegionDigOpt.this);
                    if(hasShowNoView){
                        viewNo.setVisibility(View.VISIBLE);
                        viewNo.setOnClickListener(RegionDigOpt.this);
                    }else {
                        viewNo.setVisibility(View.GONE);
                    }
                }
            })
                    .isDialog(false)
                    .setSubmitText(getString(R.string.finish))
                    .setSubmitColor(ContextCompat.getColor(context, R.color.color_2e76e5))
                    .setCancelText(getString(R.string.common_cancel))
                    .setCancelColor(ContextCompat.getColor(context, R.color.divider_color_gray_999999))
                    .setOutSideCancelable(false)
                    .build();
            pvCustomOptions.setSelectOptions(0, 1, 1);
            Dialog mDialog = pvCustomOptions.getDialog();
            if (mDialog != null) {
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        Gravity.BOTTOM);
                params.leftMargin = 0;
                params.rightMargin = 0;
                pvCustomOptions.getDialogContainerLayout().setLayoutParams(params);
                Window dialogWindow = mDialog.getWindow();
                if (dialogWindow != null) {
                    dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                    dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                    dialogWindow.setDimAmount(0.1f);
                }
            }
        }
        pvCustomOptions.setPicker(provinces, citys);
        pvCustomOptions.show();
    }
    public void changeRegion(CountryBean countryBean) {
        changeRegion(countryBean,true);
    }
    public void changeRegion(CountryBean countryBean,boolean hasShowDialog) {
        this.countryBean = countryBean;
        if (pvCustomOptions != null) {
            pvCustomOptions.dismiss();
            pvCustomOptions = null;
        }
        if (provinces != null) provinces.clear();
        if (citys != null) citys.clear();
        loadRegionData();
        if (provinces.size() == 0) {
            if(countryBean!=null)sendEvenSelectSuc(countryBean.getCname());
//            tv_region.setText(countryBean.getCname());
            if (pvCustomOptions != null) {
                pvCustomOptions.dismiss();
                pvCustomOptions = null;
            }
        } else if (citys.size() == 0) {
            if(countryBean!=null)sendEvenSelectSuc(countryBean.getCname());
//            tv_region.setText(countryBean.getCname());
            if (pvCustomOptions != null) {
                pvCustomOptions.dismiss();
                pvCustomOptions = null;
            }
        } else {
            if(hasShowDialog){
                showRegionDialog();
            }
        }
    }

    private void loadRegionData() {
        if (countryBean == null) {
            countryBean = new CountryBean();
            countryBean.setId("44");//中国
            countryBean.setCname("中国");
            countryBean.setName("China");
        }
        if (provinces == null || provinces.size() == 0 || citys == null || citys.size() == 0) {
//            if (countryBean.getId().equals("44")) {
//                getJson("citycode.json");
//            } else {
                List<List<String>> provincesDouList = worlAreaOpt.qureyProvinceList(countryBean.getId());
                provinces = provincesDouList.get(1);
                citys = worlAreaOpt.qureyCityList(provincesDouList.get(0));
                if (provinces.size() == 1 && provinces.get(0).equals("")) {
                    provinces.clear();
                    provinces.add(countryBean.getCname());
                }
//            }
        }
    }

    /**
     * 读取assets本地json
     *
     * @param fileName 文件名称
     */
    private void getJson(final String fileName) {

        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = getContext().getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
            Gson gson = new Gson();
            RegionResult regionResult = gson.fromJson(stringBuilder.toString(), RegionResult.class);
            provinces = new ArrayList<>();
            citys = new ArrayList<>();
            for (int i = 0; i < regionResult.province.size(); i++) {
                provinces.add(regionResult.province.get(i).a);
                List<String> marr = new ArrayList<>();
                for (int j = 0; j < regionResult.province.get(i).city.size(); j++) {
                    marr.add(regionResult.province.get(i).city.get(j).a);
                }
                citys.add(marr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
