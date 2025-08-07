//package com.example.demo_jdbc.legacy;
//
//import org.springframework.jdbc.core.DataClassRowMapper;
//import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public class JdbcCardRepository {
//    private final NamedParameterJdbcOperations jdbcOperations;
//
//    public JdbcCardRepository(NamedParameterJdbcOperations jdbcOperations) {
//        this.jdbcOperations = jdbcOperations;
//    }
//
//    public List<Card> findHighPoints() {
//        return jdbcOperations.query("""
//                WITH
//                  card_sum AS (
//                    SELECT
//                      card.id, card.name, card.level, card.element_id, (card.top + card.right + card.bottom + card.left) AS summary
//                    FROM card
//                  ),
//                  card_sum_max AS (
//                    SELECT
//                      card.level, MAX(card.top + card.left + card.bottom + card.right) AS summary_max
//                    FROM card
//                    GROUP BY card.level
//                  )
//
//                SELECT * FROM card_sum INNER JOIN card_sum_max
//                ON  card_sum.level = card_sum_max.level
//                AND card_sum.summary = card_sum_max.summary_max
//
//                ORDER BY card_sum.level, card_sum.id;
//                """,
//                new MapSqlParameterSource(),
//                DataClassRowMapper.newInstance(Card.class));
//    }
//
//    public List<Card> findHighPointsResultSet() {
//        return jdbcOperations.query("""
//                WITH
//                  card_sum AS (
//                    SELECT
//                      card.id, card.name, card.level, card.element_id, (card.top + card.right + card.bottom + card.left) AS summary
//                    FROM card
//                  ),
//                  card_sum_max AS (
//                    SELECT
//                      card.level, MAX(card.top + card.left + card.bottom + card.right) AS summary_max
//                    FROM card
//                    GROUP BY card.level
//                  )
//
//                SELECT * FROM card_sum INNER JOIN card_sum_max
//                ON  card_sum.level = card_sum_max.level
//                AND card_sum.summary = card_sum_max.summary_max
//
//                ORDER BY card_sum.level, card_sum.id;
//                """,
//                new MapSqlParameterSource(),
//                (resultSet, roeNum) -> {
//                    resultSet.next();
//                    Integer id = resultSet.getInt("id");
//                    String name = resultSet.getString("name");
//                    Integer level = resultSet.getInt("level");
//                    Integer elementId = resultSet.getInt("element_Id");
//                    Integer summary = resultSet.getInt("summary");
//
//                    return new Card(id, name, level, elementId, summary);
//                });
//    }
//
//    public List<Card> findHighPointByLevel(int level) {
//        return jdbcOperations.query("""
//                WITH
//                  card_sum AS (
//                    SELECT
//                      card.id, card.name, card.level, card.element_id, (card.top + card.right + card.bottom + card.left) AS summary
//                    FROM card
//                  ),
//                  card_sum_max AS (
//                    SELECT
//                      card.level, MAX(card.top + card.left + card.bottom + card.right) AS summary_max
//                    FROM card
//                    GROUP BY card.level
//                  )
//
//                SELECT * FROM card_sum INNER JOIN card_sum_max
//                ON  card_sum.level = card_sum_max.level
//                AND card_sum.summary = card_sum_max.summary_max
//
//                WHERE card_sum.level = :level
//
//                ORDER BY card_sum.level, card_sum.id;
//                """,
//                new MapSqlParameterSource()
//                        .addValue("level", level),
//                DataClassRowMapper.newInstance(Card.class));
//    }
//}
