package com.skymobi.video.framework;

import android.content.Context;

/**
 * Author:boshuai.li
 * Time:2020/3/25   17:29
 * Description:Framework 入口类
 */
public class Framework {

    private static Framework framework = null;

    private Framework() {
    }

    public static Framework getInstance() {
        if (framework == null) {
            synchronized (Framework.class) {
                framework = new Framework();
            }
        }
        return framework;
    }

    /**
     * 初始化各个SDK入口
     */
    public void initFramework(Context context) {


    }
}
