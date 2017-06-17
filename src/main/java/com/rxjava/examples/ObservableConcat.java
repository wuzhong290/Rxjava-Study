package com.rxjava.examples;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.internal.schedulers.ImmediateThinScheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wuzhong on 2017/6/16.
 */
public class ObservableConcat {
    public static void main(String[] args) {
        Observable<String> memory = Observable.create(new ObservableOnSubscribe<String>(){
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onError(new Exception("memory-Exception"));
                e.onNext("memory");
            }
        }).subscribeOn(Schedulers.newThread());

        Observable<String> dist = Observable.create(new ObservableOnSubscribe<String>(){
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
//                e.onError(new Exception("dist-Exception"));
                System.out.println(Thread.currentThread().getName() + "dist");
                e.onNext("dist");
                //e.onComplete();
            }
        }).subscribeOn(Schedulers.newThread()).cache();

        Observable<String> network = Observable.just("network").subscribeOn(Schedulers.newThread()).cache();

        Observable.concat(memory, dist, network)
                .observeOn(ImmediateThinScheduler.INSTANCE)
                .subscribe(s -> {
                    System.out.println(Thread.currentThread().getName() + "--------------onNext:" + s);
                },e ->{
                    System.out.println(Thread.currentThread().getName() + e.getMessage() + "--------------onError");
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
        System.out.println("sddd");
    }
}
