package com.example.demo_attendance.application;


import com.example.demo_attendance.domain.entity.Attendance;
import com.example.demo_attendance.domain.repository.AttendanceRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class AttendanceServiceTest {
    @Test
    void testViewAllAttendances_Find() {
        List<Attendance> testAttendances = List.of(
                new Attendance(
                        "UUID1",
                        "emp999",
                        LocalDateTime.of(2024, 8, 30, 9, 45, 0),
                        LocalDateTime.of(2024, 8, 30, 18, 10, 0)
                ),
                new Attendance(
                        "UUID2",
                        "emp998",
                        LocalDateTime.of(2024, 8, 29, 9, 45, 0),
                        LocalDateTime.of(2024, 8, 29, 18, 10, 0)
                ),
                new Attendance(
                        "UUID3",
                        "emp997",
                        LocalDateTime.of(2024, 8, 28, 9, 45, 0),
                        LocalDateTime.of(2024, 8, 28, 18, 10, 0)
                )
        );
        AttendanceRepository mockedAttendanceRepo = Mockito.mock(AttendanceRepository.class);
        Mockito.doReturn(testAttendances).when(mockedAttendanceRepo).findAll();

        Clock mockedClock = Mockito.mock(Clock.class);

        AttendanceService attendanceService = new AttendanceService(mockedAttendanceRepo, mockedClock);
        List<Attendance> actual = attendanceService.viewAllAttendances();

        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.size()).isEqualTo(testAttendances.size());
        Assertions.assertThat(actual.getFirst()).isEqualTo(testAttendances.getFirst());
        Assertions.assertThat(actual.get(1)).isEqualTo(testAttendances.get(1));
        Assertions.assertThat(actual.get(2)).isEqualTo(testAttendances.get(2));

        Mockito.verify(mockedAttendanceRepo, Mockito.times(1)).findAll();
    }

    @Test
    void testViewAllAttendances_Empty() {
        // Repositoryインターフェースをモックで作成
        AttendanceRepository mockedAttendanceRepo = Mockito.mock(AttendanceRepository.class);
        Mockito.doReturn(Collections.emptyList()).when(mockedAttendanceRepo).findAll();

        Clock mockedClock = Mockito.mock(Clock.class);

        // モックでサービスクラスを呼び出す
        AttendanceService attendanceService = new AttendanceService(mockedAttendanceRepo, mockedClock);
        List<Attendance> actual = attendanceService.viewAllAttendances();

        // 何も入っていないので空で返す
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual).isEmpty();

        // 呼び出された回数を確認
        Mockito.verify(mockedAttendanceRepo, Mockito.times(1)).findAll();
    }

    @Test
    void testViewAttendanceById_Find() {
        AttendanceRepository mockedAttendanceRepo = Mockito.mock(AttendanceRepository.class);
        Attendance findAttendance = new Attendance(
                "UUID4",
                "emp996",
                LocalDateTime.of(2024, 7, 30, 9, 45, 0),
                LocalDateTime.of(2024, 7, 30, 18, 10, 0)
        );
        Optional<Attendance> returnAttendance = Optional.of(findAttendance);
        String testId = "UUID4";
        Mockito.doReturn(returnAttendance).when(mockedAttendanceRepo).findById(testId);

        Clock mockedClock = Mockito.mock(Clock.class);

        AttendanceService attendanceService = new AttendanceService(mockedAttendanceRepo, mockedClock);
        Optional<Attendance> actual = attendanceService.viewAttendanceById(testId);

        actual.ifPresentOrElse(attendance -> {
            Assertions.assertThat(attendance.id()).isEqualTo("UUID4");
            Assertions.assertThat(attendance.employeeId()).isEqualTo("emp996");
            Assertions.assertThat(attendance.beginWork()).isEqualTo(LocalDateTime.of(2024, 7, 30, 9, 45, 0));
            Assertions.assertThat(attendance.finishWork()).isEqualTo(LocalDateTime.of(2024, 7, 30, 18, 10, 0));
        }, () -> {
            Assertions.fail("Required not null");
        });

        Mockito.verify(mockedAttendanceRepo, Mockito.times(1)).findById(testId);
    }

    @Test
    void testViewAttendanceById_Empty() {
        AttendanceRepository mockedAttendanceRepo = Mockito.mock(AttendanceRepository.class);

        String testId = "testId";
        Mockito.doReturn(Optional.empty()).when(mockedAttendanceRepo).findById(testId);

        Clock mockedClock = Mockito.mock(Clock.class);

        AttendanceService attendanceService = new AttendanceService(mockedAttendanceRepo, mockedClock);
        Optional<Attendance> actual = attendanceService.viewAttendanceById(testId);

        Assertions.assertThat(actual).isEmpty();
        Mockito.verify(mockedAttendanceRepo, Mockito.times(1)).findById(testId);
    }

    @Test
    void testRecordBeginWork() {
        // モックのリポジトリ
        AttendanceRepository mockedAttendanceRepo = Mockito.mock(AttendanceRepository.class);
        String testEmployeeId = "testId";

        // 期待する保存後の Attendance（save が返すオブジェクト）
        Attendance savedAttendance = new Attendance(
                "generated-id",      // id が生成される想定
                testEmployeeId,
                LocalDateTime.of(2024, 9, 1, 9, 0),
                null
        );

        // save が呼ばれたら savedAttendance を返す
        Mockito.doReturn(savedAttendance)
                .when(mockedAttendanceRepo).save(Mockito.any(Attendance.class));

        // Clock は固定時刻の実インスタンスを使う（モックより簡潔）
        Clock fixedClock = Clock.fixed(
                LocalDateTime.of(2024, 9, 1, 9, 0).atZone(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault()
        );

        AttendanceService attendanceService = new AttendanceService(mockedAttendanceRepo, fixedClock);

        // 実行
        Attendance actual = attendanceService.recordBeginWork(testEmployeeId);

        // 検証
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual).isEqualTo(savedAttendance); // equals が正しく実装されている前提

        Mockito.verify(mockedAttendanceRepo, Mockito.times(1)).save(Mockito.any(Attendance.class));
    }

    // 正常に動作する場合
    @Test
    void testRecordFinishWork_Success() {
        // モックの準備
        AttendanceRepository mockedAttendanceRepo = Mockito.mock(AttendanceRepository.class);
        String testId = "UUID6";

        // 既存レコード（保存前）
        Attendance beforeAttendance = new Attendance(
                testId,
                "emp992",
                LocalDateTime.of(2024, 9, 1, 9, 45, 0),
                null
        );

        // 期待する保存後のオブジェクト（save が返すもの）
        Attendance afterAttendance = new Attendance(
                testId,
                "emp992",
                LocalDateTime.of(2024, 9, 1, 9, 45, 0),
                LocalDateTime.of(2024, 9, 1, 18, 10)
        );

        // findById が見つけるようにスタブ
        Mockito.doReturn(Optional.of(beforeAttendance))
                .when(mockedAttendanceRepo).findById(testId);

        // save が呼ばれたら afterAttendance を返すようにスタブ
        Mockito.doReturn(afterAttendance)
                .when(mockedAttendanceRepo).save(Mockito.any(Attendance.class));

        // Clock は実インスタンスを使う（テスト時刻を固定）
        Clock fixedClock = Clock.fixed(
                LocalDateTime.of(2024, 9, 1, 18, 10).atZone(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault()
        );

        AttendanceService attendanceService = new AttendanceService(mockedAttendanceRepo, fixedClock);

        // 実行
        attendanceService.recordFinishWork(testId);

        // 検証：findById と save が呼ばれていること
        Mockito.verify(mockedAttendanceRepo, Mockito.times(1)).findById(testId);

        // 引数の内容を検証したい場合は ArgumentCaptor を使う
        ArgumentCaptor<Attendance> captor = ArgumentCaptor.forClass(Attendance.class);
        Mockito.verify(mockedAttendanceRepo, Mockito.times(1)).save(captor.capture());
        Attendance savedArg = captor.getValue();

        // 保存時のオブジェクトが期待通りか断言（AssertJ）
        Assertions.assertThat(savedArg.employeeId()).isEqualTo("emp992");
        Assertions.assertThat(savedArg.finishWork()).isEqualTo(LocalDateTime.of(2024, 9, 1, 18, 10));
    }

    // 存在しないIDに対する退勤処理
    // 出勤はNotNullに設定されているので退勤だけ登録はできない
    @Test
    void testRecordFinishWork_FindNoRecord() {
        // モックのリポジトリを作成
        AttendanceRepository mockedAttendanceRepo = Mockito.mock(AttendanceRepository.class);
        String testId = "testId";
        Mockito.doReturn(Optional.empty()).when(mockedAttendanceRepo).findById(testId);

        Clock mockedClock = Mockito.mock(Clock.class);

        AttendanceService attendanceService = new AttendanceService(mockedAttendanceRepo, mockedClock);

        // recordFinishWorkの呼び出しと例外の検証
        Assertions.assertThatThrownBy(() -> attendanceService.recordFinishWork(testId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No recorded attendance found for ID " + testId);

        // findByIdメソッドが1回呼び出されたことを確認
        Mockito.verify(mockedAttendanceRepo, Mockito.times(1)).findById(testId);

        // saveメソッドは呼び出されないことを確認
        Mockito.verify(mockedAttendanceRepo, Mockito.never()).save(Mockito.any(Attendance.class));
    }
}