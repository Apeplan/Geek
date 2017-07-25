package com.simon.mvp_frame;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.simon.mvp_frame.lifecycle.ComponentLifecycleObserver;
import com.simon.mvp_frame.lifecycle.LifecycleObservable;
import com.simon.mvp_frame.lifecycle.LifecycleObserver;
import com.simon.mvp_frame.lifecycle.LifecycleObserverCompat;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by: Simon
 * Created on: 2017/4/28 16:25
 * Email: hanzx1024@gmail.com
 */

public abstract class RxPresenter<V extends BaseView & LifecycleObservable>
        implements Presenter, ComponentLifecycleObserver, LifecycleObservable, UIContract {

    protected V mView;

    private CompositeSubscription mSubscriptions;

    private LifecycleObserverCompat mObserverCompat;

    public RxPresenter(@NonNull V view) {
        mObserverCompat = new LifecycleObserverCompat();
        mView = view;
        mView.subscribeLifecycle(this);
    }

    @Override
    public void subscribeLifecycle(LifecycleObserver observer) {
        mObserverCompat.subscribeLifecycle(observer);
    }

    @Override
    public boolean unsubscribeLifecycle(LifecycleObserver observer) {
        return mObserverCompat.unsubscribeLifecycle(observer);
    }

    @Override
    @CallSuper
    public void onCreate() {
        mObserverCompat.onCreate();
    }

    @Override
    @CallSuper
    public void onStart() {
        mObserverCompat.onStart();
    }

    @Override
    @CallSuper
    public void onResume() {
        mObserverCompat.onResume();
    }

    @Override
    @CallSuper
    public void onHiddenChanged(boolean hidden) {
        mObserverCompat.onHiddenChanged(hidden);
    }

    @Override
    @CallSuper
    public void onPause() {
        mObserverCompat.onPause();
    }

    @Override
    @CallSuper
    public void onStop() {
        mObserverCompat.onStop();
    }

    @Override
    @CallSuper
    public void onDestroy() {
        mObserverCompat.onDestroy();
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
            if (mView instanceof Activity) {
                return (Context) mView;
            } else if (mView instanceof Fragment) {
                return ((Fragment) mView).getActivity();
            } else if (mView instanceof RxPresenter) {
                return mView.getContext();
            }
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

    /**
     * 订阅
     *
     * @param observable 可订阅的观察者
     */
    public void subscribe(final Observable observable, final ResultSubscriber resultSubscriber) {

        if (!NetWorkHelper.isNetworkConnected(mView.getContext())) {
            showToast("无网络，请检查您的网络连接！");
            resultSubscriber.onNetworkException("无网络，请检查您的网络连接！");
            return;
        }

        Subscription subscribe = observable.compose(applySchedulers())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        resultSubscriber.onStartRequest();
                    }
                })
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultSubscriber);

        subscribe(subscribe);
    }

    static <T> Observable.Transformer<T, T> applySchedulers() {
        return new Observable.Transformer() {
            @Override
            public Object call(Object observable) {
                return ((Observable) observable).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
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

}
