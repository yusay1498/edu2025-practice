package com.example.demo_attendance.domain.entity;

import java.time.LocalDateTime;

public record Attendance(
        String id,
        String employeeId,
        LocalDateTime beginWork,
        LocalDateTime finishWork
) {
}
