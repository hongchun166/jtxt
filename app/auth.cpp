#include <jni.h>
#include <string.h>
#include <stdio.h>
#include <com_farsunset_lvxin_jni_JNI.h>

const char*  getAppSignString(JNIEnv * env, jobject obj, jobject contextObject);

const char* OSS_ACCESS_ID = "2yKc1XA77H0LxdFx";
const char* OSS_ACCESS_KEY = "6xiwP6D5wVkpOIcjlttnjLYdNBeMVL";

/**
 * http api 授权 key ，由服务端提供
 */
const char* AUTH_KEY = "0CC175B9C0F1B6A831C399E269772661";

const char* DEV_SIGN ="308201e53082014ea0030201020204546330b6300d06092a864886f70d01010505003037310b30090603550406130255533110300e060355040a1307416e64726f6964311630140603550403130d416e64726f6964204465627567301e170d3134313131323130303433385a170d3434313130343130303433385a3037310b30090603550406130255533110300e060355040a1307416e64726f6964311630140603550403130d416e64726f696420446562756730819f300d06092a864886f70d010101050003818d0030818902818100a7081ad6f93c1c738f39ca49788c95e0acf47bafe333f218cfc35ccc1384ca343738057a4bff9c8e6971d66af647a1d1711766218f40e0d7651ef362eb0b03047da949bdc8280a0421d444a3868f1cc284845ccddb670194ac5aa916a32d25a27bc9005250e544560de8bc49efd0cd4cfc5c87fd2cf68191ce4197bef2231b7d0203010001300d06092a864886f70d010105050003818100085ad7a1849a2b33a22fd5d22941a00deafebc0c69bbed4456e62c04e386d4f47c1b5942c8405c7dbb99e796464de4799a3f73a48aee1b887123c16aa30b96af5a6e623ec9e6c5b0b697a5b88b61b33e0c841262eba569945d84075b88782f1e481c4f95966a63f0d2a53bd7b780fd8db2aa0cb9600e11e5eb3c7f53a29cacf7";


/**
 * 发布的app 签名,只有和本签名一致的app 才会返回合法的 授权 key
 */

const char* RELEASE_SIGN ="308201db30820144a00302010202045455d4ff300d06092a864886f70d01010505003032310b30090603550406130238363111300f060355040713087368616e676861693110300e060355040313076a756e2e786961301e170d3134313130323036353335315a170d3339313032373036353335315a3032310b30090603550406130238363111300f060355040713087368616e676861693110300e060355040313076a756e2e78696130819f300d06092a864886f70d010101050003818d0030818902818100857b1727994347f9a4fade0c0fbdcef31bb212350558ce561837e1ba5ea5ca1becefe2c173c64423103971de81fa3cc2a2753b3a0daa3d429dee7cb590b717a7e1fc6e1428a0bedcd0890e0fe3ce2abe673e81ca68e867b680daa59d30ee59d978195bbe85bb447fd35d88859c630faa7188536bcb190f7c829b0978300efc810203010001300d06092a864886f70d0101050500038181002b6f82c293bcc93e6047ea12e6bc78525bc9dacbfc938616dfa3e452a6a15c282d1b07ab0152234ff8001733aabad98fc6d294053a378207a5ea1a011b1d405a4044d6653ef658e9e9fa0eef73fe119bd70b3496ca78565ca5ddd4d35637b50823576adb647b2a73e531a0be7e2dc2c4b95632f5241cd16891ecb2deb96e507d";

/**
 * 拿到传入的app  的 签名信息，对比合法的app 签名，防止so文件被未知应用盗用
 */
static jclass contextClass;
static jclass signatureClass;
static jclass packageNameClass;
static jclass packageInfoClass;
JNIEXPORT jstring JNICALL Java_com_farsunset_lvxin_jni_JNI_getAuthKey(
		JNIEnv * env, jobject obj, jobject contextObject) {

	const char* signStrng =  getAppSignString(env,obj,contextObject);

	if(strcmp(signStrng,RELEASE_SIGN)==0 || strcmp(signStrng,DEV_SIGN)==0)//签名一致  返回合法的 api key，否则返回错误
	{
	   return (env)->NewStringUTF(AUTH_KEY);
	}else
	{
	   return (env)->NewStringUTF("error");
	}

}


JNIEXPORT jstring JNICALL Java_com_farsunset_lvxin_jni_JNI_getOSSAccessID(JNIEnv * env, jobject obj, jobject contextObject) {
	const char* signStrng =  getAppSignString(env,obj,contextObject);
	if(strcmp(signStrng,RELEASE_SIGN)==0 || strcmp(signStrng,DEV_SIGN)==0)//签名一致  返回合法的 api key，否则返回错误
	{
	   return (env)->NewStringUTF(OSS_ACCESS_ID);
	}else
	{
	   return (env)->NewStringUTF("error");
	}

}

JNIEXPORT jstring JNICALL Java_com_farsunset_lvxin_jni_JNI_getOSSAccessKey(JNIEnv * env, jobject obj, jobject contextObject) {
	const char* signStrng =  getAppSignString(env,obj,contextObject);
	if(strcmp(signStrng,RELEASE_SIGN)==0 || strcmp(signStrng,DEV_SIGN)==0)//签名一致  返回合法的 api key，否则返回错误
	{
	   return (env)->NewStringUTF(OSS_ACCESS_KEY);
	}else
	{
	   return (env)->NewStringUTF("error");
	}

}


const char*  getAppSignString(JNIEnv * env, jobject obj, jobject contextObject)
  {
	    jmethodID getPackageManagerId = (env)->GetMethodID(contextClass, "getPackageManager","()Landroid/content/pm/PackageManager;");
	  	jmethodID getPackageNameId = (env)->GetMethodID(contextClass, "getPackageName","()Ljava/lang/String;");
	  	jmethodID signToStringId = (env)->GetMethodID(signatureClass, "toCharsString","()Ljava/lang/String;");
	  	jmethodID getPackageInfoId = (env)->GetMethodID(packageNameClass, "getPackageInfo","(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
	  	jobject packageManagerObject =  (env)->CallObjectMethod(contextObject, getPackageManagerId);
	  	jstring packNameString =  (jstring)(env)->CallObjectMethod(contextObject, getPackageNameId);
	  	jobject packageInfoObject = (env)->CallObjectMethod(packageManagerObject, getPackageInfoId,packNameString, 64);
	  	jfieldID signaturefieldID =(env)->GetFieldID(packageInfoClass,"signatures", "[Landroid/content/pm/Signature;");
	  	jobjectArray signatureArray = (jobjectArray)(env)->GetObjectField(packageInfoObject, signaturefieldID);
	  	jobject signatureObject =  (env)->GetObjectArrayElement(signatureArray,0);

	  	return (env)->GetStringUTFChars((jstring)(env)->CallObjectMethod(signatureObject, signToStringId),0);

  }


JNIEXPORT jint JNICALL JNI_OnLoad (JavaVM* vm,void* reserved){

	 JNIEnv* env = NULL;
	 jint result=-1;
	 if(vm->GetEnv((void**)&env, JNI_VERSION_1_4) != JNI_OK)
	   return result;

	 contextClass = (jclass)env->NewGlobalRef((env)->FindClass("android/content/Context"));
	 signatureClass = (jclass)env->NewGlobalRef((env)->FindClass("android/content/pm/Signature"));
	 packageNameClass = (jclass)env->NewGlobalRef((env)->FindClass("android/content/pm/PackageManager"));
	 packageInfoClass = (jclass)env->NewGlobalRef((env)->FindClass("android/content/pm/PackageInfo"));

	 return JNI_VERSION_1_4;
 }

