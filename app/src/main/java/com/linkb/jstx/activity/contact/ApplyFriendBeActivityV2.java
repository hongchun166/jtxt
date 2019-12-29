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
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.FriendListResult;
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
public class ApplyFriendBeActivityV2 extends BaseActivity implements HttpRequestListener<FriendListResult> {

    @BindView(R.id.lv_friend_data)
    RecyclerView recyclerView;

    ApplyBeFriendAdapt mAdapter;
    Unbinder unbinder;
    public static void navToActApplyFriendBe(Context context){
        Intent intent=new Intent(context,ApplyFriendBeActivityV2.class);
        context.startActivity(intent);
    }
    private Context getContext(){
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(unbinder!=null){
            unbinder.unbind();
            unbinder=null;
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_apply_friendbe_v2;
    }
    @Override
    protected void initComponents() {
        unbinder=ButterKnife.bind(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter=new ApplyBeFriendAdapt(this);
        recyclerView.setAdapter(mAdapter);
        loadFriendDate();
    }


    private void loadFriendDate(){
        ArrayList<FriendListResult.FriendShip> data=new ArrayList<>();
        {
            FriendListResult.FriendShip friendShip=new FriendListResult.FriendShip();
            friendShip.setName("狐狸");
            friendShip.setId("111");
            data.add(friendShip);
        }
        {
            FriendListResult.FriendShip friendShip=new FriendListResult.FriendShip();
            friendShip.setName("梧桐");
            friendShip.setId("222");
            data.add(friendShip);
        }

        mAdapter.addDataAll(data);
    }

    @OnClick(R.id.viewback)
    public void onEvenBack(){
        finish();
    }
    @Override
    public void onHttpRequestSucceed(FriendListResult result, OriginalCall call) {

    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {

    }

    static class ApplyBeFriendAdapt extends RecyclerView.Adapter<ApplyBeHodler> implements View.OnClickListener {
        Context context;
        List<FriendListResult.FriendShip> mContentList;

        public ApplyBeFriendAdapt(Context context) {
            this.context = context;
            mContentList=new ArrayList<>();
        }
        public void addDataAll(List<FriendListResult.FriendShip> data){
            mContentList.clear();
            mContentList.addAll(data);
            super.notifyDataSetChanged();
        }
        private Context  getFmContent(){
            return context;
        }
        @NonNull
        @Override
        public ApplyBeHodler onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
            View view= LayoutInflater.from(getFmContent()).inflate(R.layout.item_apply_friend_be_layout,viewGroup,false);
            ApplyBeHodler hodler=new ApplyBeHodler(view);
            return hodler;
        }

        @Override
        public void onBindViewHolder(ApplyBeHodler contactsHolder, int position) {
            FriendListResult.FriendShip friend = (FriendListResult.FriendShip) mContentList.get(position);
            contactsHolder.viewHead.load(FileURLBuilder.getUserIconUrl(friend.getFriendAccount()), R.mipmap.lianxiren, 999);
            contactsHolder.viewApplyUName.setText(friend.getName());
            contactsHolder.itemView.setTag(friend);
            contactsHolder.viewBtnAccept.setOnClickListener(this);
        }

        @Override
        public int getItemCount() {
            return mContentList.size();
        }

        @Override
        public void onClick(View view) {

        }
    }
    static class ApplyBeHodler extends RecyclerView.ViewHolder{

        public TextView viewApplyUName;
        public TextView viewApplyDesc;
        public WebImageView viewHead;
        public Button viewBtnAccept;

        public ApplyBeHodler(View itemView) {
            super(itemView);
            viewApplyUName = itemView.findViewById(R.id.viewApplyUName);
            viewApplyDesc = itemView.findViewById(R.id.viewApplyDesc);
            viewHead = itemView.findViewById(R.id.viewHead);
            viewBtnAccept = itemView.findViewById(R.id.viewBtnAccept);
        }
    }
}
