package orderbook;


import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;


public class OrderBookTest {
    @Test
    public void testOrderBookBuyInsertions() {

        OrderBook ob = new OrderBook();
        ob.buy(100.0, 200);
        Assert.assertEquals(Optional.of(new PriceQuantity(100.0, 200)), ob.getLevel(Side.BUY, 1));
        Assert.assertEquals(Optional.empty(), ob.getLevel(Side.BUY, 2));
        Assert.assertEquals(Optional.empty(), ob.getLevel(Side.BUY, 3));
        Assert.assertEquals(Optional.empty(), ob.getLevel(Side.BUY, 4));
        Assert.assertEquals(Optional.empty(), ob.getLevel(Side.BUY, 5));

        ob.buy(100.2, 300);

        Assert.assertEquals(Optional.of(new PriceQuantity(100.2, 300)), ob.getLevel(Side.BUY, 1));
        Assert.assertEquals(Optional.of(new PriceQuantity(100.0, 200)), ob.getLevel(Side.BUY, 2));
        Assert.assertEquals(Optional.empty(), ob.getLevel(Side.BUY, 3));
        Assert.assertEquals(Optional.empty(), ob.getLevel(Side.BUY, 4));
        Assert.assertEquals(Optional.empty(), ob.getLevel(Side.BUY, 5));

        ob.buy(100.4, 400);

        Assert.assertEquals(Optional.of(new PriceQuantity(100.4, 400)), ob.getLevel(Side.BUY, 1));
        Assert.assertEquals(Optional.of(new PriceQuantity(100.2, 300)), ob.getLevel(Side.BUY, 2));
        Assert.assertEquals(Optional.of(new PriceQuantity(100.0, 200)), ob.getLevel(Side.BUY, 3));
        Assert.assertEquals(Optional.empty(), ob.getLevel(Side.BUY, 4));
        Assert.assertEquals(Optional.empty(), ob.getLevel(Side.BUY, 5));

        ob.buy(100.4, 400);

        Assert.assertEquals(Optional.of(new PriceQuantity(100.4, 800)), ob.getLevel(Side.BUY, 1));
        Assert.assertEquals(Optional.of(new PriceQuantity(100.2, 300)), ob.getLevel(Side.BUY, 2));
        Assert.assertEquals(Optional.of(new PriceQuantity(100.0, 200)), ob.getLevel(Side.BUY, 3));
        Assert.assertEquals(Optional.empty(), ob.getLevel(Side.BUY, 4));
        Assert.assertEquals(Optional.empty(), ob.getLevel(Side.BUY, 5));

    }

    @Test
    public void testOrderBookSellInsertions() {

        OrderBook ob = new OrderBook();
        ob.sell(100.0, 200);
        Assert.assertEquals(Optional.of(new PriceQuantity(100.0, 200)), ob.getLevel(Side.SELL, 1));
        Assert.assertEquals(Optional.empty(), ob.getLevel(Side.SELL, 2));
        Assert.assertEquals(Optional.empty(), ob.getLevel(Side.SELL, 3));
        Assert.assertEquals(Optional.empty(), ob.getLevel(Side.SELL, 4));
        Assert.assertEquals(Optional.empty(), ob.getLevel(Side.SELL, 5));

        ob.sell(100.2, 300);

        Assert.assertEquals(Optional.of(new PriceQuantity(100.2, 300)), ob.getLevel(Side.SELL, 2));
        Assert.assertEquals(Optional.of(new PriceQuantity(100.0, 200)), ob.getLevel(Side.SELL, 1));
        Assert.assertEquals(Optional.empty(), ob.getLevel(Side.SELL, 3));
        Assert.assertEquals(Optional.empty(), ob.getLevel(Side.SELL, 4));
        Assert.assertEquals(Optional.empty(), ob.getLevel(Side.SELL, 5));

        ob.sell(100.4, 400);

        Assert.assertEquals(Optional.of(new PriceQuantity(100.4, 400)), ob.getLevel(Side.SELL, 3));
        Assert.assertEquals(Optional.of(new PriceQuantity(100.2, 300)), ob.getLevel(Side.SELL, 2));
        Assert.assertEquals(Optional.of(new PriceQuantity(100.0, 200)), ob.getLevel(Side.SELL, 1));
        Assert.assertEquals(Optional.empty(), ob.getLevel(Side.SELL, 4));
        Assert.assertEquals(Optional.empty(), ob.getLevel(Side.SELL, 5));

        ob.sell(100.4, 400);

        Assert.assertEquals(Optional.of(new PriceQuantity(100.4, 800)), ob.getLevel(Side.SELL, 3));
        Assert.assertEquals(Optional.of(new PriceQuantity(100.2, 300)), ob.getLevel(Side.SELL, 2));
        Assert.assertEquals(Optional.of(new PriceQuantity(100.0, 200)), ob.getLevel(Side.SELL, 1));
        Assert.assertEquals(Optional.empty(), ob.getLevel(Side.SELL, 4));
        Assert.assertEquals(Optional.empty(), ob.getLevel(Side.SELL, 5));
        Assert.assertEquals(Optional.empty(), ob.getLevel(Side.SELL, 5));

    }


