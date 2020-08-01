package strategies;

import player.observers.IntervalRecorder;
import player.observers.ResultObserver;

import java.util.Random;

public class PriorityChoiceStrategy implements Strategy{
    private Random random;
    private final IntervalRecorder recorder;
    private final int[] choicePriorities;

    public PriorityChoiceStrategy(int[] choicePriorities) {
        this.choicePriorities = choicePriorities;
        recorder = new IntervalRecorder(choicePriorities.length);
        random = new Random();
    }

    @Override
    public void reset() {
        recorder.reset();
    }

    @Override
    public ResultObserver connectedObserver() {
        return recorder;
    }

    @Override
    public int guess() {
        int[] relevantNumbersToGuess = getPossibleGuesses();
        return relevantNumbersToGuess[random.nextInt(relevantNumbersToGuess.length)];
    }

    private int[] getPossibleGuesses(){
        int countOfGuesses = numberOfTheLowest();
        int[] ouput = new int[countOfGuesses];
        int smallest = getLowestPriorityInRange();
        int currentIndexInOutput = 0;
        for(int i = recorder.getCurrentMin() ; i < recorder.getCurrentMax() ; i++){
            if(choicePriorities[i] == smallest){
                ouput[currentIndexInOutput] = i;
                currentIndexInOutput++;
            }
        }
        return ouput;
    }

    private int getLowestPriorityInRange(){
        int lowestSoFar = Integer.MAX_VALUE;
        for(int i = recorder.getCurrentMin() ; i < recorder.getCurrentMax() ; i++){
            if(lowestSoFar > choicePriorities[i])
                lowestSoFar = choicePriorities[i];
        }
        return lowestSoFar;
    }

    private int numberOfTheLowest(){
        int lowest = getLowestPriorityInRange();
        int count = 0;
        for(int i = recorder.getCurrentMin() ; i < recorder.getCurrentMax() ; i++){
            if(lowest == choicePriorities[i])
                count++;
        }
        return count;
    }
}
