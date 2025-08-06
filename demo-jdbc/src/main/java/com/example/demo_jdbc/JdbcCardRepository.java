package com.example.demo_jdbc;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcCardRepository {
    private final JdbcOperations jdbcOperations;

    public JdbcCardRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }
    
    public List<Card> findHighPoints() {
        return null;
    }
}
