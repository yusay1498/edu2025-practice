package com.example.demo_jdbc;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;

@DataJdbcTest
@Import(TestContainerConfig.class)
class JdbcCardRepositoryTest {
    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.sql.init.mode", () -> "always");
    }

    @Autowired
    JdbcClient jdbcClient;

    @Sql(statements = {
            """
            INSERT INTO card(id, name, level, element_id, top, "right", bottom, "left")
            VALUES(1000, 'Test Card', 1 , 100, 1, 2, 3, 4);
            """
    })
    @Test
    void testFindById() {
        JdbcCardRepository jdbcCardRepository = new JdbcCardRepository(jdbcClient);

        Card card = jdbcCardRepository.findById(1000);

        Assertions.assertThat(card).isNotNull();
        Assertions.assertThat(card.id()).isEqualTo(1000);
        Assertions.assertThat(card.name()).isEqualTo("Test Card");
    }

}