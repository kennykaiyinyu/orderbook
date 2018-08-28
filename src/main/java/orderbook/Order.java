package orderbook;

import java.lang.IllegalArgumentException;


class Order {
    private final Side side;
    private final double price;
    private final long quantity;
    public Order(Side side, double price, long quantity) {


        if (side == null) {
            throw new IllegalArgumentException("side must be not null");
        }
        if (quantity < 0 ) {
            throw new IllegalArgumentException("qty must not be negative");
        }
        this.side = side;
        this.price = price;
        this.quantity = quantity;

    }
}