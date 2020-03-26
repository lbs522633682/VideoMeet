package com.skymobi.video.interview.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.skymobi.video.framework.Framework;

/**
 * Author:boshuai.li
 * Time:2020/3/26   15:07
 * Description:This is BaseApp
 */
public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //只在主进程中初始化
        if (getApplicationInfo().packageName.equals(
                getCurProcessName(getApplicationContext()))) {
            //获取渠道
            //String flavor = FlavorHelper.getFlavor(this);
            //Toast.makeText(this, "flavor:" + flavor, Toast.LENGTH_SHORT).show();
            Framework.getInstance().initFramework(this);
        }
    }

    /**
     * 获取当前进程名
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess :
                activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
}
