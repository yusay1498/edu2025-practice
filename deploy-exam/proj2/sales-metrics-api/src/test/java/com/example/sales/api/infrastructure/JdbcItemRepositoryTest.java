package com.example.sales.api.infrastructure;

import com.example.sales.api.domain.entity.Item;
import com.example.sales.api.infrastructure.JdbcItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class JdbcItemRepositoryTest {
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName
            .parse("postgres"));

    @BeforeAll
    static void startContainers() {
        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);

        registry.add("spring.sql.init.mode", () -> "always");
    }

    @Autowired
    JdbcClient jdbcClient;

    @Test
    @Sql(scripts = "classpath:sql/customer_item.sql", statements = {
            """
            INSERT INTO item("id", "name", "price", "sell_price")
            VALUES(1000, 'item1', 300, 270), (1001, 'item2', 100, 50), (1002, 'item3', 1000, 100);
            """
    })
    void testFindAll() {
        JdbcItemRepository jdbcItemRepository = new JdbcItemRepository(jdbcClient);

        Page<Item> itemList = jdbcItemRepository.findAll(PageRequest.of(0, 10));

        Assertions.assertThat(itemList).isNotEmpty();
        Assertions.assertThat(itemList.getTotalElements()).isEqualTo(3);
        Assertions.assertThat(itemList.getTotalPages()).isEqualTo(1);
    }

    @Test
    @Sql(scripts = "classpath:sql/customer_item.sql", statements = {
            """
            INSERT INTO item("id", "name", "price", "sell_price")
            VALUES(1003, 'item4', 100, 70);
            """
    })
    void testFindById() {
        JdbcItemRepository jdbcItemRepository = new JdbcItemRepository(jdbcClient);

        Optional<Item> item = jdbcItemRepository.findById(1003);

        item.ifPresentOrElse(actual -> {
            Assertions.assertThat(actual.id()).isEqualTo(1003);
            Assertions.assertThat(actual.name()).isEqualTo("item4");
            Assertions.assertThat(actual.price()).isEqualTo(100);
            Assertions.assertThat(actual.sellPrice()).isEqualTo(70);
        }, () -> {
            Assertions.fail("Required not null");
        });
    }
}