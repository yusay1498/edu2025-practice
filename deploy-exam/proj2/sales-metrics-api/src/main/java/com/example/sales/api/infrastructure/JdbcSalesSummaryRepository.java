package com.example.sales.api.infrastructure;

import com.example.sales.api.config.SalesJdbcClient;
import com.example.sales.api.domain.entity.SalesSummary;
import com.example.sales.api.domain.repository.SalesSummaryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class JdbcSalesSummaryRepository implements SalesSummaryRepository {
    private final JdbcClient jdbcClient;

    public JdbcSalesSummaryRepository(
            @SalesJdbcClient JdbcClient jdbcClient
    ) {
        this.jdbcClient = jdbcClient;
    }

    public Page<SalesSummary> findAll(Pageable pageable) {
        long totalCount = (long) jdbcClient.sql("SELECT COUNT(id) FROM sales_summary")
                .query().singleValue();

        if (totalCount == 0) {
            return Page.empty();
        }

        List<SalesSummary> result = jdbcClient.sql("""
                        SELECT 
                            id,
                            item_id,
                            sales_summary.date,
                            total_amount,
                            total_quantity
                        FROM
                            sales_summary
                        ORDER BY 
                            sales_summary.date ASC,
                            item_id ASC
                        LIMIT :pageSize OFFSET :offset
                        """)
                .param("offset", pageable.getOffset())
                .param("pageSize", pageable.getPageSize())
                .query(DataClassRowMapper.newInstance(SalesSummary.class))
                .list();

        return new PageImpl<>(result, pageable, totalCount);
    }

    public Page<SalesSummary> findByItemIdAndDateBetween(int itemId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        long totalCount = (long) jdbcClient.sql("""
            SELECT COUNT(id) FROM sales_summary
            WHERE item_id = :itemId
            AND sales_summary.date BETWEEN :startDate AND :endDate
            """)
                .param("itemId", itemId)
                .param("startDate", startDate)
                .param("endDate", endDate)
                .query().singleValue();

        if (totalCount == 0) {
            return Page.empty();
        }

        List<SalesSummary> result = jdbcClient.sql("""
            SELECT
                id,
                item_id,
                sales_summary.date,
                total_amount,
                total_quantity
            FROM
                sales_summary
            WHERE item_id = :itemId
            AND sales_summary.date BETWEEN :startDate AND :endDate
            ORDER BY
                sales_summary.date ASC
            LIMIT :pageSize OFFSET :offset
            """)
                .param("itemId", itemId)
                .param("startDate", startDate)
                .param("endDate", endDate)
                .param("offset", pageable.getOffset())
                .param("pageSize", pageable.getPageSize())
                .query(DataClassRowMapper.newInstance(SalesSummary.class))
                .list();

        return new PageImpl<>(result, pageable, totalCount);
    }
}
