package com.example.sales.api.infrastructure;

import com.example.sales.api.config.CustomerItemJdbcClient;
import com.example.sales.api.domain.entity.Item;
import com.example.sales.api.domain.repository.ItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcItemRepository implements ItemRepository {
    private final JdbcClient jdbcClient;

    public JdbcItemRepository(
            @CustomerItemJdbcClient JdbcClient jdbcClient
    ) {
        this.jdbcClient = jdbcClient;
    }

    public Page<Item> findAll(Pageable pageable) {
        long totalCount = (long) jdbcClient.sql("SELECT COUNT(id) FROM item")
                .query().singleValue();

        if (totalCount == 0) {
            return Page.empty();
        }

        List<Item> result = jdbcClient.sql("""
                        SELECT 
                            id,
                            name,
                            price,
                            sell_price
                        FROM
                            item
                        ORDER BY
                            id ASC
                        LIMIT :pageSize OFFSET :offset
                        """)
                .param("offset", pageable.getOffset())
                .param("pageSize", pageable.getPageSize())
                .query(DataClassRowMapper.newInstance(Item.class))
                .list();

        return new PageImpl<>(result, pageable, totalCount);
    }

    public Optional<Item> findById(Integer id) {
        return jdbcClient.sql("""
                        SELECT 
                            id,
                            name,
                            price,
                            sell_price
                        FROM
                            item
                        WHERE
                            id = :id
                        """)
                .param("id", id)
                .query(DataClassRowMapper.newInstance(Item.class))
                .optional();
    }
}
