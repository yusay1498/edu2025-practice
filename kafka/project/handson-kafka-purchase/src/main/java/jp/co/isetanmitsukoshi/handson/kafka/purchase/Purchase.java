package jp.co.isetanmitsukoshi.handson.kafka.purchase;

public record Purchase(
        /**
         * Item type
         *
         * food_takeout
         * food_eatin
         * clothing
         * sundries
         */
        String type,
        String name,
        int price,
        int qty
) {
    public int subtotal() {
        if (type == null) return (int) (price * 1.1) * qty;
        if (type.equals("food_takeout")) return  (int) (price * 1.08) * qty;
        else return (int) (price * 1.1) * qty;
    }
}
