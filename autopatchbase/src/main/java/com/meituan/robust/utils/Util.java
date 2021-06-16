package com.meituan.robust.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Copyright (C) 2021
 * 版权所有
 * <p>
 * 功能描述：
 * <p>
 * 作者：tgl
 * 创建时间：2021/6/10
 * <p>
 * 修改人：
 * 修改描述：
 * 修改日期
 */
public class Util {
    /**
     * 插件日志输出控制
     */
    public static final String KEY_DEBUG = "robust_debug";
    /**
     * 是否自动保存需要的build文件（非release编译有效）
     */
    public static final String KEY_AUTO_SAVE_BUILD_FILE = "robust_auto_save_build_file";

    public static String getLocalPropertiesStr(String dir, String key) {
        if (dir == null || dir == "") {
            System.out.println("File path does not exist");
            return null;
        }
        File fileDir = new File(dir);
        if (!fileDir.exists()) {
            System.out.println("file does not exist");
            return null;
        }
        InputStream is = null;
        try {
            File file = new File(dir + File.separator + "local.properties");
            Properties properties = new Properties();
            is = new FileInputStream(file);
            properties.load(is);
            return (String) properties.get(key);
            //localDep != null && "true".equalsIgnoreCase(localDep) ? true : false;
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
        return null;
    }

    public static boolean getLocalProperties(String dir, String key) {
        String value = getLocalPropertiesStr(dir, key);
        if (value == null) {
            return false;
        }
        try {
            return Boolean.valueOf(value);
        } catch (Exception e) {
            System.out.println("Cannot be cast to Boolean");
            return false;
        }
    }
}
