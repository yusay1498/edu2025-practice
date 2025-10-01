package com.example.presentation;

import com.example.config.TestcontainersConfiguration;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import(TestcontainersConfiguration.class)
public class CustomerPointRestControllerTest {

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.sql.init.mode", () -> "always");
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetPoints() throws Throwable {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/points")
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
    @Sql(statements = {
            """
            INSERT INTO customer_points (customer_id, point)
            VALUES ('testId1', 100);
            """
    })
    @CsvSource(delimiterString =  "|", textBlock = """
            testId1 | testId1
            """)
    void testGetPointByCustomerId(
            String customerId, String expectedCustomerId
            ) throws Throwable {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/points/{customerId}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                ).andDo(MockMvcResultHandlers
                        .print()
                ).andExpect(MockMvcResultMatchers
                        .status()
                        .isOk()
                ).andExpect(MockMvcResultMatchers
                        .jsonPath("$.customerId", Matchers.is((expectedCustomerId)))
                );
    }

    @Test
    void testGetPointsByCustomerId_withNonexistentCustomerId() throws Throwable {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/points/{customerId}", "nonexistent_customerId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                ).andDo(MockMvcResultHandlers
                        .print()
                ).andExpect(MockMvcResultMatchers
                        .status()
                        .isNotFound());
    }

}
