package com.simon.geek.ui.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.simon.common.utils.App;
import com.simon.common.utils.ToastHelper;
import com.simon.geek.R;
import com.simon.geek.data.Api;
import com.simon.geek.ui.main.HomeActivity;
import com.simon.mvp_frame.BaseActivity;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * Created by: Simon
 * Created on: 2016/8/20 17:01
 * Email: hanzx1024@gmail.com
 */

public class SignInActivity extends BaseActivity implements SignInContract.View {

    private LinearLayout mLoading;
    private WebView mWebView;
    private SignPresenter mPresenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_sign_dribbble;
    }

    @Override
    protected void findViews() {
        mLoading = (LinearLayout) findViewById(R.id.ll_signin_loading);
        mWebView = (WebView) findViewById(R.id.wv_signin);
        mWebView.getSettings().setJavaScriptEnabled(true);//支持javascript
        mWebView.setWebViewClient(new OAuthWebViewClient());

        mWebView.loadUrl(String.format(Api.AUTHORIZE_URL, Api.CLIENT_ID));
    }

    @Override
    protected void initObjects() {
        mPresenter = new SignPresenter(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void signSuccess() {
        ToastHelper.showLongToast(App.INSTANCE,"登录成功");
        mLoading.setVisibility(View.GONE);
        setResult(RESULT_OK, new Intent(SignInActivity.this, HomeActivity.class));
        finish();
    }

    class OAuthWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mLoading.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mLoading.setVisibility(View.GONE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            interceptUrlCompat(view, url);
            return true;
        }

    }

    private void interceptUrlCompat(WebView view, String url) {
        if (isRedirectUriFound(url, Api.CALLBACK_URL)) {
            Uri uri = Uri.parse(url);
            String tokenCode = uri.getQueryParameter(Api.PARAM_CODE);
            if (TextUtils.isEmpty(tokenCode)) {
                Log.e("sqsong", "TokenCode is null");
                return;
            }
            mLoading.setVisibility(View.VISIBLE);
            mPresenter.getUserToken(tokenCode);
            return;
        }
        Log.i("sqsong", "url---> " + url);
        view.loadUrl(url);
    }

    static boolean isRedirectUriFound(String uri, String redirectUri) {
        Uri u = null;
        Uri r = null;
        try {
            u = Uri.parse(uri);
            r = Uri.parse(redirectUri);
        } catch (NullPointerException e) {
            return false;
        }
        if (u == null || r == null) {
            return false;
        }
        boolean rOpaque = r.isOpaque();
        boolean uOpaque = u.isOpaque();
        if (rOpaque != uOpaque) {
            return false;
        }
        if (rOpaque) {
            return TextUtils.equals(uri, redirectUri);
        }
        if (!TextUtils.equals(r.getScheme(), u.getScheme())) {
            return false;
        }
        if (!TextUtils.equals(r.getAuthority(), u.getAuthority())) {
            return false;
        }
        if (r.getPort() != u.getPort()) {
            return false;
        }
        if (!TextUtils.isEmpty(r.getPath()) && !TextUtils.equals(r.getPath(), u.getPath())) {
            return false;
        }
        Set<String> paramKeys = getQueryParameterNames(r);
        for (String key : paramKeys) {
            if (!TextUtils.equals(r.getQueryParameter(key), u.getQueryParameter(key))) {
                return false;
            }
        }
        String frag = r.getFragment();
        if (!TextUtils.isEmpty(frag)
                && !TextUtils.equals(frag, u.getFragment())) {
            return false;
        }
        return true;
    }

    public static Set<String> getQueryParameterNames(Uri uri) {
        if (uri.isOpaque()) {
            throw new UnsupportedOperationException("This isn't a hierarchical URI.");
        }

        String query = uri.getEncodedQuery();
        if (query == null) {
            return Collections.emptySet();
        }

        Set<String> names = new LinkedHashSet<String>();
        int start = 0;
        do {
            int next = query.indexOf('&', start);
            int end = (next == -1) ? query.length() : next;

            int separator = query.indexOf('=', start);
            if (separator > end || separator == -1) {
                separator = end;
            }

            String name = query.substring(start, separator);
            names.add(Uri.decode(name));

            // Move start to end of name
            start = end + 1;
        } while (start < query.length());

        return Collections.unmodifiableSet(names);
    }
}
