
package com.linkb.jstx.activity.trend;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.SharedElementCallback;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.listener.OnLoadRecyclerViewListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.result.MomentListResult;
import com.linkb.jstx.adapter.SelfMomentListViewAdapter;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.component.LoadMoreRecyclerView;
import com.linkb.jstx.database.MomentRepository;
import com.linkb.jstx.model.Moment;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.R;
import com.linkb.jstx.util.FileURLBuilder;

import java.util.List;
import java.util.Map;

public class MineMomentActivity extends BaseActivity implements OnLoadRecyclerViewListener,HttpRequestListener<MomentListResult>{


    private SelfMomentListViewAdapter adapter;
    private LoadMoreRecyclerView circleListView;
    private int currentPage = Constant.DEF_PAGE_INDEX;
    private InnerMomentReceiver mInnerMomentReceiver;
    private String transitionName;
    @Override
    public int getContentLayout() {
        return R.layout.activity_trend_moment_listview;
    }

    @Override
    public void initComponents() {


        User self = Global.getCurrentUser();

        circleListView = findViewById(R.id.circleListView);
        circleListView.setOnLoadEventListener(this);
        adapter = new SelfMomentListViewAdapter(this);
        circleListView.setAdapter(adapter);
        circleListView.setFooterView(adapter.getFooterView());
        adapter.addAll(MomentRepository.queryFirstPage(self.account, currentPage));
        adapter.getHeaderView().displayIcon(FileURLBuilder.getUserIconUrl(self.account));
        adapter.getHeaderView().displayBg(FileURLBuilder.getMomentFileUrl(String.valueOf(self.getBackgroudUrl())));

        circleListView.showProgressBar();
        HttpServiceManager.queryMeMomentList(currentPage,this);

        mInnerMomentReceiver = new InnerMomentReceiver();
        LvxinApplication.registerLocalReceiver(mInnerMomentReceiver, mInnerMomentReceiver.getIntentFilter());

        setExitSharedElementCallback( sharedElementCallback);

    }





    @Override
    public void onDestroy() {
        super.onDestroy();
        LvxinApplication.unregisterLocalReceiver(mInnerMomentReceiver);
    }

    private void switchEmptyView() {
        if (adapter.isEmpty()) {
            findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.emptyView).setVisibility(View.GONE);
        }
    }

    @Override
    public void onHttpRequestSucceed(MomentListResult result, OriginalCall call) {
        if (result.isNotEmpty() && currentPage == Constant.DEF_PAGE_INDEX && !adapter.listEquals(result.dataList)) {
            adapter.replaceFirstPage(result.dataList);
            MomentRepository.saveAll(result.dataList);
        }

        if (result.isNotEmpty() && currentPage > Constant.DEF_PAGE_INDEX) {
            adapter.addAll(result.dataList);
        }

        if (!result.isNotEmpty() && currentPage > Constant.DEF_PAGE_INDEX) {
            currentPage--;
        }

        switchEmptyView();

        circleListView.showMoreComplete(result.page);

    }


    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        circleListView.showMoreComplete(null);
    }




    @Override
    public int getToolbarTitle() {
        return R.string.label_moment_me;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_icon, menu);
        menu.findItem(R.id.menu_icon).setIcon(R.drawable.icon_menu_notify);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_icon) {
            this.startActivity(new Intent(this, MomentMessageActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGetNextPage() {
        currentPage++;
        HttpServiceManager.queryMeMomentList(currentPage,this);
    }

    @Override
    public void onListViewStartScroll() {

    }

    public class InnerMomentReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Moment article = (Moment) intent.getSerializableExtra(Moment.class.getName());
            adapter.remove(article);
        }

        IntentFilter getIntentFilter() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.Action.ACTION_DELETE_MOMENT);
            return filter;
        }
    }



    SharedElementCallback sharedElementCallback = new SharedElementCallback() {
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (transitionName != null) {
                sharedElements.put(transitionName,circleListView.findViewWithTag(transitionName));
                transitionName = null;
            }
        }
    };

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        transitionName = data.getStringExtra("url");
    }
}
