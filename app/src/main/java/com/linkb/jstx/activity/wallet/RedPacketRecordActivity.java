package com.linkb.jstx.activity.wallet;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.adapter.wallet.ContentPagerAdapter;
import com.linkb.jstx.fragment.FriendListFragment;
import com.linkb.jstx.fragment.GroupListFragment;
import com.linkb.jstx.fragment.MineReceivedRedPacketFragment;
import com.linkb.jstx.fragment.MineSendedRedPacketFragment;
import com.linkb.jstx.fragment.PhoneContactsFragment;

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


/**  红包记录类
* */
public class RedPacketRecordActivity extends BaseActivity {

    @BindView(R.id.red_packet_record_indicator)
    MagicIndicator magicIndicator;

    @BindView(R.id.red_packet_record_view_pager)
    ViewPager mViewPager;




    private List<String> mDataList = new ArrayList<>();
    private ContentPagerAdapter mContentPagerAdapter;

    private List<Fragment> mFragmentList= new ArrayList<>();

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);

        Fragment mineReceivedFragment = MineReceivedRedPacketFragment.getInstance();
        Fragment mineSendFragment = MineSendedRedPacketFragment.getInstance();
        mFragmentList.add(mineReceivedFragment);
        mFragmentList.add(mineSendFragment);
        String myReceived = getResources().getString(R.string.mine_received_red_packet);
        String mySended = getResources().getString(R.string.mine_send_red_packet);
        mDataList.add(myReceived);
        mDataList.add(mySended);

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
                simplePagerTitleView.setNormalColor(Color.parseColor("#88ffffff"));
                simplePagerTitleView.setSelectedColor(Color.WHITE);
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
                indicator.setColors(Color.parseColor("#FFFFFF"));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_red_packet_record;
    }

    @OnClick(R.id.back_btn)
    public void onBackBtn() {
        finish();
    }

}
