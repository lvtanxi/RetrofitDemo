package com.lv.test.net;

import android.support.annotation.NonNull;


public interface  WidgetInterface {

    void showLoadingView();

    void hideLoadingView();

    void showSuccessToast(@NonNull String message);

    void showFailToast(@NonNull String message);

    void notifyDialog(@NonNull String message);
}
