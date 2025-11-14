package com.example.sales.api.infrastructure;

import com.example.sales.api.domain.entity.SalesSummary;
import com.example.sales.api.infrastructure.JdbcSalesSummaryRepository;
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

import java.time.LocalDate;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class JdbcSalesSummaryRepositoryTest {
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
    @Sql(scripts = "classpath:/sql/schema.sql", statements = {
            """
            INSERT INTO sales_summary (id, item_id, date, total_amount, total_quantity)
            VALUES ('test-id-500', 20000, '2024-10-10', 30000, 200),
                   ('test-id-501', 20001, '2024-10-10', 90000, 900),
                   ('test-id-502', 20002, '2024-10-10', 60000, 150);
            """
    })
    void testFindAll() {
        JdbcSalesSummaryRepository jdbcSalesSummaryRepository = new JdbcSalesSummaryRepository(jdbcClient);

        Page<SalesSummary> SalesSummaryList = jdbcSalesSummaryRepository.findAll(PageRequest.of(0, 20));

        Assertions.assertThat(SalesSummaryList).isNotEmpty();
        Assertions.assertThat(SalesSummaryList.getTotalElements()).isEqualTo(3);
        Assertions.assertThat(SalesSummaryList.getTotalPages()).isEqualTo(1);
    }

    @Test
    @Sql(scripts = "classpath:/sql/schema.sql", statements = {
            """
            INSERT INTO sales_summary (id, item_id, date, total_amount, total_quantity)
            VALUES ('test-id-503', 20003, '2024-10-15', 30000, 200),
                   ('test-id-504', 20003, '2024-10-16', 90000, 900),
                   ('test-id-505', 20004, '2024-10-16', 60000, 150);
            """
    })
    void testFindByItemIdAndDateBetween() {
        JdbcSalesSummaryRepository jdbcSalesSummaryRepository = new JdbcSalesSummaryRepository(jdbcClient);

        Page<SalesSummary> SalesSummaryList = jdbcSalesSummaryRepository.findByItemIdAndDateBetween(
                20003, LocalDate.of(2024, 10, 10), LocalDate.of(2025, 10, 10), PageRequest.of(0, 20));

        Assertions.assertThat(SalesSummaryList).isNotEmpty();
        Assertions.assertThat(SalesSummaryList.getTotalElements()).isEqualTo(2);
        Assertions.assertThat(SalesSummaryList.getTotalPages()).isEqualTo(1);
    }
}