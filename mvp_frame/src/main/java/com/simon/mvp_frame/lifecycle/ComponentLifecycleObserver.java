package com.simon.mvp_frame.lifecycle;

/**
 * Created by: Simon
 * Created on: 2017/4/28 15:38
 * Email: hanzx1024@gmail.com
 */

public interface ComponentLifecycleObserver extends LifecycleObserver {
    void onResume();

    void onStart();

    void onHiddenChanged(boolean hidden);

    void onPause();

    void onStop();
}
