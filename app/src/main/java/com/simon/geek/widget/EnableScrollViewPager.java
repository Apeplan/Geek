package com.simon.geek.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * describe:
 *
 * @author Apeplan
 * @date 2017/2/13
 * @email hanzx1024@gmail.com
 */

public class EnableScrollViewPager extends ViewPager {
    public EnableScrollViewPager(Context context) {
        super(context);
    }

    public EnableScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean isEnableScroll = true;

    public void setEnableScroll(boolean isEnableScroll) {
        this.isEnableScroll = isEnableScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isEnableScroll) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!isEnableScroll) {
            return false;
        }
        return super.onTouchEvent(ev);
    }
}
