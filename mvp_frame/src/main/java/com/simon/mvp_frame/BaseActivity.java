package com.simon.mvp_frame;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.jakewharton.rxbinding.view.RxView;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.simon.mvp_frame.lifecycle.LifecycleObservable;
import com.simon.mvp_frame.lifecycle.LifecycleObserver;
import com.simon.mvp_frame.lifecycle.LifecycleObserverCompat;
import com.simon.mvp_frame.permission.Permission;
import com.simon.mvp_frame.permission.PermissionCallback;
import com.simon.mvp_frame.permission.RxPermissions;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by: Simon
 * Created on: 15/10/12 17:28
 * Email: hanzx1024@gmail.com
 */
public abstract class BaseActivity extends AppCompatActivity implements LifecycleObservable {

    protected LayoutInflater mInflater = null;

    private RxPermissions mRxPermissions;

    private boolean mIsDestory;

    private CompositeSubscription compositeSubscription;

    private LifecycleObserverCompat mObserverCompat = null;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {//防止重建时导致fragment恢复
            String FRAGMENTS_TAG = "android:support:fragments";
            savedInstanceState.remove(FRAGMENTS_TAG);
        }
        super.onCreate(savedInstanceState);
        beforeContentViewLoaded();
        inflateContentView();

        if (isStatusBarTranslucent()) {
            setStatusBarTranslucent();
        }

        initBaseSelf();
        onContentViewLoaded();
        onInstanceStateRestore(savedInstanceState);

        if (isAutoCall()) {
            parseBundle(getIntent().getExtras());
            findViews();
            initObjects();
            initData();
            setListener();
        }
        mObserverCompat.onCreate();
        mRxPermissions = new RxPermissions(this);
    }

    @CallSuper
    protected void initBaseSelf() {
        mObserverCompat = new LifecycleObserverCompat();
    }

    @Override
    protected final void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            parseBundle(intent.getExtras());
        }
    }

    /**
     * 注入业务布局
     */
    protected void inflateContentView() {
        mInflater = LayoutInflater.from(this);
        setContentView(getLayoutResId());
    }

    /**
     * 将在{@link AppCompatActivity#setContentView(int)}之前调用<br>
     * 可以设置一些关于本Window相关的设置,详见{@link #requestWindowFeature(int)}方法
     */
    protected void beforeContentViewLoaded() {

    }

    /**
     * 将在{@link AppCompatActivity#setContentView(int)}之后调用<br>
     * 可以进行相关的基础页面控件的初始化,例如TopBar等
     */
    protected void onContentViewLoaded() {

    }

    /**
     * 状态存储恢复方法,详情请参阅{@link #onSaveInstanceState(Bundle)}
     *
     * @param savedInstanceState 需要回复的Bundle,可能为空
     */
    protected void onInstanceStateRestore(@Nullable Bundle savedInstanceState) {

    }

    /**
     * 视图主布局,用于包含InnerLayout
     *
     * @return 布局ResID
     */
    @LayoutRes
    protected abstract int getLayoutResId();

    /**
     * 初始化所有的view从ContentLayout中
     */
    protected abstract void findViews();

    /**
     * 处理传入参数,初始化传入参数
     *
     * @param bundle bundle参数,可能为空
     */
    protected void parseBundle(@Nullable Bundle bundle) {
    }

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
     * 获取Activity对象,返回当前的Activity实例
     *
     * @return 返回当前的Activity实例
     */
    public Context getContext() {
        return this;
    }

    /**
     * 添加生命周期观察者
     *
     * @param observer 生命周期观察者
     */
    @Override
    public final void subscribeLifecycle(@NonNull LifecycleObserver observer) {
        mObserverCompat.subscribeLifecycle(observer);
    }

    /**
     * 移除生命周期观察者
     *
     * @param observer 生命周期观察者
     */
    @Override
    public final boolean unsubscribeLifecycle(@NonNull LifecycleObserver observer) {
        return mObserverCompat.unsubscribeLifecycle(observer);
    }

    /**
     * 申请运行时权限,可以单个申请，也可以以组申请
     *
     * @param callback    权限申请结果回调
     * @param permissions 申请的权限
     */
    protected final void requestPermission(final PermissionCallback callback, final String...
            permissions) {
        // Must be done during an initialization phase like onCreate
        mRxPermissions.requestEach(permissions).subscribe(new Action1<Permission>() {
            @Override
            public void call(Permission permission) {
                if (permission.granted) {
                    if (null != callback) {
                        callback.onGranted(permissions);
                    }
                } else if (permission.shouldShowRequestPermissionRationale) {
                    // Denied permission without ask never again
                    if (null != callback) {
                        callback.onRequestPermissionRationale(permissions);
                    }
                } else {
                    // Denied permission with ask never again
                    // Need to go to the settings
                    if (null != callback) {
                        callback.onRefuse(permissions);
                    }
                }
            }
        });
    }

    /**
     * 点击 View 申请权限,可以单个申请，也可以以组申请
     *
     * @param callback    权限申请结果回调
     * @param permissions 申请的权限
     */
    protected final void requestPermission(View view, final PermissionCallback callback, final
    String... permissions) {
        // Must be done during an initialization phase like onCreate
        RxView.clicks(view).compose(mRxPermissions.ensureEach(permissions))
                .subscribe(new Action1<Permission>() {

                    @Override
                    public void call(Permission permission) {
                        if (permission.granted) {
                            if (null != callback) {
                                callback.onGranted(permissions);
                            }
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // Denied permission without ask never again
                            if (null != callback) {
                                callback.onRequestPermissionRationale(permissions);
                            }
                        } else {
                            // Denied permission with ask never again
                            // Need to go to the settings
                            if (null != callback) {
                                callback.onRefuse(permissions);
                            }
                        }
                    }
                });
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
    protected void onStart() {
        super.onStart();
        mObserverCompat.onStart();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mObserverCompat.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mObserverCompat.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mObserverCompat.onStop();
    }

    @Override
    protected void onDestroy() {
        mIsDestory = true;
        super.onDestroy();
        unSubscribe();
        mObserverCompat.onDestroy();
    }

    public boolean isDestroyed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return super.isDestroyed();
        } else {
            return mIsDestory;
        }
    }

    /**
     * 设置通用的 ToolBar
     *
     * @param toolbar
     * @param title
     */
    protected void setCommonBackToolBack(Toolbar toolbar, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new Toolbar.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * 状态栏是否设置为透明
     */
    protected boolean isStatusBarTranslucent() {
        return true;
    }

    // TODO:适配4.4
    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected void setStatusBarTranslucent() {

//        StatusbarUtil.setBgColor(this,R.color.statusBar);
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            // 激活状态栏
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.statusBar);
//            tintManager.setStatusBarTintColor(getResources().getColor(R.color.statusBar));
        }
    }

}
