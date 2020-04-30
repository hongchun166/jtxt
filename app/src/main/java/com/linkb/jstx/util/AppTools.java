
package com.linkb.jstx.util;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.dialog.PermissionDialog;
import com.linkb.BuildConfig;
import com.linkb.R;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;


public class AppTools {

    private AppTools() {
    }

    public static String getPackageName(Context context) {
        PackageInfo info;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            // e.printStackTrace();
        }
        return "";
    }

    public static String howTimeAgo(long when) {
        GregorianCalendar then = new GregorianCalendar();
        then.setTimeInMillis(when);

        GregorianCalendar now = new GregorianCalendar();

        int format_flags = DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL;

        if (then.get(Calendar.YEAR) != now.get(Calendar.YEAR)) {
            format_flags |= DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE;
        } else if (then.get(Calendar.DAY_OF_YEAR) != now.get(Calendar.DAY_OF_YEAR)) {
            format_flags |= DateUtils.FORMAT_SHOW_DATE;
        }

        return DateUtils.formatDateTime(LvxinApplication.getInstance(), when, format_flags);
    }

    public static CharSequence getRecentTimeString(long when) {
        return DateUtils.getRelativeTimeSpanString(when, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE);
    }

    public static void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    public static Bitmap toGrayscale(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap faceIconGreyBitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(faceIconGreyBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(
                colorMatrix);
        paint.setColorFilter(colorMatrixFilter);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return faceIconGreyBitmap;
    }


    public static String getDateTimeString(long t) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(t));
    }

    public static String getDay(long t) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        return sdf.format(new Date(t));
    }

    public static String getMonth(long t) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        return sdf.format(new Date(t));
    }


    public static boolean isNetworkConnected() {
        try {
            ConnectivityManager nw = (ConnectivityManager) LvxinApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = nw.getActiveNetworkInfo();
            return networkInfo != null;

        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
    }

    public static boolean isWifiConnected() {
        try {
            ConnectivityManager nw = (ConnectivityManager) LvxinApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = nw.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
        } catch (Exception e) {
        }
        return false;
    }

    private static boolean checkCameraPermission(AppCompatActivity context) {

        if (EasyPermissions.hasPermissions(context,Manifest.permission.CAMERA)){
            return true;
        }
        List<String> permissions = new ArrayList<>();
        permissions.add( Manifest.permission.CAMERA);
        if (EasyPermissions.somePermissionPermanentlyDenied(context,permissions)) {
            PermissionDialog.create(context).show();
            return false;
        }
        EasyPermissions.requestPermissions(context, context.getString(R.string.tip_permission_camera_disable), 8, Manifest.permission.CAMERA);
        return false;

    }

    public static void startCameraActivity(AppCompatActivity context) {

        if (!checkCameraPermission(context)) {
            return;
        }

        File targetFile = new File(LvxinApplication.CACHE_DIR_IMAGE + "/" + System.currentTimeMillis() + ".jpg");
        Global.setPhotoGraphFilePath(targetFile.getAbsolutePath());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = getUriFromFile(targetFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(targetFile));
        }

        try {
            context.startActivityForResult(intent, Constant.RESULT_CAMERA);
        } catch (ActivityNotFoundException e) {
            MLog.e("startCameraActivity", "ActivityNotFoundException", e);
        }

    }


    public static void startWallpaperCrop(AppCompatActivity context, Uri uri) {

        File targetFile = new File(LvxinApplication.CACHE_DIR_IMAGE, StringUtils.getUUID() + ".jpg");
        Global.setCropPhotoFilePath(targetFile.getAbsolutePath());

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1.8);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 720);
        intent.putExtra("outputY", 400);
        intent.putExtra("scale", false);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(targetFile));

        context.startActivityForResult(intent, Constant.RESULT_ZOOM);

    }

    public static void startIconPhotoCrop(AppCompatActivity context, Uri uri) {
        startIconPhotoCrop(context,uri,Constant.RESULT_ZOOM);
    }
    public static void startIconPhotoCrop(AppCompatActivity context, Uri uri,int requestCode) {
        startIconPhotoCrop(context, uri, 256,256,requestCode);
    }
    public static void startIconPhotoCrop(AppCompatActivity context, Uri uri,int w,int h,int requestCode) {

        File targetFile = new File(LvxinApplication.CACHE_DIR_IMAGE, StringUtils.getUUID() + ".jpg");
        Global.setCropPhotoFilePath(targetFile.getAbsolutePath());

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", w);
        intent.putExtra("outputY", h);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(targetFile));

        context.startActivityForResult(intent,requestCode);

    }

    /**
     * 基于余弦定理求两经纬度距离
     *
     * @param lon1 第一点的精度
     * @param lat1 第一点的纬度
     * @param lon2 第二点的精度
     * @param lat2 第二点的纬度
     * @return 返回的距离，单位km
     */
    public static String transformDistance(Double lon1, Double lat1, Double lon2, Double lat2) {

        double distance = getDistance(lon1, lat1, lon2, lat2);

        if (distance > 1000) {
            return LvxinApplication.getInstance().getString(R.string.label_format_kilometre, (float) (distance / 1000));
        } else {
            MathContext v = new MathContext(0, RoundingMode.HALF_DOWN);
            BigDecimal d = new BigDecimal(distance, v);
            return LvxinApplication.getInstance().getString(R.string.label_format_metre, d.intValue());
        }

    }

    /**
     * 基于余弦定理求两经纬度距离
     *
     * @param lon1 第一点的精度
     * @param lat1 第一点的纬度
     * @param lon2 第二点的精度
     * @param lat2 第二点的纬度
     * @return 返回的距离，单位m
     */
    public static double getDistance(Double lon1, Double lat1, Double lon2, Double lat2) {

        if (lon1 == null) {
            lon1 = 0d;
        }
        if (lat1 == null) {
            lat1 = 0d;
        }
        if (lon2 == null) {
            lon2 = 0d;
        }
        if (lat2 == null) {
            lat2 = 0d;
        }
        double EARTH_RADIUS = 6378.137;
        double radLat1 = lat1 * Math.PI / 180.0;
        double radLat2 = lat2 * Math.PI / 180.0;
        double a = radLat1 - radLat2;
        double b = lon1 * Math.PI / 180.0 - lon2 * Math.PI / 180.0;

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10;

        return s;
    }


    public static boolean contains(int[] loc, MotionEvent event) {
        return event.getY() >= loc[1] && event.getY() <= loc[3]
                && event.getX() >= loc[0] && event.getX() <= loc[2];
    }


    public static String getSignature(Context context) {
        try {
            /** 通过包管理器获得指定包名包含签名的包信息 **/
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            /******* 通过返回的包信息获得签名数组 *******/
            Signature[] signatures = packageInfo.signatures;
            /******* 循环遍历签名数组拼接应用签名 *******/
            return signatures[0].toCharsString();
            /************** 得到应用签名 **************/
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static int dip2px(float dip) {
        return (int) ((Resources.getSystem().getDisplayMetrics().density * dip) + 0.5f);
    }

    /**
     * 在SD卡上创建目录
     *
     * @param dirName
     */
    public static void creatSDDir(String dirName) {
        File dir = new File(dirName);
        dir.mkdirs();
    }


    public static void creatFileQuietly(File file) {

        if (!file.exists()) {
            try {
                FileUtils.forceMkdirParent(file);
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getFilePath(Uri uri, ContentResolver contentResolver) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};
        Cursor cursor = contentResolver.query(uri, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
            cursor.close();
        } else {
            filePath = uri.getPath();
        }
        return filePath;
    }

    public static Uri getUriFromFile(File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authority = BuildConfig.APPLICATION_ID + ".fileprovider";
            Uri uri = FileProvider.getUriForFile(LvxinApplication.getInstance(), authority, file);


            return uri;
        } else {
            return (Uri.fromFile(file));
        }
    }

    public static void closeQuietly(final Cursor closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception ioe) {
        }
    }

    public static Bitmap getVideoThumbnail(File video) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(video.getAbsolutePath());
        Bitmap bitmap = media.getFrameAtTime();
        return bitmap;
    }

    public static File getVideoThumbnailFile(File video) {
        Bitmap bitmap = AppTools.getVideoThumbnail(video);
        String imageName = video.getName().substring(0, video.getName().length() - 4);
        File imageFile = new File(LvxinApplication.CACHE_DIR_VIDEO, imageName + ".jpg");
        Bitmap thumbBitmap = BitmapUtils.saveVideoThumbBitmap2File(bitmap, imageFile);
        thumbBitmap.recycle();
        bitmap.recycle();
        return imageFile;
    }

    public static int getNavigationBarHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        int height = dm.heightPixels;
        display.getRealMetrics(dm);
        int realHeight = dm.heightPixels;
        return realHeight - height;
    }

    public static void setCursorDrawable(View editText,@DrawableRes  int id) {
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(editText,id);
        } catch (Exception ignored) {
        }
    }

    public static void setCheckBoxState(View view,boolean check) {
        if (view instanceof CheckBox){
            (( CheckBox)view).setChecked(check);
        }
    }

    public static void showToastView(Context context,String text) {
//        View toastView = LayoutInflater.from(context).inflate(R.layout.layout_toast_view, null);
//        ((TextView) toastView.findViewById(R.id.text)).setText(text);
//        Toast toast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
//        toast.setView(toastView);
//        toast.show();
        Toast.makeText(context, text , Toast.LENGTH_SHORT).show();
    }
    public static void showToastView(Context context,int text) {
       showToastView(context,context.getString(text));
    }

    public static boolean isOutOfBounds(Activity context, MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
        final View decorView = context.getWindow().getDecorView();
        return (x < -slop) || (y < -slop) || (x > (decorView.getWidth() + slop)) || (y > (decorView.getHeight() + slop));
    }
}
