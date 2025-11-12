package com.example.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public record SalesLog(
        String id,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "uuuu-MM-dd HH:mm:ss")
        LocalDateTime dateTime,
        Integer discount,
        Integer paidPoint,
        Integer paidCash,
        List<Item> items,
        Customer customer,
        Integer total,
        Integer givenPoint
) {
    public record Item(
            Integer id,
            String name,
            Integer unitPrice,
            Integer quantity,
            Integer subtotal
    ) {
    }

    public record Customer(
            Integer id,
            String name,
            String gender
    ) {
    }

    public List<Sales> toSalesList() {
        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        return List.of(new Sales(
                id(),  // このSalesLogの一意のID
                dateTime(),
                discount(),
                paidPoint(),
                paidCash(),
                toSalesItems(),
                total(),
                givenPoint()
        ));
    }

    private List<SalesItems> toSalesItems() {
        return items.stream()
                .map(item -> new SalesItems(
                        UUID.randomUUID().toString(),
                        item.id(),
                        item.unitPrice(),
                        item.quantity(),
                        item.subtotal()
                ))
                .toList();
    }

    public List<SalesItemSubtotal> toSalesItemSubtotalList() {
        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        LocalDate salesDate = dateTime.toLocalDate();

        return items.stream()
                .map(item -> new SalesItemSubtotal(
                        UUID.randomUUID().toString(),
                        item.id(),
                        salesDate,
                        item.subtotal,
                        item.quantity
                ))
                .toList();
    }
}