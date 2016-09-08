package com.lv.rx2demo.helper;

import android.text.TextUtils;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class LoadingSubscriber<T> implements Observer<T> {

    private WeakReference<WidgetInterface> mWeakReference;
    private boolean mShowLoadingView;
    private boolean mShowToast;

    public LoadingSubscriber(WidgetInterface widgetInterface) {
        this(widgetInterface, true);
    }

    public LoadingSubscriber(WidgetInterface widgetInterface, boolean showLoadingView) {
        this(widgetInterface, showLoadingView, true);
    }

    public LoadingSubscriber(WidgetInterface widgetInterface, boolean showLoadingView, boolean showToast) {
        mWeakReference = new WeakReference<WidgetInterface>(widgetInterface);
        mShowLoadingView = showLoadingView;
        mShowToast = showToast;
    }


    @Override
    public void onError(Throwable e) {
        showMessageAndHideLoadingView(e);
        onFinish();
    }

    @Override
    public void onComplete() {
        showMessageAndHideLoadingView(null);
        onFinish();
    }

    private void showMessageAndHideLoadingView(Throwable e) {
        if (isNull())
            return;
        if (e != null && mShowToast)
            mWeakReference.get().showFailToast(parseMessageFromError(e));
        if (mShowLoadingView)
            mWeakReference.get().hideLoadingView();
    }

    private String parseMessageFromError(Throwable e) {
        String alertMessage;
        if (e instanceof ConnectException) {
            alertMessage = "服务器连接错误，请稍后重试。";
        } else if (e instanceof SocketTimeoutException) {
            alertMessage = "服务器连接超时，请稍后重试。";
        } else {
            alertMessage = e.getMessage();
            alertMessage = TextUtils.equals("null", alertMessage) ? "未知错误，请稍后重试" : alertMessage;
        }
        return alertMessage;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (!isNull()) {
            mWeakReference.get().addDisposable(d);
            if (mShowLoadingView)
                mWeakReference.get().showLoadingView();
        }
    }

    @Override
    public void onNext(T t) {
        if (t != null)
            onSuccess(t);
        onSuccessWithNull(t);
    }

    protected void onSuccessWithNull(T t) {

    }

    protected void onSuccess(T t) {

    }

    public void onFinish() {
        // subclass impl
    }

    private boolean isNull() {
        return mWeakReference == null || mWeakReference.get() == null;
    }
}
