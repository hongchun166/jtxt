
package com.linkb.jstx.listener;

public interface DatabaseChangedListener {
    /**
     * 当切换用户登录时，需要安装用户id切换database
     */
    void onDatabaseChanged();
    /**
     * 当切换服务端地址时，需要清除本地所有记录
     */
    void onTableClearAll();

    /**
     * 退出程序时间需要关闭数据库
     */
    void onDatabaseDestroy();

}
