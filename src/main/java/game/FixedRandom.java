package game;

import java.util.Random;

public class FixedRandom extends Random {
    private final int toReturn;

    public FixedRandom(int toReturn) {
        this.toReturn = toReturn;
    }

    @Override
    public int nextInt(int bound) {
        return toReturn;
    }
}
