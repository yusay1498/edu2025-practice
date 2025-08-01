package com.example.demo_spring_mvc;

import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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

    @PutMapping("/{id}")
    public Person put(
            @PathVariable String id,
            @RequestBody Person person
    ) {
        Person updatePerson = new Person("0001", "Alice", 20);

        if (!Objects.equals(id, updatePerson.id())) {
            throw new IllegalArgumentException("not match");
        }
        return person;
    }
}
