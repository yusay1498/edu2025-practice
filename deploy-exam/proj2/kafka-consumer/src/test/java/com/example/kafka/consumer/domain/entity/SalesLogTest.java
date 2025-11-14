package com.example.kafka.consumer.domain.entity;

import com.example.kafka.consumer.domain.entity.Sales;
import com.example.kafka.consumer.domain.entity.SalesItemSubtotal;
import com.example.kafka.consumer.domain.entity.SalesLog;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

class SalesLogTest {
    @Test
    void testToSalesList() {
        SalesLog salesLog = new SalesLog(
                "test-sales-log-id-10",
                LocalDateTime.of(1999, 9, 15, 10, 10, 0),
                0,
                0,
                6110,
                List.of(
                        new SalesLog.Item(2004, "GoldenClaw", 6000, 1, 6000),
                        new SalesLog.Item(2005, "TrainingUniform", 110, 1, 110)
                ),
                new SalesLog.Customer(100, "Ortega", "male"),
                6110,
                6
        );

        List<Sales> salesList = salesLog.toSalesList();

        Assertions.assertThat(salesList.get(0).id()).isEqualTo("test-sales-log-id-10");
        Assertions.assertThat(salesList.get(0).dateTime()).isEqualTo(LocalDateTime.of(1999, 9, 15, 10, 10, 0));
        Assertions.assertThat(salesList.get(0).discount()).isEqualTo(0);
        Assertions.assertThat(salesList.get(0).paidPoint()).isEqualTo(0);
        Assertions.assertThat(salesList.get(0).paidCash()).isEqualTo(6110);

        Assertions.assertThat(salesList.get(0).items()).hasSize(2);
        Assertions.assertThat(salesList.get(0).items().get(0).itemId()).isEqualTo(2004);
        Assertions.assertThat(salesList.get(0).items().get(0).unitPrice()).isEqualTo(6000);
        Assertions.assertThat(salesList.get(0).items().get(0).quantity()).isEqualTo(1);
        Assertions.assertThat(salesList.get(0).items().get(0).subtotal()).isEqualTo(6000);

        Assertions.assertThat(salesList.get(0).items().get(1).itemId()).isEqualTo(2005);
        Assertions.assertThat(salesList.get(0).items().get(1).unitPrice()).isEqualTo(110);
        Assertions.assertThat(salesList.get(0).items().get(1).quantity()).isEqualTo(1);
        Assertions.assertThat(salesList.get(0).items().get(1).subtotal()).isEqualTo(110);

        Assertions.assertThat(salesList.get(0).total()).isEqualTo(6110);
        Assertions.assertThat(salesList.get(0).givenPoint()).isEqualTo(6);
    }

    @Test
    void testToSalesItemSubtotalList() {
        SalesLog salesLog = new SalesLog(
                "test-sales-log-id-11",
                LocalDateTime.of(1999, 9, 15, 10, 10, 10),
                0,
                0,
                7000,
                List.of(
                        new SalesLog.Item(2010, "CopperSword", 100, 1, 100),
                        new SalesLog.Item(2011, "MagicArmor", 6900, 1, 6900)
                ),
                new SalesLog.Customer(100, "Ortega", "male"),
                7000,
                7
        );

        List<SalesItemSubtotal> salesItemSubtotalList = salesLog.toSalesItemSubtotalList();

        Assertions.assertThat(salesItemSubtotalList).hasSize(2);

        SalesItemSubtotal copperSwordSummary = salesItemSubtotalList.stream()
                .filter(summary -> summary.itemId() == 2010)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Item 2010 not found in sales summary"));

        Assertions.assertThat(copperSwordSummary.itemId()).isEqualTo(2010);
        Assertions.assertThat(copperSwordSummary.date()).isEqualTo(LocalDate.of(1999, 9, 15)); // dateのみ
        Assertions.assertThat(copperSwordSummary.amount()).isEqualTo(100); // 小計が100
        Assertions.assertThat(copperSwordSummary.quantity()).isEqualTo(1); // 数量が1

        SalesItemSubtotal magicArmorSummary = salesItemSubtotalList.stream()
                .filter(summary -> summary.itemId() == 2011)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Item 2011 not found in sales summary"));

        Assertions.assertThat(magicArmorSummary.itemId()).isEqualTo(2011);
        Assertions.assertThat(magicArmorSummary.date()).isEqualTo(LocalDate.of(1999, 9, 15)); // dateのみ
        Assertions.assertThat(magicArmorSummary.amount()).isEqualTo(6900); // 小計が6900
        Assertions.assertThat(magicArmorSummary.quantity()).isEqualTo(1); // 数量が1
    }
}