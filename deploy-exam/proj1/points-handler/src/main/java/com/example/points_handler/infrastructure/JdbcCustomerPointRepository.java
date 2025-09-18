package com.example.points_handler.infrastructure;

import com.example.points_handler.domain.entity.CustomerPoint;
import com.example.points_handler.domain.repository.CustomerPointRepository;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcCustomerPointRepository implements CustomerPointRepository {
    private final JdbcClient jdbcClient;

    public JdbcCustomerPointRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<CustomerPoint>  findAll() {
        return jdbcClient.sql("""
                        SELECT customer_id, point FROM customer_points
                        ORDER BY customer_id ASC
                        """)
                .query(DataClassRowMapper.newInstance(CustomerPoint.class))
                .list();
    }

    public Optional<CustomerPoint> findByCustomerId(String customerId) {
        return null;
    }
}
