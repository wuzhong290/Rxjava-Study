package com.rxjava.examples;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.schedulers.ImmediateThinScheduler;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

/**
 * Created by wuzhong on 2017/6/17.
 */
public class ObservableTimeout {

    public static void main(String[] args) {
        RxJavaPlugins.setErrorHandler(error ->{
            System.out.println(Thread.currentThread().getName() + error.getMessage()+ "--------------setErrorHandler");
        });
        Observable<String> memory = Observable.create(new ObservableOnSubscribe<String>(){
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                        try {
                            Thread.sleep(200);
                        }catch (Exception ex){
                            e.onError(new Exception("sleep"));
                        }
                        System.out.println(Thread.currentThread().getName() + "memory");
                        //e.onError(new Exception("memory-Exception"));
                        e.onNext("memory");
                    }
                })
                .timeout(100, TimeUnit.MILLISECONDS)
                .onErrorReturn(error ->{
                    System.out.println(Thread.currentThread().getName() + "onErrorReturn");
                    return "onErrorReturn";
                })
                .subscribeOn(Schedulers.newThread());

        Observable<String> dist = Observable.create(new ObservableOnSubscribe<String>(){
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                //e.onError(new Exception("dist-Exception"));
                System.out.println(Thread.currentThread().getName() + "dist");
                e.onNext("dist");
                //e.onComplete();
            }
        }).subscribeOn(Schedulers.newThread());

        Observable<String> network = Observable.just("network").subscribeOn(Schedulers.newThread());

        Observable.zip(memory, dist, new BiFunction<String, String, Object>() {
                    @Override
                    public Object apply(@NonNull String s, @NonNull String s2) throws Exception {
                        return s+":"+s2;
                    }
                })
                .observeOn(ImmediateThinScheduler.INSTANCE)
                .subscribe(s -> {
                    System.out.println(Thread.currentThread().getName() + "--------------onNext:" + s);
                },e ->{
                    System.out.println(Thread.currentThread().getName() + e.getMessage()+ "--------------onError");
                },() ->{
                    System.out.println(Thread.currentThread().getName() + "--------------onComplete");
                });

        System.out.println(Thread.activeCount());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.activeCount());
    }
}
