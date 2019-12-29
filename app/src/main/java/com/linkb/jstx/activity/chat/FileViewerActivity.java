
package com.linkb.jstx.activity.chat;


import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.network.CloudFileDownloader;
import com.linkb.jstx.util.AppTools;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.listener.OSSFileDownloadListener;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.network.model.ChatFile;
import com.linkb.R;
import com.linkb.jstx.util.FileURLBuilder;
import com.linkb.jstx.util.FileUtils;
import com.google.gson.Gson;

import java.io.File;
import java.text.DecimalFormat;

public class FileViewerActivity extends BaseActivity implements OSSFileDownloadListener {


    private ChatFile chatFile;
    private File file;
    private DecimalFormat format = new DecimalFormat("0.00");
    private TextView downLoadButton;
    private TextView progressSizeText;
    private TextView progressTxt;
    private ProgressBar fileProgressBar;
    private String threadKey;
    private Message message;

    @Override
    public void initComponents() {
        message = MessageRepository.queryById(this.getIntent().getLongExtra("id",0));
        chatFile =  new Gson().fromJson(message.content, ChatFile.class);

        findViewById(R.id.openFileButton).setOnClickListener(this);
        (downLoadButton = findViewById(R.id.downLoadButton)).setOnClickListener(this);
        findViewById(R.id.stopDownLoadButton).setOnClickListener(this);
        progressSizeText = findViewById(R.id.progressSizeText);
        progressTxt = findViewById(R.id.progressTxt);
        fileProgressBar = findViewById(R.id.fileProgressBar);

        ((TextView) findViewById(R.id.name)).setText(chatFile.name);
        ((TextView) findViewById(R.id.size)).setText(org.apache.commons.io.FileUtils.byteCountToDisplaySize(chatFile.size));
        ((ImageView) findViewById(R.id.icon)).setImageResource(FileUtils.getFileIcon(chatFile.name));

        if (chatFile.getLocalFile() != null && chatFile.getLocalFile().exists()) {
            file = chatFile.getLocalFile();
            findViewById(R.id.openFileButton).setVisibility(View.VISIBLE);
            return;
        }
        file = new File(LvxinApplication.CACHE_DIR_FILE, chatFile.name);
        if (file.exists() && file.length() == chatFile.size) {
            findViewById(R.id.openFileButton).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.path)).setText(getString(R.string.label_file_path_format,file.getAbsolutePath()));
        } else {
            findViewById(R.id.downLoadButton).setVisibility(View.VISIBLE);
        }
        if (file.length() > 0 && file.length() < chatFile.size) {
            findViewById(R.id.downloadPanel).setVisibility(View.VISIBLE);
            downLoadButton.setText(R.string.common_resume_download);
            progressSizeText.setText(getString(R.string.label_file_download, org.apache.commons.io.FileUtils.byteCountToDisplaySize(file.length())) + "/" + org.apache.commons.io.FileUtils.byteCountToDisplaySize(this.chatFile.size));
            progressTxt.setText(format.format((double) file.length() / chatFile.size * 100) + "%");
            fileProgressBar.setProgress((int) (file.length() * 100 / this.chatFile.size));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.openFileButton:
                String filePath = file.getAbsolutePath();

                this.startActivity(FileUtils.getFileIntent(filePath));
                break;
            case R.id.downLoadButton:
                findViewById(R.id.downLoadButton).setVisibility(View.GONE);
                findViewById(R.id.downloadPanel).setVisibility(View.VISIBLE);
                findViewById(R.id.stopDownLoadButton).setVisibility(View.VISIBLE);
                file = new File(LvxinApplication.CACHE_DIR_FILE,chatFile.name);
                AppTools.creatFileQuietly(file);
                file.setLastModified(message.timestamp);
                String url = FileURLBuilder.getChatFileUrl(chatFile.file);
                threadKey = url;
                CloudFileDownloader.asyncDownload(file, url, this);
                break;

            case R.id.stopDownLoadButton:

                CloudFileDownloader.stop(threadKey);
                findViewById(R.id.stopDownLoadButton).setVisibility(View.GONE);
                findViewById(R.id.openFileButton).setVisibility(View.GONE);
                downLoadButton.setText(R.string.common_resume_download);
                downLoadButton.setVisibility(View.VISIBLE);
                break;

        }
    }

    @Override
    public void onDestroy() {
        CloudFileDownloader.stop(threadKey);
        super.onDestroy();
    }

    @Override
    public int getContentLayout() {

        return R.layout.activity_file_viewer;
    }

    @Override
    public int getToolbarTitle() {

        return R.string.label_file_info;
    }


    @Override
    public void onDownloadCompleted(final File file, String currentKey) {
        showToastView(getString(R.string.tip_file_download_complete, file.getAbsolutePath()));
        findViewById(R.id.stopDownLoadButton).setVisibility(View.GONE);
        findViewById(R.id.openFileButton).setVisibility(View.VISIBLE);
        downLoadButton.setVisibility(View.GONE);
        findViewById(R.id.downloadPanel).setVisibility(View.GONE);
        ((TextView) findViewById(R.id.path)).setText(getString(R.string.label_file_path_format,file.getAbsolutePath()));
    }

    @Override
    public void onDownloadFailured(File file, String currentKey) {

        findViewById(R.id.stopDownLoadButton).setVisibility(View.GONE);
        downLoadButton.setText(R.string.common_resume_download);
        downLoadButton.setVisibility(View.VISIBLE);
        FileViewerActivity.this.showToastView(getString(R.string.tip_file_download_failed));

    }

    @Override
    public void onDownloadProgress(String key, float progress) {
        progressSizeText.setText(getString(R.string.label_file_download, org.apache.commons.io.FileUtils.byteCountToDisplaySize((long) (chatFile.size * progress) / 100)) + "/" + org.apache.commons.io.FileUtils.byteCountToDisplaySize(chatFile.size));
        progressTxt.setText(format.format(progress) + "%");
        fileProgressBar.setProgress((int) progress);
    }

}
