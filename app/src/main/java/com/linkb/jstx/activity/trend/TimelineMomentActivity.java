
package com.linkb.jstx.activity.trend;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.support.v4.app.SharedElementCallback;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.linkb.jstx.activity.base.CIMMonitorActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.listener.OnListViewRefreshListener;
import com.linkb.jstx.model.Comment;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.result.BasePersonInfoResult;
import com.linkb.jstx.network.result.CommentResult;
import com.linkb.jstx.network.result.MomentListResult;
import com.farsunset.cim.sdk.android.model.Message;
import com.linkb.jstx.activity.util.VideoRecorderActivity;
import com.linkb.jstx.adapter.MomentListViewAdapter;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.CommentListView;
import com.linkb.jstx.component.CustomSwipeRefreshLayout;
import com.linkb.jstx.component.LoadMoreRecyclerView;
import com.linkb.jstx.component.SimpleInputPanelView;
import com.linkb.jstx.component.TimelineMomentView;
import com.linkb.jstx.database.CommentRepository;
import com.linkb.jstx.database.MomentRepository;
import com.linkb.jstx.listener.OnCommentSelectedListener;
import com.linkb.jstx.listener.OnInputPanelEventListener;
import com.linkb.jstx.listener.OnLoadRecyclerViewListener;
import com.linkb.jstx.model.Moment;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.R;
import com.linkb.jstx.util.FileURLBuilder;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TimelineMomentActivity extends CIMMonitorActivity implements OnListViewRefreshListener, OnLoadRecyclerViewListener, OnCommentSelectedListener, OnInputPanelEventListener, OnItemClickedListener, HttpRequestListener {
    private CustomSwipeRefreshLayout swipeRefreshLayout;
    private MomentListViewAdapter adapter;
    private LoadMoreRecyclerView momentListView;
    private User self;
    private int currentPage = 0;
    private SimpleInputPanelView inputPanelView;
    private InnerMomentReceiver mInnerMomentReceiver;

    private CommentListView mCommentListView;
    private int mFullHeight;

    private Comment comment;
    private Moment moment;
    private String transitionName;

    @Override
    public void initComponents() {
        self = Global.getCurrentUser();
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setLimited(true);
        momentListView = findViewById(R.id.circleListView);
        momentListView.setOnLoadEventListener(this);

        inputPanelView = findViewById(R.id.inputPanelView);
        inputPanelView.setOnInputPanelEventListener(this);

        adapter = new MomentListViewAdapter(this);
        adapter.setOnCommentSelectedListener(this);
        momentListView.setAdapter(adapter);
        momentListView.setFooterView(adapter.getFooterView());
        adapter.addAll(MomentRepository.queryFirstPage());

        adapter.getHeaderView().displayIcon(FileURLBuilder.getUserIconUrl(self.account));
        adapter.getHeaderView().showMessageRemind(MessageRepository.queryNewMoments(3));
        adapter.getHeaderView().setOnIconClickedListener(this);
        mInnerMomentReceiver = new InnerMomentReceiver();
        LvxinApplication.registerLocalReceiver(mInnerMomentReceiver, mInnerMomentReceiver.getIntentFilter());

        com.linkb.jstx.model.Message newMomentMsg = MessageRepository.queryNewMomentMessage();
        if (adapter.getItemCount() < Constant.MOMENT_PAGE_SIZE || newMomentMsg != null) {
            swipeRefreshLayout.startRefreshing();
        }
        MessageRepository.deleteByAction(Constant.MessageAction.ACTION_500);

        mFullHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        toolbar.setOnClickListener(this);

        setExitSharedElementCallback(sharedElementCallback);
    }


    @Override
    public void onGetNextPage() {
        currentPage++;
        HttpServiceManager.queryMomentTimeline(currentPage, this);
    }

    @Override
    public void onGetFirstPage() {
        currentPage = Constant.DEF_PAGE_INDEX;
        HttpServiceManager.queryMomentTimeline(currentPage, this);
    }

    @Override
    public void onListViewStartScroll() {
        if (inputPanelView.getVisibility() == View.VISIBLE) {
            inputPanelView.dismiss();
            cleanCommentInfo();
        }
    }


    private void onHttpRequestSucceed(MomentListResult result) {

        swipeRefreshLayout.onRefreshCompleted();
        if (result.isNotEmpty() && currentPage == Constant.DEF_PAGE_INDEX && !adapter.listEquals(result.dataList)) {
            adapter.replaceFirstPage(result.dataList);
            MomentRepository.saveAll(result.dataList);
        }
        if (result.isNotEmpty() && currentPage > Constant.DEF_PAGE_INDEX) {
            adapter.addAll(result.dataList);
        }
        if (result.isEmpty()) {
            currentPage = currentPage > Constant.DEF_PAGE_INDEX ? currentPage-- : currentPage;
        }
        momentListView.showMoreComplete(result.page);
    }

    private void onHttpRequestSucceed(CommentResult result) {
        CommentRepository.add(result.data);
        mCommentListView.addComment(result.data);
        cleanCommentInfo();
    }

    @Override
    public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
        if (result instanceof CommentResult) {
            onHttpRequestSucceed((CommentResult) result);
        }
        if (result instanceof MomentListResult) {
            onHttpRequestSucceed((MomentListResult) result);
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        momentListView.showMoreComplete(null);
        swipeRefreshLayout.onRefreshCompleted();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == MomentPublishActivity.REQUEST_CODE) {
            Moment article = (Moment) data.getSerializableExtra(Moment.class.getName());
            adapter.add(article);
            momentListView.scrollToPosition(0);
        }
        if (resultCode == RESULT_OK && requestCode == VideoRecorderActivity.REQUEST_CODE) {
            data.setClass(this, MomentPublishActivity.class);
            this.startActivityForResult(data, MomentPublishActivity.REQUEST_CODE);
        }
    }

    @Override
    public void onCommentSelected(CommentListView currentView, Moment moment, Comment comment) {

        this.comment = comment;
        this.moment = moment;

        if (!Objects.equals(moment.account, self.account)) {
            String account = comment.account == null ? moment.account : comment.account;
            asynViewName(inputPanelView, account);

//            String name = FriendRepository.queryFriendName(comment.account == null ? moment.account : comment.account);
//            inputPanelView.setHint(getString(R.string.hint_comment, name));
        }
        mCommentListView = currentView;
        int inputPanelHeight = inputPanelView.getPanelHeight();
        inputPanelView.show();
        new Thread(() -> {
            try {
                Thread.sleep(250);
                if (comment.sourceId == 0) {
                    //20为误差
                    runOnUiThread(() -> momentListView.smoothScrollBy(0, mCommentListView.getEndYOnScrenn() - (mFullHeight - inputPanelHeight)));
                } else {
                    runOnUiThread(() -> momentListView.smoothScrollBy(0, inputPanelHeight - (-mFullHeight - mCommentListView.getLastTouchY())));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void asynViewName(final SimpleInputPanelView simpleInputPanelView, String account) {
        HttpServiceManager.queryPersonInfo(account, new HttpRequestListener<BasePersonInfoResult>() {
            @Override
            public void onHttpRequestSucceed(BasePersonInfoResult result, OriginalCall call) {
                if (result.isSuccess()) {
                    Friend friend = User.UserToFriend(result.getData());
                    FriendRepository.save(friend);
                    simpleInputPanelView.setHint(getString(R.string.hint_comment, friend.name));
                }
            }

            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {

            }
        });
    }

    private void performAddCommentRequest(String content) {
        comment.content = content;
        HttpServiceManager.publish(comment, moment.account, this);
    }


    @Override
    public void onSendButtonClicked(String content) {

        performAddCommentRequest(content);

        inputPanelView.dismiss();
    }

    @Override
    public void onMessageInsertAt() {

    }

    @Override
    public void onMessageTextDelete(String txt) {

    }

    @Override
    public void onKeyboardStateChanged(boolean visable) {

    }

    @Override
    public void onPanelStateChanged(boolean switchToPanel) {

    }

    private void cleanCommentInfo() {
        inputPanelView.setHint(null);
        inputPanelView.setContent(null);
    }


    @Override
    public int getContentLayout() {
        return R.layout.activity_trend_circle;
    }


    @Override
    public int getToolbarTitle() {
        return R.string.label_function_moment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_moment_timeline, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_publish) {
            Intent intent = new Intent(this, MomentPublishActivity.class);
            this.startActivityForResult(intent, MomentPublishActivity.REQUEST_CODE);
        }
        if (item.getItemId() == R.id.menu_camera) {
            Intent intent = new Intent(this, VideoRecorderActivity.class);
            this.startActivityForResult(intent, VideoRecorderActivity.REQUEST_CODE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        LvxinApplication.unregisterLocalReceiver(mInnerMomentReceiver);
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        if (view == toolbar) {
            momentListView.scrollToPosition(0);
        }

    }

    @Override
    public void onMessageReceived(Message message) {
        if (message.getAction().equals(Constant.MessageAction.ACTION_501) || message.getAction().equals(Constant.MessageAction.ACTION_502)) {
            Comment comment = new Gson().fromJson(message.getContent(), Comment.class);
            long articleId = comment.targetId;
            TimelineMomentView momentView = momentListView.findViewWithTag(articleId);
            if (momentView != null) {
                if (comment.type.equals(Comment.TYPE_2)) {
                    momentView.addPraise(comment);
                } else {
                    momentView.addComment(comment);
                }
            }
            adapter.getHeaderView().showMessageRemind(MessageRepository.queryNewMoments(3));
        }
    }

    @Override
    public void onItemClicked(Object obj, View view) {
        startActivity(new Intent(this, MineMomentActivity.class));
    }

    public class InnerMomentReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Moment article = (Moment) intent.getSerializableExtra(Moment.class.getName());

            if (intent.getAction().equals(Constant.Action.ACTION_DELETE_MOMENT)) {
                adapter.remove(article);
            }
            if (intent.getAction().equals(Constant.Action.ACTION_REFRESH_MOMENT)) {
                adapter.update(article);
            }
        }

        IntentFilter getIntentFilter() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.Action.ACTION_DELETE_MOMENT);
            filter.addAction(Constant.Action.ACTION_REFRESH_MOMENT);
            return filter;
        }
    }


    SharedElementCallback sharedElementCallback = new SharedElementCallback() {
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (transitionName != null) {
                sharedElements.put(transitionName, momentListView.findViewWithTag(transitionName));
                transitionName = null;
            }
        }
    };

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        transitionName = data.getStringExtra("url");
    }

    /**
     * 重写不做沉浸式处理，交由自己处理
     */
    @Override
    public void setImmersionBar() {

    }
}
