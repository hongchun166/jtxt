package com.linkb.jstx.activity.setting;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gyf.barlibrary.ImmersionBar;
import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.adapter.ModifyLabelAdapterV2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyLabelActivityV2 extends BaseActivity {
    @BindView(R.id.lv_labels)
    ListView lvLabels;

    private ModifyLabelAdapterV2 adapterV2;
    private List<String> datas = new ArrayList<>();
    private int labelItem = 0;

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);
        ImmersionBar.with(this)
                .keyboardEnable(true)
                .statusBarDarkFont(true)
                .init();
        initData();
        initUI();
    }

    private void initData() {
        labelItem = getIntent().hasExtra("labelItem") ? getIntent().getIntExtra("labelItem", 0) : 0;
        datas.add("不限");
        datas.add("金融");
        datas.add("互联网");
        datas.add("VC");
        datas.add("PE");
        datas.add("直销");
        datas.add("保险");
        datas.add("区块链");
        datas.add("比特币");
        datas.add("投资方");
        datas.add("项目方");
        datas.add("创业者");
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_modify_label_v2;
    }

    @OnClick(R.id.back_btn)
    public void onBack() {
        finish();
    }

    private void initUI() {
        adapterV2 = new ModifyLabelAdapterV2(this, datas, labelItem);
        lvLabels.setAdapter(adapterV2);
        lvLabels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapterV2.setSelectedItem(i);
                adapterV2.notifyDataSetChanged();
                String labelName = datas.get(i);
                finish();
            }
        });
    }
}
