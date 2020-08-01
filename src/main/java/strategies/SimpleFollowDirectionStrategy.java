package strategies;

import player.observers.ResultObserver;
import player.observers.ResultRecorder;

public class SimpleFollowDirectionStrategy implements Strategy {
    private final int startGuess;
    private final ResultRecorder recorder;


    private boolean isFirstGuess;
    private int lastGuess;

    public SimpleFollowDirectionStrategy(int startGuess){
        this(startGuess,new ResultRecorder());
    }

    public SimpleFollowDirectionStrategy(int startGuess, ResultRecorder recorder) {
        this.startGuess = startGuess;
        this.recorder = recorder;
        isFirstGuess = true;
    }

    @Override
    public int guess() {
        if(isFirstGuess) {
            isFirstGuess = false;
            return makeGuess(startGuess);
        }
        return makeGuess(lastGuess + recorder.getLastResult());
    }

    @Override
    public void reset(){
        isFirstGuess = true;
        recorder.reset();
    }

    @Override
    public ResultObserver connectedObserver() {
        return recorder;
    }

    private int makeGuess(int numberToGuessOn){
        lastGuess = numberToGuessOn;
        return lastGuess;
    }
}
