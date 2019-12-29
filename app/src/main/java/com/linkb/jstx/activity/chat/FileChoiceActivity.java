
package com.linkb.jstx.activity.chat;


import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.linkb.jstx.adapter.FileChooseViewAdapter;
import com.linkb.jstx.dialog.CustomDialog;
import com.linkb.jstx.listener.OnItemCheckedListener;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.comparator.FileDirectoryComparator;
import com.linkb.jstx.comparator.FileModifiedDescComparator;
import com.linkb.jstx.component.GlobalEmptyView;
import com.linkb.jstx.dialog.FileSourceWindow;
import com.linkb.jstx.listener.OnDialogButtonClickListener;
import com.linkb.jstx.listener.OnModeChangedListener;
import com.linkb.jstx.network.model.ChatFile;
import com.linkb.R;
import com.linkb.jstx.util.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class FileChoiceActivity extends BaseActivity implements OnDialogButtonClickListener, OnItemClickedListener, OnModeChangedListener, OnItemCheckedListener {

    private static int MODE_APP = 0;
    private static int MODE_SDCARD = 1;
    private int mode;
    private FileSourceWindow fileSourceWindow;

    private FileChooseViewAdapter adapter;
    private ArrayList<File> fileList = new ArrayList<>();
    private File currentFile;
    private File currentDir;

    private CustomDialog customDialog;
    private ArrayMap<String, Integer> indexMap = new ArrayMap<>();
    private LinearLayoutManager linearLayoutManager;
    private TextView modeName ;
    private TextView checkedFileInfo ;
    private Button sendButton;
    private FileFilter fileFilter;
    private GlobalEmptyView emptyView;

    @Override
    public void initComponents() {
        setBackIcon(R.drawable.abc_ic_clear_material);
        modeName = findViewById(R.id.modeName);
        checkedFileInfo = findViewById(R.id.fileSizeInfo);
        RecyclerView fileListView = findViewById(R.id.fileListView);
        findViewById(R.id.modeBar).setOnClickListener(this);
        linearLayoutManager = new LinearLayoutManager(this);
        fileListView.setLayoutManager(linearLayoutManager);
        fileListView.setAdapter(adapter = new FileChooseViewAdapter());
        adapter.setOnItemClickedListener(this);
        adapter.setOnItemCheckedListener(this);
        customDialog = new CustomDialog(this);
        customDialog.setOnDialogButtonClickListener(this);
        customDialog.setIcon(R.drawable.icon_dialog_hint);
        customDialog.setMessage((R.string.tip_file_too_large));
        customDialog.hideCancelButton();
        emptyView = findViewById(R.id.emptyView);
        emptyView.setTips(R.string.tips_empty_folder);

        fileSourceWindow = new FileSourceWindow(this);
        fileSourceWindow.setOnModeChangedListener(this);
        fileFilter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.getName().startsWith(".")){
                    return false;
                }
                if (file.isFile() && file.length() == 0){
                    return false;
                }
                return !file.isDirectory() || file.listFiles().length != 0;
            }
        };
        toggleFileMode(MODE_APP);
    }


    private void toggleFileMode(int mode){
        this.mode = mode;
        fileList.clear();
        if (mode == MODE_APP){
            toolbar.setTitle(R.string.label_app_file);
            toolbar.setSubtitle(null);
            modeName.setText(R.string.label_app_file);
            currentDir = new File(LvxinApplication.CACHE_DIR_FILE);
            fileList.addAll(Arrays.asList(currentDir.listFiles(fileFilter)));
            Collections.sort(fileList, new FileModifiedDescComparator());

        }else {
            toolbar.setTitle(R.string.label_sdcard_file);
            modeName.setText(R.string.label_sdcard_file);
            currentDir = Environment.getExternalStorageDirectory();
            fileList.addAll(Arrays.asList(Environment.getExternalStorageDirectory().listFiles(fileFilter)));
            Collections.sort(fileList, new FileDirectoryComparator());

        }
        adapter.addAll(fileList);

        emptyView.toggle(adapter);
    }

    @Override
    public void onBackPressed(){
        if (mode == MODE_APP || Objects.equals(currentDir,Environment.getExternalStorageDirectory())){
            super.onBackPressed();
            return;
        }

        onItemClicked(currentDir.getParentFile(),null);

        Integer currentDirIndex = indexMap.get(currentDir.getAbsolutePath());
        if (currentDirIndex != null && currentDirIndex >= 0) {
            linearLayoutManager.scrollToPositionWithOffset(currentDirIndex, 0);
        }
    }

    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.modeBar) {
            fileSourceWindow.show();
        }
        if (v.getId() == R.id.button) {
            if (currentFile.length() >= 20 * org.apache.commons.io.FileUtils.ONE_MB) {
                customDialog.show();
            } else {
                finishAndSendFile();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.single_button, menu);
        sendButton = menu.findItem(R.id.menu_button).getActionView().findViewById(R.id.button);
        sendButton.setOnClickListener(this);
        sendButton.setText(R.string.common_send);
        sendButton.setEnabled(false);
        return super.onCreateOptionsMenu(menu);
    }

    private void finishAndSendFile() {
        Intent intent = new Intent();
        ChatFile file = new ChatFile();
        file.size = currentFile.length();
        file.name = currentFile.getName();
        file.path = currentFile.getAbsolutePath();
        String postFileName;
        if (currentFile.getName().contains(".")){
            postFileName = currentFile.getName().substring(currentFile.getName().indexOf("."));
        }else {
            postFileName = currentFile.getName();
        }

        file.file = StringUtils.getUUID() + postFileName;
        intent.putExtra(ChatFile.class.getName(), file);
        setResult(Activity.RESULT_OK, intent);
        this.finish();
    }




    @Override
    public int getContentLayout() {
        return R.layout.activity_filechoice;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.label_file_choice;
    }


    @Override
    public void onLeftButtonClicked() {
        customDialog.dismiss();
    }

    @Override
    public void onRightButtonClicked() {
        customDialog.dismiss();
    }


    @Override
    public void onItemClicked(Object obj, View view) {
        currentDir = ((File) obj);

        indexMap.put(currentDir.getParentFile().getAbsolutePath(), linearLayoutManager.findFirstVisibleItemPosition());
        if (Objects.equals(currentDir,Environment.getExternalStorageDirectory())){
            toolbar.setSubtitle(null);
        }else{
            toolbar.setSubtitle(currentDir.getName());
        }
        fileList.clear();
        fileList.addAll(Arrays.asList(currentDir.listFiles(fileFilter)));
        Collections.sort(fileList, new FileDirectoryComparator());
        adapter.addAll(fileList);

        emptyView.toggle(adapter);
    }

    @Override
    public void onModeChanged(int mode) {
        toggleFileMode(mode);
    }

    @Override
    public void onItemChecked(Object obj, View view, boolean checked) {
        if (checked){
            currentFile = (File) obj;
            String size = org.apache.commons.io.FileUtils.byteCountToDisplaySize(currentFile.length());
            checkedFileInfo.setText(getString(R.string.label_checked_info_format,size));
        }else{
            checkedFileInfo.setText(null);
        }
        sendButton.setEnabled(checked);
    }
}
