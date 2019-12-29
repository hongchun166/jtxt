package com.linkb.jstx.activity.contact;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.linkb.BuildConfig;
import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.adapter.PhoneContactsListAdapter;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.GlobalEmptyView;
import com.linkb.jstx.model.ContactInfo;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhoneContactsActivity extends BaseActivity {

    @BindView(R.id.emptyView)
    GlobalEmptyView emptyView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;

    private PhoneContactsListAdapter adapter;
    private User user;

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);
        setToolbarTitle(getResources().getString(R.string.phone_contacts));

        user = Global.getCurrentUser();

        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initData(false);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        initData(true);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_common_recyclerview;
    }

    private void initData(boolean enableShowLoading){
        if (enableShowLoading){
            showProgressDialog("");
        }
        new FetchContact().execute(this);
    }

    private class FetchContact extends AsyncTask<Context, Void, ArrayList<ContactInfo>> {

        private final String DISPLAY_NAME = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY : ContactsContract.Contacts.DISPLAY_NAME;

        private final String FILTER = DISPLAY_NAME + " NOT LIKE '%@%'";

        private final String ORDER = String.format("%1$s COLLATE NOCASE", DISPLAY_NAME);

        @SuppressLint("InlinedApi")
        private final String[] PROJECTION = {
                ContactsContract.Contacts._ID,
                DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
        };
        @Override
        protected ArrayList<ContactInfo> doInBackground(Context... params) {
            try {
                ArrayList<ContactInfo> contacts = new ArrayList<>();

                ContentResolver cr = PhoneContactsActivity.this.getContentResolver();
                Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, PROJECTION, FILTER, null, ORDER);
                if (cursor != null && cursor.moveToFirst()) {

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

                        if (contacts.size()  > 200 ){
                            cursor.moveToLast();
                        }
                    } while (cursor.moveToNext());

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

                emptyView.setVisibility(contacts.size() > 0 ? View.GONE : View.VISIBLE);
                // TODO: 2019/2/25 短信内容需要从接口获取
                String shareStr = "";
                if (!TextUtils.isEmpty(user.code)){
                    shareStr = BuildConfig.SHARE_WEB + "?" + user.code.substring(5);
                }
                adapter = new PhoneContactsListAdapter(contacts, PhoneContactsActivity.this, getResources().getString(R.string.invite_contacts_sms_content, shareStr));
                recyclerView.setAdapter(adapter);
            } else {
                // show failure
                // syncFailed();
            }
        }
    }
}
