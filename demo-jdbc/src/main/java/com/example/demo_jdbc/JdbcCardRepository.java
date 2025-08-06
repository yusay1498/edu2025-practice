package com.example.demo_jdbc;

import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public class JdbcCardRepository {
    private final NamedParameterJdbcOperations jdbcOperations;

    public JdbcCardRepository(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }
    
    public List<Card> findHighPoints() {
        List<Card> cards = jdbcOperations.query("""
                WITH
                  card_sum AS (
                    SELECT
                      card.id, card.name, card.level, card.element_id, (card.top + card.right + card.bottom + card.left) AS summary
                    FROM card
                  ),
                  card_sum_max AS (
                    SELECT
                      card.level, MAX(card.top + card.left + card.bottom + card.right) AS summary_max
                    FROM card
                    GROUP BY card.level
                  )
                
                SELECT * FROM card_sum INNER JOIN card_sum_max
                ON  card_sum.level = card_sum_max.level
                AND card_sum.summary = card_sum_max.summary_max
                
                ORDER BY card_sum.level, card_sum.id;
                """,
                new MapSqlParameterSource(),
                DataClassRowMapper.newInstance(Card.class));

        return cards;
    }
}
