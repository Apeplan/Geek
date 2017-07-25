package com.simon.common.utils;

import android.text.TextUtils;
import android.view.View;

import com.simon.common.R;
import com.simon.common.state.StateView;

/**
 * Created by: Simon
 * Created on: 2017/5/3 17:59
 * Email: hanzx1024@gmail.com
 */

public class StateHelper {

    /**
     * 显示正在加载页面
     *
     * @param message
     */
    public static void showLoading(StateView stateView, String message) {
        if (null == stateView) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }
        if (!TextUtils.isEmpty(message)) {
            stateView.setTitle(message);
        }
        stateView.setState(StateView.STATE_LOADING);
    }

    /**
     * 显示内容页面
     */
    public static void showContent(StateView stateView) {
        if (null == stateView) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        stateView.setState(StateView.STATE_CONTENT);
    }


    /**
     * 显示空数据页面
     *
     * @param message         提示信息
     * @param onClickListener 是否支持重新加载
     */
    public static void showEmtry(StateView stateView, String message, View.OnClickListener onClickListener) {
        if (null == stateView) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        stateView.setState(StateView.STATE_EMPTY);
        if (!TextUtils.isEmpty(message)) {
            stateView.setTitle(message);
        }
        if (null != onClickListener) {
            stateView.setOnClickListener(onClickListener);
        }
    }

    /**
     * 显示错误页面
     *
     * @param error           提示信息
     * @param onClickListener 是否支持重新加载
     */
    public static void showError(StateView stateView, String error, View.OnClickListener onClickListener) {
        if (null == stateView) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        stateView.setState(StateView.STATE_ERROR);
        if (!TextUtils.isEmpty(error)) {
            stateView.setTitle(error);
        }
        if (null != onClickListener) {
            stateView.setOnClickListener(onClickListener);
        }
    }

    /**
     * 显示网络错误页面
     *
     * @param error
     * @param onClickListener 是否支持重新加载
     */
    public static void showNetworkError(StateView stateView, String error, View.OnClickListener onClickListener) {
        if (null == stateView) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        stateView.setState(StateView.STATE_ERROR);
        if (!TextUtils.isEmpty(error)) {
            stateView.setTitle(error);
        }
        stateView.setIcon(R.drawable.state_net_error);
        if (null != onClickListener) {
            stateView.setOnClickListener(onClickListener);
        }
    }
}
