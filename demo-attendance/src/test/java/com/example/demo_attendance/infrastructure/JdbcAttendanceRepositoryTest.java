package com.example.demo_attendance.infrastructure;

import com.example.demo_attendance.config.TestContainerConfig;
import com.example.demo_attendance.domain.entity.Attendance;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestContainerConfig.class)
class JdbcAttendanceRepositoryTest {
    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.sql.init.mode", () -> "always");
    }

    @Autowired
    JdbcClient jdbcClient;

    @Test
    @Sql(statements = {
            """
            INSERT INTO attendances (id, employee_id, begin_work, finish_work) 
            VALUES ('a5e5e9d4-40d7-4f7d-8b95-5a5d6d881ba5', 'emp000', '2025-08-30 09:00:00', '2025-08-30 17:00:00');
            """
    })
    void testFindAll() {
        JdbcAttendanceRepository jdbcAttendanceRepository = new JdbcAttendanceRepository(jdbcClient);

        List<Attendance> attendances = jdbcAttendanceRepository.findAll();

        Assertions.assertThat(attendances).isNotEmpty();
        Assertions.assertThat(attendances.getFirst().id()).isNotNull();
    }

    @Test
    @Sql(statements = {
            """
            INSERT INTO attendances (id, employee_id, begin_work, finish_work)
            VALUES ('b5e5e9d4-40d7-4f7d-8b95-5a5d6d881ba5', 'emp100', '2025-07-30 09:00:00', '2025-07-30 17:00:00');
            """
    })
    void testFindById() {
        JdbcAttendanceRepository jdbcAttendanceRepository = new JdbcAttendanceRepository(jdbcClient);

        Optional<Attendance> attendance = jdbcAttendanceRepository.findById("b5e5e9d4-40d7-4f7d-8b95-5a5d6d881ba5");

        attendance.ifPresentOrElse(actual -> {
            Assertions.assertThat(actual.id()).isEqualTo("b5e5e9d4-40d7-4f7d-8b95-5a5d6d881ba5");
            Assertions.assertThat(actual.employeeId()).isEqualTo("emp100");
            Assertions.assertThat(actual.beginWork()).isEqualTo(LocalDateTime.of(2025, 7, 30, 9, 0));
            Assertions.assertThat(actual.finishWork()).isEqualTo(LocalDateTime.of(2025, 7, 30, 17, 0));
        }, () -> {
            Assertions.fail("Required not null");
        });
    }

    @Test
    void testSaveINSERT() {
        JdbcAttendanceRepository jdbcAttendanceRepository  = new JdbcAttendanceRepository(jdbcClient);

        Attendance attendance = new Attendance(
                null,
                "emp300",
                LocalDateTime.of(2025, 9, 15, 8, 0, 0),
                LocalDateTime.of(2025, 9, 15, 17, 0, 0));

        Attendance savedAttendance = jdbcAttendanceRepository.save(attendance);

        Assertions.assertThat(savedAttendance.id()).isNotNull();
        Assertions.assertThat(savedAttendance.employeeId()).isEqualTo("emp300");
        Assertions.assertThat(savedAttendance.beginWork()).isEqualTo(LocalDateTime.of(2025, 9, 15, 8, 0, 0));
        Assertions.assertThat(savedAttendance.finishWork()).isEqualTo(LocalDateTime.of(2025, 9, 15, 17, 0, 0));

        jdbcClient.sql("""
                     SELECT
                         id,
                         employee_id,
                         begin_work,
                         finish_work
                     FROM
                         attendances
                     WHERE
                         employee_id = 'emp300'
                 """)
                .query(DataClassRowMapper.class)
                .single();
    }

    @Test
    @Sql(statements = {
            """
            INSERT INTO attendances (id, employee_id, begin_work, finish_work)
            VALUES ('a5e5e9d4-40d7-4f7d-8b95-5a5d6d881ba6', 'emp400', '2025-06-30 09:00:00', null);
            """
    })
    void testSaveUPDATE() {
        JdbcAttendanceRepository jdbcAttendanceRepository  = new JdbcAttendanceRepository(jdbcClient);

        Attendance attendance = new Attendance(
                "a5e5e9d4-40d7-4f7d-8b95-5a5d6d881ba6",
                "emp400",
                LocalDateTime.of(2025, 8, 25, 10, 30, 0),
                LocalDateTime.of(2025, 8, 25, 19, 30, 0));

        Attendance savedAttendance = jdbcAttendanceRepository.save(attendance);
        Assertions.assertThat(savedAttendance.id()).isNotNull();
        Assertions.assertThat(savedAttendance.id()).isEqualTo("a5e5e9d4-40d7-4f7d-8b95-5a5d6d881ba6");
        Assertions.assertThat(savedAttendance.employeeId()).isEqualTo("emp400");
        Assertions.assertThat(savedAttendance.beginWork()).isEqualTo(LocalDateTime.of(2025, 8, 25, 10, 30, 0));
        Assertions.assertThat(savedAttendance.finishWork()).isEqualTo(LocalDateTime.of(2025, 8, 25, 19, 30, 0));
    }
}