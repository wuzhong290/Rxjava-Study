package com.rxjava.examples;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
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
                }else{
                    e.onComplete();
                }
            }
        });

        Observable<String> network = Observable.just("network");

        Observable.concat(memory , network)
                .subscribe(s -> {
                    memoryCache = "memory";
                    System.out.println("--------------subscribe:" + s);
                });
    }
}
