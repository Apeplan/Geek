package com.simon.mvp_frame.schedulers;

import android.support.annotation.NonNull;

import rx.Scheduler;

/**
 * Allow providing different types of {@link Scheduler}s.
 * <p>
 * Created by: Simon
 * Created on: 2017/4/28 14:56
 * Email: hanzhanxi@01zhuanche.com
 */

public interface BaseSchedulerProvider {
    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler io();

    @NonNull
    Scheduler ui();
}
