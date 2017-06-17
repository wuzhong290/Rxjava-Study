package com.rxjava.examples;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.schedulers.ImmediateThinScheduler;
import io.reactivex.schedulers.Schedulers;
import org.apache.commons.lang.StringUtils;

/**
 * Created by wuzhong on 2017/6/16.
 */
public class ObservableConcat {
    static String memoryCache = "1";
    public static void main(String[] args) {
        Observable<String> memory = Observable.create(new ObservableOnSubscribe<String>(){
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                if(StringUtils.isNotBlank(memoryCache)){
                    e.onNext(memoryCache);
                    //e.onComplete();
                }else{
                    e.onComplete();
                }
            }
        }).subscribeOn(ImmediateThinScheduler.INSTANCE);

        Observable<String> dist = Observable.create(new ObservableOnSubscribe<String>(){
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                System.out.println(Thread.currentThread().getName() + "dist");
                e.onNext("dist");
                //e.onComplete();
            }
        }).subscribeOn(ImmediateThinScheduler.INSTANCE).cache();

        Observable<String> network = Observable.just("network").subscribeOn(Schedulers.newThread());

        Observable.concat(memory, dist, network)
                .observeOn(ImmediateThinScheduler.INSTANCE)
                .subscribe(s -> {
                    memoryCache = "memory";
                    System.out.println(Thread.currentThread().getName() + "--------------subscribe1:" + s);
                });
        System.out.println(Thread.currentThread().getName() + "=======================2");
        Observable.merge(memory, network)
                .observeOn(ImmediateThinScheduler.INSTANCE)
                .subscribe(s -> {
                    System.out.println(Thread.currentThread().getName() + "--------------subscribe2:" + s);
                });

        System.out.println(Thread.currentThread().getName() + "=======================3");

        Observable.zip(memory, dist, new BiFunction<String, String, Object>() {
                    @Override
                    public Object apply(@NonNull String s, @NonNull String s2) throws Exception {
                        return s+":"+s2;
                    }
                })
                .observeOn(ImmediateThinScheduler.INSTANCE)
                .subscribe(s -> {
                    System.out.println(Thread.currentThread().getName() + "--------------subscribe3:" + s);
                });

        System.out.println(Thread.currentThread().getName() + "=======================4");

        Observable.zip(network, dist, new BiFunction<String, String, Object>() {
                    @Override
                    public Object apply(@NonNull String s, @NonNull String s2) throws Exception {
                        return s+":"+s2;
                    }
                })
                .observeOn(ImmediateThinScheduler.INSTANCE)
                .subscribe(s -> {
                    System.out.println(Thread.currentThread().getName() + "--------------subscribe4:" + s);
                });
    }
}
