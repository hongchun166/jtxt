
package com.linkb.jstx.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkb.R;
import com.linkb.jstx.activity.chat.FriendChatActivity;
import com.linkb.jstx.activity.contact.GroupListActivity;
import com.linkb.jstx.activity.contact.MicroServerListActivity;
import com.linkb.jstx.activity.contact.OrganizationActivity;
import com.linkb.jstx.activity.contact.TagListActivity;
import com.linkb.jstx.adapter.viewholder.ContactsHeaderHolder;
import com.linkb.jstx.adapter.viewholder.ContactsViewHolder;
import com.linkb.jstx.adapter.viewholder.TextViewHolder;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.network.result.FriendListResult;
import com.linkb.jstx.util.FileURLBuilder;

import java.util.ArrayList;
import java.util.List;

/** 发送名片
* */
public class ContactsSendCardsAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_CHAR = 1;
    private static final int TYPE_CONTACTS = 2;
    private static final int TYPE_FOOTER = 3;
    private List<Object> mContentList = new ArrayList<>();
    private List<String> mStarMarkList = new ArrayList<>();
    private int mContactsSize;
    private Context mContext;
    private OnItemClickedListener<Object> mListener;


    public ContactsSendCardsAdapter(Context mContext, List<String> starMarkList, OnItemClickedListener<Object> listener) {
        this.mContext = mContext;
        this.mStarMarkList = starMarkList;
        this.mListener = listener;
    }


    public void notifyDataSetChanged(int contactsSize, List<Object> contentList) {
        mContactsSize = contactsSize;
        this.mContentList.clear();
        mContentList.addAll(contentList);
        super.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER)
        {
            return new ContactsHeaderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_contact_header, parent, false));
        }
        if (viewType == TYPE_FOOTER)
        {
            return new TextViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_contact_footer, parent, false));
        }
        if (viewType == TYPE_CHAR)
        {
            return new TextViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_contact_char, parent, false));
        }
        if (viewType == TYPE_CONTACTS)
        {
            return new ContactsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_contact_friend, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContactsHeaderHolder)
        {
            ContactsHeaderHolder headerHolder = (ContactsHeaderHolder) holder;
            headerHolder.accountBar.setOnClickListener(this);
            headerHolder.accountBar.setTag(R.id.accountBar);
            headerHolder.groupBar.setOnClickListener(this);
            headerHolder.groupBar.setTag(R.id.groupBar);
            headerHolder.orgBar.setOnClickListener(this);
            headerHolder.orgBar.setTag(R.id.orgBar);
            headerHolder.tagBar.setOnClickListener(this);
            headerHolder.tagBar.setTag(R.id.tagBar);
            return;
        }

        if (holder instanceof TextViewHolder && position == getItemCount() - 1)
        {
            TextViewHolder textHolder = (TextViewHolder) holder;
            textHolder.textView.setText(textHolder.textView.getContext().getString(R.string.label_contacts_frend_count,mContactsSize));
            return;
        }

        if (holder instanceof ContactsViewHolder)
        {
            final FriendListResult.FriendShip friend = (FriendListResult.FriendShip) mContentList.get(position);
            final ContactsViewHolder contactsHolder = (ContactsViewHolder) holder;

            contactsHolder.icon.load(FileURLBuilder.getUserIconUrl(friend.getFriendAccount()), R.mipmap.lianxiren, 999);
            contactsHolder.name.setText(friend.getName());
            contactsHolder.itemView.setTag(friend);
            contactsHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null){
                        mListener.onItemClicked(friend, contactsHolder.itemView);
                    }
                }
            });
            if (position == getItemCount() - 2){
                contactsHolder.dividerView.setVisibility(View.INVISIBLE);
            }else {
                contactsHolder.dividerView.setVisibility(View.VISIBLE);
            }
            return;
        }

        if (holder instanceof TextViewHolder && mContentList.get(position) instanceof Character){
            TextViewHolder textViewHolder = (TextViewHolder) holder;
            textViewHolder.textView.setText(mContentList.get(position).toString());
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(position == getItemCount() -1){
            return TYPE_FOOTER;
        }
        if (mContentList.get(position) instanceof  FriendListResult.FriendShip)
        {
            return TYPE_CONTACTS;
        }else {
            return TYPE_CHAR;
        }
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mContentList.size() + 1;
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(char section) {
        return mContentList.indexOf(section);
    }


//    private void buildContentList(List<Friend> friendList) {
//
//        List<Friend> topList = new ArrayList<>();
//        for (Friend friend : friendList) {
//            // 汉字转换成拼音
//            if (friend.name != null){
//                friend.fristPinyin = CharacterParser.getFirstPinYinChar(friend.name);
//
//                if (mStarMarkList.contains(friend.account)) {
//                    friend = friend.clone();
//                    friend.fristPinyin = String.valueOf(CharSelectorBar.STAR);
//                    topList.add(friend);
//                }
//            }
//        }
//
//        friendList.addAll(topList);
//        Collections.sort(friendList, new NameAscComparator());
//
//        for (Friend friend : friendList) {
//            Character name = friend.fristPinyin.charAt(0);
//            if (!contentList.contains(name))
//            {
//                contentList.add(name);
//            }
//            contentList.add(friend);
//        }
//    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals( R.id.accountBar))
        {
            v.getContext().startActivity(new Intent(v.getContext(), MicroServerListActivity.class));
        }
        if (v.getTag().equals( R.id.groupBar))
        {
            v.getContext().startActivity(new Intent(v.getContext(), GroupListActivity.class));
        }
        if (v.getTag().equals( R.id.orgBar))
        {
            v.getContext().startActivity(new Intent(v.getContext(), OrganizationActivity.class));
        }
        if (v.getTag().equals( R.id.tagBar))
        {
            v.getContext().startActivity(new Intent(v.getContext(), TagListActivity.class));
        }
//        if (v.getTag() instanceof FriendListResult.FriendShip)
//        {
//            FriendListResult.FriendShip friend = (FriendListResult.FriendShip) v.getTag();
//            Intent intent = new Intent(v.getContext(), FriendChatActivity.class);
//            intent.putExtra(Constant.CHAT_OTHRES_ID, friend.getFriendAccount());
//            intent.putExtra(Constant.CHAT_OTHRES_NAME, friend.getName());
//            v.getContext().startActivity(intent);
//        }
    }

    public void notifyLogoChanged(Friend friend) {
        int index = mContentList.indexOf(friend);
        if (index>=0){
            notifyItemChanged(index+1);
        }
    }


}
