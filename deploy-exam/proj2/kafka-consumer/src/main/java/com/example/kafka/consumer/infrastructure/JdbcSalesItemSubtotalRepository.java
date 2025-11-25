package com.example.kafka.consumer.infrastructure;

import com.example.kafka.consumer.domain.entity.SalesItemSubtotal;
import com.example.kafka.consumer.domain.repository.SalesItemSubtotalRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcSalesItemSubtotalRepository implements SalesItemSubtotalRepository {
    private final JdbcClient jdbcClient;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcSalesItemSubtotalRepository(
            JdbcClient jdbcClient,
            NamedParameterJdbcTemplate namedParameterJdbcTemplate
    ) {
        this.jdbcClient = jdbcClient;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Optional<SalesItemSubtotal> findByItemIdAndDate(int itemId, LocalDate date) {
        return jdbcClient.sql("""
                        SELECT
                            id,
                            item_id,
                            sales_summary.date,
                            total_amount AS amount,
                            total_quantity AS quantity
                        FROM
                            sales_summary
                        where
                            item_id = :itemId
                        AND
                            sales_summary.date = :date
                        """)
                .param("itemId", itemId)
                .param("date", date)
                .query(SalesItemSubtotal.class)
                .optional();
    }

    public List<SalesItemSubtotal> saveAll(List<SalesItemSubtotal> salesItemSubtotalList) {
        List<MapSqlParameterSource> salesSummaryBatchParams = new ArrayList<>();
        for (SalesItemSubtotal salesItemSubtotal : salesItemSubtotalList) {
            salesSummaryBatchParams.add(new MapSqlParameterSource()
                    .addValue("id", salesItemSubtotal.id())
                    .addValue("itemId", salesItemSubtotal.itemId())
                    .addValue("date", salesItemSubtotal.date())
                    .addValue("totalAmount", salesItemSubtotal.amount())
                    .addValue("totalQuantity", salesItemSubtotal.quantity()));
        }

        int[] updateCounts = namedParameterJdbcTemplate.batchUpdate("""
                INSERT INTO sales_summary (id, item_id, date, total_amount, total_quantity)
                VALUES (:id, :itemId, :date, :totalAmount, :totalQuantity)
                ON CONFLICT (item_id, date)
                DO UPDATE
                SET total_quantity = sales_summary.total_quantity + EXCLUDED.total_quantity,
                    total_amount = sales_summary.total_amount + EXCLUDED.total_amount
                """, salesSummaryBatchParams.toArray(new MapSqlParameterSource[0])
        );

        if (Arrays.stream(updateCounts).sum() != salesItemSubtotalList.size()) {
            throw new RuntimeException("Batch update returned unexpected number of results");
        }

        return salesItemSubtotalList.stream()
                .map(salesItemSubtotal -> findByItemIdAndDate(salesItemSubtotal.itemId(), salesItemSubtotal.date())
                        .orElseThrow(() -> new RuntimeException("Failed to register SalesItemSubtotal record."))
                )
                .toList();
    }
}
