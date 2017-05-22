package com.simon.mvp_frame.lifecycle;

import android.support.annotation.MainThread;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: Simon
 * Created on: 2017/4/28 15:39
 * Email: hanzhanxi@01zhuanche.com
 */

public class LifecycleObserverCompat implements LifecycleObservable, ComponentLifecycleObserver {
    private boolean mIsCreated = false;

    private boolean mIsStarted = false;

    private boolean mIsDestroy = false;

    private boolean mIsResumed = false;

    private boolean mIsHidden = false;

    private final List<LifecycleObserver> mObserverList = new ArrayList<>();

    @Override
    @MainThread
    public void onCreate() {
        for (LifecycleObserver observer : mObserverList) {
            observer.onCreate();
        }
        mIsDestroy = false;
        mIsCreated = true;
    }

    @Override
    @MainThread
    public void onStart() {
        for (LifecycleObserver observer : mObserverList) {
            if (observer instanceof ComponentLifecycleObserver) {
                ((ComponentLifecycleObserver) observer).onStart();
            }
        }
        mIsStarted = true;
    }

    @Override
    @MainThread
    public void onResume() {
        for (LifecycleObserver observer : mObserverList) {
            if (observer instanceof ComponentLifecycleObserver) {
                ((ComponentLifecycleObserver) observer).onResume();
            }
        }
        mIsResumed = true;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        for (LifecycleObserver observer : mObserverList) {
            if (observer instanceof ComponentLifecycleObserver) {
                ((ComponentLifecycleObserver) observer).onHiddenChanged(hidden);
            }
        }
        mIsHidden = hidden;
    }

    @Override
    @MainThread
    public void onPause() {
        for (LifecycleObserver observer : mObserverList) {
            if (observer instanceof ComponentLifecycleObserver) {
                ((ComponentLifecycleObserver) observer).onPause();
            }
        }
        mIsResumed = false;
    }

    @Override
    @MainThread
    public void onStop() {
        for (LifecycleObserver observer : mObserverList) {
            if (observer instanceof ComponentLifecycleObserver) {
                ((ComponentLifecycleObserver) observer).onStop();
            }
        }
        mIsStarted = false;
    }

    @Override
    @MainThread
    public void onDestroy() {
        for (LifecycleObserver observer : mObserverList) {
            observer.onDestroy();
        }
        mIsCreated = false;
        mIsDestroy = true;
        mObserverList.clear();
    }

    @Override
    public void subscribeLifecycle(LifecycleObserver observer) {
        synchronized (mObserverList) {
            mObserverList.add(observer);
            if (observer instanceof ComponentLifecycleObserver) {
                addComponentObserver((ComponentLifecycleObserver) observer);
            } else {
                addBaseObserver(observer);
            }
        }
    }

    @Override
    public boolean unsubscribeLifecycle(LifecycleObserver observer) {
        synchronized (mObserverList) {
            return mObserverList.remove(observer);
        }
    }

    private void addComponentObserver(ComponentLifecycleObserver observer) {
        if (!mIsDestroy) {
            if (mIsCreated) {
                observer.onCreate();
            }
            if (mIsStarted) {
                observer.onStart();
            }
            if (mIsResumed) {
                observer.onResume();
            }
            if (mIsHidden) {
                observer.onHiddenChanged(true);
            }
        } else {
            if (!mIsResumed) {
                observer.onPause();
            }
            if (!mIsStarted) {
                observer.onStop();
            }
            observer.onDestroy();
            mObserverList.remove(observer);
        }
    }

    private void addBaseObserver(LifecycleObserver observer) {
        if (!mIsDestroy) {
            if (mIsCreated) {
                observer.onCreate();
            }
        } else {
            observer.onDestroy();
            mObserverList.remove(observer);
        }
    }

}
