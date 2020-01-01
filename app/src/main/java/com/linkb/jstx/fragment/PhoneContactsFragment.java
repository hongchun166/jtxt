package com.linkb.jstx.fragment;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkb.BuildConfig;
import com.linkb.R;
import com.linkb.jstx.adapter.PhoneContactsListAdapter;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.GlobalEmptyView;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.database.PhoneContactsRepository;
import com.linkb.jstx.model.ContactInfo;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseResult;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhoneContactsFragment extends LazyLoadFragment {

    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.groupList)
    RecyclerView recyclerView;
    @BindView(R.id.emptyView)
    GlobalEmptyView emptyView;
    private PhoneContactsListAdapter mAdapter;
    private User user;
    /** 数据单页数量
    * */
    private int singlePageNumber = 50;
    /** 数据页数
    * */
    private int mPage = 0;
    private List<ContactInfo> contactList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_contacts, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }
        });
    }

    @Override
    public void requestData() {
        user = Global.getCurrentUser();

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        contactList = PhoneContactsRepository.queryContactsList();
        if (contactList.size() > 0){
            loadDate(contactList);
        }else {
            showProgressDialog("");
            new FetchContacts(mPage).execute(getContext());
        }
    }

    private void loadDate(List<ContactInfo> contacts){
        emptyView.setVisibility(contacts.size() > 0 ? View.GONE : View.VISIBLE);
        // TODO: 2019/2/25 短信内容需要从接口获取

        String shareStr = "";
        if (!TextUtils.isEmpty(user.code)){
            shareStr = BuildConfig.SHARE_WEB + "?" + user.code.substring(5);
        }
        mAdapter = new PhoneContactsListAdapter(contacts, getActivity(), getResources().getString(R.string.invite_contacts_sms_content, shareStr));
        recyclerView.setAdapter(mAdapter);
    }

    private class FetchContacts extends AsyncTask<Context, Void, ArrayList<ContactInfo>> {

        private final String DISPLAY_NAME = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY : ContactsContract.Contacts.DISPLAY_NAME;

        private final String FILTER = DISPLAY_NAME + " NOT LIKE '%@%'";

        private final String ORDER = String.format("%1$s COLLATE NOCASE", DISPLAY_NAME);

        private int page;

        @SuppressLint("InlinedApi")
        private final String[] PROJECTION = {
                ContactsContract.Contacts._ID,
                DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
        };

        public FetchContacts(int page) {
            this.page = page;
        }

        @Override
        protected ArrayList<ContactInfo> doInBackground(Context... params) {
            try {
                ArrayList<ContactInfo> contacts = new ArrayList<>();

                ContentResolver cr = getContext().getContentResolver();
                Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, PROJECTION, FILTER, null, ORDER);
                if (cursor != null && cursor.moveToFirst() ) {
                    do {
                        // get the contact's information
                        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                        Integer hasPhone = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        // get the user's email address
                        String email = null;
                        Cursor ce = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
                        if (ce != null && ce.moveToFirst()) {
                            email = ce.getString(ce.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                            ce.close();
                        }

                        // get the user's phone number
                        String phone = null;
                        if (hasPhone > 0) {
                            Cursor cp = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                            if (cp != null && cp.moveToFirst()) {
                                phone = cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                cp.close();
                            }
                        }

                        // if the user user has an email or phone then add it to contacts
                        if ((!TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                                && !email.equalsIgnoreCase(name)) || (!TextUtils.isEmpty(phone))) {
                            ContactInfo contact = new ContactInfo(id, name, phone);
                            contacts.add(contact);
                        }

                        if (contacts.size()  > 199 ){
                            cursor.moveToLast();
                        }

                    } while (cursor.moveToNext() );

                    // clean up cursor
                    cursor.close();
                }

                return contacts;
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<ContactInfo> contacts) {
            hideProgressDialog();
            if (contacts != null) {
                // success
                PhoneContactsRepository.saveAll(contacts);
                loadDate(contacts);


                flatContact(contacts);
//                if (!ClientConfig.getOneTimeFlagMapFriend()){
//                    ClientConfig.setOneTimeFlagMapFriend(true);
//                    flatContact(contacts);
//                }
            } else {
                // show failure
                // syncFailed();
            }
        }
    }

    /** 获取联系人中已经注册APP的好友
    * */
    private void flatContact(List<ContactInfo> contacts){
        new MappingFriendTask().execute(contacts);

    }

    private class MappingFriendTask extends AsyncTask<List<ContactInfo>, Void, Void>{

        @Override
        protected Void doInBackground(List<ContactInfo>... lists) {
            if (lists[0] == null || lists[0].size() <= 0) return null;

            for (int i = 0; i < lists[0].size(); i++) {
                //如果通讯录中已经注册 则添加好友
                if (FriendRepository.queryFriendExit(lists[0].get(i).phone) == null){
                    HttpServiceManager.addFriend(lists[0].get(i).phone,lists[0].get(i).name, addFriendRequest);
                }
                if (i == lists[0].size() - 1){
                    LvxinApplication.sendLocalBroadcast(new Intent(Constant.Action.ACTION_RELOAD_CONTACTS));
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

        }
    }

//    private class AddContactsFriendsTask extends AsyncTask<Void, Void, Void> {
//
//        private String account ;
//
//        public AddContactsFriendsTask(String account) {
//            this.account = account;
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            HttpServiceManager.addFriend(account,addFriendRequest);
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            LvxinApplication.sendLocalBroadcast(new Intent(Constant.Action.ACTION_RELOAD_CONTACTS));
//        }
//    }

    private HttpRequestListener<BaseResult> addFriendRequest = new HttpRequestListener<BaseResult>() {
        @Override
        public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
            if (result.isSuccess()){

            }else {
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {
        }
    };
}
