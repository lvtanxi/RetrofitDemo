package com.lv.test.helper;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RxResultHelper {
    /**
     * 对结果进行预处理
     *
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<RestResult<T>, T> handleResult2() {
        return new Observable.Transformer<RestResult<T>, T>() {
            @Override
            public Observable<T> call(Observable<RestResult<T>> tObservable) {
                return tObservable.flatMap(new Func1<RestResult<T>, Observable<T>>() {
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
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };

    }


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

                );
            }
        };
    }

}