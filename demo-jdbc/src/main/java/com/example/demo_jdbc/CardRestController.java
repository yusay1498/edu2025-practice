package com.example.demo_jdbc;

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

    @GetMapping("/highPoints2")
    public List<Card> getHighPointsResultSet() {
        return jdbcCardRepository.findHighPointsResultSet();
    }

    @GetMapping("/highPoints/{level}")
    public List<Card> getHighPoints(@PathVariable int level) {
        return jdbcCardRepository.findHighPointByLevel(level);
    }
}
