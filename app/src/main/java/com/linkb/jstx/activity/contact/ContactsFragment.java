
package com.linkb.jstx.activity.contact;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.linkb.R;
import com.linkb.jstx.activity.QrcodeScanActivity;
import com.linkb.jstx.activity.base.CIMMonitorFragment;
import com.linkb.jstx.activity.wallet.WalletActivityV2;
import com.linkb.jstx.adapter.wallet.ContentPagerAdapter;
import com.linkb.jstx.component.QuickOperationPopupWindow;
import com.linkb.jstx.fragment.FriendListFragment;
import com.linkb.jstx.fragment.GroupListFragment;
import com.linkb.jstx.fragment.PhoneContactsFragment;
import com.linkb.jstx.util.ConvertUtils;

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
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ContactsFragment extends CIMMonitorFragment implements  View.OnClickListener, QuickOperationPopupWindow.QuickOperationClickListener {

    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;

    @BindView(R.id.search_img_btn)
    View searchBtn;

    @BindView(R.id.right_img_btn)
    View expressBtn;

    @BindView(R.id.contact_indicator)
    MagicIndicator magicIndicator;

    @BindView(R.id.contact_view_pager)
    ViewPager mViewPager;

    private List<String> mDataList = new ArrayList<>();
    private ContentPagerAdapter mContentPagerAdapter;

    private List<Fragment> mFragmentList= new ArrayList<>();
    private QuickOperationPopupWindow mQuickOperationPopupWindow;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_contact, container, false);
        toolBarTitle = view.findViewById(R.id.tool_bar_title);
        imageBtn = view.findViewById(R.id.right_img_btn);
        toolBarTitle.setText(R.string.common_contacts);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FriendListFragment friendListFragment = new FriendListFragment();
        GroupListFragment friendListFragment2 = new GroupListFragment();
        PhoneContactsFragment friendListFragment3 = new PhoneContactsFragment();
        mFragmentList.add(friendListFragment);
        mFragmentList.add(friendListFragment2);
        mFragmentList.add(friendListFragment3);

        String blinkFriend = getResources().getString(R.string.my_friend);
        String mineGroup = getResources().getString(R.string.mine_group);
        String phoneContacts = getResources().getString(R.string.phone_contacts);
        mDataList.add(blinkFriend);
        mDataList.add(mineGroup);
        mDataList.add(phoneContacts);

        mContentPagerAdapter = new ContentPagerAdapter(getActivity().getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mContentPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 1){
                    searchBtn.setVisibility(View.VISIBLE);
                }else {
                    searchBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        initMagicIndicator();
    }

    @Override
    public void requestData() {

    }

    private void initMagicIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(getContext());
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
    public void onClick(View view) {
        switch (view.getId()){
//            case R.id.create_group_chat_cly:
//                startActivity(new Intent(view.getContext(), CreateGroupActivity.class));
//                break;
        }
    }

    @OnClick(R.id.right_img_btn)
    public void onExpress(){
        if (mQuickOperationPopupWindow == null){
            mQuickOperationPopupWindow = new QuickOperationPopupWindow(getContext(), this);
        }
        //显示窗口
        mQuickOperationPopupWindow.showAsDropDown(imageBtn, -ConvertUtils.dp2px(105), ConvertUtils.dp2px(0), Gravity.LEFT); //设置layout在PopupWindow中显示的位置
    }

    @OnClick(R.id.search_img_btn)
    public void onSearch(){
        startActivity(new Intent(getActivity(),SearchGroupActivity.class));
    }

    @Override
    public void createGroup() {
        startActivity(new Intent(getActivity(), CreateGroupActivity.class));
    }

    @Override
    public void addFriends() {
        startActivity(new Intent(getActivity(), SearchFriendActivityV2.class));
    }

    @Override
    public void mineWallet() {
        startActivity(new Intent(getActivity(), WalletActivityV2.class));
    }

    @Override
    public void scanQrCode() {
        requestCodeQRCodePermissions();
    }

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQRCodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(getActivity(), perms)) {
            EasyPermissions.requestPermissions(this, "扫描二维码需要打开相机和散光灯的权限", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        }else {
            startActivity(new Intent(getActivity(), QrcodeScanActivity.class));
        }
    }

}
