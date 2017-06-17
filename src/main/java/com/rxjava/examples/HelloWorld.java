package com.rxjava.examples;

/**
 * Created by thinkpad on 2017/5/21.
 */

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class HelloWorld {
    public static void main(String[] args) {
        Flowable.just("Hello world").subscribe(System.out::println);
    }
    public String helloWorld(){
        DemoObserver observer = new DemoObserver();
        Observable<String> observable = Observable.just("Hello"); // provides datea
        observable.subscribe(observer); // Callable as subscriber
        return observer.getResult();
    }

}
class DemoObserver implements Observer<java.lang.String>{
    private String result = "";
    @Override
    public void onSubscribe(@NonNull Disposable d) {
        System.out.println(Thread.currentThread().getName() + d.isDisposed() + "--------------onSubscribe");
    }

    @Override
    public void onNext(@NonNull String o) {
        System.out.println(Thread.currentThread().getName() + "--------------onNext:" + o);
        result = o;
    }


    @Override
    public void onError(@NonNull Throwable e) {
        System.out.println(Thread.currentThread().getName() + e.getMessage()+ "--------------onError");
    }

    @Override
    public void onComplete() {
        System.out.println(Thread.currentThread().getName() + "--------------onComplete");
    }

    public String getResult() {
        return result;
    }
}
