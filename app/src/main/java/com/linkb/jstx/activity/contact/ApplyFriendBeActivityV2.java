package com.linkb.jstx.activity.contact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.HttpServiceManagerV2;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseDataResult;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.FriendApplyBeResult;
import com.linkb.jstx.network.result.FriendListResult;
import com.linkb.jstx.network.result.FriendResult;
import com.linkb.jstx.util.FileURLBuilder;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 别人申请我的好友
 */
public class ApplyFriendBeActivityV2 extends BaseActivity implements HttpRequestListener<FriendApplyBeResult> {

    @BindView(R.id.lv_friend_data)
    RecyclerView recyclerView;

    ApplyBeFriendAdapt mAdapter;
    Unbinder unbinder;

    public static void navToActApplyFriendBe(Context context) {
        Intent intent = new Intent(context, ApplyFriendBeActivityV2.class);
        context.startActivity(intent);
    }

    private Context getContext() {
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_apply_friendbe_v2;
    }

    @Override
    protected void initComponents() {
        unbinder = ButterKnife.bind(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ApplyBeFriendAdapt(this);
        recyclerView.setAdapter(mAdapter);
        loadFriendDate();
    }


    private void loadFriendDate() {
        showProgressDialog("");
        HttpServiceManagerV2.getListFriendApplyV2(Global.getCurrentUser().account, this);

    }

    /**
     * 处理状态（1同意，2拒绝） integer
     *
     * @param friendId
     */
    private void accrptFriend(String friendId) {
        showProgressDialog("");
        HttpServiceManagerV2.agreeFriendV2(Global.getCurrentUser().account, friendId, "1", new HttpRequestListener<BaseResult>() {
            @Override
            public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
                hideProgressDialog();
                if (result.isSuccess()) {
                    showToastView(getString(R.string.operation_suc));
                    loadFriendDate();
                }
            }

            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {
                hideProgressDialog();
                showToastView(getString(R.string.operation_faill));
            }
        });
    }

    private void refuseFriend(String friendId) {
        showProgressDialog("");
        HttpServiceManagerV2.agreeFriendV2(Global.getCurrentUser().account, friendId, "2", new HttpRequestListener<BaseResult>() {
            @Override
            public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
                hideProgressDialog();
                if (result.isSuccess()) {
                    showToastView(getString(R.string.operation_suc));
                    loadFriendDate();
                }
            }

            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {
                hideProgressDialog();
                showToastView(getString(R.string.operation_faill));
            }
        });
    }

    @OnClick(R.id.back_btn)
    public void onEvenBack() {
        finish();
    }

    @Override
    public void onHttpRequestSucceed(FriendApplyBeResult result, OriginalCall call) {
        hideProgressDialog();
        if (result.isSuccess() && result.isNotEmpty()) {
            mAdapter.addDataAll(result.getData());
        } else {
            hideProgressDialog();
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        hideProgressDialog();
    }

    class ApplyBeFriendAdapt extends RecyclerView.Adapter<ApplyBeHodler>  {
        Context context;
        List<FriendApplyBeResult.FriendBeAppLy> mContentList;

        public ApplyBeFriendAdapt(Context context) {
            this.context = context;
            mContentList = new ArrayList<>();
        }

        public void addDataAll(List<FriendApplyBeResult.FriendBeAppLy> data) {
            mContentList.clear();
            mContentList.addAll(data);
            super.notifyDataSetChanged();
        }

        private Context getFmContent() {
            return context;
        }

        @NonNull
        @Override
        public ApplyBeHodler onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
            View view = LayoutInflater.from(getFmContent()).inflate(R.layout.item_apply_friend_be_layout, viewGroup, false);
            ApplyBeHodler hodler = new ApplyBeHodler(view);
            return hodler;
        }
        /**
         * 0未处理
         * 1、已同意
         * 2、已拒绝
         * 3、已过期
         */
        @Override
        public void onBindViewHolder(ApplyBeHodler contactsHolder, int position) {
            FriendApplyBeResult.FriendBeAppLy friend = (FriendApplyBeResult.FriendBeAppLy) mContentList.get(position);
            contactsHolder.friend = friend;
//            contactsHolder.viewHead.load(FileURLBuilder.getUserIconUrl(friend.getId()), R.mipmap.lianxiren, 999);
            contactsHolder.viewApplyUName.setText(friend.getName());
            contactsHolder.itemView.setTag(friend);
            if("0".equals(friend.getState())){
                contactsHolder.viewBtnAccept.setVisibility(View.VISIBLE);
                contactsHolder.viewBtnRefuse.setVisibility(View.VISIBLE);
                contactsHolder.viewHandlerDesc.setVisibility(View.GONE);
            }else if("1".equals(friend.getState())){
                contactsHolder.viewBtnAccept.setVisibility(View.GONE);
                contactsHolder.viewBtnRefuse.setVisibility(View.GONE);
                contactsHolder.viewHandlerDesc.setVisibility(View.VISIBLE);
                contactsHolder.viewHandlerDesc.setText(R.string.be_accept);
            }else if("2".equals(friend.getState())){
                contactsHolder.viewBtnAccept.setVisibility(View.GONE);
                contactsHolder.viewBtnRefuse.setVisibility(View.GONE);
                contactsHolder.viewHandlerDesc.setVisibility(View.VISIBLE);
                contactsHolder.viewHandlerDesc.setText(R.string.be_refuse);
            }else if("3".equals(friend.getState())){
                contactsHolder.viewBtnAccept.setVisibility(View.GONE);
                contactsHolder.viewBtnRefuse.setVisibility(View.GONE);
                contactsHolder.viewHandlerDesc.setVisibility(View.VISIBLE);
                contactsHolder.viewHandlerDesc.setText(R.string.be_timeout);
            }
        }

        @Override
        public int getItemCount() {
            return mContentList.size();
        }
    }

    class ApplyBeHodler extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView viewApplyUName;
        public TextView viewApplyDesc;
        public WebImageView viewHead;
        public Button viewBtnAccept;
        public Button viewBtnRefuse;
        public TextView viewHandlerDesc;
        FriendApplyBeResult.FriendBeAppLy friend;

        public ApplyBeHodler(View itemView) {
            super(itemView);
            viewApplyUName = itemView.findViewById(R.id.viewApplyUName);
            viewApplyDesc = itemView.findViewById(R.id.viewApplyDesc);
            viewHead = itemView.findViewById(R.id.viewHead);
            viewBtnAccept = itemView.findViewById(R.id.viewBtnAccept);
            viewBtnRefuse = itemView.findViewById(R.id.viewBtnRefuse);
            viewHandlerDesc = itemView.findViewById(R.id.viewHandlerDesc);
            viewBtnAccept.setOnClickListener(this);
            viewBtnRefuse.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.viewBtnAccept:
                    accrptFriend(friend.getId());
                    break;
                case R.id.viewBtnRefuse:
                    refuseFriend(friend.getId());
                    break;
            }
        }
    }
}
