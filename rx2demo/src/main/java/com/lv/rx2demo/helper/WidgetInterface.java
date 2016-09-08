package com.lv.rx2demo.helper;

import android.support.annotation.NonNull;

import io.reactivex.disposables.Disposable;


public interface  WidgetInterface {

    void showLoadingView();

    void hideLoadingView();

    void showFailToast(@NonNull String message);

    void addDisposable(Disposable disposable);

}
