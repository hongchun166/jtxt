
package com.linkb.jstx.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkb.jstx.activity.trend.MicroAppActivity;
import com.linkb.jstx.adapter.viewholder.LogoNameViewHolder;
import com.linkb.jstx.model.MicroApp;
import com.linkb.R;
import com.linkb.jstx.util.FileURLBuilder;

import java.util.ArrayList;
import java.util.List;

public class MicroAppLishAdapter extends RecyclerView.Adapter<LogoNameViewHolder> implements View.OnClickListener {

    private List<MicroApp> appList = new ArrayList<>();

    @Override
    public LogoNameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LogoNameViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_micro_app, parent, false));
    }

    @Override
    public void onBindViewHolder(LogoNameViewHolder holder, int position) {

        MicroApp app = appList.get(position);
        holder.name.setText(app.name);
        holder.icon.load(FileURLBuilder.getAppIconUrl(app.code), R.drawable.icon_function_microapp);
        holder.itemView.setTag(app);
        holder.itemView.setOnClickListener(this);
    }

    public void addAll(List<MicroApp> list) {
        appList.clear();
        appList.addAll(list);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return  appList.size();
    }


    @Override
    public void onClick(View v) {
        MicroApp app = (MicroApp) v.getTag();
        Intent intent = new Intent(v.getContext(), MicroAppActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        intent.putExtra(MicroApp.class.getSimpleName(), app);
        v.getContext().startActivity(intent);
    }


}
