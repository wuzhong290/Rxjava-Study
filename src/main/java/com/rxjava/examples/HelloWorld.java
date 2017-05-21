package com.rxjava.examples;

/**
 * Created by thinkpad on 2017/5/21.
 */
import io.reactivex.*;

public class HelloWorld {
    public static void main(String[] args) {
        Flowable.just("Hello world").subscribe(System.out::println);
    }
}
