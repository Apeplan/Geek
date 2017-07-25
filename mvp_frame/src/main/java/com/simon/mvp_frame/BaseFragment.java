package com.simon.mvp_frame;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simon.mvp_frame.lifecycle.LifecycleObservable;
import com.simon.mvp_frame.lifecycle.LifecycleObserver;
import com.simon.mvp_frame.lifecycle.LifecycleObserverCompat;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by: Simon
 * Created on: 15/10/12 17:28
 * Email: hanzx1024@gmail.com
 */
public abstract class BaseFragment extends Fragment implements LifecycleObservable {

    private View root;

    private LifecycleObserverCompat mObserverCompat = new LifecycleObserverCompat();

    private CompositeSubscription compositeSubscription;

    protected Activity mActivity;

    /**
     * 当前Fragment是否处于可见状态标志，防止因ViewPager的缓存机制而导致回调函数的触发
     */
    private boolean isFragmentVisible;

    public boolean isFirst;  // 是否是第一次加载

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isFragmentVisible = true;
        }
        if (root == null) {
            return;
        }
        // 可见，并且没有加载过
        if (isFragmentVisible && !isFirst) {
            onFragmentVisibleChange(true);
            isFirst = true;
            return;
        }
        // 由可见——>不可见 已经加载过
        if (isFragmentVisible) {
            onFragmentVisibleChange(false);
            isFragmentVisible = false;
        }
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container,
                                   Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutResId(), container, false);
        this.root = rootView;
        return rootView;
    }

    @Override
    public final void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isAutoCall()) {
            parseBundle(getArguments());
            findViews();
            initObjects();
            initData();
            setListener();
        }

        // 当TabLayout跳转到一个没有预加载过的Fragment，连续调用两次setUserVisibleHint方法，
        // 但此时rootView为空，不能进行加载。需要在onCreateView()方法最后判断是否进行网络加载，
        // 然后调用onFragmentVisibleChange(true)方法
        // 可见，但是并没有加载过
        if (isFragmentVisible && !isFirst) {
            onFragmentVisibleChange(true);
            isFirst = true;
        }

        mObserverCompat.onCreate();
    }

    /**
     * 当前fragment可见状态发生变化时会回调该方法
     * 如果当前fragment是第一次加载，等待onCreateView后才会回调该方法，其它情况回调时机跟 {@link #setUserVisibleHint(boolean)}一致
     * 在该回调方法中你可以做一些加载数据操作，甚至是控件的操作，因为配合fragment的view复用机制，你不用担心在对控件操作中会报 null 异常
     *
     * @param isVisible true  不可见 -> 可见
     *                  false 可见  -> 不可见
     */
    protected void onFragmentVisibleChange(boolean isVisible) {
        Log.w("BaseFragment", "onFragmentVisibleChange -> isVisible: " + isVisible);
    }


    @Override
    public final void subscribeLifecycle(LifecycleObserver observer) {
        mObserverCompat.subscribeLifecycle(observer);
    }

    @Override
    public final boolean unsubscribeLifecycle(LifecycleObserver observer) {
        return mObserverCompat.unsubscribeLifecycle(observer);
    }

    /**
     * 查找当前Fragment中的view对象
     *
     * @param resId 需要查找的View对象的ID值
     *
     * @return 找到的View对象, 可能为空
     */
    @Nullable
    public final <T extends View> T findViewById(@IdRes int resId) {
        return (T) root.findViewById(resId);
    }

    /**
     * 视图主布局,用于包含InnerLayout
     *
     * @return 布局ResID
     */
    @LayoutRes
    protected abstract int getLayoutResId();

    /**
     * 处理传入参数,初始化传入参数
     *
     * @param bundle bundle参数,可能为空
     */
    protected void parseBundle(Bundle bundle) {
    }

    /**
     * 初始化所有的view从ContentLayout中
     */
    protected abstract void findViews();

    /**
     * 初始化所有的非View对象,例如Adapter,Fragment,Manager,Presenter等相关对象
     */
    protected abstract void initObjects();

    /**
     * 初始化页面上的数据,可以设置相关view的显示方式,静态数据,或者发起网络请求,数据库加载等
     */
    protected abstract void initData();

    /**
     * 设置所有的监听器,广播接收器等相关回调
     */
    protected void setListener() {
    }

    /**
     * 是否由父类控制基础方法的调用顺序
     * 基础方法包括
     * <p>
     * parseBundle(Bundle);
     * findViews();
     * initObjects();
     * initData();
     * setListener();
     * </p>
     *
     * @return true则由基类控制以上基础方法调用顺序, false反之, 基类将不会主动调用
     */
    protected boolean isAutoCall() {
        return true;
    }

    /**
     * 通过此方法获得该Fragment的Title
     *
     * @return 该fragment具有的业务属性字符串, 将会用做fragment的唯一标示
     */
    public String getTitle() {
        return this.getClass().getSimpleName();
    }

    /**
     * 增加到RXjava的订阅管理器中,统一管理订阅事件,用于销毁时统一销毁订阅事件
     *
     * @param subscription 订阅对象
     */
    public void addSubscribe(Subscription subscription) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);
    }

    public void unSubscribe() {
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mObserverCompat.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mObserverCompat.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mObserverCompat.onHiddenChanged(hidden);
    }

    @Override
    public void onPause() {
        super.onPause();
        mObserverCompat.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mObserverCompat.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unSubscribe();
        mObserverCompat.onDestroy();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }
}
