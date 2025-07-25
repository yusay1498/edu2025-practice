package com.example.demo_junit_basic;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class EmployeeTests {
    @Test
    void testEmployeeCollectValue() {
        int expectedId = 1;
        String expectedName = "Alice";
        int expectedAge = 22;
        String expectedDepartment = "engineering";
        Gender expectedGender = Gender.FEMALE;

        Employee actual = new Employee(expectedId, expectedName, expectedAge, expectedDepartment, expectedGender);

        Assertions.assertThat(actual.id()).isEqualTo(expectedId);
        Assertions.assertThat(actual.name()).isEqualTo(expectedName);
        Assertions.assertThat(actual.age()).isEqualTo(expectedAge);
        Assertions.assertThat(actual.department()).isEqualTo(expectedDepartment);
        Assertions.assertThat(actual.gender()).isEqualTo(expectedGender);
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
            1, alice, 25, engineering
            2, bob  , 30, accounting
            """)
    void testEmployeeCollectValues(
            int expectedId, String expectedName, int expectedAge, String expectedDepartment
    ) {
        Employee actual = new Employee(expectedId, expectedName, expectedAge, expectedDepartment, Gender.FEMALE);

        Assertions.assertThat(actual.id()).isEqualTo(expectedId);
        Assertions.assertThat(actual.name()).isEqualTo(expectedName);
        Assertions.assertThat(actual.age()).isEqualTo(expectedAge);
        Assertions.assertThat(actual.department()).isEqualTo(expectedDepartment);
    }

    @Test
    void testInvalidId() {
        int invalidId = -1;

        String expectedMessage = "Invalid Employee ID. ID must be a positive Integer.";

        Assertions.assertThatThrownBy(() -> new Employee(
                invalidId,
                "alice",
                25,
                "engineering",
                Gender.FEMALE
                ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(expectedMessage);
    }
}