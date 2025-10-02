package com.example.point.consumer.infrastructure;

import com.example.point.consumer.domain.entity.CustomerPoint;
import com.example.point.consumer.domain.repository.CustomerPointRepository;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JdbcCustomerPointRepository implements CustomerPointRepository {
    private final JdbcClient jdbcClient;

    public JdbcCustomerPointRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Optional<CustomerPoint> findByCustomerId(String customerId) {
        return jdbcClient.sql("""
                        SELECT customer_id, point FROM customer_points
                        WHERE customer_id = :customerId
                        """)
                .param("customerId", customerId)
                .query(DataClassRowMapper.newInstance(CustomerPoint.class))
                .optional();
    }

    public CustomerPoint save(CustomerPoint customerPoint) {
        //受け取った顧客idがデータベースに存在していない場合、INSERT でid とポイントを新規保存
        if (findByCustomerId(customerPoint.customerId()).isEmpty()) {
            jdbcClient.sql("""
                            INSERT INTO customer_points (customer_id, point)
                            VALUES (:customerId, :point)
                            """)
                    .param("customerId", customerPoint.customerId())
                    .param("point", customerPoint.point())
                    .update();
            // 保存後にデータを再取得
            return findByCustomerId(customerPoint.customerId())
                    .orElseThrow(() -> new RuntimeException(
                            "Failed to INSERT customer_point for customer_id: " + customerPoint.customerId())
                    );
        } else {//顧客id がデータベースに存在する場合、UPDATE でポイントを更新
            int updateRows = jdbcClient.sql("""
                                  UPDATE customer_points
                                  SET point = :point
                                  WHERE customer_id = :customerId
                            """)
                    .param("customerId", customerPoint.customerId())
                    .param("point", customerPoint.point())
                    .update();
            if (updateRows == 1) {
                return findByCustomerId(customerPoint.customerId()).orElseThrow(
                        () -> new RuntimeException("Failed to UPDATE customer_point for customer_id: " + customerPoint.customerId())
                );
            } else if (updateRows == 0) {
                throw new RuntimeException("Failed to UPDATE customer_point for customer_id: " + customerPoint.customerId() + " — no records found.");
            } else {
                throw new RuntimeException("Failed to UPDATE customer_point for customer_id: " + customerPoint.customerId() + " — multiple records found.");
            }
        }
    }

}
