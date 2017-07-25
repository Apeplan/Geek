package com.simon.mvp_frame.wedgit;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.simon.mvp_frame.R;

/**
 * Created by: Simon
 * Created on: 2017/5/15 14:43
 * Email: hanzx1024@gmail.com
 */

public class CustomProgressDialog extends ProgressDialog {
    public CustomProgressDialog(Context context) {
        super(context);
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProgressBar progressBar = (ProgressBar) findViewById(android.R.id.progress);
        int color = getContext().getResources().getColor(R.color.color_progress);
        if (null != progressBar) {
            progressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }
}
