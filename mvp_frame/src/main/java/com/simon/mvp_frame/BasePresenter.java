package com.simon.mvp_frame;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.simon.mvp_frame.lifecycle.ComponentLifecycleObserver;
import com.simon.mvp_frame.lifecycle.LifecycleObservable;
import com.simon.mvp_frame.lifecycle.LifecycleObserver;
import com.simon.mvp_frame.lifecycle.LifecycleObserverCompat;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by: Simon
 * Created on: 2017/4/28 15:10
 * Email: hanzx1024@gmail.com
 */

public class BasePresenter<V extends BaseView & LifecycleObservable> implements Presenter,
        LifecycleObservable, ComponentLifecycleObserver, UIContract {

    protected V mView;

    private LifecycleObserverCompat mObserverCompat;

    private CompositeSubscription mSubscriptions;

    public BasePresenter(@NonNull V view) {
        mView = view;
        mObserverCompat = new LifecycleObserverCompat();
        mView.subscribeLifecycle(this);
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

    @Override
    public void showDialog() {
        if (mView instanceof UIContract) {
            ((UIContract) mView).showDialog();
        }
    }

    @Override
    public void showDialog(String msg) {
        if (mView instanceof UIContract) {
            ((UIContract) mView).showDialog(msg);
        }
    }

    @Override
    public void showDialog(String msg, boolean cancelable) {
        if (mView instanceof UIContract) {
            ((UIContract) mView).showDialog(msg, cancelable);
        }
    }

    @Override
    public void closeDialog() {
        if (mView instanceof UIContract) {
            ((UIContract) mView).closeDialog();
        }
    }

    @Override
    public boolean isWaitDialogShowing() {
        if (mView instanceof UIContract) {
            return ((UIContract) mView).isWaitDialogShowing();
        }
        return false;
    }

    @Override
    public void showToast(String msg) {
        if (mView instanceof UIContract) {
            ((UIContract) mView).showToast(msg);
        }
    }

    @Override
    public void showToast(String msg, int time) {
        if (mView instanceof UIContract) {
            ((UIContract) mView).showToast(msg, time);
        }
    }

    @Override
    public void onCreate() {
        mObserverCompat.onCreate();
    }

    @Override
    public void onStart() {
        mObserverCompat.onStart();
    }

    @Override
    public void onResume() {
        mObserverCompat.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        mObserverCompat.onHiddenChanged(hidden);
    }

    @Override
    public void onPause() {
        mObserverCompat.onPause();
    }

    @Override
    public void onStop() {
        mObserverCompat.onStop();
    }

    @Override
    public void onDestroy() {
        mObserverCompat.onDestroy();
    }

    @Override
    public void subscribeLifecycle(LifecycleObserver observer) {
        mObserverCompat.subscribeLifecycle(observer);
    }

    @Override
    public boolean unsubscribeLifecycle(LifecycleObserver observer) {
        return mObserverCompat.unsubscribeLifecycle(observer);
    }

    public boolean isViewAttached() {
        return null != mView;
    }

    public V getView() {
        return mView;
    }

    public Context getContext() {
        if (isViewAttached()) {
            if (mView instanceof Activity) {
                return (Context) mView;
            } else if (mView instanceof Fragment) {
                return ((Fragment) mView).getActivity();
            } else if (mView instanceof BasePresenter) {
                return mView.getContext();
            }
        }
        return null;
    }
}
