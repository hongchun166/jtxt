package com.linkb.jstx.model.world_area;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.linkb.jstx.activity.contact.GroupQrCodeActivityV2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorlAreaOpt {
    public interface OnQureyCountryCB{
        void onQureyCountryCB(List<CountryBean> cList);
    }
    public interface OnQureyProvinceCB{
        void onQureyProvinceCB(List<ProvinceBean> cList);
    }
    public interface OnQureyCityCB{
        void onQureyCityCB(List<CityBean> cList);
    }

    List<CountryBean> countryList=new ArrayList<>();
    List<ProvinceBean> provinceList=new ArrayList<>();
    List<CityBean> cityList=new ArrayList<>();

    public List<CountryBean> qureyCountryList(OnQureyCountryCB onQureyCountryCB){
        if(onQureyCountryCB!=null)onQureyCountryCB.onQureyCountryCB(countryList);
        return countryList;
    }
    public List<List<String>> qureyProvinceList(String countryId){
        List<String> listCB=new ArrayList<>();
        List<String> listCBId=new ArrayList<>();
        for (ProvinceBean provinceBean : provinceList) {
            if(provinceBean.getCountry_id().equals(countryId)){
                listCB.add(provinceBean.getCname());
                listCBId.add(provinceBean.getId());
            }
        }
        List<List<String>> listsDou=new ArrayList<>();
        listsDou.add(listCBId);
        listsDou.add(listCB);
        return listsDou;
    }
    public  List<List<String>>  qureyCityList(List<String> provinceList){
        List<List<String>>  listCB=new ArrayList<>();
        for (String pro : provinceList) {
            List<String> cityArr=new ArrayList<>();
            for (CityBean cityBean : cityList) {
                if(cityBean.getState_id().equals(pro)){
                    cityArr.add(cityBean.getCname());
                }
            }
            listCB.add(cityArr);
        }
        return listCB;
    }

    public void loadWorldAreaData(Context context){
       final Context contextFinal=context.getApplicationContext();
        new Thread(new Runnable() {
            @Override
            public void run() {
                WorlAreaOpt worlAreaOpt=new WorlAreaOpt();

                long startTime=System.currentTimeMillis();
                countryList.addAll(worlAreaOpt.getCountryList(contextFinal));
                System.out.println("world_area==2=="+(System.currentTimeMillis()-startTime));

                startTime=System.currentTimeMillis();
                provinceList.addAll(worlAreaOpt.getProvinceList(contextFinal));
                System.out.println("world_area==3=="+(System.currentTimeMillis()-startTime));

                startTime=System.currentTimeMillis();
                cityList.addAll(worlAreaOpt.getCityList(contextFinal));
                System.out.println("world_area==4=="+(System.currentTimeMillis()-startTime));
            }
        }).start();
    }
    /**
     *  guo
     * @param context
     * @return
     */
    private List<CountryBean> getCountryList(Context context){
        CountryResultBean resultBean=getJson(context,"world_area/countries.json",CountryResultBean.class);
        return resultBean.getRECORDS();
    }
    private List<ProvinceBean> getProvinceList(Context context){
        ProvinceResultBean resultBean=getJson(context,"world_area/states.json",ProvinceResultBean.class);
        return resultBean.getRECORDS();
    }
    private List<CityBean> getCityList(Context context){
        CityResultBean resultBean=getJson(context,"world_area/cities.json",CityResultBean.class);
        return resultBean.getRECORDS();
    }
    private List<CountryBean> getCountyList(Context context){
//        CountryResultBean countryResultBean=getJson(context,"world_area/regions.json",CountryResultBean.class);
//        return countryResultBean.getRECORDS();
        return null;
    }
    /*
     *读取assets本地json
     * @param fileName 文件名称
     */
    private  <T> T getJson(Context context, String fileName,Class<T> clazz) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        T resultBean =null;
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
            Gson gson = new Gson();
            resultBean = gson.fromJson(stringBuilder.toString(),clazz);
            System.out.println("world_area==getJson=="+clazz.getSimpleName()+"=="+gson.toJson(resultBean));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultBean;
    }
    

}
