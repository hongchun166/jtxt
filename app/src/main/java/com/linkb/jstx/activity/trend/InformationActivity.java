package com.linkb.jstx.activity.trend;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.activity.base.BaseActivityWithoutImmersion;
import com.linkb.jstx.adapter.wallet.ContentPagerAdapter;
import com.linkb.jstx.fragment.ExpressFragment;
import com.linkb.jstx.fragment.NewsFragment;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
* 新版本的资讯页面
* */
public class InformationActivity extends BaseActivityWithoutImmersion {

    @BindView(R.id.information_indicator)
    MagicIndicator magicIndicator;

    @BindView(R.id.information_view_pager)
    ViewPager mViewPager;

    private ContentPagerAdapter mContentPagerAdapter;
    private List<Fragment> mFragmentList= new ArrayList<>();
    private List<String> mDataList = new ArrayList<>();

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);

        String news = getResources().getString(R.string.news);
        String express = getResources().getString(R.string.express);

        NewsFragment newsFragment = new NewsFragment();
        ExpressFragment expressFragment = new ExpressFragment();

        mDataList.add(news);
        mDataList.add(express);
        mFragmentList.add(newsFragment);
        mFragmentList.add(expressFragment);

        mContentPagerAdapter = new ContentPagerAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mContentPagerAdapter);
        initMagicIndicator();
    }

    private void initMagicIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {

                final BadgePagerTitleView badgePagerTitleView = new BadgePagerTitleView(context);

                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                simplePagerTitleView.setNormalColor(Color.parseColor("#000000"));
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.tex_color_blue_1068ed));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                        badgePagerTitleView.setBadgeView(null); // cancel badge when click tab
                    }
                });
                badgePagerTitleView.setInnerPagerTitleView(simplePagerTitleView);


                return badgePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(Color.parseColor("#1068ED"));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_information;
    }

    @OnClick(R.id.back_btn)
    public void onBack(){finish();}
}
