package com.example.sales.api.infrastructure;

import com.example.sales.api.config.SalesJdbcClient;
import com.example.sales.api.domain.entity.Sales;
import com.example.sales.api.domain.entity.SalesItems;
import com.example.sales.api.domain.repository.SalesHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcSalesHistoryRepository implements SalesHistoryRepository {
    private final JdbcClient jdbcClient;

    public JdbcSalesHistoryRepository(
            @SalesJdbcClient JdbcClient jdbcClient
    ) {
        this.jdbcClient = jdbcClient;
    }

    public Page<Sales> findAll(Pageable pageable) {
        long totalCount = (long) jdbcClient.sql("SELECT COUNT(sales.id) FROM sales")
                .query().singleValue();

        if (totalCount == 0) {
            return Page.empty();
        }

        List<Sales> result = jdbcClient.sql("""
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
                        FROM sales
                        INNER JOIN 
                            sales_items sales_items ON sales.id = sales_items.sales_id
                        ORDER BY 
                            date_time ASC
                        LIMIT :pageSize OFFSET :offset
                        """)
                .param("offset", pageable.getOffset())
                .param("pageSize", pageable.getPageSize())
                .query(
                        (ResultSet resultSet, int rowNum) -> {
                            String id = resultSet.getString("id");
                            LocalDateTime dateTime = resultSet.getTimestamp("date_time").toLocalDateTime();
                            int discount = resultSet.getInt("discount");
                            int paidPoint = resultSet.getInt("paid_point");
                            int paidCash = resultSet.getInt("paid_cash");
                            int total = resultSet.getInt("total");
                            int givenPoint = resultSet.getInt("given_point");

                            List<SalesItems> salesItems = new ArrayList<>();
                            // 1つのSalesについて複数のSalesItemsを読み込む
                            do {
                                SalesItems item = new SalesItems(
                                        resultSet.getString("items_table_id"),
                                        resultSet.getInt("item_id"),
                                        resultSet.getInt("unit_price"),
                                        resultSet.getInt("quantity"),
                                        resultSet.getInt("subtotal")
                                );
                                salesItems.add(item);
                            } while (resultSet.next() && resultSet.getString("id").equals(id));

                            return new Sales(
                                    id,
                                    dateTime,
                                    discount,
                                    paidPoint,
                                    paidCash,
                                    salesItems,
                                    total,
                                    givenPoint
                            );
                        }
                )
                .list();

        return new PageImpl<>(result, pageable, totalCount);
    }
}
