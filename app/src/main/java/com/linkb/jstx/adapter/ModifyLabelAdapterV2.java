package com.linkb.jstx.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ModifyLabelAdapterV2 extends BaseAdapter {
    public int selectedItem = 0;  //选中的条目
    public Context mContext;
    public List<String> data;

    public ModifyLabelAdapterV2(Context context, List<String> datas, int selectedItem) {
        this.data = datas;
        this.mContext = context;
        this.selectedItem = selectedItem;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int i) {
        return data == null ? "" : data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    public class LabelHolder {
        TextView tvName;
        ImageView imgSelected;
    }
}

