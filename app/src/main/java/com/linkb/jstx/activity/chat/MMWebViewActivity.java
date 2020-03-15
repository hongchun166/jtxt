
package com.linkb.jstx.activity.chat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.activity.chat.bean.WebViewNavToParam;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.activity.contact.MessageForwardActivity;
import com.linkb.jstx.activity.trend.MomentPublishActivity;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.dialog.EditorRedBagDig;
import com.linkb.jstx.dialog.TextSizeSettingWindow;
import com.linkb.jstx.dialog.TextSizeSettingWindow.OnSizeSelectedListener;
import com.linkb.jstx.listener.AnimationListenerImpl;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManagerV2;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.model.MomentLink;
import com.linkb.R;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.v2.GetEditorInfoResult;
import com.linkb.jstx.network.result.v2.GetRedBagResult;

import org.json.JSONObject;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

public class MMWebViewActivity extends BaseActivity implements OnSizeSelectedListener {
    private WebView webview;
    private WebSettings settings;

    private Button viewGetRedBag;


    WebViewNavToParam navToParam;

    public static WebViewNavToParam createNavToParam(Uri url){
        WebViewNavToParam navToParam=new WebViewNavToParam(url,"");
        return navToParam;
    }

    public static class RedBagGetBean{

       public boolean hasSuc=false;
        public  double number;

    }

    private WebViewClient client = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            navToParam.urlStr=url;
        }

    };
    private ProgressBar loadingProgressBar;
    @SuppressLint("HandlerLeak")
    private Handler progressHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            Animation animation = AnimationUtils.loadAnimation(MMWebViewActivity.this, R.anim.disappear);
            animation.setAnimationListener(new AnimationListenerImpl() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    loadingProgressBar.setVisibility(View.GONE);
                }
            });
            loadingProgressBar.startAnimation(animation);
        }
    };
    private TextSizeSettingWindow textSizeWindow;
    private View backgroundView;
    private WebChromeClient chromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            loadingProgressBar.setProgress(newProgress);
            loadingProgressBar.setVisibility(View.VISIBLE);
            if (newProgress == 100) {
                progressHandler.sendEmptyMessage(0);
            }
            if (newProgress > 50) {
                backgroundView.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            setToolbarTitle(title);
        }
    };

    @Override
    public boolean getSwipeBackEnable(){
        return false;
    }

    @Override
    public void initComponents() {

        setBackIcon(R.drawable.abc_ic_clear_material);

        Bundle bundle=getIntent().getExtras();
        navToParam=bundle.getParcelable("NavToParam");
        navToParam.urlStr =navToParam.url.toString();

        viewGetRedBag=findViewById(R.id.viewGetRedBag);
        webview = findViewById(R.id.webview);
        TextView provider = findViewById(R.id.provider);
        settings = webview.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        webview.setWebViewClient(client);
        webview.setWebChromeClient(chromeClient);
        webview.loadUrl(navToParam.urlStr);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        textSizeWindow = new TextSizeSettingWindow(this, this);
        backgroundView = findViewById(R.id.background);
        try {
            provider.setText(getString(R.string.label_web_provider, new URL(navToParam.urlStr).getHost()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        viewGetRedBag.setOnClickListener(v -> getOrShowRed());
        httpGetEditorInfo();
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_webview;
    }


    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.web_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_copy_url:
                ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setPrimaryClip(ClipData.newPlainText(null, navToParam.urlStr));
                Snackbar.make(webview, R.string.tip_copy_successed, Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.menu_open_browser:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(navToParam.urlStr));
                startActivity(intent);
                break;
            case R.id.menu_share_moment:
                MomentLink link = new MomentLink();
                link.title = webview.getTitle();
                link.link = webview.getUrl();
                Intent intent1 = new Intent(this, MomentPublishActivity.class);
                intent1.putExtra(MomentLink.NAME, link);
                startActivity(intent1);
                break;

            case R.id.menu_share_friend:
                Intent intent2 = new Intent(this, MessageForwardActivity.class);
                com.linkb.jstx.model.Message message = new com.linkb.jstx.model.Message();
                message.sender = Global.getCurrentAccount();
                message.format = Constant.MessageFormat.FORMAT_TEXT;
                message.content = webview.getTitle() + "\n" + webview.getUrl();
                intent2.putExtra(com.linkb.jstx.model.Message.NAME, message);
                startActivity(intent2);
                break;
            case R.id.menu_text_size:
                textSizeWindow.showAtLocation(webview, Gravity.BOTTOM, 0, 0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSizeSelected(int size) {
        settings.setTextZoom(size);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webview.destroy();
        webview = null;
        chromeClient = null;
        client = null;
        textSizeWindow = null;
        progressHandler.removeCallbacksAndMessages(null);
    }



    RedBagGetBean redBagGetBean=new RedBagGetBean();
    private void getOrShowRed(){
        if(redBagGetBean.hasSuc){
            getOrShowRed(true,redBagGetBean.number);
        }else {
            httpGetRedBag();
        }
    }
    private void getOrShowRed(boolean hasSuc,double moneyNum){
        EditorRedBagDig.RedBagBParam param=new EditorRedBagDig.RedBagBParam();
        param.number=moneyNum;
        param.state=hasSuc?EditorRedBagDig.RedBagBParam.STATE_SUC:EditorRedBagDig.RedBagBParam.STATE_FAIL;
        EditorRedBagDig.build().buildDialog(this).setRedBagBParam(param).showDialog();
    }
    private void updateGetRedBagState(){
        if(redBagGetBean.hasSuc){
            String getRedBagHint=getString(R.string.hint_red_receive_ed)+redBagGetBean.number+"KKC";
            viewGetRedBag.setText(getRedBagHint);
            viewGetRedBag.setEnabled(false);
        }else {
            viewGetRedBag.setText(R.string.hint_red_receive);
            viewGetRedBag.setEnabled(true);
        }
    }
    private void httpGetEditorInfo(){
        HttpServiceManagerV2.getEditorInfo(String.valueOf(navToParam.beanId), new HttpRequestListener<GetEditorInfoResult>() {
            @Override
            public void onHttpRequestSucceed(GetEditorInfoResult result, OriginalCall call) {
                if(result.isSuccess() ){
                    if(result.getData().getLottery_amount()!=null){
                        redBagGetBean.hasSuc=true;
                        redBagGetBean.number=result.getData().getLottery_amount().doubleValue();
                        updateGetRedBagState();
                    }
                }
            }
            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {
            }
        });
    }

    private void httpGetRedBag(){
        String account=Global.getCurrentUser().getAccount();
        HttpServiceManagerV2.getRedBag(account, navToParam.beanId, new HttpRequestListener<GetRedBagResult>() {
            @Override
            public void onHttpRequestSucceed(GetRedBagResult result, OriginalCall call) {
                hideProgressDialog();
                if(result.isSuccess()){
                    if(result.getData()!=null){
                        redBagGetBean.hasSuc=true;
                        getOrShowRed(true,result.getData().doubleValue());
                        updateGetRedBagState();
                    }else {
                        getOrShowRed(false,result.getData().doubleValue());
                    }
                }else {
                    showToastView(String.valueOf(result.message));
                }
            }
            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {
                hideProgressDialog();
            }
        });
    }

}
