package com.linkb.jstx.adapter.wallet;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.network.result.WithdrawBillResult;
import com.linkb.jstx.network.result.v2.ListMyBalanceFlowResult;
import com.linkb.jstx.util.BigDecimalUtils;
import com.linkb.jstx.util.TimeUtils;

import java.util.List;

public class BillListAdapterV2 extends BaseAdapter {
    private List<ListMyBalanceFlowResult.DataBean> mList;
    private Context mContext;

    public BillListAdapterV2(List<ListMyBalanceFlowResult.DataBean> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    public void setData(List<ListMyBalanceFlowResult.DataBean> data) {
        this.mList = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList == null ? null : mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if (view == null) {
            holder = new Holder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_bill_list, null, false);
            holder.imgType = view.findViewById(R.id.img_type);
            holder.tvType = view.findViewById(R.id.tv_type);
            holder.tvTime = view.findViewById(R.id.tv_time);
            holder.tvAmount = view.findViewById(R.id.tv_amount);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        ListMyBalanceFlowResult.DataBean dataBean = mList.get(i);
        setTypeView(holder.imgType, holder.tvType, dataBean);
        holder.tvTime.setText(dataBean.getAddTimeFinal());

        int amountColor = dataBean.getType() == 0 ? ContextCompat.getColor(mContext, R.color.color_E75B28) :
                ContextCompat.getColor(mContext, R.color.tex_color_gray_333);
        holder.tvAmount.setTextColor(amountColor);
        String amountStr = dataBean.getType() == 0 ? "+" + dataBean.getAmount() + "KKC" : "-" + dataBean.getAmount() + "KKC";
        holder.tvAmount.setText(amountStr);

//        holder.tvTime.setText(TimeUtils.formatTime(dataBean.getAdd_date(), TimeUtils.ALL_FORMAT));
//        int amountColor = dataBean.getRed_type() == 0 ? ContextCompat.getColor(mContext, R.color.color_E75B28) :
//                ContextCompat.getColor(mContext, R.color.tex_color_gray_333);
//        holder.tvAmount.setTextColor(amountColor);
//        String amount = BigDecimalUtils.mul2(String.valueOf(1), String.valueOf(dataBean.getMoney()));
//        String amountStr = dataBean.getRed_type() == 0 ? "+" + amount + "KKC" :
//                "-" + amount + "KKC";
//        holder.tvAmount.setText(amountStr);
        return view;
    }

    /**
     * 设置类型
     */
    private void setTypeView(ImageView imageView, TextView textView, ListMyBalanceFlowResult.DataBean bean) {
        switch (bean.getType()) {
            case 0:
                imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.icon_bill1));
                textView.setText(bean.getRemark());
                break;
            case 1:
                imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.icon_bill2));
                textView.setText(bean.getRemark());
                break;
            case 2:
                imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.icon_bill3));
                textView.setText(bean.getRemark());
                break;
//            case 3:
//                imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.icon_bill4));
//                break;
            default:
                imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.icon_bill4));
                textView.setText(bean.getRemark());
                break;
        }

    }

    public class Holder {
        public ImageView imgType;
        public TextView tvType;
        public TextView tvTime;
        public TextView tvAmount;

    }
}
