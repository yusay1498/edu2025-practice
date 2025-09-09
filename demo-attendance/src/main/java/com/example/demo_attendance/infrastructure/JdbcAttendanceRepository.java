package com.example.demo_attendance.infrastructure;

import com.example.demo_attendance.domain.entity.Attendance;
import com.example.demo_attendance.domain.repository.AttendanceRepository;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcAttendanceRepository implements AttendanceRepository {
    private final JdbcClient jdbcClient;

    public JdbcAttendanceRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Attendance> findAll() {
        return jdbcClient.sql("""
                    SELECT
                        id,
                        employee_id,
                        begin_work,
                        finish_work
                    FROM
                        attendances
                """)
                .query(DataClassRowMapper.newInstance(Attendance.class))
                .list();
    }

    public Optional<Attendance> findById(String id) {
        return jdbcClient.sql("""
                    SELECT
                        id,
                        employee_id,
                        begin_work,
                        finish_work
                    FROM
                        attendances
                    WHERE
                        id = :id
                """)
                .param("id", id)
                .query(DataClassRowMapper.newInstance(Attendance.class))
                .optional();
    }
}
