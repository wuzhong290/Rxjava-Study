package com.rxjava.examples;

/**
 * Created by thinkpad on 2017/5/21.
 */

import io.reactivex.Flowable;
import io.reactivex.Observable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class HelloWorld {

    public static void main(String[] args) {
        Flowable.just("Hello world").subscribe(System.out::println);

        System.out.println(new HelloWorld().helloWorld());
    }

    public String helloWorld(){
        Future<String> f = Observable.just("Hello").toFuture();
        if (f.isDone()) {
            try {
               return f.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}