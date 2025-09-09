package com.example.demo_attendance.application;

import com.example.demo_attendance.domain.entity.Attendance;
import com.example.demo_attendance.domain.repository.AttendanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final Clock clock;

    public AttendanceService(AttendanceRepository attendanceRepository, Clock clock) {
        this.attendanceRepository = attendanceRepository;
        this.clock = clock;
    }

    public List<Attendance> viewAllAttendances() {
        return attendanceRepository.findAll();
    }

    public Optional<Attendance> viewAttendanceById(String id) {
        return attendanceRepository.findById(id);
    }

    public Attendance recordBeginWork(String employeeId) {
        return attendanceRepository.save(new Attendance(
                null,
                employeeId,
                obtainCurrentTime(),
                null
                ));
    }

    public void recordFinishWork(String id) {
        Optional<Attendance> result = attendanceRepository.findById(id);

        // Optional が空でないことを確認してから値を取得する
        // そうしないとエラー処理が不適切な時に発生するNoSuchElementExceptionが発生する
        result.ifPresentOrElse(attendance -> {
            attendanceRepository.save(new Attendance(
                    attendance.id(),
                    attendance.employeeId(),
                    attendance.beginWork(),
                    obtainCurrentTime()
            ));
        }, () -> {
            throw new RuntimeException("No recorded attendance found for ID " + id);
        });
    }

    private LocalDateTime obtainCurrentTime() {
        return LocalDateTime.now(clock).withNano(0);
    }
}
