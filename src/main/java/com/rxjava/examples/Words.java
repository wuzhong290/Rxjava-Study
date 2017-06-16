package com.rxjava.examples;

import io.reactivex.Observable;

import java.util.Arrays;
import java.util.List;

/**
 * Created by wuzhong on 2017/6/16.
 */
public class Words {

    public static void main(String[] args) {
        List<String> words = Arrays.asList(
                "the",
                "quick",
                "brown",
                "fox",
                "jumped",
                "over",
                "the",
                "lazy",
                "dogs"
        );

        Observable.fromIterable(words)
                .flatMap(word -> Observable.fromArray(word.split("")))
                .distinct()
                .sorted()
                .zipWith(Observable.range(1, Integer.MAX_VALUE),
                        (string, count) -> String.format("%2d. %s", count, string))
                .subscribe(System.out::println);
    }
}
