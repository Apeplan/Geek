package com.simon.mvp_frame;

import android.app.ProgressDialog;
import android.widget.Toast;

/**
 *
 * Created by: Simon
 * Created on: 15/10/12 17:27
 * Email: hanzhanxi@01zhuanche.com
 */
public abstract class BaseFragmentWithUIContract extends BaseFragment implements UIContract {
    /**
     * 等待对话框
     */
    private ProgressDialog progressDialog = null;

    @Override
    public void showDialog() {
        showDialog(getString(R.string.progress_hint_text), true);
    }

    @Override
    public void showDialog(String msg) {
        showDialog(msg, true);
    }

    @Override
    public void showDialog(String msg, boolean cancelable) {
        if (getActivity() == null) {
            return;
        }
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
        }
        progressDialog.setCancelable(cancelable);
        progressDialog.setCanceledOnTouchOutside(cancelable);
        progressDialog.setMessage(msg);
        if (!getActivity().isFinishing()) {
            progressDialog.show();
        }

    }


    @Override
    public void closeDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public boolean isWaitDialogShowing() {
        return progressDialog.isShowing();
    }

    @Override
    public void showToast(String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    @Override
    public void showToast(String msg, int time) {
        Toast.makeText(getContext(), msg, time).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeDialog();
    }
}
