package com.simon.mvp_frame.lifecycle;

/**
 * The Observable class that implements the Reactive Pattern.
 * <p>
 * This class provides methods for subscribing to the Observable as well as delegate methods to
 * the various Observers.
 * <p>
 * Created by: Simon
 * Created on: 2017/4/28 15:24
 * Email: hanzx1024@gmail.com
 */

public interface LifecycleObservable<T extends LifecycleObserver> {

    void subscribeLifecycle(T observer);

    boolean unsubscribeLifecycle(T observer);
}
