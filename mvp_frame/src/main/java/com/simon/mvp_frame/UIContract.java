package com.simon.mvp_frame;

/**
 * Created by: Simon
 * Created on: 2017/4/28 16:03
 * Email: hanzx1024@gmail.com
 */

public interface UIContract {

    void showDialog();

    void showDialog(String msg);

    void showDialog(String msg, boolean cancelable);

    void closeDialog();

    boolean isWaitDialogShowing();

    void showToast(String msg);

    void showToast(String msg,  int time);
}
