package com.example;

public class DemoGenerics<T> {
    private final T someObject;

    DemoGenerics(T someObject) {
        this.someObject = someObject;
    }

    T getSomeObject() {
        return someObject;
    }
}
