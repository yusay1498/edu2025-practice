package jp.co.isetanmitsukoshi.handson.kafka.purchase;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class PurchaseTest {
    @DisplayName(
            """
            Given purchasing objects for each product type,
            When the subtotal is calculated,
            Then a subtotal applied with the standard tax rate or the reduced tax rate is returned.
            """
    )
    @ParameterizedTest(name = "{0} tax-rate is {4}")
    @CsvSource(delimiter = '|', textBlock =
            """
            'food_takeout' | 'McDonald BigMac'                         | 450  | 2 | 0.08
            'food_eatin'   | 'McDonald BigMac'                         | 450  | 2 | 0.10
            'food_takeout' | 'KFC Original chicken'                    | 290  | 4 | 0.08
            'food_eatin'   | 'KFC Original chicken'                    | 290  | 4 | 0.10
            'food_takeout' | 'BurgerKing Whopper'                      | 590  | 1 | 0.08
            'food_eatin'   | 'BurgerKing Whopper'                      | 590  | 1 | 0.10
            'clothing'     | 'UNIQLO Crew neck T-shirt (short sleeve)' | 1500 | 1 | 0.10
            'clothing'     | 'UNIQLO Slim fit jeans'                   | 3990 | 1 | 0.10
            'sundries'     | 'Elleair Toilet tissue 12-role (single)'  | 562  | 1 | 0.10
            """
    )
    void testPurchaseSubtotal(String type, String name, int price, int qty, double taxRate) {
        /*
         * Given
         */
        Purchase purchase = new Purchase(type, name, price, qty);

        /*
         * When
         */
        int subtotal = purchase.subtotal();

        /*
         * Then
         */
        Assertions.assertThat(subtotal).isEqualTo((int) (price + price * taxRate) * qty);
    }
}
