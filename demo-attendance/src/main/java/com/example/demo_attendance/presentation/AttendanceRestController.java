package com.example.demo_attendance.presentation;

import com.example.demo_attendance.application.AttendanceService;
import com.example.demo_attendance.domain.entity.Attendance;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/attendances")
public class AttendanceRestController {
    private final AttendanceService attendanceService;

    public AttendanceRestController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping
    public List<Attendance> getAllAttendances() {
        return attendanceService.viewAllAttendances();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Attendance> getAttendanceById(
            @PathVariable String id
    ) {
        return attendanceService.viewAttendanceById(id)
                // mapでOptionalが値を持っていたらResponseEntity::ok
                // ResponseEntity::okは、Attendanceオブジェクトが存在する場合に、
                // そのオブジェクトを含むHTTP 200 OKレスポンスを作成
                .map(ResponseEntity::ok)
                // ResponseEntity.notFound().build()は、HTTP 404 Not Foundレスポンスを作成
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> postAttendance(
            @RequestBody Attendance attendance
    ) {
        // ResponseEntityにすることでHTTPステータスコードを制御
        Attendance newAttendance = attendanceService.recordBeginWork(attendance.employeeId());

        // 新しいリソースのURIを作成する
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newAttendance.id())
                .toUri();

        // Createdステータスと新しいリソースのURIを返す
        // リソースが正常に作成されたことを示すために適切なHTTPステータスコードで応答
        return ResponseEntity.created(location).build();
    }
}
