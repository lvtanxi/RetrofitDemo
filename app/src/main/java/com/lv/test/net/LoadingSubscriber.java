package com.lv.test.net;

import android.text.TextUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;


public abstract class LoadingSubscriber<T> extends Subscriber<T> {

    private WidgetInterface mWidgetInterface;
    private boolean mShowLoadingView;
    private boolean mShowToast;

    public LoadingSubscriber(WidgetInterface widgetInterface) {
        this(widgetInterface, true);
    }

    public LoadingSubscriber(WidgetInterface widgetInterface, boolean showLoadingView) {
        this(widgetInterface, showLoadingView, true);
    }

    public LoadingSubscriber(WidgetInterface widgetInterface, boolean showLoadingView, boolean showToast) {
        mWidgetInterface=widgetInterface;
        mShowLoadingView = showLoadingView;
        mShowToast = showToast;
    }

    @Override
    public void onStart() {
        if (mWidgetInterface != null && mShowLoadingView) {
            mWidgetInterface.showLoadingView();
        }
    }

    @Override
    public void onError(Throwable e) {
        showMessageAndHideLoadingView(e);
        onFinish();
    }

    @Override
    public void onCompleted() {
        showMessageAndHideLoadingView(null);
        onFinish();
    }

    private void showMessageAndHideLoadingView(Throwable e) {
        if (mWidgetInterface == null)
            return;
        if (e != null && mShowToast)
            mWidgetInterface.showFailToast(parseMessageFromError(e));
        if (mShowLoadingView)
            mWidgetInterface.hideLoadingView();
    }

    private String parseMessageFromError(Throwable e) {
        String alertMessage;
        if (e instanceof ConnectException) {
            alertMessage = "服务器连接错误，请稍后重试。";
        } else if (e instanceof SocketTimeoutException) {
            alertMessage = "服务器连接超时，请稍后重试。";
        } else {
            alertMessage = e.getMessage();
            alertMessage = TextUtils.equals("null",alertMessage) ? "未知错误，请稍后重试" : alertMessage;
        }
        return alertMessage;
    }

    @Override
    public void onNext(T t) {
        if (t != null)
            onSuccess(t);
        onSuccessWithNull(t);
    }

    protected  void onSuccessWithNull(T t){

    }

    protected  void onSuccess(T t){

    }

    public void onFinish() {
        // subclass impl
    }
}
