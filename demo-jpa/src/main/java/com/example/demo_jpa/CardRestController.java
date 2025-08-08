package com.example.demo_jpa;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardRestController {
    private final JpaCardRepository jpaCardRepository;

    public CardRestController(JpaCardRepository jpaCardRepository) {
        this.jpaCardRepository = jpaCardRepository;
    }

    @GetMapping
    public List<Card> getList(
            @RequestParam(value = "level", required = false)
            Integer level
    ) {
        if (level == null) {
            return jpaCardRepository.findAll();
        } else {
            return jpaCardRepository.findAllByLevel(level);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getById(
            @PathVariable("id") Integer id
    ) {
        return jpaCardRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Card post(
            @RequestBody Card card
    ) {
        return jpaCardRepository.save(card);
    }

    @PutMapping("/{id}")
    public Card put(
            @PathVariable("id") Integer id,
            @RequestBody Card card
    ) {
        return jpaCardRepository.findById(id).map(
                        c -> {
                            c.setName(card.getName());
                            c.setLevel(card.getLevel());
                            c.setElement(card.getElement());
                            c.setTop(card.getTop());
                            c.setRight(card.getRight());
                            c.setBottom(card.getBottom());
                            c.setLeft(card.getLeft());
                            return jpaCardRepository.save(c);
                        })
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "resource not found")
                );
    }
}
