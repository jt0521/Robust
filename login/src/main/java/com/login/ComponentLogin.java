package com.login;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.CCUtil;
import com.billy.cc.core.component.IComponent;

/**
 * Copyright (C) 2021
 * 版权所有
 * <p>
 * 功能描述：
 * <p>
 * 作者：tgl
 * 创建时间：2021/5/12
 * <p>
 * 修改人：
 * 修改描述：
 * 修改日期
 */
public class ComponentLogin implements IComponent {
    @Override
    public String getName() {
        return "login";
    }

    @Override
    public boolean onCall(CC cc) {
        String actionName = cc.getActionName();
        switch (actionName) {
            //响应actionName为"loginActivity"的组件调用
            case "Login":
                //未登录，打开登录界面，在登录完成后再回调结果，异步实现
                CCUtil.navigateTo(cc, LoginActivity.class);

                //异步实现，不立即调用CC.sendCCResult,返回true
                return true;
            default:
                //其它actionName当前组件暂时不能响应，可以通过如下方式返回状态码为-12的CCResult给调用方
                CC.sendCCResult(cc.getCallId(), CCResult.errorUnsupportedActionName());
                return false;
        }
    }

}
