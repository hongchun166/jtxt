# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/lishiting/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#---------------------------------基本指令区----------------------------------
-optimizationpasses 5 # 指定优化5次
-dontskipnonpubliclibraryclassmembers
-printmapping proguardMapping.pro
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses,Signature,EnclosingMethod
-allowaccessmodification
-dontpreverify
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
-dontoptimize #指定不要优化class(jpush sdk)
-ignorewarnings
#----------------------------------------------------------------------------

#========================通用==========================
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
# 混淆
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
# Understand the @Keep support annotation.
-keep class android.support.annotation.Keep

-keep @android.support.annotation.Keep class * {*;}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <methods>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <fields>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <init>(...);
}
#发布时去掉Log日志
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}
# support design
-dontwarn android.support.design.**
-dontnote android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

#android support v4
-dontnote android.support.**
-dontwarn android.support.**
-keep class android.support.** {*;}
-dontwarn android.support.multidex.**
-keep class android.support.multidex.**{
    *;
}
-dontnote android.support.v4.**
-dontwarn android.support.v4.**
-keep class android.support.v4.**{
    *;
}
#android support v7
-dontwarn android.support.v7.**
-keep class android.support.v7.** {
    *;
}
#系统级
-keep public class com.google.vending.licensing.ILicensingService
-dontnote com.google.vending.licensing.ILicensingService

-keep public class com.android.vending.licensing.ILicensingService
-dontnote com.android.vending.licensing.ILicensingService

-keep public class * extends android.app.Activity                               # 保持哪些类不被混淆
-keep public class * extends android.app.Application                            # 保持哪些类不被混淆
-keep public class * extends android.app.Service                                # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver                  # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider                    # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper               # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference                      # 保持哪些类不被混淆
-keep public class * extends android.os.IInterface
-keep public class * extends android.view.View

#-keep public class * extends android.view.View{
#    *** get*();
#    void set*(***);
#    public (android.content.Context);
#    public (android.content.Context, android.util.AttributeSet);
#    public (android.content.Context, android.util.AttributeSet, int);
#}

-keepclassmembers class * {
    void *(**On*Event);
}
-keepclasseswithmembernames class * {                                           # 保持 native 方法不被混淆
    native <methods>;
}
-keepclassmembers class * extends android.app.Activity {                        # 保持自定义控件类不被混淆
   public void *(android.view.View);
}

-keepclassmembers enum * {                                                      # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {                                # 保持 Parcelable 不被混淆
    public static final android.os.Parcelable$Creator *;
    public static final ** CREATOR;
}

#-keepclasseswithmembers class * {                                               # 保持自定义控件类不被混淆
#    public (android.content.Context, android.util.AttributeSet);
#    public (android.content.Context, android.util.AttributeSet, int);
#}


#需要用到再打开
#-keep public class com.crazyspread.common.BaseFragment
#-keep public class * extends com.crazyspread.common.BaseFragment                # 保持Fragment类不被混淆

#model保持
-keepclassmembers public class * extends android.view.View {
    void set*(%);
    void set*(%, %);
    void set*(%, %, %, %);
    void set*(%[]);
    void set*(**[]);
    void set*(!**Listener);
    % get*();
    %[] get*();
    **[] get*();
    !**Listener get*();
}
#不混淆Serializable的子类
# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keepclassmembers class * {
        *** run();
}
#========================通用==========================

##===================第三方==========================

#==================gson==========================
-dontwarn com.google.**
-keep class com.google.gson.** {*;}
#==================protobuf======================
-dontwarn com.google.**
-keep class com.google.protobuf.** {*;}

##butterknife
#-keep class butterknife.** { *; }
#-dontwarn butterknife.internal.**
#-keep class **$$ViewBinder { *; }
#
#-keepclasseswithmembernames class * {
#    @butterknife.* <fields>;
#}
#
#-keepclasseswithmembernames class * {
#    @butterknife.* <methods>;
#}


##Eventbus混淆
-keepclassmembers class ** {
    public void onEvent*(***);
}
# Only required if you use AsyncExecutor
-keepclassmembers class * extends de.greenrobot.event.util.ThrowableFailureEvent {
    public <init>(java.lang.Throwable);
}
# Don't warn for missing support classes
-dontwarn de.greenrobot.event.util.*$Support
-dontwarn de.greenrobot.event.util.*$SupportManagerFragment


#打包排除文件读写错误
-dontwarn org.codehaus.mojo.animal_sniffer.**
-dontwarn java.nio.file.**
-keep class org.codehaus.mojo.animal_sniffer.**{
*;
}
-keep class java.nio.file.**{
*;}

#RxJava RxAndroid
-dontwarn rx.**
-keep class rx.** { *; }
-dontwarn rx.android.**
-keep class rx.android.** { *; }

