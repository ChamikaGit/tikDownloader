package com.cd.statussaver.api;

import android.app.Activity;

import com.cd.statussaver.model.TiktokModel;
import com.cd.statussaver.model.TwitterResponse;
import com.cd.statussaver.util.Utils;
import com.google.gson.JsonObject;

import java.util.HashMap;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class CommonClassForAPI {
    private static Activity mActivity;
    private static CommonClassForAPI CommonClassForAPI;
    public static CommonClassForAPI getInstance(Activity activity) {
        if (CommonClassForAPI == null) {
            CommonClassForAPI = new CommonClassForAPI();
        }
        mActivity = activity;
        return CommonClassForAPI;
    }
    public void callResult(final DisposableObserver observer,String URL) {
        RestClient.getInstance(mActivity).getService().callResult(URL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(JsonObject o) {
                        observer.onNext(o);
                    }
                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }
                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }
    public void callTwitterApi(final DisposableObserver observer, String URL, String twitterModel) {
        RestClient.getInstance(mActivity).getService().callTwitter(URL,twitterModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TwitterResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(TwitterResponse o) {
                        observer.onNext(o);
                    }
                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }
                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void callTiktokVideo(final DisposableObserver observer, String Url) {
        HashMap<String, String> hashMapFinal = new HashMap<>();
        hashMapFinal.put("invoice_id",Utils.INVOICENUMBER);
        hashMapFinal.put("package_name",mActivity.getPackageName()+"");
        hashMapFinal.put("link",Url);
        RestClient.getInstance(mActivity).getService().getTiktokData(Utils.TikTokUrl,hashMapFinal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TiktokModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(TiktokModel o) {
                        observer.onNext(o);
                    }
                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }
                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }


}