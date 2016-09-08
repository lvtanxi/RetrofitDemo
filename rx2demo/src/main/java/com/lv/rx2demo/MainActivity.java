package com.lv.rx2demo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.lv.rx2demo.client.RetrofitClient;
import com.lv.rx2demo.helper.LoadingSubscriber;
import com.lv.rx2demo.helper.RxSchedulers;
import com.lv.rx2demo.helper.WidgetInterface;
import com.lv.rx2demo.model.TestBean;

import org.reactivestreams.Subscriber;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements WidgetInterface {
    private CompositeDisposable mCompositeDisposable;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressDialog = new ProgressDialog(this);
    }

    public void onData(View view) {
        RetrofitClient.getInstance()
                .getApiInterface()
                .dataOne2()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LoadingSubscriber<TestBean>(this){
                    @Override
                    protected void onSuccess(TestBean testBean) {
                        DLog.d(testBean);
                    }
                });
    }

    @Override
    public void showLoadingView() {
        if (null != mProgressDialog && !mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    @Override
    public void hideLoadingView() {
        if (null != mProgressDialog && mProgressDialog.isShowing())
            mProgressDialog.hide();
    }

    @Override
    public void showFailToast( String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addDisposable(Disposable disposable) {
        if (mCompositeDisposable == null)
            mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(disposable);
    }


    protected <T> void addSubscription(Flowable<T> flowable, Subscriber<T> subscriber) {
        flowable.compose(RxSchedulers.<T>io_main())
                .subscribe(subscriber);
    }

    @Override
    protected void onDestroy() {
        DLog.d(">>>>>>>>>>>>>");
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
            mCompositeDisposable = null;
        }
        super.onDestroy();
    }

}