package com.example.sales.api.presentation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.nio.charset.StandardCharsets;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class ItemRestControllerTest {
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName
            .parse("postgres"));

    @BeforeAll
    static void startContainers() {
        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.customer-item.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.customer-item.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.customer-item.password", postgreSQLContainer::getPassword);

        registry.add("spring.sql.init.mode", () -> "always");
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql(
            scripts = "classpath:sql/customer_item.sql",
            statements = {
                    """
                    INSERT INTO item("id", "name", "price", "sell_price")
                    VALUES(10000, 'item10', 300, 270), (10001, 'item20', 100, 50), (10002, 'item30', 1000, 100);
                    """
            },
            config = @SqlConfig(
                    dataSource = "customerItemDataSource"
            )
    )
    void testGetItem() throws Throwable {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                ).andDo(MockMvcResultHandlers
                        .print()
                ).andExpect(MockMvcResultMatchers
                        .status()
                        .isOk()
                );
    }
}