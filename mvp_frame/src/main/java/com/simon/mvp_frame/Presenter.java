package com.simon.mvp_frame;

import rx.Subscription;

/**
 * Created by: Simon
 * Created on: 2017/4/28 14:51
 * Email: hanzx1024@gmail.com
 */

public interface Presenter {

    void subscribe(Subscription subscription);

    void unsubscribe();
}
