package com.example.demo_spring_mvc;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/persons")
public class PersonRestController {
    @GetMapping
    public Person get() {
        return new Person(
                "0001",
                "Alice",
                25
        );
    }

    @GetMapping("{id}")
    public Person getById(
            @PathVariable String id
    ) {
        return new Person(
                id,
                "Alice",
                25
        );
    }
}
