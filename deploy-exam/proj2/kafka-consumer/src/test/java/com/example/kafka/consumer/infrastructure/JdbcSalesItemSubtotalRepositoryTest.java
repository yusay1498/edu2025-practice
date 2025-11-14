package com.example.kafka.consumer.infrastructure;

import com.example.kafka.consumer.config.TestcontainersConfiguration;
import com.example.kafka.consumer.domain.entity.SalesItemSubtotal;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@DataJdbcTest
@Import(TestcontainersConfiguration.class)
class JdbcSalesItemSubtotalRepositoryTest {
    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.sql.init.mode", () -> "always");
    }

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    JdbcClient jdbcClient;

    @Test
    @Sql(statements = {
            """
            INSERT INTO sales_summary (id, item_id, date, total_amount, total_quantity)
            VALUES ('test-id-1000', 10000, '2024-10-10', 30000, 200);
            """
    })
    void testFindByItemIdAndDate() {
        JdbcSalesItemSubtotalRepository jdbcSalesItemSubtotalRepository =
                new JdbcSalesItemSubtotalRepository(jdbcClient, namedParameterJdbcTemplate);

        Optional<SalesItemSubtotal> salesItemSubtotal = jdbcSalesItemSubtotalRepository
                .findByItemIdAndDate(10000, LocalDate.of(2024, 10, 10));

        Assertions.assertThat(salesItemSubtotal).isPresent();
        Assertions.assertThat(salesItemSubtotal.get().id()).isEqualTo("test-id-1000");
        Assertions.assertThat(salesItemSubtotal.get().itemId()).isEqualTo(10000);
        Assertions.assertThat(salesItemSubtotal.get().date()).isEqualTo(LocalDate.of(2024, 10, 10));
        Assertions.assertThat(salesItemSubtotal.get().amount()).isEqualTo(30000);
        Assertions.assertThat(salesItemSubtotal.get().quantity()).isEqualTo(200);
    }

    @Test
    void testSaveAll() {
        JdbcSalesItemSubtotalRepository jdbcSalesItemSubtotalRepository =
                new JdbcSalesItemSubtotalRepository(jdbcClient, namedParameterJdbcTemplate);

        List<SalesItemSubtotal> newSalesItemSubtotalList = List.of(
                new SalesItemSubtotal("test-sales-summary-1", 5000, LocalDate.of(1999, 9, 15), 20000, 200),
                new SalesItemSubtotal("test-sales-summary-2", 5001, LocalDate.of(1999, 9, 15), 10000, 200),
                new SalesItemSubtotal("test-sales-summary-3", 5002, LocalDate.of(1999, 9, 15), 20000, 100)
        );

        List<SalesItemSubtotal> savedSalesItemSubtotal = jdbcSalesItemSubtotalRepository.saveAll(newSalesItemSubtotalList);
        Assertions.assertThat(savedSalesItemSubtotal).isNotNull();
        Assertions.assertThat(savedSalesItemSubtotal.size()).isEqualTo(newSalesItemSubtotalList.size());
        Assertions.assertThat(savedSalesItemSubtotal.get(0)).isEqualTo(newSalesItemSubtotalList.get(0));
        Assertions.assertThat(savedSalesItemSubtotal.get(1)).isEqualTo(newSalesItemSubtotalList.get(1));
        Assertions.assertThat(savedSalesItemSubtotal.get(2)).isEqualTo(newSalesItemSubtotalList.get(2));

        Optional<SalesItemSubtotal> insertedSalesSummary = jdbcClient.sql("""
                SELECT
                    id,
                    item_id,
                    sales_summary.date,
                    total_amount AS amount,
                    total_quantity AS quantity
                FROM
                    sales_summary
                where
                    id = :id
                """)
                .param("id", newSalesItemSubtotalList.get(0).id())
                .query(DataClassRowMapper.newInstance(SalesItemSubtotal.class))
                .optional();

        Assertions.assertThat(insertedSalesSummary).isNotEmpty();
        Assertions.assertThat(insertedSalesSummary.get().id()).isEqualTo(newSalesItemSubtotalList.get(0).id());
        Assertions.assertThat(insertedSalesSummary.get().itemId()).isEqualTo(newSalesItemSubtotalList.get(0).itemId());
        Assertions.assertThat(insertedSalesSummary.get().date()).isEqualTo(newSalesItemSubtotalList.get(0).date());
        Assertions.assertThat(insertedSalesSummary.get().amount()).isEqualTo(newSalesItemSubtotalList.get(0).amount());
        Assertions.assertThat(insertedSalesSummary.get().quantity()).isEqualTo(newSalesItemSubtotalList.get(0).quantity());
    }
}