package com.example.demo_attendance.domain.repository;

import com.example.demo_attendance.domain.entity.Attendance;

import java.util.List;

public interface AttendanceRepository {
    List<Attendance> findAll();
}
