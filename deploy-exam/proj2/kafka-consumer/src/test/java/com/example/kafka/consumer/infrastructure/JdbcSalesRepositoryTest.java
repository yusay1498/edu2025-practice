package com.example.kafka.consumer.infrastructure;

import com.example.kafka.consumer.config.TestcontainersConfiguration;
import com.example.kafka.consumer.domain.entity.Sales;
import com.example.kafka.consumer.domain.entity.SalesItems;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@DataJdbcTest
@Import(TestcontainersConfiguration.class)
class JdbcSalesRepositoryTest {
    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.sql.init.mode", () -> "always");
    }

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    JdbcClient jdbcClient;

    @Test
    void testSaveAll() {
        JdbcSalesRepository jdbcSalesRepository = new JdbcSalesRepository(namedParameterJdbcTemplate);

        List<SalesItems> newSalesItemsList = List.of(
                new SalesItems("test-sales-items-1-1", 1000, 500, 10, 5000),
                new SalesItems("test-sales-items-1-2", 1001, 200, 25, 5000),
                new SalesItems("test-sales-items-2-1", 1002, 300, 15, 4500),
                new SalesItems("test-sales-items-2-2", 1003, 1000, 5, 5000)
        );

        List<Sales> newSalesList = List.of(
                new Sales("test-sales-id-1-1", LocalDateTime.of(2024, 8, 30, 9, 45, 0),
                        0, 0, 10000, newSalesItemsList.subList(0, 2), 10000, 10),
                new Sales("test-sales-id-1-2", LocalDateTime.of(2024, 8, 30, 10, 15, 0),
                        5, 0, 9500, newSalesItemsList.subList(2, 4), 9500, 9)
        );

        List<Sales> savedSales = jdbcSalesRepository.saveAll(newSalesList);
        Assertions.assertThat(savedSales).isNotNull();
        Assertions.assertThat(savedSales.get(0)).isEqualTo(newSalesList.get(0));
        Assertions.assertThat(savedSales.get(0).items().get(0)).isEqualTo(newSalesItemsList.get(0));

        Optional<Sales> insertedSales = jdbcClient.sql("""
                        SELECT
                            sales.id,
                            sales.date_time,
                            sales.discount,
                            sales.paid_point,
                            sales.paid_cash,
                            sales.total,
                            sales.given_point,
                            sales_items.id AS items_table_id,
                            sales_items.item_id AS  item_id,
                            sales_items.unit_price,
                            sales_items.quantity,
                            sales_items.subtotal
                        FROM sales sales
                        INNER JOIN
                            sales_items sales_items ON sales.id = sales_items.sales_id
                        WHERE
                            sales.id = :id
                        """)
                .param("id", newSalesList.get(0).id())
                .query((ResultSet resultSet, int rowNum) -> {
                    String setId = resultSet.getString("id");
                    LocalDateTime dateTime = resultSet.getTimestamp("date_time").toLocalDateTime();
                    int discount = resultSet.getInt("discount");
                    int paidPoint = resultSet.getInt("paid_point");
                    int paidCash = resultSet.getInt("paid_cash");
                    int total = resultSet.getInt("total");
                    int givenPoint = resultSet.getInt("given_point");

                    List<SalesItems> salesItems = new ArrayList<>();
                    do { // この書き方だと最初の行も取得できる
                        SalesItems items = new SalesItems(
                                resultSet.getString("items_table_id"),
                                resultSet.getInt("item_id"),
                                resultSet.getInt("unit_price"),
                                resultSet.getInt("quantity"),
                                resultSet.getInt("subtotal")
                        );
                        salesItems.add(items);
                    } while (resultSet.next());

                    return new Sales(
                            setId,
                            dateTime,
                            discount,
                            paidPoint,
                            paidCash,
                            salesItems,
                            total,
                            givenPoint
                    );
                })
                .optional();

        Assertions.assertThat(insertedSales).isNotEmpty();
        Assertions.assertThat(insertedSales.get().id()).isEqualTo("test-sales-id-1-1");
        Assertions.assertThat(insertedSales.get().dateTime()).isEqualTo(LocalDateTime.of(2024, 8, 30, 9, 45, 0));
        Assertions.assertThat(insertedSales.get().discount()).isEqualTo(0);
        Assertions.assertThat(insertedSales.get().paidPoint()).isEqualTo(0);
        Assertions.assertThat(insertedSales.get().paidCash()).isEqualTo(10000);
        Assertions.assertThat(insertedSales.get().items().stream()
                        .map(SalesItems::id)
                        .collect(Collectors.toList()))
                .containsExactlyInAnyOrder("test-sales-items-1-1", "test-sales-items-1-2");
        Assertions.assertThat(insertedSales.get().total()).isEqualTo(10000);
        Assertions.assertThat(insertedSales.get().givenPoint()).isEqualTo(10);
    }
}
