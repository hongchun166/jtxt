
package com.linkb.jstx.dialog;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.linkb.jstx.bean.Bucket;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.adapter.AlbumBucketListAdapter;
import com.linkb.R;

import java.util.List;

public class AlbumBucketWindow extends BottomSheetDialog    {
    private AlbumBucketListAdapter adapter;
    public AlbumBucketWindow(Context context, OnItemClickedListener listener) {
        super(context);
        RecyclerView recyclerView = (RecyclerView) LayoutInflater.from(context).inflate(R.layout.layout_album_window, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter = new AlbumBucketListAdapter());
        adapter.setOnItemClickedListener(listener);
        setContentView(recyclerView);
    }
    public void setAlbumBucketList(List<Bucket> list) {
        adapter.addAll(list);
    }
}
