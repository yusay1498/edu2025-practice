package com.example.point.consumer.infrastructure;

import com.example.point.consumer.config.TestcontainersConfiguration;
import com.example.point.consumer.domain.entity.CustomerPoint;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

@DataJdbcTest
@Import(TestcontainersConfiguration.class)
class JdbcCustomerPointRepositoryTest {
    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.sql.init.mode", () -> "always");
    }

    @Autowired
    JdbcClient jdbcClient;

    @Test
    @Sql(statements = {
            """
            INSERT INTO customer_points (customer_id, current_points)
            VALUES ('testId1', 200);
            """
    })
    void testFindByCustomerId() {
        JdbcCustomerPointRepository jdbcCustomerPointRepository = new JdbcCustomerPointRepository(jdbcClient);

        Optional<CustomerPoint> customerPoint = jdbcCustomerPointRepository.findByCustomerId("testId1");

        Assertions.assertThat(customerPoint).isNotEmpty();
        Assertions.assertThat(customerPoint.get().customerId()).isEqualTo("testId1");
        Assertions.assertThat(customerPoint.get().currentPoints()).isEqualTo(200);
    }

    @Test
    void testSaveNewCustomerPoint() {
        JdbcCustomerPointRepository jdbcCustomerPointRepository = new JdbcCustomerPointRepository(jdbcClient);

        CustomerPoint newCustomerPoint = new CustomerPoint("testId2", 150);
        CustomerPoint savedCustomerPoint = jdbcCustomerPointRepository.save(newCustomerPoint);
        Assertions.assertThat(savedCustomerPoint).isNotNull();
        Assertions.assertThat(savedCustomerPoint.customerId()).isEqualTo("testId2");
        Assertions.assertThat(savedCustomerPoint.currentPoints()).isEqualTo(150);

        Optional<CustomerPoint> insertedCustomerPoint = jdbcClient.sql("""
                                SELECT customer_id, current_points FROM customer_points
                                WHERE customer_id = :customerId
                        """)
                .param("customerId", newCustomerPoint.customerId())
                .query(CustomerPoint.class)
                .optional();

        Assertions.assertThat(insertedCustomerPoint).isNotEmpty();
        Assertions.assertThat(insertedCustomerPoint.get().customerId()).isEqualTo("testId2");
        Assertions.assertThat(insertedCustomerPoint.get().currentPoints()).isEqualTo(150);
    }

    @Test
    @Sql(statements = {
            """
            INSERT INTO customer_points (customer_id, current_points)
            VALUES ('testId3', 100);
            """
    })
    void testSaveExistingCustomerPoint() {
        JdbcCustomerPointRepository jdbcCustomerPointRepository = new JdbcCustomerPointRepository(jdbcClient);

        CustomerPoint existingCustomerPoint = new CustomerPoint("testId3", 250);
        CustomerPoint savedCustomerPoint = jdbcCustomerPointRepository.save(existingCustomerPoint);
        Assertions.assertThat(savedCustomerPoint).isNotNull();
        Assertions.assertThat(savedCustomerPoint.customerId()).isEqualTo("testId3");
        Assertions.assertThat(savedCustomerPoint.currentPoints()).isEqualTo(250);

        Optional<CustomerPoint> updatedCustomerPoint = jdbcClient.sql("""
                                SELECT customer_id, current_points FROM customer_points
                                WHERE customer_id = :customerId
                        """)
                .param("customerId", existingCustomerPoint.customerId())
                .query(CustomerPoint.class)
                .optional();

        Assertions.assertThat(updatedCustomerPoint).isNotEmpty();
        Assertions.assertThat(updatedCustomerPoint.get().customerId()).isEqualTo("testId3");
        Assertions.assertThat(updatedCustomerPoint.get().currentPoints()).isEqualTo(250);
    }

}