#retrofit2
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

#adapter-rxjava
#-dontwarn retrofit2.adapter.rxjava.**
#-keep class retrofit2.adapter.rxjava.** {*;}

#jackson
#-dontwarn com.fasterxml.jackson.annotation.**
#-keep class com.fasterxml.jackson.annotation.** {*;}
#-dontwarn com.fasterxml.jackson.core.**
#-keep class com.fasterxml.jackson.core.** {*;}
#-dontwarn com.fasterxml.jackson.databind.**
#-keep class com.fasterxml.jackson.databind.** {*;}

#Picasso配置
#-dontnote com.squareup.picasso.**
#-dontnote okhttp3.**
#-dontwarn okhttp3.**
#-keep class okhttp3.** { *; }
#-dontwarn com.squareup.picasso.**
#-dontwarn com.squareup.okhttp.**
##okio配置
#-dontwarn okio.**
#-keep class okio.** { *; }

#com.liulishuo.filedownloader
#-dontwarn com.liulishuo.filedownloader.**
#-keep class com.liulishuo.filedownloader.** {*;}

# glide 的混淆代码
#-keep public class * implements com.bumptech.glide.module.GlideModule
#-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
#  **[] $VALUES;
#  public *;
#}
-dontwarn org.apache.http.**
-keep class org.apache.http.** {
*;
}
-dontwarn org.codehaus.jackson.map.ext.JodaDeserializers$**
-keep class org.codehaus.jackson.map.ext.JodaDeserializers$** {
*;
}
-dontwarn org.codehaus.jackson.map.ext.JodaSerializers
-keep class org.codehaus.jackson.map.ext.** {
*;
}
-dontwarn org.codehaus.jackson.map.ext.**
-dontwarn de.greenrobot.event.util.ErrorDialogManager$**
-keep class de.greenrobot.event.util.ErrorDialogManager$** {
*;
}
-dontwarn fm.jiecao.jcvideoplayer_lib.**
-keep  class fm.jiecao.jcvideoplayer_lib.** {
*;
}
# banner 的混淆代码
-keep class com.youth.banner.** {
    *;
 }

  #okgo
-dontwarn com.lzy.okgo.**
-keep class com.lzy.okgo.**{*;}
  #okrx
-dontwarn com.lzy.okrx.**
-keep class com.lzy.okrx.**{*;}

  #okserver
-dontwarn com.lzy.okserver.**
-keep class com.lzy.okserver.**{*;}

  #okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}
 -dontwarn javax.annotation.Nullable
 -dontwarn javax.annotation.ParametersAreNonnullByDefault
   #okio
-dontwarn okio.**
-keep class okio.**{*;}

#本项目
-keep public class com.vistyle.adha.activity.**
-keep public class com.vistyle.adha.activity.** { *;}
-keep public class com.vistyle.adha.base.** { *; }
-keep public class com.vistyle.adha.base.**
-keep public class com.vistyle.adha.model.** {*;}
-keep public class com.vistyle.adha.model.**
-keep public class com.vistyle.adha.common.** {*;}
-keep public class com.vistyle.adha.common.**
-keep public class com.vistyle.adha.receiver.** {*;}
-keep public class com.vistyle.adha.receiver.**
-keep public class com.vistyle.adha.fragment.** {*;}
-keep public class com.vistyle.adha.fragment.**
-keep public class com.vistyle.adha.adapter.** {*;}
-keep public class com.vistyle.adha.adapter.**
-keep public class com.vistyle.adha.service.** {*;}
-keep public class com.vistyle.adha.service.**
-keep public class com.vcoo.**
-keep public class com.vcoo.{*;}
#-dontwarn com.squareup.haha.guava.**
#-keep class com.squareup.haha.guava.** { *; }
##---------------------------------webview------------------------------------
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#    public *;
#}
#-keepclassmembers class * extends android.webkit.webViewClient {
#public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
#public boolean *(android.webkit.WebView, java.lang.String);
#}
#-keepclassmembers class * extends android.webkit.webViewClient {
#public void *(android.webkit.webView, jav.lang.String);
#}
#-keepclassmembers class * extends android.webkit.WebChromeClient{
#   		public void openFileChooser(...);
#   		public boolean onShowFileChooser(...);
#}
##-------------------------------------------------

#gif加载库
#-keep public class pl.droidsonroids.gif.GifIOException{<init>(int);}
#-keep class pl.droidsonroids.gif.GifInfoHandle{<init>(long,int,int,int);}
#——————————————————————————————————


#--------------------------------------------------------
#混淆Bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
#------------------------------------------
#----------Glide-----------------------------------------
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#----------JPush-----------------------------------------
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }
# for DexGuard only  引入单独Module项目需要使用
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule