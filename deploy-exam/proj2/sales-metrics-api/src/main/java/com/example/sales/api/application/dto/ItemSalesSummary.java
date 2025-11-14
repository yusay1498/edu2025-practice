package com.example.application.dto;

import java.time.LocalDate;

public record ItemSalesSummary(
        /** 商品ID */
        Integer itemId,
        /** 商品名 */
        String itemName,
        /** 商品の価格 */
        Integer itemPrice,
        /** 売上日 */
        LocalDate date,
        /** 売上金額 */
        int totalAmount,
        /** 販売数量 */
        int totalQuantity
) {
}
