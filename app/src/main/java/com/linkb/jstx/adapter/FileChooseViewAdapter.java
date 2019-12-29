
package com.linkb.jstx.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkb.jstx.adapter.viewholder.FileItemViewHolder;
import com.linkb.jstx.listener.OnItemCheckedListener;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.R;
import com.linkb.jstx.util.AppTools;
import com.linkb.jstx.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class FileChooseViewAdapter extends RecyclerView.Adapter<FileItemViewHolder> implements View.OnClickListener {

    private OnItemCheckedListener onItemCheckedListener;
    private ArrayList<File> list = new ArrayList<>();
    private OnItemClickedListener onItemClickedListener;
    private File target;
    private ViewGroup parent;
    public void addAll(ArrayList<File> tempList) {
        list = tempList;
        super.notifyDataSetChanged();
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }



    public void setOnItemCheckedListener(OnItemCheckedListener onItemCheckedListener) {
        this.onItemCheckedListener = onItemCheckedListener;
    }

    @Override
    public FileItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        return new FileItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filechoose, parent, false));
    }

    @Override
    public void onBindViewHolder(FileItemViewHolder holder, int position) {
        File file = list.get(position);
        holder.name.setText(file.getName());

        if (file.isDirectory()) {
            holder.checkBox.setVisibility(View.GONE);
            holder.description.setText(holder.itemView.getContext().getString(R.string.label_file_sum_format,file.listFiles().length));
            holder.icon.setImageResource(R.drawable.icon_folder);
        } else {
            String size = org.apache.commons.io.FileUtils.byteCountToDisplaySize(file.length());
            String time = AppTools.getDateTimeString(file.lastModified());
            holder.description.setText(holder.itemView.getContext().getString(R.string.label_file_desc_format,size,time));
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(Objects.equals(file,target));
            holder.icon.setImageResource(FileUtils.getFileIcon(file.getName()));
        }
        holder.itemView.setTag(R.id.holder,holder);
        holder.itemView.setTag(file);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {
        File file = (File) v.getTag();
        FileItemViewHolder holder = (FileItemViewHolder) v.getTag(R.id.holder);

        if (file.isDirectory()){
            onItemClickedListener.onItemClicked(file, v);
        }else {
            if(Objects.equals(file,target)){
                target = holder.checkBox.isChecked() ? null : target;
                holder.checkBox.setChecked(!holder.checkBox.isChecked());
                onItemCheckedListener.onItemChecked(file,v,holder.checkBox.isChecked());
                return;
            }
            if (target == null) {
                holder.checkBox.setChecked(true);
                target = file;
                onItemCheckedListener.onItemChecked(file,v,holder.checkBox.isChecked());
                return;
            }

            holder.checkBox.setChecked(true);
            View view = parent.findViewWithTag(target);
            if (view != null){
                holder = (FileItemViewHolder) view.getTag(R.id.holder);
                holder.checkBox.setChecked(false);
            }
            target = file;
            onItemCheckedListener.onItemChecked(file,v,true);
        }
    }
}
