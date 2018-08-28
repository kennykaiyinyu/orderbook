package orderbook;


import org.junit.Assert;
import org.junit.Test;


public class PlayerSymbolTest {
    @Test
    public void testOppositePlayer() {
        Assert.assertEquals(PlayerSymbol.O, PlayerSymbol.X.oppositePlayer());
        Assert.assertEquals(PlayerSymbol.X, PlayerSymbol.O.oppositePlayer());
        Assert.assertEquals(PlayerSymbol.Neither, PlayerSymbol.Neither.oppositePlayer());
    }
}
