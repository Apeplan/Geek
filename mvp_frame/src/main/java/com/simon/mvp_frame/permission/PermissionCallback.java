package com.simon.mvp_frame.permission;

import android.support.annotation.Nullable;

/**
 * Created by: Simon
 * Created on: 2017/4/20 16:25
 * Email: hanzx1024@gmail.com
 */

public interface PermissionCallback {
    /**
     * 被授予权限
     *
     * @param permissions 已授权的权限名称
     */
    void onGranted(@Nullable String... permissions);

    /**
     * 权限被拒绝
     *
     * @param permissions 拒绝授权的权限名称
     */
    void onRefuse(@Nullable String... permissions);

    /**
     * 再次请求权限的理由
     *
     * @param permissions 拒绝授权的权限名称
     */
    void onRequestPermissionRationale(@Nullable String... permissions);
}