    @Test
    public void testOrderBookBuyAndSellInsertionsWithMatching() {

        OrderBook ob = new OrderBook();
        ob.sell(100.0, 200);
        ob.sell(100.2, 300);
        ob.sell(100.4, 400);
        ob.sell(100.4, 400);

        Assert.assertArrayEquals(new PriceQuantity[] {null, null, null, null, null}, ob.getAllLevels(Side.BUY));
        Assert.assertArrayEquals(new PriceQuantity[] {
                new PriceQuantity(100.0, 200),
                new PriceQuantity(100.2, 300),
                new PriceQuantity(100.4, 800),
                null,
                null}, ob.getAllLevels(Side.SELL));

        // send two non-colliding buy orders
        ob.buy(99.88, 200);
        ob.buy(99.86, 300);

        Assert.assertArrayEquals(new PriceQuantity[] {
                new PriceQuantity(99.88, 200),
                new PriceQuantity(99.86, 300),
                null,
                null,
                null}, ob.getAllLevels(Side.BUY));
        Assert.assertArrayEquals(new PriceQuantity[] {
                new PriceQuantity(100.0, 200),
                new PriceQuantity(100.2, 300),
                new PriceQuantity(100.4, 800),
                null,
                null}, ob.getAllLevels(Side.SELL));

        // send a buy order to cross-spread
        ob.buy(100.0, 100);


        Assert.assertArrayEquals(new PriceQuantity[] {
                new PriceQuantity(99.88, 200),
                new PriceQuantity(99.86, 300),
                null,
                null,
                null}, ob.getAllLevels(Side.BUY));
        Assert.assertArrayEquals(new PriceQuantity[] {
                new PriceQuantity(100.0, 100),
                new PriceQuantity(100.2, 300),
                new PriceQuantity(100.4, 800),
                null,
                null}, ob.getAllLevels(Side.SELL));

        // send a buy order to further cross-spread
        ob.buy(100.0, 100);
        Assert.assertArrayEquals(new PriceQuantity[] {
                new PriceQuantity(99.88, 200),
                new PriceQuantity(99.86, 300),
                null,
                null,
                null}, ob.getAllLevels(Side.BUY));
        Assert.assertArrayEquals(new PriceQuantity[] {
                new PriceQuantity(100.2, 300),
                new PriceQuantity(100.4, 800),
                null,
                null,
                null}, ob.getAllLevels(Side.SELL));

        // send a very aggresive buy order to hit 2 levels of ask
        ob.buy(100.4, 500);
        Assert.assertArrayEquals(new PriceQuantity[] {
                new PriceQuantity(99.88, 200),
                new PriceQuantity(99.86, 300),
                null,
                null,
                null}, ob.getAllLevels(Side.BUY));
        Assert.assertArrayEquals(new PriceQuantity[] {

                new PriceQuantity(100.4, 600),
                null,
                null,
                null,
                null}, ob.getAllLevels(Side.SELL));

        // another very aggresive buy order
        ob.buy(100.6, 500);

        Assert.assertArrayEquals(new PriceQuantity[] {
                new PriceQuantity(99.88, 200),
                new PriceQuantity(99.86, 300),
                null,
                null,
                null}, ob.getAllLevels(Side.BUY));
        Assert.assertArrayEquals(new PriceQuantity[] {
                new PriceQuantity(100.4, 100),
                null,
                null,
                null,
                null}, ob.getAllLevels(Side.SELL));

        // another very aggresive buy order to take away all offers
        ob.buy(100.6, 500);

        Assert.assertArrayEquals(new PriceQuantity[] {
                new PriceQuantity(100.6, 400),
                new PriceQuantity(99.88, 200),
                new PriceQuantity(99.86, 300),
                null,
                null}, ob.getAllLevels(Side.BUY));
        Assert.assertArrayEquals(new PriceQuantity[] {
                null,
                null,
                null,
                null,
                null}, ob.getAllLevels(Side.SELL));

        // send back some ask orders
        ob.sell(100.8, 500);

        Assert.assertArrayEquals(new PriceQuantity[] {
                new PriceQuantity(100.6, 400),
                new PriceQuantity(99.88, 200),
                new PriceQuantity(99.86, 300),
                null,
                null}, ob.getAllLevels(Side.BUY));
        Assert.assertArrayEquals(new PriceQuantity[] {
                new PriceQuantity(100.8, 500),
                null,
                null,
                null,
                null}, ob.getAllLevels(Side.SELL));

        // very aggressive sell order to hit the market bid's first 2 levels (not fully filling the 2nd level)
        ob.sell(99.88, 500);

        Assert.assertArrayEquals(new PriceQuantity[] {
                new PriceQuantity(99.88, 100),
                new PriceQuantity(99.86, 300),
                null,
                null,
                null}, ob.getAllLevels(Side.BUY));
        Assert.assertArrayEquals(new PriceQuantity[] {
                new PriceQuantity(100.8, 500),
                null,
                null,
                null,
                null}, ob.getAllLevels(Side.SELL));

        // another very aggressive sell order to hit the market bid's first 2 levels (fully filling all)
        ob.sell(99.82, 1500);

        Assert.assertArrayEquals(new PriceQuantity[] {
                null,
                null,
                null,
                null,
                null}, ob.getAllLevels(Side.BUY));
        Assert.assertArrayEquals(new PriceQuantity[] {
                new PriceQuantity(99.82, 1100),
                new PriceQuantity(100.8, 500),
                null,
                null,
                null}, ob.getAllLevels(Side.SELL));


    }



}
