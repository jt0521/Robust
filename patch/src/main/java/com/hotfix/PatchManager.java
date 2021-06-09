package com.hotfix;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.meituan.robust.Patch;
import com.meituan.robust.PatchExecutor;
import com.meituan.robust.RobustCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Copyright (C) 2021
 * 版权所有
 * <p>
 * 功能描述：加载patch,可自定义
 * <p>
 * 作者：tgl
 * 创建时间：2021/6/2
 * <p>
 * 修改人：
 * 修改描述：
 * 修改日期
 */
public class PatchManager {

    /**
     * 一级目录
     */
    public static String mFolder;
    /**
     * 二级目录，即补丁文件夹
     */
    public static final String PATCH_FOLDER = "robust";
    public static boolean mDebug;

    public static void setDebug(boolean debug) {
        mDebug = debug;
    }

    //RobustApkHashUtils.readRobustApkHash(context)
    public static void loadPatch(Context context) {
        loadPatch(context, null);
    }

    /**
     * 加载指定补丁
     *
     * @param context
     * @param specifyPatchPath 指定补丁路径
     */
    public static void loadPatch(Context context, String specifyPatchPath) {
        getPatchFolder(context);
        new PatchExecutor(context, new PatchManipulateImp(specifyPatchPath), new RobustCallBack() {
            @Override
            public void onPatchListFetched(boolean result, boolean isNet, List<Patch> patches) {

            }

            @Override
            public void onPatchFetched(boolean result, boolean isNet, Patch patch) {

            }

            @Override
            public void onPatchApplied(boolean result, Patch patch) {

            }

            @Override
            public void logNotify(String log, String where) {

            }

            @Override
            public void exceptionNotify(Throwable throwable, String where) {

            }
        }).start();
    }

    /**
     * 获取补丁文件夹路径
     *
     * @param context
     */
    public static void getPatchFolder(Context context) {
        if (mFolder != null) {
            return;
        }
        if (!(context instanceof Application)) {
            context = context.getApplicationContext();
        }
        mFolder = mDebug ? context.getExternalFilesDir(null).getAbsolutePath()
                + File.separator + PATCH_FOLDER : context.getFilesDir().getAbsolutePath() + File.separator + PATCH_FOLDER;
        File folderFile = new File(mFolder);
        if (!folderFile.exists()) {
            folderFile.mkdirs();
        }
    }

    /**
     * 下载到本地的补丁命名规则
     *
     * @param key   id或者版本号
     * @param valid 是否可用
     * @param name  补丁名称
     * @return
     */
    public static String getNewPatchFileName(String key, boolean valid, String name) {
        StringBuffer sb = new StringBuffer();
        sb.append(key);
        sb.append("-");
        sb.append(valid ? 1 : 0);
        sb.append("-");
        sb.append(name);
        return sb.toString();
    }

    /**
     * 根据下载地址获取文件名称
     *
     * @return
     */
    public static String getPatchName(String downloadPath) {
        if (TextUtils.isEmpty(downloadPath) || downloadPath.lastIndexOf("/") <= 0) {
            return UUID.randomUUID().toString().hashCode() + "";
        }

        //使用hashCode避免文件名称太长，无法创建文件
        return downloadPath.substring(downloadPath.lastIndexOf("/") + 1).hashCode() + "";
    }

    /**
     * 删除无效补丁
     *
     * @param fileName
     */
    private static void delInvalidPatch(String fileName) {
        File file = new File(mFolder, fileName);
        if (file.exists()) {
            file.delete();
        }
    }
}
