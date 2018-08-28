package orderbook;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

class OrderBook {

    private static final DoubleComparator doubleComparator = new DoubleComparator();

    private TreeMap<Double, Long> buyLevels = new TreeMap<Double, Long>(doubleComparator);
    private TreeMap<Double, Long> sellLevels = new TreeMap<Double, Long>(doubleComparator);

    private Queue<PriceQuantity> executionQueue = new ConcurrentLinkedQueue();

    public synchronized void buy(double price, long quantity) {
        long newQty = quantity;
        if (buyLevels.containsKey(price)) {
            newQty += buyLevels.get(price);
        }
        buyLevels.put(price, newQty);
        match(Side.BUY);
    }

    public synchronized void sell(double price, long quantity) {
        long newQty = quantity;
        if (sellLevels.containsKey(price)) {
            newQty += sellLevels.get(price);
        }
        sellLevels.put(price, newQty);
        match(Side.SELL);
    }

    public synchronized PriceQuantity[] getAllLevels (Side side) {

        PriceQuantity[] ret = new PriceQuantity[5] ;

        for (int i = 0; i < 5; i++) {
            Optional<PriceQuantity> levelOption = getLevel(side, i + 1);
            ret[i] = levelOption.isPresent() ? levelOption.get() : null;
        }

        return ret;

    }

    public synchronized Optional<PriceQuantity> getLevel(Side side, int level) {

        if (side == null) {
            throw new IllegalArgumentException("side shouldn't be null");
        }

        if (level > 5 || level < 1) {
            return Optional.empty();
        }

        Iterator <Double> itr = (side == Side.BUY) ? buyLevels.descendingKeySet().iterator() : sellLevels.keySet().iterator();
        double bestPrice = 0.0;
        for (int i = 0; i < level; i++) {
            if (itr.hasNext()) {
                bestPrice = itr.next();
            } else {
                return Optional.empty();
            }
        }

        return Optional.of(new PriceQuantity(bestPrice, side == Side.BUY ? buyLevels.get(bestPrice) : sellLevels.get(bestPrice)  ));

    }






    private synchronized void match(Side sideThatTriggersTheMatch) {

        while (true) {
            boolean hasBestBid = !buyLevels.isEmpty();
            boolean hasBestAsk = !sellLevels.isEmpty();
            if (!hasBestAsk || !hasBestBid) {
                // nothing to match
                break;
            }

            // get the best bid and best ask
            Map.Entry<Double, Long> bestBidAndQty = buyLevels.lastEntry();
            Map.Entry<Double, Long> bestAskAndQty = sellLevels.firstEntry();
            if (doubleComparator.compare(bestBidAndQty.getKey(), bestAskAndQty.getKey()) < 0) {
                // nothing to match
                break;
            }
            double bestBid = bestBidAndQty.getKey();
            double bestAsk = bestAskAndQty.getKey();
            long bestBidQty = bestBidAndQty.getValue();
            long bestAskQty = bestAskAndQty.getValue();

            long executionQty = Math.min(bestBidQty, bestAskQty);

            // create a new execution
            executionQueue.add(new PriceQuantity(sideThatTriggersTheMatch == Side.BUY ? bestAsk : bestBid, executionQty));

            // update the order book after execution
            if (bestBidQty > bestAskQty) {
                buyLevels.put(bestBid, bestBidQty - executionQty);
                sellLevels.remove(bestAsk);
            } else if (bestBidQty < bestAskQty) {
                buyLevels.remove(bestBid);
                sellLevels.put(bestAsk, bestAskQty - executionQty);
            } else {
                buyLevels.remove(bestAsk);
                sellLevels.remove(bestAsk);
            }
        }
    }
}






