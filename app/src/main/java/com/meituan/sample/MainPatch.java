package com.meituan.sample;

import android.content.Context;
import android.widget.Toast;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.lib.ut.util.ToastUtils;
import com.meituan.robust.patch.annotaion.Add;

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
@Add
public class MainPatch {

    Context context;
    public MainPatch(Context context){
        this.context = context;
    }

    public void show(){
        ToastUtils.showShort("哈哈哈哈哈");
//        Toast.makeText(context,"打哈哈的黑色素的",Toast.LENGTH_SHORT).show();
        //toAccount();
    }

    private void toAccount() {
        CC.obtainBuilder("login")
                .setActionName("Login")
                .build()
                .callAsyncCallbackOnMainThread(new IComponentCallback() {
                    @Override
                    public void onResult(CC cc, CCResult result) {
                        if (result.isSuccess()) {

                        } else {

                        }
                    }
                });
    }
}
