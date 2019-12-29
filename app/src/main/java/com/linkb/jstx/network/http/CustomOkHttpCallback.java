
package com.linkb.jstx.network.http;

        import android.content.Intent;

        import com.linkb.jstx.activity.LoginActivityV2;
        import com.linkb.jstx.app.Constant;
        import com.linkb.jstx.app.LvxinApplication;
        import com.linkb.jstx.network.result.BaseResult;
        import com.linkb.jstx.util.BackgroundThreadHandler;
        import com.linkb.jstx.util.MLog;
        import com.google.gson.Gson;

        import org.apache.commons.io.IOUtils;

        import java.io.IOException;

        import okhttp3.Call;
        import okhttp3.Callback;
        import okhttp3.Response;


abstract class CustomOkHttpCallback implements Callback {
    private final static String TAG = CustomOkHttpCallback.class.getSimpleName();
    private Class<? extends BaseResult> mClass;
    private boolean mainThread;
    public CustomOkHttpCallback(Class<? extends BaseResult> clazs,boolean mainThread) {
        mClass = clazs;
        this.mainThread = mainThread;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        onInnerFailure(call, e);
    }

    private void onInnerFailure(final Call call, final Exception e) {
        MLog.e(TAG, "", e);
        if (mainThread){
            BackgroundThreadHandler.postUIThread(new Runnable() {
                @Override
                public void run() {
                    onFailured(call, e);
                }
            });
        }else {
            onFailured(call, e);
        }
    }

    private void onInnerSuccess( final Call call, final BaseResult result){
        if (mainThread){
            BackgroundThreadHandler.postUIThread(new Runnable() {
                @Override
                public void run() {
                    onResponse(call, result);
                }
            });
        }else {
            onResponse(call, result);
        }

    }

    @Override
    public void onResponse(final Call call, Response response) {
        try {
            String data = response.body().string();
            MLog.i(TAG, data);
            if (mClass == null) {
                return;
            }

            final BaseResult baseResult = new Gson().fromJson(data, mClass);

            if (Constant.ReturnCode.CODE_401.equals(String.valueOf(response.code()))) {
                Intent intent = new Intent(LvxinApplication.getInstance(), LoginActivityV2.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                LvxinApplication.getInstance().startActivity(intent);
                return;
            }
            if (baseResult != null){
                onInnerSuccess(call,baseResult);
            }else {
                onInnerFailure(call,new IllegalAccessException());
            }
        } catch (Exception e) {
            onInnerFailure(call, e);
        } finally {
            IOUtils.closeQuietly(response);
        }
    }

    protected abstract void onResponse(Call call, BaseResult response);

    protected abstract void onFailured(Call call, Exception e);

}
