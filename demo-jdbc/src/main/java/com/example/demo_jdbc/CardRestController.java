package com.example.demo_jdbc;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardRestController {
    private final JdbcCardRepository jdbcCardRepository;

    public CardRestController(JdbcCardRepository jdbcCardRepository) {
        this.jdbcCardRepository = jdbcCardRepository;
    }

    @GetMapping("/highPoints")
    public List<Card> getHighPoints() {
        return jdbcCardRepository.findHighPoints();
    }
}
