package com.example.kafka.consumer.infrastructure;

import com.example.kafka.consumer.domain.entity.Sales;
import com.example.kafka.consumer.domain.entity.SalesItems;
import com.example.kafka.consumer.domain.repository.SalesRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JdbcSalesRepository implements SalesRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcSalesRepository(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate
    ) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<Sales> saveAll(List<Sales> salesList) {
        List<MapSqlParameterSource> salesBatchParams = new ArrayList<>();
        List<MapSqlParameterSource> salesItemsBatchParams = new ArrayList<>();
        for (Sales sale : salesList) {
            salesBatchParams.add(new MapSqlParameterSource()
                    .addValue("id", sale.id())
                    .addValue("dateTime", sale.dateTime())
                    .addValue("discount", sale.discount())
                    .addValue("paidPoint", sale.paidPoint())
                    .addValue("paidCash", sale.paidCash())
                    .addValue("total", sale.total())
                    .addValue("givenPoint", sale.givenPoint()));

            for (SalesItems item : sale.items()) {
                salesItemsBatchParams.add(new MapSqlParameterSource()
                        .addValue("id", item.id())
                        .addValue("salesId", sale.id())
                        .addValue("itemId", item.itemId())
                        .addValue("unitPrice", item.unitPrice())
                        .addValue("quantity", item.quantity())
                        .addValue("subtotal", item.subtotal()));
            }
        }

        namedParameterJdbcTemplate.batchUpdate("""
                INSERT INTO sales (id, date_time, discount, paid_point, paid_cash, total, given_point)
                VALUES (:id, :dateTime, :discount, :paidPoint, :paidCash, :total, :givenPoint)
                """, salesBatchParams.toArray(new MapSqlParameterSource[0])
        );

        namedParameterJdbcTemplate.batchUpdate("""
                INSERT INTO sales_items (id, sales_id, item_id, unit_price, quantity, subtotal)
                VALUES (:id, :salesId, :itemId, :unitPrice, :quantity, :subtotal)
                """, salesItemsBatchParams.toArray(new MapSqlParameterSource[0]));

        return salesList;
    }
}