package com.linkb.jstx.adapter.wallet;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

public class ContentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList ;

    private FragmentManager fragmentManager;

    public ContentPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentManager = fm;
        this.fragmentList = fragmentList;
    }
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }
    @Override
    public int getCount() {
        return fragmentList.size();
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        this.fragmentManager.beginTransaction().show(fragment).commit();
        return fragment;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Fragment fragment = fragmentList.get(position);
        fragmentManager.beginTransaction().hide(fragment).commit();
    }

}
