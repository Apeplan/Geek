package com.simon.mvp_frame.wedgit;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.simon.mvp_frame.R;

/**
 * Created by: Simon
 * Created on: 2017/5/15 14:36
 * Email: hanzhanxi@01zhuanche.com
 */

public class CustomProgressBar extends ProgressBar {
    public CustomProgressBar(Context context) {
        super(context);
        init(context, null);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int
            defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        int color = context.getResources().getColor(R.color.color_progress);
        setColor(color);
    }

    public void setColor(int color) {
        getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        this.postInvalidate();
    }
}
