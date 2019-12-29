
package com.linkb.jstx.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.linkb.R;
import com.linkb.jstx.adapter.viewholder.ContactsViewHolder;
import com.linkb.jstx.adapter.viewholder.TextViewHolder;
import com.linkb.jstx.comparator.FriendShipNameAscComparator;
import com.linkb.jstx.listener.OnContactHandleListener;
import com.linkb.jstx.network.result.FriendListResult;
import com.linkb.jstx.util.CharacterParser;
import com.linkb.jstx.util.FileURLBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContactSelectorAdapter extends RecyclerView.Adapter implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private OnContactHandleListener onContactHandleListener;
    private ArrayList<Object> contentList = new ArrayList<>();
    private ArrayList<FriendListResult.FriendShip> selectedList = new ArrayList<>();
    private static final int TYPE_CHAR = 1;
    private static final int TYPE_CONTACTS = 2;

    private boolean charVisable = true;
    private boolean singleMode =  false;

    public void setCharVisable(boolean charVisable) {
        this.charVisable = charVisable;
    }

    public void setSingleMode(boolean singleMode) {
        this.singleMode = singleMode;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_CHAR)
        {
            return new TextViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_contact_char, parent, false));
        }
        if (viewType == TYPE_CONTACTS)
        {
            return new ContactsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_selector, parent, false));
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof ContactsViewHolder)
        {
            FriendListResult.FriendShip friend = (FriendListResult.FriendShip) contentList.get(position);
            ContactsViewHolder contactsHolder = (ContactsViewHolder) holder;
            contactsHolder.icon.load(FileURLBuilder.getUserIconUrl(friend.getFriendAccount()), R.mipmap.lianxiren);
            contactsHolder.name.setText(friend.getName());
            contactsHolder.itemView.setTag(friend);
            contactsHolder.itemView.setOnClickListener(this);

            contactsHolder.checkBox.setOnCheckedChangeListener(null);
            if (selectedList.contains(friend)){
                contactsHolder.checkBox.setChecked(true);
            }else {
                contactsHolder.checkBox.setChecked(false);
            }

            contactsHolder.checkBox.setOnCheckedChangeListener(this);
            contactsHolder.checkBox.setTag(friend);

            if (singleMode){
                contactsHolder.checkBox.setVisibility(View.INVISIBLE);
            }
            return;
        }

        if (holder instanceof TextViewHolder && contentList.get(position) instanceof Character){
            TextViewHolder textViewHolder = (TextViewHolder) holder;
            String charName = contentList.get(position).toString();
            textViewHolder.textView.setText(charName);
        }

    }

    @Override
    public int getItemViewType(int position) {

        if (contentList.get(position) instanceof FriendListResult.FriendShip)
        {
            return TYPE_CONTACTS;
        }else {
            return TYPE_CHAR;
        }
    }

    public void notifyDataSetChanged(List<FriendListResult.FriendShip> friendList) {
        this.contentList.clear();
        buildContentList(friendList);
        super.notifyDataSetChanged();
    }

    public ArrayList<FriendListResult.FriendShip> getSelectedList() {
        return selectedList;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    public boolean isEmpty() {
        return contentList.isEmpty();
    }


    @Override
    public void onClick(View v) {
        CheckBox checkbox = v.findViewById(R.id.checkbox);
        checkbox.setChecked(!checkbox.isChecked());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {

            selectedList.add((FriendListResult.FriendShip) buttonView.getTag());
            FriendListResult.FriendShip friendShip = (FriendListResult.FriendShip) buttonView.getTag();
            boolean result =  onContactHandleListener.onContactSelected(FriendListResult.FriendShipToFriend(friendShip));
            if (!result){
                buttonView.setChecked(false);
            }
        } else {
            selectedList.remove(buttonView.getTag());
            FriendListResult.FriendShip friendShip = (FriendListResult.FriendShip) buttonView.getTag();
            onContactHandleListener.onContactCanceled(FriendListResult.FriendShipToFriend(friendShip));
        }
    }

    public void setOnContactHandleListener(OnContactHandleListener onContactHandleListener) {
        this.onContactHandleListener = onContactHandleListener;
    }

    private void buildContentList(List<FriendListResult.FriendShip> friendList) {

        for (FriendListResult.FriendShip friend : friendList) {
            // 汉字转换成拼音
            friend.setFristPinyin(CharacterParser.getFirstPinYinChar(friend.getName()));
        }

        Collections.sort(friendList, new FriendShipNameAscComparator());

        if (!charVisable){

            contentList.addAll(friendList);
            return;
        }

        for (FriendListResult.FriendShip friend : friendList) {
            Character name = friend.getFristPinyin().charAt(0);
            if (!contentList.contains(name))
            {
                contentList.add(name);
            }
            contentList.add(friend);
        }
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(char section) {
        return contentList.indexOf(section);
    }

}
