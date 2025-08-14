package com.example.demo_di;

import org.springframework.stereotype.Component;

@Component
public class Duck implements Animal {
    @Override
    public String call() {
        return "Quack";
    }
}
