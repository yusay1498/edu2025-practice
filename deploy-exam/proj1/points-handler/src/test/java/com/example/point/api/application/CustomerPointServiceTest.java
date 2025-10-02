package com.example.point.api.application;

import com.example.point.api.domain.entity.CustomerPoint;
import com.example.point.api.domain.repository.CustomerPointRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CustomerPointServiceTest {
    @Test
    void testViewAllCustomerPoints_Find() {
        // テストデータの準備
        List<CustomerPoint> testCustomerPoints = List.of(
                new CustomerPoint("testId1", 100),
                new CustomerPoint("testId2", 200),
                new CustomerPoint("testId3", 300)
        );

        // リポジトリをMockitoでモック化（実際のDBアクセスは行わない）
        CustomerPointRepository mockedRepo = Mockito.mock(CustomerPointRepository.class);

        // モックのfindAll()メソッドが呼ばれたときtestCustomerPointsのデータを返すように設定
        Mockito.doReturn(testCustomerPoints).when(mockedRepo).findAll();

        // モックリポジトリを使ってサービスインスタンスを生成
        CustomerPointService customerPointService = new CustomerPointService(mockedRepo);

        // サービスの「全顧客ポイント取得」を呼び出し、結果を受け取る
        List<CustomerPoint> actual = customerPointService.viewAllCustomerPoints();

        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.size()).isEqualTo(testCustomerPoints.size());
        Assertions.assertThat(actual.get(0)).isEqualTo(testCustomerPoints.get(0));
        Assertions.assertThat(actual.get(1)).isEqualTo(testCustomerPoints.get(1));
        Assertions.assertThat(actual.get(2)).isEqualTo(testCustomerPoints.get(2));

        // モックのfindAll()が1回だけ呼ばれたことを検証
        Mockito.verify(mockedRepo, Mockito.times(1)).findAll();
    }

    @Test
    void testViewAllCustomerPoints_Empty() {
        CustomerPointRepository mockedRepo = Mockito.mock(CustomerPointRepository.class);

        Mockito.doReturn(Collections.emptyList()).when(mockedRepo).findAll();

        CustomerPointService customerPointService = new CustomerPointService(mockedRepo);

        List<CustomerPoint> actual = customerPointService.viewAllCustomerPoints();

        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual).isEmpty();

        Mockito.verify(mockedRepo, Mockito.times(1)).findAll();
    }

    @Test
    void testViewCustomerPointByCustomerId_Find() {
        CustomerPoint testCustomerPoint = new CustomerPoint("testId1", 100);
        Optional<CustomerPoint> returnCustomerPoint = Optional.of(testCustomerPoint);

        CustomerPointRepository mockedRepo = Mockito.mock(CustomerPointRepository.class);

        Mockito.doReturn(returnCustomerPoint).when(mockedRepo).findByCustomerId("testId1");

        CustomerPointService customerPointService = new CustomerPointService(mockedRepo);

        Optional<CustomerPoint> actual = customerPointService.viewCustomerPointById("testId1");

        actual.ifPresentOrElse(customerPoint -> {
                    Assertions.assertThat(customerPoint.customerId()).isEqualTo(testCustomerPoint.customerId());
                    Assertions.assertThat(customerPoint.currentPoints()).isEqualTo(testCustomerPoint.currentPoints());
                }, () -> {
                    Assertions.fail("Required not null");
                }
        );
    }

    @Test
    void testViewCustomerPointByCustomerId_Empty() {
        CustomerPointRepository mockedRepo = Mockito.mock(CustomerPointRepository.class);

        Mockito.doReturn(Optional.empty()).when(mockedRepo).findByCustomerId("testId1");

        CustomerPointService customerPointService = new CustomerPointService(mockedRepo);

        Optional<CustomerPoint> actual = customerPointService.viewCustomerPointById("testId1");

        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual).isEmpty();

        Mockito.verify(mockedRepo, Mockito.times(1)).findByCustomerId("testId1");
    }

}