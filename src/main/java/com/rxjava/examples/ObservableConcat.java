package com.rxjava.examples;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
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
        });

        Observable<String> dist = Observable.create(new ObservableOnSubscribe<String>(){
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                System.out.println("dist");
                e.onNext("dist");
                //e.onComplete();
            }
        });

        Observable<String> network = Observable.just("network");

        Observable.concat(memory, dist, network)
                .subscribe(s -> {
                    memoryCache = "memory";
                    System.out.println("--------------subscribe:" + s);
                });
        System.out.println("=======================");
        Observable.merge(memory, network).subscribe(s -> {
            System.out.println("--------------subscribe:" + s);
        });

        System.out.println("=======================");

        Observable.zip(memory, dist, new BiFunction<String, String, Object>() {
            @Override
            public Object apply(@NonNull String s, @NonNull String s2) throws Exception {
                return s+":"+s2;
            }
        }).subscribe(s -> {
            System.out.println("--------------subscribe:" + s);
        });
    }
}
