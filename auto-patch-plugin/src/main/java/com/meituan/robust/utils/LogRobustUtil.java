package com.meituan.robust.utils;

/**
 * Copyright (C) 2021
 * 版权所有
 * <p>
 * 功能描述：
 * <p>
 * 作者：tgl
 * 创建时间：2021/5/27
 * <p>
 * 修改人：
 * 修改描述：
 * 修改日期
 */
public class LogRobustUtil {
    public static String Green = "\033[40;32m";

    public static void d(String msg) {
        System.out.println("${LogUtils.Green}${msg}${LogUtils.Green}");
    }
}
