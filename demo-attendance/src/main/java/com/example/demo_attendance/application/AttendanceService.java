package com.example.demo_attendance.application;

import com.example.demo_attendance.domain.entity.Attendance;
import com.example.demo_attendance.domain.repository.AttendanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public List<Attendance> viewAllAttendances() {
        return attendanceRepository.findAll();
    }

    public Optional<Attendance> viewAttendanceById(String id) {
        return attendanceRepository.findById(id);
    }
}
