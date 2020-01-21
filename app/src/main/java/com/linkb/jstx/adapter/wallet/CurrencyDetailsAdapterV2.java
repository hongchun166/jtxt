package com.linkb.jstx.adapter.wallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.linkb.R;
import com.linkb.jstx.network.result.v2.CurrencyInfoBean;

import java.util.List;

public class CurrencyDetailsAdapterV2 extends BaseAdapter {
    private List<CurrencyInfoBean> records;
    private Context context;

    public CurrencyDetailsAdapterV2(List<CurrencyInfoBean> records, Context context) {
        this.records = records;
        this.context = context;
    }

    public void setData(List<CurrencyInfoBean> data) {
        this.records = data;
        notifyDataSetChanged();
    }

    public List<CurrencyInfoBean> getData() {
        return records;
    }

    @Override
    public int getCount() {
        return records == null ? 0 : records.size();
    }

    @Override
    public Object getItem(int i) {
        return records == null ? null : records.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            holder = new Holder();
            view = LayoutInflater.from(context).inflate(R.layout.item_currency_details_v2, null, false);
            holder.header = view.findViewById(R.id.image_header);
            holder.tvName = view.findViewById(R.id.tv_name);
            holder.tvTime = view.findViewById(R.id.tv_time);
            holder.tvAmount = view.findViewById(R.id.tv_amount);
            holder.tvStatus = view.findViewById(R.id.tv_status);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        CurrencyInfoBean data = records.get(i);
        holder.tvName.setText(data.name);
        holder.tvTime.setText(data.time);
        holder.tvAmount.setText(data.amount);
        holder.tvStatus.setText(data.status == 0 ? "完成" : "处理中");
        return view;
    }

    public class Holder {
        public RoundedImageView header;
        TextView tvName;
        TextView tvTime;
        TextView tvAmount;
        TextView tvStatus;
    }
}
