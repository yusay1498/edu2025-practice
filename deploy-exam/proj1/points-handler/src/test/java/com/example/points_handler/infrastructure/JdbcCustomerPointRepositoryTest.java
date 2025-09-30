package com.example.points_handler.infrastructure;

import com.example.points_handler.config.TestcontainersConfiguration;
import com.example.points_handler.domain.entity.CustomerPoint;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

@DataJdbcTest
@Import(TestcontainersConfiguration.class)
public class JdbcCustomerPointRepositoryTest {

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.sql.init.mode", () -> "always");
    }

    @Autowired
    JdbcClient jdbcClient;

    @Test
    @Sql(statements = {
            """
            INSERT INTO customer_points (customer_id, point)
            VALUES ('testId1', 200);
            """
    })
    void testFindAll() {
        JdbcCustomerPointRepository jdbcCustomerPointRepository = new JdbcCustomerPointRepository(jdbcClient);

        List<CustomerPoint> customerPoints = jdbcCustomerPointRepository.findAll();

        Assertions.assertThat(customerPoints).isNotEmpty();
        Assertions.assertThat(customerPoints).hasSize(1);
        Assertions.assertThat(customerPoints.get(0).customerId()).isEqualTo("testId1");
        Assertions.assertThat(customerPoints.get(0).point()).isEqualTo(200);
    }

    @Test
    @Sql(statements = {
            """
            INSERT INTO customer_points (customer_id, point)
            VALUES ('testId1', 200);
            """
    })
    void testFindByCustomerId() {
        JdbcCustomerPointRepository jdbcCustomerPointRepository = new JdbcCustomerPointRepository(jdbcClient);

        Optional<CustomerPoint> customerPoint = jdbcCustomerPointRepository.findByCustomerId("testId1");

        Assertions.assertThat(customerPoint).isNotEmpty();
        Assertions.assertThat(customerPoint.get().customerId()).isEqualTo("testId1");
        Assertions.assertThat(customerPoint.get().point()).isEqualTo(200);
    }
}
