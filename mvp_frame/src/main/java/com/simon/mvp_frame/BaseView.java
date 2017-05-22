package com.simon.mvp_frame;

import android.content.Context;

import com.simon.mvp_frame.lifecycle.LifecycleObservable;
import com.simon.mvp_frame.lifecycle.LifecycleObserver;

/**
 * Created by: Simon
 * Created on: 2017/4/28 14:49
 * Email: hanzhanxi@01zhuanche.com
 */

public interface BaseView<P extends LifecycleObserver> extends LifecycleObservable<P> {
    Context getContext();
}
