
package com.linkb.jstx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.model.MicroServer;
import com.linkb.jstx.database.ChatTopRepository;
import com.linkb.jstx.listener.OnContactHandleListener;
import com.linkb.jstx.model.ChatTop;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.Group;
import com.linkb.jstx.model.MessageSource;
import com.linkb.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//最近对话列表
public class RencentContactsAdapter extends BaseAdapter implements Comparator<MessageSource>, View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    List<MessageSource> dataList = new ArrayList<>();
    boolean isSearchReslut = false;
    private OnContactHandleListener onContactHandleListener;
    private Context context;
    private List<MessageSource> selectedList = new ArrayList<>();
    private List<ChatTop> chatTopList = new ArrayList<>();
    private boolean isMultiselect = false;

    public RencentContactsAdapter(Context c) {
        super();
        this.context = c;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageSource source = dataList.get(position);
        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_choice_rencent_contact, null);
            viewHolder = new ViewHolder();
            viewHolder.name = convertView.findViewById(R.id.name);
            viewHolder.category = convertView.findViewById(R.id.category);
            viewHolder.icon = convertView.findViewById(R.id.icon);
            viewHolder.checkBox = convertView.findViewById(R.id.checkbox);
            convertView.setTag(R.id.target, viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag(R.id.target);
        }
        viewHolder.checkBox.setOnCheckedChangeListener(null);
        convertView.setTag(source);
        viewHolder.name.setTag(source);
        viewHolder.name.setText(source.getName());
        viewHolder.icon.load(source.getWebIcon());
        viewHolder.category.setVisibility(View.GONE);
        if ((position == 0 && source instanceof Friend) && isSearchReslut) {
            viewHolder.category.setText(R.string.common_contact);
            viewHolder.category.setVisibility(View.VISIBLE);
        }
        if (((position == 0 && source instanceof Group) || getFirstIndex(Group.class) == position) && isSearchReslut) {
            viewHolder.category.setText(R.string.label_contacts_group_chat);
            viewHolder.category.setVisibility(View.VISIBLE);
        }
        if (((position == 0 && source instanceof MicroServer) || getFirstIndex(MicroServer.class) == position) && isSearchReslut) {
            viewHolder.category.setText(R.string.label_contacts_public);
            viewHolder.category.setVisibility(View.VISIBLE);
        }
        //显示消息来源头像
        if (source.getWebIcon() == null) {
            viewHolder.icon.setImageResource(source.getDefaultIconRID());
        } else {
            viewHolder.icon.load(source.getWebIcon(), source.getDefaultIconRID());
        }
        if (isMultiselect) {
            viewHolder.checkBox.setVisibility(View.VISIBLE);
        } else {
            viewHolder.checkBox.setVisibility(View.GONE);
        }
        if (selectedList.contains(source)) {
            viewHolder.checkBox.setChecked(true);
        } else {
            viewHolder.checkBox.setChecked(false);
        }
        viewHolder.checkBox.setOnCheckedChangeListener(this);
        viewHolder.checkBox.setTag(source);
        convertView.setOnClickListener(this);
        return convertView;
    }


    private int getFirstIndex(Class source) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getClass() == source) {
                return i;
            }
        }
        return -1;
    }

    public void setMultiSelect() {
        isMultiselect = true;
        notifyDataSetChanged();
    }

    public void setSinglSelect() {
        isMultiselect = false;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    public List<MessageSource> getSelectedList() {
        return selectedList;
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void addAll(List<MessageSource> list) {
        addAll(list, false);
    }

    public void addAll(List<MessageSource> list, boolean isSearchReslut) {
        if (list == null || list.isEmpty()) {
            dataList.clear();
            super.notifyDataSetChanged();
            return;
        }
        this.isSearchReslut = isSearchReslut;
        dataList.clear();
        this.dataList.addAll(list);
        chatTopList.addAll(ChatTopRepository.queryList());
        Collections.sort(dataList, this);
        super.notifyDataSetChanged();
    }


    @Override
    public boolean isEmpty() {
        return dataList.isEmpty();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.item_contact) {
            if (isMultiselect) {
                CheckBox checkbox = v.findViewById(R.id.checkbox);
                checkbox.setChecked(!checkbox.isChecked());
            } else {
                onContactHandleListener.onContactClicked((MessageSource) v.getTag());
            }
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            selectedList.add((MessageSource) buttonView.getTag());
            onContactHandleListener.onContactSelected((MessageSource) buttonView.getTag());
        } else {
            selectedList.remove(buttonView.getTag());
            onContactHandleListener.onContactCanceled((MessageSource) buttonView.getTag());
        }
    }

    private int indexOfChatTop(MessageSource chat) {
        int count = chatTopList.size();
        for (int i = 0; i < count; i++) {
            if (chatTopList.get(i).sourceName.equals(chat.getClass().getName()) && chatTopList.get(i).sender.equals(chat.getId())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int compare(MessageSource arg0, MessageSource arg1) {

        int index0 = indexOfChatTop(arg0);
        int index1 = indexOfChatTop(arg1);
        if (index0 >= 0 && index1 >= 0) {
            return (int) (chatTopList.get(index1).sort - chatTopList.get(index0).sort);
        }
        if (index0 >= 0 && index1 < 0) {
            return -1;
        }
        if (index1 >= 0 && index0 < 0) {
            return 1;
        }
        return 0;
    }

    public void setOnContactHandleListener(OnContactHandleListener onContactHandleListener) {
        this.onContactHandleListener = onContactHandleListener;
    }

    private static class ViewHolder {
        TextView category;
        TextView name;
        WebImageView icon;
        CheckBox checkBox;
    }
}
