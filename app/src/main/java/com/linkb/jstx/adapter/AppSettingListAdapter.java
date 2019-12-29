
package com.linkb.jstx.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.linkb.jstx.app.ClientConfig;
import com.linkb.jstx.bean.AppSetting;
import com.linkb.jstx.adapter.viewholder.AppSettingViewHolder;
import com.linkb.R;

import java.util.List;

public class AppSettingListAdapter extends RecyclerView.Adapter<AppSettingViewHolder> implements CompoundButton.OnCheckedChangeListener {

    private List<AppSetting> list ;

    public AppSettingListAdapter(List<AppSetting> list){
        this.list = list;
    }
    @Override
    public AppSettingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AppSettingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_setting, parent, false));
    }

    @Override
    public void onBindViewHolder(AppSettingViewHolder viewHolder, int position) {
        AppSetting setting = list.get(position);
        viewHolder.hint.setText(setting.hint);
        viewHolder.label.setText(setting.label);
        viewHolder.appSwitch.setTag(setting.key);
        viewHolder.appSwitch.setOnCheckedChangeListener(null);
        viewHolder.appSwitch.setChecked(getSettingSwitch(setting));
        viewHolder.appSwitch.setOnCheckedChangeListener(this);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onCheckedChanged(CompoundButton arg0, boolean flag) {
        Object tag = arg0.getTag();
        ClientConfig.setBooleanConfig(tag.toString(),flag);
    }

    private boolean getSettingSwitch(AppSetting setting) {
        return  ClientConfig.getBooleanConfig(setting.key,Boolean.valueOf(setting.def));
    }
}
