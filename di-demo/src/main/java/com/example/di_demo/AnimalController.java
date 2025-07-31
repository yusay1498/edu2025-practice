package com.example.di_demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnimalController {
    private final Animal animal;

    public AnimalController(Animal animal) {
        this.animal = animal;
    }

    @GetMapping("/")
    public String get() {
        return animal.call();
    }
}
