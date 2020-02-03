package com.linkb.jstx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.network.result.v2.ListTagsResult;

import java.util.List;

public class ModifyLabelAdapterV2 extends BaseAdapter {
    public int selectedItem = 0;  //选中的条目
    public Context mContext;
    public List<ListTagsResult.DataBean> data;


    public int getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
    }

    public ModifyLabelAdapterV2(Context context, List<ListTagsResult.DataBean> datas, int selectedItem) {
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
        LabelHolder holder = null;
        if (view == null) {
            holder = new LabelHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_modify_label, null, false);
            holder.tvName = view.findViewById(R.id.tv_label_name);
            holder.imgSelected = view.findViewById(R.id.img_selected);
            view.setTag(holder);
        } else {
            holder = (LabelHolder) view.getTag();
        }
        ListTagsResult.DataBean dataBean=data.get(i);
        holder.tvName.setText(dataBean.getName());
        holder.imgSelected.setVisibility(selectedItem == i ? View.VISIBLE : View.GONE);
        return view;
    }

    public class LabelHolder {
        TextView tvName;
        ImageView imgSelected;
    }
}

