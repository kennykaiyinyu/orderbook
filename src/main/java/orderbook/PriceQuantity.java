package orderbook;

/**
 * Created by kennyyu on 8/28/2018.
 */
public class PriceQuantity {

    public static final DoubleComparator priceComparator = new DoubleComparator();

    public final double price;
    public final long quantity;
    public PriceQuantity(double price, long quantity) {

        if (quantity < 0 ) {
            throw new IllegalArgumentException("qty must not be negative");
        }

        this.price = price;
        this.quantity = quantity;

    }

    @Override
    public String toString() {
        return "PriceQuantity(price : " + price + ", quantity = " + quantity + ")";
    }

    @Override
    public boolean equals(Object o2) {
        if (o2 == null || !(o2 instanceof PriceQuantity)) {
            return false;
        }
        PriceQuantity pq2 = (PriceQuantity) o2;
        return priceComparator.compare(this.price, pq2.price) == 0 && this.quantity == pq2.quantity;
    }


}
