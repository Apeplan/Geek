package com.simon.mvp_frame.schedulers;

import android.support.annotation.NonNull;

import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Implementation of the {@link BaseSchedulerProvider} making all {@link Scheduler}s immediate.
 * <p>
 * Created by: Simon
 * Created on: 2017/4/28 15:06
 * Email: hanzhanxi@01zhuanche.com
 */

public class ImmediateSchedulerProvider implements BaseSchedulerProvider {
    @NonNull
    @Override
    public Scheduler computation() {
        return Schedulers.immediate();
    }

    @NonNull
    @Override
    public Scheduler io() {
        return Schedulers.immediate();
    }

    @NonNull
    @Override
    public Scheduler ui() {
        return Schedulers.immediate();
    }
}
