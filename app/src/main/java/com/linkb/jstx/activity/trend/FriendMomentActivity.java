
package com.linkb.jstx.activity.trend;

import android.content.Intent;
import android.support.v4.app.SharedElementCallback;
import android.text.TextUtils;
import android.view.View;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.activity.contact.PersonInfoActivity;
import com.linkb.jstx.adapter.SelfMomentListViewAdapter;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.LoadMoreRecyclerView;
import com.linkb.jstx.database.MomentRepository;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.listener.OnLoadRecyclerViewListener;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.HttpServiceManagerV2;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.MomentListResult;
import com.linkb.jstx.network.result.v2.QueryUserInfoResult;
import com.linkb.jstx.util.FileURLBuilder;

import java.util.List;
import java.util.Map;

public class FriendMomentActivity extends BaseActivity implements OnLoadRecyclerViewListener, OnItemClickedListener,HttpRequestListener<MomentListResult> {

    private SelfMomentListViewAdapter adapter;
    private LoadMoreRecyclerView circleListView;
    private Friend friend;
    private int currentPage = 0;
    private String transitionName;

    @Override
    public void initComponents() {


        friend = (Friend) getIntent().getSerializableExtra(Friend.class.getName());
        setToolbarTitle(friend.getTitle());
        circleListView = findViewById(R.id.circleListView);
        circleListView.setOnLoadEventListener(this);
        adapter = new SelfMomentListViewAdapter(this);
        circleListView.setAdapter(adapter);
        circleListView.setFooterView(adapter.getFooterView());
        adapter.addAll(MomentRepository.queryFirstPage(friend.account, currentPage));
        adapter.getHeaderView().displayIcon(FileURLBuilder.getUserIconUrl(friend.account));
        adapter.getHeaderView().setOnIconClickedListener(this);

        circleListView.showProgressBar();

        HttpServiceManager.queryOtherMomentList(friend.account,currentPage,this);
        setExitSharedElementCallback( sharedElementCallback);

        User self= Global.getCurrentUser();
        HttpServiceManagerV2.queryUserInfo(self.account, friend.account, new HttpRequestListener<QueryUserInfoResult>() {
            @Override
            public void onHttpRequestSucceed(QueryUserInfoResult result, OriginalCall call) {
                if(adapter==null)return;
                if(result.isSuccess() && result.getData().size()>0){
                    String backage=result.getData().get(0).getBackgroudUrl();
                    if(!TextUtils.isEmpty(backage)){
                        adapter.getHeaderView().displayBg(backage);
                    }
                }
            }
            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {

            }
        });
    }




    @Override
    public void onGetNextPage() {
        currentPage++;
        HttpServiceManager.queryOtherMomentList(friend.account,currentPage,this);
    }

    private void switchEmptyView() {
        if (adapter.isEmpty()) {
            findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
            circleListView.hideHintView();
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
        circleListView.showMoreComplete(result.page);
        switchEmptyView();
    }


    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        circleListView.showMoreComplete(null);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_trend_moment_listview;
    }

    @Override
    public void onListViewStartScroll() {

    }

    @Override
    public void onItemClicked(Object obj, View view) {
        Intent intent = new Intent(this, PersonInfoActivity.class);
        intent.putExtra(Friend.class.getName(), friend);
        startActivity(intent);
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
