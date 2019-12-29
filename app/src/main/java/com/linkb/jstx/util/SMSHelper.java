package com.linkb.jstx.util;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * 短信电话相关工具类<br/>
 * 目录： <br/>
 * 1.发送短信 sendSMS() <br/>
 * 2.拨打电话短信 callPhone() <br/>
 * 3.获取所有短信记录getAllSms() <br/>
 * 4.获取所有联系人getContactsList() <br/>
 * 5.获取所有联系人getContactList() : 与第4条所生成的数据结构不同 <br/>

 * <b>注</b>：使用时请添加相关权限
 *
 */
public class SMSHelper {

    /**
     * 发送短信 (需要权限SMS_Send)
     *
     * @param num  电话
     * @param text 短信内容
     */
    public static void sendSMS(String num, String text) {
        SmsManager.getDefault().sendTextMessage(num, null, text, null, null);
    }

    public static void callPhone(Context context, String num) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + num));
        // 开启系统拨号器
        context.startActivity(intent);
    }

    /**
     * 获取所有联系人 需要权限
     * <uses-permission android:name="android.permission.READ_CONTACTS" />
     * 数据结构：ContactBean(String displayName, String numList)
     *
     * @param
     * @return
     */
    public static List<ContactBean> getContactList(Context context) {

        Log.d("SMSHelper", "-----------SMSHelper#getContactList()----------");
        Log.d("SMSHelper", "开始查询 Data 表");
        List<ContactBean> list = new ArrayList<ContactBean>();

//        ContactsContract.Data._ID
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Data.CONTENT_URI, null, null, null, null);

        if (cursor == null && cursor.getCount() <= 0) {
            return null;
        }
        Log.d("SMSHelper", "cursor.getCount():" + cursor.getCount());

        while (cursor.moveToNext()) {

            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
            String number = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DATA1));

            //数据清洗
            if (TextUtils.isEmpty(number)) {
                continue;
            }
            if (!TextUtils.isDigitsOnly(number = number.replace(" ", ""))) {
                continue;
            }

            if (TextUtils.isEmpty(name)) {
                name = number;
            }
            list.add(new ContactBean(name, number));
        }
        cursor.close();
        return list;
    }

    /**
     * 获取所有联系人
     * 数据结构：ContactsBean(String displayName, List<String> numList)
     * <p/>
     * 需要权限：
     * <uses-permission android:name="android.permission.READ_CONTACTS" />
     *
     * @param
     * @return List<ContactsBean>
     */
    public static List<ContactsBean> getContactsList(Context context) {

        Log.d("SMSHelper", "-----------SMSHelper#getContactsList()----------");
        Log.d("SMSHelper", "开始查询 Contacts 表");
        List<ContactsBean> list = new ArrayList<ContactsBean>();
        ContactsBean bean = null;
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cursor == null && cursor.getCount() <= 0) {
            return null;
        }
        Log.d("SMSHelper", "cursor.getCount():" + cursor.getCount());

        while (cursor.moveToNext()) {
            bean = new ContactsBean();

            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));//姓名

            wirteNumbers(resolver, name, bean);
            list.add(bean);
        }
        cursor.close();
        return list;
    }

    /**
     * 根据联系人姓名查询电话
     * 并写入
     */
    private static void wirteNumbers(final ContentResolver contentResolver, String name, final ContactsBean bean) {
        Log.d("SMSHelper", "开始查询 Data 表 : 查询联系人：" + name);

        Cursor dataCursor = contentResolver.query(ContactsContract.Data.CONTENT_URI,
                new String[]{ContactsContract.Data.DATA1},
                ContactsContract.Data.DISPLAY_NAME + "= ? ",
                new String[]{name}, null);
        if (dataCursor == null) {
            Log.w("SMSHelper", "dataCursor == null ");

            return;
        }
        if (dataCursor.getCount() > 0) {
            bean.setName(name);
            Log.w("SMSHelper", " 电话信息 -- size: " + dataCursor.getCount());
            while (dataCursor.moveToNext()) {
                String number = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));

                if (TextUtils.isEmpty(number) || !TextUtils.isDigitsOnly(number = number.replace(" ", ""))) {
                    Log.w("SMSHelper", " 电话信息(异常) -- number: " + number);
                    continue;
                }
                bean.getNumList().add(number);
                Log.w("SMSHelper", " 电话信息 -- number: " + number);
            }
            dataCursor.close();
        } else {
            Log.w("SMSHelper", " 无电话信息 -- name: " + name);
        }
        return;
    }

    /**
     * 获取短信记录 （需要bean: Smsinfo.java和 Read_SMS权限）
     * <p>
     * <uses-permission android:name="android.permission.READ_SMS" />
     *
     * @return List<Smsinfo>
     * @author zlw QQ:739043667
     */
    public static List<SmsInfo> getAllSms(Context context) {
        List<SmsInfo> smsinfos = new ArrayList<SmsInfo>();

        Uri uri = Uri.parse("content://sms/");
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{"address", "date", "type", "body"}, null, null, null);

        SmsInfo smsinfo = null;
        while (cursor.moveToNext()) {
            String address = cursor.getString(0);
            String date = cursor.getString(1);
            String type = cursor.getString(2);
            String body = cursor.getString(3);
            smsinfo = new SmsInfo(address, date, type, body);
            smsinfos.add(smsinfo);
        }

        cursor.close();
        return smsinfos;
    }

    /**
     * 短信信息Bean 类
     *
     * @param
     * @author zlw QQ:739043667
     * @return
     */
    public static class SmsInfo {
        String address;
        String date;
        String type;
        String body;

        public SmsInfo(String address, String date, String type, String body) {
            super();
            this.address = address;
            this.date = date;
            this.type = type;
            this.body = body;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

    }

    /**
     * 联系人集合Bean类
     */
    public static class ContactsBean {

        private String name;
        private List<String> numList;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getNumList() {
            return numList;
        }

        public void setNumList(List<String> numList) {
            this.numList = numList;
        }

        public ContactsBean() {
            numList = new ArrayList<>();
        }

        public ContactsBean(String displayName, List<String> numList) {
            super();
            this.name = displayName;
            if (numList == null) {
                this.numList = new ArrayList<>();
            } else {
                this.numList = numList;
            }
        }

        @Override
        public String toString() {
            return "ContactsBean [name=" + name + ", numList=" + numList + "]";
        }
    }


    /**
     * 联系人Bean类
     */
    public static class ContactBean {

        private String name;
        private String number;

        public ContactBean() {
        }

        public ContactBean(String name, String number) {
            this.name = name;
            this.number = number;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "ContactBean{" +
                    "name='" + name + '\'' +
                    ", number='" + number + '\'' +
                    '}';
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }

}

