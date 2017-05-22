package com.simon.mvp_frame.lifecycle;

/**
 * Provides a mechanism for receiving push-based notifications.
 * <p>
 * Created by: Simon
 * Created on: 2017/4/28 15:25
 * Email: hanzhanxi@01zhuanche.com
 */

public interface LifecycleObserver {
    void onCreate();

    void onDestroy();
}
