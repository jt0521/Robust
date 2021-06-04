package com.hotfix;

import android.content.Context;
import android.text.TextUtils;

import com.meituan.robust.Patch;
import com.meituan.robust.PatchManipulate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mivanzhang on 17/2/27.
 * <p>
 * We recommend you rewrite your own PatchManipulate class ,adding your special patch Strategy，in the demo we just load the patch directly
 *
 * <br>
 * Pay attention to the difference of patch's LocalPath and patch's TempPath
 *
 * <br>
 * We recommend LocalPath store the origin patch.jar which may be encrypted,while TempPath is the true runnable jar
 * <br>
 * <br>
 * 我们推荐继承PatchManipulate实现你们App独特的A补丁加载策略，其中setLocalPath设置补丁的原始路径，这个路径存储的补丁是加密过得，setTempPath存储解密之后的补丁，是可以执行的jar文件
 * <br>
 * setTempPath设置的补丁加载完毕即刻删除，如果不需要加密和解密补丁，两者没有啥区别
 */

public class PatchManipulateImp extends PatchManipulate {

    /**
     * 加载指定，为空表示加载所有补丁
     */
    public String mSpecifyPatchName;

    private PatchManipulateImp() {
    }

    public PatchManipulateImp(String specifyPatchName) {
        mSpecifyPatchName = specifyPatchName;
    }

    /***
     * connect to the network ,get the latest patches
     * l联网获取最新的补丁,默认加载所有补丁
     * @param context
     *
     * @return
     */
    @Override
    protected List<Patch> fetchPatchList(Context context) {
        List patches = new ArrayList<Patch>();
        File folder = new File(PatchManager.mFolder);
        if (folder.exists()) {
            for (File f : folder.listFiles()) {
                if (mSpecifyPatchName != null && !TextUtils.equals(f.getAbsolutePath(), mSpecifyPatchName)) {
                    continue;
                }
                String name = f.getName();
                Patch patch = new Patch();
                int i = name.lastIndexOf(".");
                patch.setName(i > 0 ? name.substring(0, i) : name);
                patch.setPatchesInfoImplClassFullName("com.hotfix.PatchesInfoImpl");
                patch.setLocalPath(f.getParentFile().getAbsolutePath() + File.separator + patch.getName());
                patches.add(patch);

            }
        }
        return patches;
    }

    /**
     * @param context
     * @param patch
     * @return you can verify your patches here
     */
    @Override
    protected boolean verifyPatch(Context context, Patch patch) {
        String folder = context.getCacheDir() + File.separator + PatchManager.PATCH_FOLDER;
        File folderFile = new File(folder);
        if (!folderFile.exists()) {
            folderFile.mkdirs();
        }
        //do your verification, put the real patch to patch
        //放到app的私有目录
        patch.setTempPath(folder + File.separator + patch.getName());
        //in the sample we just copy the file
        try {
            copy(patch.getLocalPath(), patch.getTempPath());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("copy source patch to local patch error, no patch execute in path " + patch.getTempPath());
        }

        return true;
    }

    public void copy(String srcPath, String dstPath) throws IOException {
        File src = new File(srcPath);
        if (!src.exists()) {
            throw new RuntimeException("source patch does not exist ");
        }
        File dst = new File(dstPath);

        if (!dst.getParentFile().exists()) {
            dst.getParentFile().mkdirs();
        }
        if (dst.exists()) {
            dst.delete();
        }
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

    /**
     * @param patch
     * @return you may download your patches here, you can check whether patch is in the phone
     */
    @Override
    protected boolean ensurePatchExist(Patch patch) {
        return true;
    }
}
