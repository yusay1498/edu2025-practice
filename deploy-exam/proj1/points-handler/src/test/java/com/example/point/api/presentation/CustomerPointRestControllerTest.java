package com.example.point.api.presentation;

import com.example.point.api.application.CustomerNotFoundException;
import com.example.point.api.application.CustomerPointService;
import com.example.point.api.domain.entity.CustomerPoint;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

@WebMvcTest(CustomerPointRestController.class)
public class CustomerPointRestControllerTest {

    @Autowired
    private MockMvcTester mockMvcTester;

    @MockitoBean
    private CustomerPointService mockedCustomerPointService;

    @Test
    void testGetPoints() {
        MvcTestResult actual = mockMvcTester
                .get()
                .uri("/points")
                .exchange();

        Assertions.assertThat(actual).hasStatusOk();
    }

    @Test
    void testGetPointByCustomerId() {
        CustomerPoint testCustomerPoint = new CustomerPoint("testId1", 100);

        Mockito.doReturn(testCustomerPoint).when(mockedCustomerPointService).lookupCustomerPointById("testId1");

        MvcTestResult actual = mockMvcTester
                .get()
                .uri("/points/{customerId}", "testId1")
                .exchange();

        String correctResponse = """
                                {
                                  "customerId":testId1,
                                  "currentPoints":100
                                }
                                """;

        Assertions.assertThat(actual)
                .debug()
                .hasStatusOk()
                .bodyJson()
                .isStrictlyEqualTo(correctResponse);
    }

    @Test
    void testGetPointsByCustomerId_withNonexistentCustomerId() {
        Mockito.doThrow(new CustomerNotFoundException("nonTestId"))
                .when(mockedCustomerPointService)
                .lookupCustomerPointById("nonTestId");

        MvcTestResult actual = mockMvcTester
                .get()
                .uri("/points/{customerId}", "nonTestId")
                .exchange();

        Assertions.assertThat(actual).hasStatus(HttpStatus.NOT_FOUND);
    }

}
