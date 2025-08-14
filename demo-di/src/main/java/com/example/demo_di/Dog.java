package com.example.demo_di;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class Dog implements Animal {
    @Override
    public String call() {
        return "Bow Bow";
    }
}
