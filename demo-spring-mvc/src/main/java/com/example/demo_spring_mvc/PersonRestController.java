package com.example.demo_spring_mvc;

import org.springframework.web.bind.annotation.*;

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

//    @GetMapping("{id}")
    @GetMapping("{id:^[0-9]{4}}")
    public Person getById(
            @PathVariable String id
    ) {
        return new Person(
                id,
                "Alice",
                25
        );
    }

    @PostMapping
    public Person post(
            @RequestBody Person person
    ) {
        return person;
    }
}
