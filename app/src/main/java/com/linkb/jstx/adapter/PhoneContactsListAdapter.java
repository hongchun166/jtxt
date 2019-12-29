package com.linkb.jstx.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.model.ContactInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhoneContactsListAdapter extends RecyclerView.Adapter<PhoneContactsListAdapter.PhoneContactsViewHolder> {

    private String smsInvite ;
    private List<ContactInfo> phoneContacts = new ArrayList<>();
    private Context mContext;

    public PhoneContactsListAdapter(List<ContactInfo> phoneContacts, Context mContext, String sms) {
        this.phoneContacts = phoneContacts;
        this.mContext = mContext;
        this.smsInvite = sms;
    }

    @NonNull
    @Override
    public PhoneContactsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_phone_contact, viewGroup, false);
        return new PhoneContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneContactsViewHolder phoneContactsViewHolder, int i) {
        final ContactInfo contactInfo = phoneContacts.get(i);
        if (contactInfo.name != null && !TextUtils.isEmpty(contactInfo.name)){
            phoneContactsViewHolder.nameTv.setText(contactInfo.name);
            phoneContactsViewHolder.inviteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri smsToUri = Uri.parse("smsto:"  + contactInfo.phone);
                    Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
                    intent.putExtra("sms_body", smsInvite);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return phoneContacts.size();
    }

    public class PhoneContactsViewHolder extends RecyclerView.ViewHolder{

//        @BindView(R.id.icon)
//        WebImageView icon;
        public TextView nameTv;
//        @BindView(R.id.char_index_tv)
//        TextView indexNameTv;
//        @BindView(R.id.char_fly)
//        View indexView;
//        @BindView(R.id.button5)
        public Button inviteBtn;

        public PhoneContactsViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.name);
            inviteBtn = itemView.findViewById(R.id.button5);
        }
    }
}
