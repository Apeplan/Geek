package com.simon.mvp_frame;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.view.View;

import com.simon.mvp_frame.lifecycle.LifecycleObservable;
import com.simon.mvp_frame.lifecycle.LifecycleObserver;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by: Simon
 * Created on: 2017/4/28 16:35
 * Email: hanzhanxi@01zhuanche.com
 */

public abstract class BaseViewPresenter<V extends View & BaseView & LifecycleObservable>
        implements Presenter, LifecycleObserver {

    protected V mView;

    private CompositeSubscription mSubscriptions;

    public BaseViewPresenter(@NonNull V view) {
        mView = view;
        mView.subscribeLifecycle(this);
    }

    @Override
    @CallSuper
    public void onCreate() {

    }

    @Override
    @CallSuper
    public void onDestroy() {
        unsubscribe();
    }

    public boolean isViewAttached() {
        return null != mView;
    }

    public V getView() {
        return mView;
    }

    public Context getContext() {
        if (isViewAttached()) {
            return mView.getContext();
        }
        return null;
    }

    @Override
    public void subscribe(Subscription subscription) {
        if (null == subscription) return;

        if (null == mSubscriptions) {
            mSubscriptions = new CompositeSubscription();
        }
        mSubscriptions.add(subscription);
    }

    @Override
    public void unsubscribe() {
        if (null != mSubscriptions) {
            mSubscriptions.unsubscribe();
        }
    }
}
