package com.example.demo_attendance.infrastructure;

import com.example.demo_attendance.domain.entity.Attendance;
import com.example.demo_attendance.domain.repository.AttendanceRepository;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        // Optionalにして、結果が存在するかどうかの判断をより直感的に行い、コードの可読性と安全性が向上させる
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

    public Attendance save(Attendance attendance) {
        // 勤怠idが存在しない（初回登録時）はINSERT
        // 勤怠idが存在する場合（更新や退勤時刻登録時）はUPDATE
        if (attendance.id() == null) {
            String newId = UUID.randomUUID().toString();

            jdbcClient.sql("""
                INSERT INTO attendances (id, employee_id, begin_work, finish_work)
                VALUES (:id, :employee_id, :begin_work, :finish_work)
                """)
                    .param("id", newId)
                    .param("employee_id", attendance.employeeId())
                    .param("begin_work", attendance.beginWork())
                    .param("finish_work", attendance.finishWork())
                    .update();

            // 登録されたことを確認するためfindByIdで取得した値を返す
            return findById(newId).orElseThrow(
                    () -> new RuntimeException("INSERT operation was unsuccessful"));
        } else {
            // COALESCE文を使用して、指定されたパラメータがNULLでない場合にのみその値でカラムを更新
            // そうでない場合は、カラムの現在の値を保持
            int updatedRow = jdbcClient.sql("""
                    UPDATE attendances
                    SET
                        begin_work  = COALESCE(:beginWork, begin_work),
                        finish_work = COALESCE(:finishWork, finish_work)
                    WHERE id = :id
                """)
                    .param("beginWork", attendance.beginWork())
                    .param("finishWork", attendance.finishWork())
                    .param("id", attendance.id())
                    .update();

            if (updatedRow == 1) {
                // 更新が成功した場合はそのままAttendanceオブジェクトを返す
                return findById(attendance.id()).orElseThrow(
                        () -> new RuntimeException("UPDATE operation was unsuccessful"));
            } else if (updatedRow == 0) {
                // 更新が成功しなかった場合（idが存在しない場合）の処理
                throw new RuntimeException("Failed to update attendance for ID " + attendance.id());
            } else {
                // 更新が成功しなかった場合（複数更新される場合）の処理
                throw new RuntimeException("Failed to update attendance for multiple updates are performed");
            }
        }
    }

    public void deleteById(String id) {
        jdbcClient.sql("""
                DELETE FROM attendances
                WHERE id = :id
                """)
                .param("id", id)
                .update();
    }
}
