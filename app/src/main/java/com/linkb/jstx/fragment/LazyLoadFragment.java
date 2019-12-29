package com.linkb.jstx.fragment;

import android.os.Bundle;

import com.linkb.jstx.activity.base.BaseFragment;

public abstract class LazyLoadFragment extends BaseFragment {

    protected boolean isViewInitiated;
    protected boolean isDataLoaded;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareRequestData();
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        prepareRequestData();
    }
    public abstract void requestData();

    public boolean prepareRequestData() {
        return prepareRequestData(false);
    }
    public boolean prepareRequestData(boolean forceUpdate) {
        if (getUserVisibleHint() && isViewInitiated && (!isDataLoaded || forceUpdate)) {
            requestData();
            isDataLoaded = true;
            return true;
        }
        return false;
    }


}
