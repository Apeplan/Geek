/*
 * Copyright (c) 2015 [1076559197@qq.com | tchen0707@gmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.simon.mvp_frame;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import java.util.Locale;

/**
 * describe: Help class for NetWork
 *
 * @author Simon Han
 * @date 2015.08.27
 * @email hanzx1024@gmail.com
 */
public class NetWorkHelper {

    public enum NetType {
        WIFI, CMNET, CMWAP, NONE
    }

    /**
     * 网络是否可用
     *
     * @return true if network is connected
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = getConnectManager(context);
        NetworkInfo mNetworkInfo = cm.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
//            NetworkInfo.State state = mNetworkInfo.getState();
//            return mNetworkInfo.isAvailable() && NetworkInfo.State.CONNECTED == state;
            return mNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * WIFI 是否连接
     *
     * @return true if WIFI is connected
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = getConnectManager(context);
        NetworkInfo mWiFiNetworkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWiFiNetworkInfo != null) {
            return mWiFiNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 手机移动网络是否连接
     *
     * @return true if mobile network is connected
     */
    public static boolean isMobileConnected(Context context) {
        ConnectivityManager cm = getConnectManager(context);
        NetworkInfo mMobileNetworkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mMobileNetworkInfo != null) {
            return mMobileNetworkInfo.isAvailable();
        }
        return false;
    }


    /**
     * 判断移动数据开关是否打开
     *
     * @return true if mobile network is open
     */
    public static boolean isMobileOpen(Context context) {

        ConnectivityManager cm = getConnectManager(context);
        //mobile 3G/4G Data Network
        NetworkInfo.State mobile = null;
        if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null) {
            mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        } else {
            return false;
        }

        //wifi
//        NetworkInfo.State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();

        if (mobile == NetworkInfo.State.CONNECTED) {// || wifi == NetworkInfo.State.CONNECTED) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取网络连接类型
     *
     * @return 网络连接类型
     */
    public static int getConnectedType(Context context) {
        ConnectivityManager cm = getConnectManager(context);
        NetworkInfo mNetworkInfo = cm.getActiveNetworkInfo();
        if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
            return mNetworkInfo.getType();
        }
        return -1;
    }

    /**
     * 获取APN类型
     *
     * @return APN类型
     */
    public static NetType getAPNType(Context context) {
        ConnectivityManager cm = getConnectManager(context);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null) {
            return NetType.NONE;
        }
        int nType = networkInfo.getType();

        if (nType == ConnectivityManager.TYPE_MOBILE) {
            if (networkInfo.getExtraInfo().toLowerCase(Locale.getDefault()).equals("cmnet")) {
                return NetType.CMNET;
            } else {
                return NetType.CMWAP;
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            return NetType.WIFI;
        }
        return NetType.NONE;
    }

    /**
     * 检查当前环境网络是否可用，不可用跳转至开启网络界面,不设置网络强制关闭当前Activity
     */
    public void validateNetWork(final Activity context) {
        if (!isNetworkConnected(context)) {
            Builder dialogBuilder = new Builder(context);
            dialogBuilder.setTitle("网络设置");
            dialogBuilder.setMessage("网络不可用，是否现在设置网络？");
            dialogBuilder.setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            context.startActivityForResult(
                                    new Intent(Settings.ACTION_SETTINGS), which);
                        }
                    });
            dialogBuilder.setNegativeButton(android.R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            dialogBuilder.create();
            dialogBuilder.show();
        }
    }

    private static ConnectivityManager getConnectManager(Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }
}
