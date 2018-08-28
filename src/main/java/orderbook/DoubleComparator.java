package orderbook;

import java.util.Comparator;

class DoubleComparator implements Comparator<Double> {

    private static final double EPSILON = 0.00001;

    @Override
    public int compare(Double d1, Double d2) {
        double diff = (d1 - d2);
        if (diff < -EPSILON) {
            return -1;
        } else if (diff > EPSILON) {
            return 1;
        } else {
            return 0;
        }
    }


}
