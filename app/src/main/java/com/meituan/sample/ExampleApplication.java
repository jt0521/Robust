package com.meituan.sample;

import android.app.Application;

import com.billy.cc.core.component.CC;

/**
 * Copyright (C) 2021
 * 版权所有
 * <p>
 * 功能描述：
 * <p>
 * 作者：tgl
 * 创建时间：2021/5/13
 * <p>
 * 修改人：
 * 修改描述：
 * 修改日期
 */
public class ExampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initCC();
    }

    private void initCC() {
        CC.enableDebug(BuildConfig.DEBUG);
        CC.enableVerboseLog(BuildConfig.DEBUG);
        CC.enableRemoteCC(BuildConfig.DEBUG);
    }
}
