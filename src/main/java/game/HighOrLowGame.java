package game;

import java.util.Random;

public class HighOrLowGame {
    public static final int DEFAULT_HIGHEST_BOUND = 32;

    private final Random targetSeeder;

    private final int highestBound;
    private int targetNumber;

    public HighOrLowGame(int highestBound, int targetNumber) {
        this.highestBound = highestBound;
        this.targetNumber = targetNumber;
        targetSeeder = new FixedRandom(targetNumber);
    }

    public HighOrLowGame(int highestBound) {
        this(highestBound, new Random());
    }

    public HighOrLowGame() {
        this(DEFAULT_HIGHEST_BOUND);
    }

    public HighOrLowGame(int highestBound, Random random){
        this.highestBound = highestBound;
        targetSeeder = random;
        targetNumber = random.nextInt(highestBound);
    }

    public int makeGuess(int guess){
        return (int)Math.signum(targetNumber-guess);
    }

    public int getHighestBound() {
        return highestBound;
    }

    public void reset(){
        targetNumber = targetSeeder.nextInt(highestBound);
    }

    public int getTargetNumber() {
        return targetNumber;
    }
}
