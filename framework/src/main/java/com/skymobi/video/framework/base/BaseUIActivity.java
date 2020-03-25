package com.skymobi.video.framework.base;

import android.os.Bundle;

import com.skymobi.video.framework.utils.SystemUI;

/**
 * FileName: BaseUIActivity
 * Founder: LiuGuiLin
 * Profile: UI 基类
 */
public class BaseUIActivity extends BaseActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SystemUI.fixSystemUI(this);
    }
}
