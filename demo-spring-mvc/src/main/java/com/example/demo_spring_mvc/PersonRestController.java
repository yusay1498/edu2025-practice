package com.example.demo_spring_mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/persons")
public class PersonRestController {
    private final Logger logger = LoggerFactory.getLogger(PersonRestController.class);

    @GetMapping
    public Person get() {
        logger.debug("Invoke get method");

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
        logger.debug("Invoke getById method args{}", id);

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

        if (!Objects.equals(id, updatePerson.getId())) {
            throw new IllegalArgumentException("not match");
        }
        return person;
    }

    @PatchMapping("/{id}")
    public Person patch(
            @PathVariable("id") String id,
            @RequestBody PersonPatchRequest personPatch
    ) {
        Person exitedPerson = new Person(
                "0001", "alice", 7);

        return exitedPerson.copy(
                personPatch.getId(),
                personPatch.getName(),
                personPatch.getAge()
        );
    }
}
