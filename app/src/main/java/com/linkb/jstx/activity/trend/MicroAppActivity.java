
package com.linkb.jstx.activity.trend;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.listener.CloudImageLoadListener;
import com.linkb.jstx.model.MicroApp;
import com.linkb.jstx.network.CloudImageLoaderFactory;
import com.linkb.R;
import com.linkb.jstx.util.FileURLBuilder;
import com.google.gson.Gson;

public class MicroAppActivity extends BaseActivity implements CloudImageLoadListener {
    private WebView webview;
    private WebSettings settings;
    private MicroApp app;
    private AppBarLayout appBar;
    private ValueCallback<Uri[]> uploadMessageAboveL;
    private View loadingView;

    @Override
    protected void initComponents() {
        app = (MicroApp) getIntent().getSerializableExtra(MicroApp.class.getSimpleName());
        initAppBarView();
        initLoadingView();
        initWebView();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_micro_app;
    }


    private void initAppBarView(){

        CloudImageLoaderFactory.get().downloadOnly(FileURLBuilder.getAppIconUrl(app.code),this);
        setBackIcon(R.drawable.abc_ic_clear_material);
        setTitle(app.name);
        ((TextView)toolbar.findViewById(R.id.title)).setText(app.name);
        appBar = findViewById(R.id.appbar);
        if (TextUtils.isEmpty(app.color)){
           return;
        }
        try {
            int color = Color.parseColor(app.color);
            appBar.setBackgroundColor(color);
            setStatusBarColor(color);
        }catch (Exception e){

        }
    }

    public void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }


    private void initLoadingView(){
        loadingView = findViewById(R.id.loadingView);
        ((WebImageView)loadingView.findViewById(R.id.icon)).load(FileURLBuilder.getAppIconUrl(app.code), R.drawable.icon_function_microapp);
        ((TextView)loadingView.findViewById(R.id.name)).setText(app.name);
    }

    private void initWebView(){
        webview = findViewById(R.id.webview);
        settings = webview.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setBlockNetworkImage(false);//解决图片不显示
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");
        webview.addJavascriptInterface(new AppBridge(),"app");
        webview.setWebViewClient(new WebViewClient(){public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {return false;}});
        webview.setWebChromeClient(chromeClient);
        webview.loadUrl(app.url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_icon, menu);
        menu.findItem(R.id.menu_icon).setIcon(R.drawable.icon_menu_minimize);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_icon) {
           moveTaskToBack(true);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 321) {
            uploadMessageAboveL.onReceiveValue(new Uri[]{data.getData()});
            uploadMessageAboveL = null;
        }
        if (resultCode != Activity.RESULT_OK && requestCode == 321) {
            uploadMessageAboveL.onReceiveValue(null);
            uploadMessageAboveL = null;
        }
    }


    @Override
    public void onImageLoadFailure(Object key) {
        setTaskDescription(new ActivityManager.TaskDescription(app.name));
    }

    @Override
    public void onImageLoadSucceed(Object key, Bitmap resource) {
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            setTaskDescription(new ActivityManager.TaskDescription(app.name, resource));
        }
    }

    public class AppBridge extends Object {

        @JavascriptInterface
        public String getToken() {
            return Global.getAccessToken();
        }

        @JavascriptInterface
        public String getUser() {
            User user = Global.getCurrentUser();
            user.password = null;
            return new Gson().toJson(user);
        }
    }

    private WebChromeClient chromeClient = new WebChromeClient(){
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100){
                loadingView.setVisibility(View.GONE);
            }
        }

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams params) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            if (params.getAcceptTypes().length > 0 && !TextUtils.isEmpty(params.getAcceptTypes()[0])){
                intent.setType(params.getAcceptTypes()[0]);
            }else {
                intent.setType("*/*");
            }
            startActivityForResult(Intent.createChooser(intent, getString(R.string.label_file_selector)), 321);
            return true;
        }
    };

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void onBackPressed(){
        finishAndRemoveTask();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((ViewGroup)webview.getParent()).removeAllViews();
        webview.destroy();
        webview = null;
    }

}
