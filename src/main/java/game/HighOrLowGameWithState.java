package game;

import java.util.Arrays;
import java.util.Random;

public class HighOrLowGameWithState extends HighOrLowGame{
    private final int[] currentBoardState;
    private boolean solutionFound;

    public HighOrLowGameWithState(int highestBound, int targetNumber) {
        super(highestBound, targetNumber);
        currentBoardState = new int[highestBound];
    }

    public HighOrLowGameWithState(int highestBound) {
        super(highestBound);
        currentBoardState = new int[highestBound];
    }

    public HighOrLowGameWithState() {
        currentBoardState = new int[HighOrLowGame.DEFAULT_HIGHEST_BOUND];
    }

    public HighOrLowGameWithState(int highestBound, Random random) {
        super(highestBound, random);
        currentBoardState = new int[highestBound];
    }

    @Override
    public int makeGuess(int guess) {
        int guessResult = super.makeGuess(guess);
        currentBoardState[guess] = guessResult;
        if(guessResult == 0)
            solutionFound = true;
        return guessResult;
    }

    @Override
    public void reset() {
        super.reset();
        Arrays.fill(currentBoardState, 0);
        solutionFound = false;
    }

    public int[] getCurrentBoardState() {
        return currentBoardState;
    }

    public boolean isSolutionFound() {
        return solutionFound;
    }

    public int getNumberOfGoodChoices(){
        int count = 0;
        for(int i = 0 ; i < currentBoardState.length ; i++){
            if(currentBoardState[i] == 1) {
                count = 0;
            }else if(currentBoardState[i] == -1) {
                return count;
            }else{
                count++;
            }
        }
        return count;
    }
}
