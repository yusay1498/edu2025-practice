package com.example.sales.api.presentation;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
class ItemSalesSummaryRestControllerTest {
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName
            .parse("postgres"));

    @BeforeAll
    static void startContainers() {
        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.sales.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.sales.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.sales.password", postgreSQLContainer::getPassword);

        registry.add("spring.datasource.customer-item.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.customer-item.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.customer-item.password", postgreSQLContainer::getPassword);

        registry.add("spring.sql.init.mode", () -> "always");
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql(
            scripts = "classpath:/sql/schema.sql",
            statements = {
                    """
                    INSERT INTO sales_summary (id, item_id, date, total_amount, total_quantity)
                    VALUES ('test-id-1500', 3001, '2024-10-01', 10000, 20),
                           ('test-id-1501', 3002, '2024-10-02', 4000, 20),
                           ('test-id-1502', 3003, '2024-10-03', 6000, 20);
                    """
            },
            config = @SqlConfig(
                    dataSource = "salesDataSource"
            )
    )
    @Sql(
            scripts = "classpath:sql/customer_item.sql",
            statements = {
            """
            INSERT INTO item("id", "name", "price", "sell_price")
            VALUES(3001, 'itemA', 500, 270), (3002, 'itemB', 200, 50), (3003, 'itemC', 300, 100);
            """
            },
            config = @SqlConfig(
                    dataSource = "customerItemDataSource"
            )
    )
    void testGetItemSalesSummary() throws Throwable {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/salesSummaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                ).andDo(MockMvcResultHandlers
                        .print()
                ).andExpect(MockMvcResultMatchers
                        .status()
                        .isOk()
                );
    }

    @ParameterizedTest
    @Sql(scripts = "classpath:/sql/schema.sql", statements = {
            """
            INSERT INTO sales_summary (id, item_id, date, total_amount, total_quantity)
            VALUES ('test-id-1503', 3004, '2024-10-01', 10000, 20),
                   ('test-id-1504', 3004, '2024-10-02', 4000, 8),
                   ('test-id-1505', 3004, '2024-10-03', 6000, 12);
            """}, config = @SqlConfig(
            dataSource = "salesDataSource"
    ))
    @Sql(scripts = "classpath:sql/customer_item.sql", statements = {
            """
            INSERT INTO item("id", "name", "price", "sell_price")
            VALUES(3004, 'itemD', 500, 270);
            """}, config = @SqlConfig(
            dataSource = "customerItemDataSource"
    ))
    @CsvSource(delimiterString = "|", textBlock = """
            3004 | 3004| itemD | 500 | 2024-10-01 | 10000 | 20
            """)
    void testGetItemSalesSummaryByItemIdAndDateRange(
            Integer itemId,
            Integer expectedItemId, String expectedItemName, Integer expectedItemPrice,
            String expectedDate, Integer expectedTotalAmount, Integer expectedTotalQuantity
    ) throws Throwable {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/salesSummaries/{itemId}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                ).andDo(MockMvcResultHandlers
                        .print()
                ).andExpect(MockMvcResultMatchers
                        .status()
                        .isOk()
                ).andExpect(MockMvcResultMatchers
                        .jsonPath("$.content[0].itemId", Matchers.is(expectedItemId))
                ).andExpect(MockMvcResultMatchers
                        .jsonPath("$.content[0].itemName", Matchers.is(expectedItemName))
                ).andExpect(MockMvcResultMatchers
                        .jsonPath("$.content[0].itemPrice", Matchers.is(expectedItemPrice))
                ).andExpect(MockMvcResultMatchers
                        .jsonPath("$.content[0].date", Matchers.is(expectedDate))
                ).andExpect(MockMvcResultMatchers
                        .jsonPath("$.content[0].totalAmount", Matchers.is(expectedTotalAmount))
                ).andExpect(MockMvcResultMatchers
                        .jsonPath("$.content[0].totalQuantity", Matchers.is(expectedTotalQuantity))
                );
    }

    @ParameterizedTest
    @Sql(scripts = "classpath:/sql/schema.sql", statements = {
            """
            INSERT INTO sales_summary (id, item_id, date, total_amount, total_quantity)
            VALUES ('test-id-1506', 3005, '2024-10-01', 10000, 20),
                   ('test-id-1507', 3005, '2024-10-02', 4000, 8),
                   ('test-id-1508', 3005, '2024-10-03', 6000, 12);
            """}, config = @SqlConfig(
            dataSource = "salesDataSource"
    ))
    @Sql(scripts = "classpath:sql/customer_item.sql", statements = {
            """
            INSERT INTO item("id", "name", "price", "sell_price")
            VALUES(3005, 'itemE', 500, 270);
            """}, config = @SqlConfig(
            dataSource = "customerItemDataSource"
    ))
    @CsvSource(delimiterString = "|", textBlock = """
            3005 | 2024-10-01 | 2024-10-03 | 3005 | itemE | 500 | 2024-10-01 | 10000 | 20
            """)
    void testGetItemSalesSummaryByItemIdAndDateRangeWithStartDateAndEndDate(
            Integer itemId, String startDate, String endDate,
            Integer expectedItemId, String expectedItemName, Integer expectedItemPrice,
            String expectedDate, Integer expectedTotalAmount, Integer expectedTotalQuantity
    ) throws Throwable {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/salesSummaries/{itemId}", itemId)
                        .param("startDate", startDate)
                        .param("endDate", endDate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                ).andDo(MockMvcResultHandlers
                        .print()
                ).andExpect(MockMvcResultMatchers
                        .status()
                        .isOk()
                ).andExpect(MockMvcResultMatchers
                        .jsonPath("$.content[0].itemId", Matchers.is(expectedItemId))
                ).andExpect(MockMvcResultMatchers
                        .jsonPath("$.content[0].itemName", Matchers.is(expectedItemName))
                ).andExpect(MockMvcResultMatchers
                        .jsonPath("$.content[0].itemPrice", Matchers.is(expectedItemPrice))
                ).andExpect(MockMvcResultMatchers
                        .jsonPath("$.content[0].date", Matchers.is(expectedDate))
                ).andExpect(MockMvcResultMatchers.
                        jsonPath("$.content[0].totalAmount", Matchers.is(expectedTotalAmount))
                ).andExpect(MockMvcResultMatchers
                .jsonPath("$.content[0].totalQuantity", Matchers.is(expectedTotalQuantity))
                );
    }

    @ParameterizedTest
    @Sql(scripts = "classpath:/sql/schema.sql", statements = {
            """
            INSERT INTO sales_summary (id, item_id, date, total_amount, total_quantity)
            VALUES ('test-id-1509', 3006, '2024-10-01', 10000, 20),
                   ('test-id-1510', 3006, '2024-10-02', 4000, 8),
                   ('test-id-1511', 3006, '2024-10-03', 6000, 12);
            """}, config = @SqlConfig(
            dataSource = "salesDataSource"
    ))
    @Sql(scripts = "classpath:sql/customer_item.sql", statements = {
            """
            INSERT INTO item("id", "name", "price", "sell_price")
            VALUES(3006, 'itemF', 500, 270);
            """}, config = @SqlConfig(
            dataSource = "customerItemDataSource"
    ))
    @CsvSource(delimiterString = "|", textBlock = """
            3006 | 2024-10-01 | 3006 | itemF | 500 | 2024-10-01 | 10000 | 20
            """)
    void testGetItemSalesSummaryByItemIdAndStartDate(
            Integer itemId, String startDate,
            Integer expectedItemId, String expectedItemName, Integer expectedItemPrice,
            String expectedDate, Integer expectedTotalAmount, Integer expectedTotalQuantity
    ) throws Throwable {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/salesSummaries/{itemId}", itemId)
                        .param("startDate", startDate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                ).andDo(MockMvcResultHandlers
                        .print()
                ).andExpect(MockMvcResultMatchers
                        .status()
                        .isOk()
                ).andExpect(MockMvcResultMatchers
                        .jsonPath("$.content[0].itemId", Matchers.is(expectedItemId))
                ).andExpect(MockMvcResultMatchers
                        .jsonPath("$.content[0].itemName", Matchers.is(expectedItemName))
                ).andExpect(MockMvcResultMatchers
                        .jsonPath("$.content[0].itemPrice", Matchers.is(expectedItemPrice))
                ).andExpect(MockMvcResultMatchers.
                        jsonPath("$.content[0].date", Matchers.is(expectedDate))
                ).andExpect(MockMvcResultMatchers
                        .jsonPath("$.content[0].totalAmount", Matchers.is(expectedTotalAmount))
                ).andExpect(MockMvcResultMatchers
                .jsonPath("$.content[0].totalQuantity", Matchers.is(expectedTotalQuantity))
                );
    }

    @ParameterizedTest
    @Sql(scripts = "classpath:/sql/schema.sql", statements = {
            """
            INSERT INTO sales_summary (id, item_id, date, total_amount, total_quantity)
            VALUES ('test-id-1512', 3007, '2024-10-01', 10000, 20),
                   ('test-id-1513', 3007, '2024-10-02', 4000, 8),
                   ('test-id-1514', 3007, '2024-10-03', 6000, 12);
            """}, config = @SqlConfig(
            dataSource = "salesDataSource"
    ))
    @Sql(scripts = "classpath:sql/customer_item.sql", statements = {
            """
            INSERT INTO item("id", "name", "price", "sell_price")
            VALUES(3007, 'itemG', 500, 270);
            """}, config = @SqlConfig(
            dataSource = "customerItemDataSource"
    ))
    @CsvSource(delimiterString = "|", textBlock = """
            3007 | 2024-10-03 | 3007 | itemG | 500 | 2024-10-01 | 10000 | 20
            """)
    void testGetItemSalesSummaryByItemIdAndEndDate(
            Integer itemId, String endDate,
            Integer expectedItemId, String expectedItemName, Integer expectedItemPrice,
            String expectedDate, Integer expectedTotalAmount, Integer expectedTotalQuantity
    ) throws Throwable {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/salesSummaries/{itemId}", itemId)
                        .param("endDate", endDate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                ).andDo(MockMvcResultHandlers
                        .print()
                ).andExpect(MockMvcResultMatchers
                        .status()
                        .isOk()
                ).andExpect(MockMvcResultMatchers
                        .jsonPath("$.content[0].itemId", Matchers.is(expectedItemId))
                ).andExpect(MockMvcResultMatchers
                        .jsonPath("$.content[0].itemName", Matchers.is(expectedItemName))
                ).andExpect(MockMvcResultMatchers
                        .jsonPath("$.content[0].itemPrice", Matchers.is(expectedItemPrice))
                ).andExpect(MockMvcResultMatchers
                        .jsonPath("$.content[0].date", Matchers.is(expectedDate))
                ).andExpect(MockMvcResultMatchers
                        .jsonPath("$.content[0].totalAmount", Matchers.is(expectedTotalAmount))
                ).andExpect(MockMvcResultMatchers
                        .jsonPath("$.content[0].totalQuantity", Matchers.is(expectedTotalQuantity))
                );
    }
}