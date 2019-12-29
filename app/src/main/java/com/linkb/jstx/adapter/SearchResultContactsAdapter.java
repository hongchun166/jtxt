
package com.linkb.jstx.adapter;

import android.content.Context;

import com.linkb.jstx.model.MessageSource;

import java.util.List;

public class SearchResultContactsAdapter extends RencentContactsAdapter {

    public SearchResultContactsAdapter(Context c) {
        super(c);
    }

    @Override
    public void addAll(List<MessageSource> list) {
        this.isSearchReslut = true;
        dataList.clear();
        this.dataList.addAll(list);
        super.notifyDataSetChanged();
    }


}
