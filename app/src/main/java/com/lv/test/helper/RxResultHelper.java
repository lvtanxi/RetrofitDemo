package com.lv.test.helper;

import rx.Observable;
import rx.functions.Func1;

public class RxResultHelper {

    public static <T> Observable.Transformer<RestResult<T>, T> handleResult() {
        return new Observable.Transformer<RestResult<T>, T>() {
            @Override
            public Observable<T> call(Observable<RestResult<T>> tObservable) {
                return tObservable.flatMap(
                        new Func1<RestResult<T>, Observable<T>>() {
                            @Override
                            public Observable<T> call(RestResult<T> result) {
                                if (result.getCode() == RestResult.SUCCESS) {
                                    return Observable.just(result.getData());
                                } else if (result.getCode() == RestResult.SIGN_OUT) {
                                    // 处理被踢出登录情况
                                } else {
                                    return Observable.error(new Exception(result.getMessage()));
                                }
                                return Observable.empty();
                            }
                        }

                ).onErrorReturn(new Func1<Throwable, T>() {
                    @Override
                    public T call(Throwable throwable) {
                        throw new IllegalArgumentException(throwable.getMessage());
                    }
                });
            }
        };
    }

}