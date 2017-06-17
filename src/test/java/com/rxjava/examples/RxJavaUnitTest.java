package com.rxjava.examples;

import io.reactivex.Observable;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by wuzhong on 2017/6/17.
 */
public class RxJavaUnitTest {
    String result="";

    // Simple subscription to a fix value
    @Test
    public void returnAValue(){
        result = "";
        Observable<String> observer = Observable.just("Hello"); // provides datea
        observer.subscribe(s -> result=s); // Callable as subscriber
        Assert.assertTrue(result.equals("Hello"));
    }
    @Test
    public void testMain(){
        new HelloWorld().main(null);
    }
    @Test
    public void testHelloWorld(){
        Assert.assertTrue(new HelloWorld().helloWorld().equals("Hello"));
    }

}
