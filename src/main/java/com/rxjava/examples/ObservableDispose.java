package com.rxjava.examples;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.internal.schedulers.ImmediateThinScheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wuzhong on 2017/6/17.
 */
public class ObservableDispose {
    public static void main(String[] args) {
        Observable<String> memory = Observable.create(new ObservableOnSubscribe<String>(){
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("memory");
            }
        }).subscribeOn(Schedulers.newThread());

        Observable<String> dist = Observable.create(new ObservableOnSubscribe<String>(){
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                System.out.println(Thread.currentThread().getName() + "dist");
                e.onNext("dist");
            }
        }).subscribeOn(Schedulers.newThread());

        Observable<String> network = Observable.just("network").subscribeOn(Schedulers.newThread());
        Observable.merge(memory,dist, network)
                .observeOn(ImmediateThinScheduler.INSTANCE)
                .subscribe(s -> {
                    System.out.println(Thread.currentThread().getName() + "--------------onNext:" + s);
                },e ->{
                    System.out.println(Thread.currentThread().getName() + e.getMessage()+ "--------------onError");
                },() ->{
                    System.out.println(Thread.currentThread().getName() + "--------------onComplete");
                },d ->{
                    d.dispose();
                    System.out.println(Thread.currentThread().getName() + d.isDisposed() + "--------------onSubscribe");
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
